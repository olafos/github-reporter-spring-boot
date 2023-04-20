package pl.otomczak.empik;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.otomczak.empik.githubreporter.adapters.github.RestFacade;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static pl.otomczak.empik.githubreporter.adapters.github.Assumptions.assumeRateLimitNotExceeded;
import static pl.otomczak.empik.githubreporter.adapters.github.RestFacade.TEST_USER;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class GithubReporterApplicationTests {

    @Value(value = "${local.server.port}")
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    RestFacade github;

    @Test
    void userReportShouldWorkAccordingToTaskDescription() {
        assumeRateLimitNotExceeded(github);
        final Map<String, Object> payload = this.restTemplate.getForObject(
                getExpectedEndpointPath(TEST_USER),
                Map.class);
        assertAll("Stwórz RESTowy serwis, który zwróci informacje:",
                () -> assertThat(payload).isNotNull(),
                () -> assertThat(payload.get("id")).describedAs("identyfikator").isInstanceOf(Integer.class),
                () -> assertThat(payload.get("login")).describedAs("login").isInstanceOf(String.class),
                () -> assertThat(payload.get("name")).describedAs("nazwa").isInstanceOf(String.class),
                () -> assertThat(payload.get("type")).describedAs("typ").isInstanceOf(String.class),
                () -> assertThat(payload.get("avatarUrl")).describedAs("URL do avatara").isInstanceOf(String.class),
                () -> assertThat(payload.get("createdAt")).describedAs("datę stworzenia").isInstanceOf(String.class),
                () -> assertThat(payload.get("calculations")).describedAs("obliczenia").isInstanceOf(Double.class)
        );
    }

    String getExpectedEndpointPath(String login) {
        return String.format("http://localhost:%d/users/%s", port, login);
    }
}
