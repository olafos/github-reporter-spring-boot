package pl.otomczak.empik.githubreporter.adapters.github;

import org.junit.jupiter.api.Test;
import pl.otomczak.empik.githubreporter.core.model.User;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

    @Test
    void shouldCreateUserModel() {
        final UserResponse response = new UserResponse();
        response.login = "octocat";
        response.id = 583231;
        response.avatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4";
        response.type = "User";
        response.name = "The Octocat";
        response.publicRepos = 8;
        response.followers = 8972;
        response.createdAt = ZonedDateTime.parse("2011-01-25T18:44:36Z");

        final User user = response.toUser();
        assertThat(user.getLogin()).isEqualTo(response.login);
        assertThat(user.getId()).isEqualTo(response.id);
        assertThat(user.getAvatarUrl()).isEqualTo(response.avatarUrl);
        assertThat(user.getType()).isEqualTo(response.type);
        assertThat(user.getName()).isEqualTo(response.name);
        assertThat(user.getCreatedAt()).isEqualTo(response.createdAt);
        assertThat(user.getNumberOfPublicRepos()).isEqualTo(response.publicRepos);
        assertThat(user.getNumberOfFollowers()).isEqualTo(response.followers);
    }
}