package pl.otomczak.empik.githubreporter.adapters.metrics;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pl.otomczak.empik.githubreporter.adapters.db.InMemoryMetricsRepository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class BufferedMetricsPublisherTest {

    final InMemoryMetricsRepository repository = new InMemoryMetricsRepository();
    final BufferedMetricsPublisher publisher = new BufferedMetricsPublisher(repository);

    @Test
    void publishingRequestShouldNotIncrementRequestCountUntilFlushed() {
        publisher.publishRequestFor("alice");
        assertThat(repository.getRequestCount("alice")).isEqualTo(0L);
    }

    @Test
    void publishingRequestShouldIncrementRequestCountWhenFlushed() {
        publisher.publishRequestFor("alice");
        publisher.flush();
        assertThat(repository.getRequestCount("alice")).isEqualTo(1L);
    }

    @Test
    void consecutiveFlushesShouldNotChangeRequestCount() {
        publishingRequestShouldIncrementRequestCountWhenFlushed();
        publisher.flush();
        assertThat(repository.getRequestCount("alice")).isEqualTo(1L);
    }

    static class FlushJob implements Callable<Void> {

        volatile boolean done = false;
        final long rate;
        final BufferedMetricsPublisher publisher;

        FlushJob(BufferedMetricsPublisher publisher, long rate) {
            this.rate = rate;
            this.publisher = publisher;
        }

        void shutdown() {
            done = true;
        }

        @Override
        public Void call() throws Exception {
            while (!done) {
                flush();
                Thread.sleep(rate);
            }
            return null;
        }

        public void flush() throws InterruptedException {
            boolean failure;
            do {
                try {
                    publisher.flush();
                    failure = false;
                } catch (Exception ex) {
                    failure = true;
                    Thread.sleep(rate);
                }
            } while (failure);
        }
    }


    static class PublishJob implements Callable<Void> {

        final BufferedMetricsPublisher publisher;
        private final String[] logins;
        private final int requestsPerUser;

        PublishJob(BufferedMetricsPublisher publisher, int requestsPerUser, String... logins) {
            this.publisher = publisher;
            this.requestsPerUser = requestsPerUser;
            this.logins = logins;
        }

        @Override
        public Void call() {
            for (int i = 0; i < requestsPerUser; ++i) {
                for (String login : shuffledLogins()) {
                    publisher.publishRequestFor(login);
                }
            }
            return null;
        }

        private List<String> shuffledLogins() {
            final List<String> logins = Lists.newArrayList(this.logins);
            Collections.shuffle(logins);
            return logins;
        }
    }

    @Test
    @Tag("slow")
    void publisherShouldBeThreadSafe() throws Exception {

        runThreadedTest(0.0);
    }

    @Test
    @Tag("slow")
    void publisherShouldBeFaultTolerant() throws Exception {

        runThreadedTest(0.5);
    }

    private void runThreadedTest(double repositoryFailureRatio) throws InterruptedException {
        final InMemoryMetricsRepository failingRepository = new InMemoryMetricsRepository(repositoryFailureRatio);
        final BufferedMetricsPublisher tolerantPublisher = new BufferedMetricsPublisher(failingRepository);

        final String[] logins = {"alice", "bob", "charlie"};
        final int publishingThreadCount = 100;
        final int requestsPerUser = 100_000;
        final int expectedRequestsPerLogin = publishingThreadCount * requestsPerUser;

        final ExecutorService flushingPool = Executors.newSingleThreadExecutor();
        final ExecutorService publishingPool = Executors.newFixedThreadPool(publishingThreadCount);

        final FlushJob flushJob = new FlushJob(tolerantPublisher, 100L);
        flushingPool.submit(flushJob);

        for (int i = 0; i < publishingThreadCount; ++i) {
            publishingPool.submit(new PublishJob(tolerantPublisher, requestsPerUser, logins));
        }

        publishingPool.shutdown();
        publishingPool.awaitTermination(300, TimeUnit.SECONDS);

        flushJob.flush();
        for (String login : logins) {
            assertThat(failingRepository.getRequestCount(login)).isEqualTo(expectedRequestsPerLogin);
        }

        flushingPool.shutdown();
        flushJob.shutdown();
        flushingPool.awaitTermination(1, TimeUnit.SECONDS);
    }
}