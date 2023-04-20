package pl.otomczak.empik.githubreporter.adapters.metrics;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.otomczak.empik.githubreporter.core.ports.MetricsPublisher;
import pl.otomczak.empik.githubreporter.core.ports.MetricsRepository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * Adapter for {@link MetricsPublisher} using {@link java.util.concurrent.ConcurrentMap} to buffer
 * requests. The buffer is periodically flushed to {@link MetricsRepository}. Implementation is
 * thread safe and fault-tolerant ({@link MetricsRepository} may be down periodically and metrics
 * won't be lost as long as this publisher is not destroyed.
 *
 * Flush interval (in ms) can be configured using <code>application.metrics.publisher.flush_interval_ms</code>
 */
@Component
public class BufferedMetricsPublisher implements MetricsPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(BufferedMetricsPublisher.class);

    private final ConcurrentHashMap<String, Integer> buffer = new ConcurrentHashMap<>();

    private final MetricsRepository metricsRepository;

    private BiFunction<String, Integer, Integer> decrementByOrRemove(int persistedCount) {
        return (u_, currentCount) -> currentCount <= persistedCount ? null : currentCount - persistedCount;
    }

    @Autowired
    public BufferedMetricsPublisher(MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }

    @PreDestroy
    @Scheduled(fixedRateString = "${application.metrics.publisher.flush_interval_ms}")
    void flush() {
        buffer.forEachKey(1_000, login -> {
            final int requestCount = buffer.get(login);
            if (requestCount > 0) {
                metricsRepository.incrementRequestCountBy(login, requestCount);
                buffer.computeIfPresent(login, decrementByOrRemove(requestCount));
            }
        });
    }

    @Override
    public void publishRequestFor(String login) {
        buffer.merge(login, 1, Integer::sum);
    }

}
