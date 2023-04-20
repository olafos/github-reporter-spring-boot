package pl.otomczak.empik.githubreporter.adapters.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import pl.otomczak.empik.githubreporter.core.model.UserReport;

import java.time.ZonedDateTime;

/**
 * Output data structure used solely by {@link GithubReportEndpoint}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
class UserReportResponse {

    private int id;
    private String login;
    private String name;
    private String type;
    private String avatarUrl;
    private ZonedDateTime createdAt;
    private double calculations;

    public static UserReportResponse of(UserReport userReport) {
        return UserReportResponse.builder()
                .id(userReport.getUser().getId())
                .login(userReport.getUser().getLogin())
                .name(userReport.getUser().getName())
                .type(userReport.getUser().getType())
                .avatarUrl(userReport.getUser().getAvatarUrl())
                .createdAt(userReport.getUser().getCreatedAt())
                .calculations(userReport.getCalculations())
                .build();
    }
}
