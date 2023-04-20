package pl.otomczak.empik.githubreporter.core.ports;

/**
 * Port to metrics repository (updating user request counts in persistent storage).
 */
public interface MetricsRepository {

    void incrementRequestCountBy(String login, long count);

    long getRequestCount(String login);
}
