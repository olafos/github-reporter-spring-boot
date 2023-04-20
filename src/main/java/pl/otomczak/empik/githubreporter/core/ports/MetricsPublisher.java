package pl.otomczak.empik.githubreporter.core.ports;

/**
 * Port to metrics publisher (publishing user request events).
 */
public interface MetricsPublisher {

    void publishRequestFor(String login);
}
