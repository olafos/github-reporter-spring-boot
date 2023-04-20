package pl.otomczak.empik.githubreporter.core.ports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.otomczak.empik.githubreporter.core.model.UserReport;

/**
 * Application service.
 */
public class GithubReportService {

    private final static Logger LOG = LoggerFactory.getLogger(GithubReportService.class);

    private final GithubFacade github;
    private final MetricsPublisher metricsPublisher;

    public GithubReportService(GithubFacade githubFacade, MetricsPublisher metricsPublisher) {
        this.github = githubFacade;
        this.metricsPublisher = metricsPublisher;
    }

    public UserReport getUserReport(String login) {
        try {
            return UserReport.of(github.getUser(login));
        } finally {
            metricsPublisher.publishRequestFor(login);
        }
    }
}
