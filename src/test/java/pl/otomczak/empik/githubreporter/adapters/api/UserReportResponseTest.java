package pl.otomczak.empik.githubreporter.adapters.api;

import org.junit.jupiter.api.Test;
import pl.otomczak.empik.githubreporter.core.model.TestUserFactory;
import pl.otomczak.empik.githubreporter.core.model.User;
import pl.otomczak.empik.githubreporter.core.model.UserReport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserReportResponseTest {

    TestUserFactory userFactory = new TestUserFactory();

    @Test
    void shouldBePopulatedFromUserReport() {
        final User alice = userFactory.createUser("Alice", 1, 2);
        final UserReport report = UserReport.builder()
                .user(alice)
                .build();

        final UserReportResponse response = UserReportResponse.of(report);
        assertThat(response.getId()).isEqualTo(alice.getId());
        assertThat(response.getLogin()).isEqualTo(alice.getLogin());
        assertThat(response.getName()).isEqualTo(alice.getName());
        assertThat(response.getType()).isEqualTo(alice.getType());
        assertThat(response.getAvatarUrl()).isEqualTo(alice.getAvatarUrl());
        assertThat(response.getCreatedAt()).isEqualTo(alice.getCreatedAt());
        assertThat(response.getCalculations()).isGreaterThan(0.0);
    }
}