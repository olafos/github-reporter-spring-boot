package pl.otomczak.empik.githubreporter.adapters.db;

import pl.otomczak.empik.githubreporter.core.ports.MetricsRepository;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryMetricsRepository implements MetricsRepository {

    private final Random random = new Random();
    private final double failureRatio;

    private final ConcurrentMap<String, Long> collection = new ConcurrentHashMap<>();

    public InMemoryMetricsRepository() {
        this(0.0);
    }

    public InMemoryMetricsRepository(double failureRatio) {
        if (failureRatio < 0.0 || failureRatio > 1.0) {
            throw new IllegalArgumentException("failure ratio should be [0, 1]");
        }
        this.failureRatio = failureRatio;
    }

    public long getRequestCount(String login) {
        return collection.getOrDefault(login, 0L);
    }

    @Override
    public void incrementRequestCountBy(String login, long count) {
        if (failureRatio > 0.0 && random.nextDouble() <= failureRatio) {
            throw new IllegalStateException("Failure!");
        }
        collection.merge(login, count, Long::sum);
    }
}
