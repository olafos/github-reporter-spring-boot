package pl.otomczak.empik.githubreporter.adapters.github;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import pl.otomczak.empik.githubreporter.core.model.User;

import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static pl.otomczak.empik.githubreporter.adapters.github.RestFacade.TEST_USER;

@SpringBootTest
class RestFacadeTest {

    final static String NO_SUCH_USER = "~";

    @Autowired
    RestFacade github;

    @Test
    void getUserShouldLoadUserDataFromGithub() {
        assumeTrue(hasRemainingRequests());
        final User user = github.getUser(TEST_USER);
        assertThat(user.getId()).isEqualTo(583231L);
        assertThat(user.getLogin()).isEqualTo(TEST_USER);
        assertThat(user.getName()).isEqualTo("The Octocat");
        assertThat(user.getAvatarUrl()).startsWith("https://avatars.githubusercontent.com");
        assertThat(user.getType()).isEqualTo("User");
        assertThat(user.getCreatedAt()).isEqualTo(ZonedDateTime.parse("2011-01-25T18:44:36Z[UTC]"));
        assertThat(user.getNumberOfFollowers()).isGreaterThan(0);
        assertThat(user.getNumberOfPublicRepos()).isGreaterThan(0);
    }

    @Test
    void getUserShouldThrowNotFoundIfUserDoesNotExist() {
        assumeTrue(hasRemainingRequests());
        assertThrows(HttpClientErrorException.NotFound.class, () -> github.getUser(NO_SUCH_USER));
    }

    @Test
    void getUserShouldThrowForbiddenIfRateLimitIsExceeded() {
        assumeFalse(hasRemainingRequests());
        assertThrows(HttpClientErrorException.Forbidden.class, () -> github.getUser(TEST_USER));
    }

    boolean hasRemainingRequests() {
        return !github.getRateLimits().isExceeded();
    }
}