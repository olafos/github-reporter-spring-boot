package pl.otomczak.empik.githubreporter.adapters.github;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.otomczak.empik.githubreporter.core.model.User;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;

/**
 * Data structure used solely by {@link RestFacade} to represent GitHub REST API User.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class UserResponse {

    @JsonProperty("login")
    @Nonnull
    public String login;
    @JsonProperty("id")
    public int id;
    @JsonProperty("avatar_url")
    public String avatarUrl;
    @JsonProperty("type")
    @Nonnull
    public String type;
    @JsonProperty("name")
    public String name;
    @JsonProperty("public_repos")
    public int publicRepos;
    @JsonProperty("followers")
    public int followers;
    @JsonProperty("created_at")
    @Nonnull
    public ZonedDateTime createdAt;

    public User toUser() {
        return User.builder()
                .id(id)
                .login(login)
                .name(name)
                .type(type)
                .avatarUrl(avatarUrl)
                .createdAt(createdAt)
                .numberOfFollowers(followers)
                .numberOfPublicRepos(publicRepos)
                .build();
    }
}
