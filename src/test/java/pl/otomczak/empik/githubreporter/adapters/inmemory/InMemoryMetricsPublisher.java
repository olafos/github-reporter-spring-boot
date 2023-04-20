package pl.otomczak.empik.githubreporter.adapters.inmemory;

import pl.otomczak.empik.githubreporter.core.ports.MetricsPublisher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryMetricsPublisher implements MetricsPublisher {

    private final ConcurrentMap<String, Long> inMemoryStore = new ConcurrentHashMap<>();

    @Override
    public void publishRequestFor(String login) {
        inMemoryStore.merge(login, 1L, Long::sum);
    }

    public long getRequestCount(String login) {
        return inMemoryStore.getOrDefault(login, 0L);
    }
}
