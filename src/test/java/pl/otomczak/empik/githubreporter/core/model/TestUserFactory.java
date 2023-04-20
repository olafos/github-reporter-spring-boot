package pl.otomczak.empik.githubreporter.core.model;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TestUserFactory {

    private final Clock clock;

    public TestUserFactory(String fixedDateString) {
        this(Clock.fixed(Instant.parse(String.format("%sT00:00:00.000Z", fixedDateString)), ZoneId.of("UTC")));
    }

    public TestUserFactory(Clock clock) {
        this.clock = clock;
    }

    public TestUserFactory() {
        this(Clock.systemDefaultZone());
    }

    public User createUser(String firstName) {
        return createUser(firstName, 1, 10);
    }

    public User createUser(String firstName, int followers, int publicRepos) {
        final int id = firstName.hashCode();
        return User.builder()
                .id(id)
                .login(firstName.toLowerCase())
                .name(String.format("%s Johnson", firstName))
                .type("User")
                .avatarUrl(String.format("https://avatars.githubusercontent.com/u/%d?v=4", id))
                .createdAt(ZonedDateTime.now(clock))
                .numberOfFollowers(followers)
                .numberOfPublicRepos(publicRepos)
                .build();
    }
}
