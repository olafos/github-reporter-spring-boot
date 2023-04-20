package pl.otomczak.empik.githubreporter.core.ports;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import pl.otomczak.empik.githubreporter.adapters.inmemory.InMemoryGithubFacade;
import pl.otomczak.empik.githubreporter.adapters.inmemory.InMemoryMetricsPublisher;
import pl.otomczak.empik.githubreporter.core.model.TestUserFactory;
import pl.otomczak.empik.githubreporter.core.model.User;
import pl.otomczak.empik.githubreporter.core.model.UserReport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GithubReportServiceTest {

    final TestUserFactory users = new TestUserFactory("2020-01-01");
    final InMemoryMetricsPublisher publisher = new InMemoryMetricsPublisher();
    final InMemoryGithubFacade githubFacade = new InMemoryGithubFacade();
    final GithubReportService service = new GithubReportService(githubFacade, publisher);

    {
        final User alice = users.createUser("Alice", 50, 100);
        githubFacade.register(alice);

        final User bob = users.createUser("Bob", 0, 10);
        githubFacade.register(bob);

        final User charlie = users.createUser("Charlie", 1, 0);
        githubFacade.register(charlie);
    }

    @Test
    void userReportShouldBeAggregatedFromGithubUser() {
        final UserReport report = service.getUserReport("alice");
        assertThat(report).isNotNull();
        assertThat(report.getUser()).isNotNull();
        assertThat(report.getUser().getLogin()).isEqualTo("alice");
        assertThat(report.getCalculations()).isNotEqualTo(0.0);
    }

    @Test
    void notFoundErrorsFromGithubShouldBePropagated() {
        final HttpClientErrorException error = assertThrows(HttpClientErrorException.class,
                () -> service.getUserReport("danny")
        );
        assertThat(error.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void rateLimitErrorsFromGithubShouldBePropagated() {
        final HttpClientErrorException error = assertThrows(HttpClientErrorException.class,
                () -> service.getUserReport(InMemoryGithubFacade.LOGIN_FOR_RATE_LIMIT_ERROR)
        );
        assertThat(error.getStatusCode().value()).isEqualTo(403);
    }

    @Test
    void gettingUserReportSuccessfullyShouldIncrementRequestCount() {
        service.getUserReport("alice");
        service.getUserReport("bob");
        service.getUserReport("bob");
        assertThat(publisher.getRequestCount("charlie")).isEqualTo(0);
        assertThat(publisher.getRequestCount("alice")).isEqualTo(1);
        assertThat(publisher.getRequestCount("bob")).isEqualTo(2);
    }

    @Test
    void failedAttemptToGetUserReportShouldAlsoIncrementRequestCount() {
        assertThrows(Exception.class, () -> service.getUserReport("danny"));
        assertThat(publisher.getRequestCount("danny")).isEqualTo(1);
    }

}