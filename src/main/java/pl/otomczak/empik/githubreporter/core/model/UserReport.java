package pl.otomczak.empik.githubreporter.core.model;

import lombok.*;

import javax.annotation.Nonnull;

@AllArgsConstructor(staticName = "of")
@Builder
@EqualsAndHashCode
public class UserReport {

    @Getter
    @Nonnull
    private final User user;

    public double getCalculations() {
        if (user.getNumberOfFollowers() > 0) {
            return 6.0 / user.getNumberOfFollowers() * (2 + user.getNumberOfPublicRepos());
        }
        return 0.0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserReport(");
        sb.append("id=").append(user.getId()).append(", ");
        sb.append("login=").append(user.getLogin()).append(", ");
        sb.append("name=").append(user.getName()).append(", ");
        sb.append("type=").append(user.getType()).append(", ");
        sb.append("avatarUrl=").append(user.getAvatarUrl()).append(", ");
        sb.append("createdAt=").append(user.getCreatedAt()).append(", ");
        sb.append("calculations=").append(getCalculations());
        sb.append(')');
        return sb.toString();
    }
}
