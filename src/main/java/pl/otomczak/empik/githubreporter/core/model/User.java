package pl.otomczak.empik.githubreporter.core.model;

import lombok.*;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;

@AllArgsConstructor(staticName = "of")
@Builder
@ToString
@EqualsAndHashCode
public class User {

    @Getter
    private final int id;
    @Getter
    @Nonnull
    private final String login;
    @Getter
    private final String name;
    @Getter
    @Nonnull
    private final String type;
    @Getter
    private final String avatarUrl;
    @Getter
    @Nonnull
    private final ZonedDateTime createdAt;
    @Getter
    private final int numberOfPublicRepos;
    @Getter
    private final int numberOfFollowers;
}
