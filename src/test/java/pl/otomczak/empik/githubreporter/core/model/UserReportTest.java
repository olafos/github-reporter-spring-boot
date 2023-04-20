package pl.otomczak.empik.githubreporter.core.model;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

class UserReportTest {

    static final Percentage CALCULATIONS_ERROR = withPercentage(0.0001);

    final TestUserFactory users = new TestUserFactory();

    @Test
    void calculationsShouldEqualZeroWhenUserHasNoFollowers() {

        UserReport reportForAlice = UserReport.builder()
                .user(users.createUser("Alice", 0, 0))
                .build();

        assertThat(reportForAlice.getCalculations()).isEqualTo(0.0);
    }

    @Test
    void calculationsShouldBeCalculatedAccordingToFormula() {
        // Calculation formula: 6 / numberOfFollowers * (2 + numberOfPublicRepos)
        UserReport reportForAlice = UserReport.builder()
                .user(users.createUser("Alice", 1000, 50))
                .build();
        assertThat(reportForAlice.getCalculations()).isCloseTo(0.312, CALCULATIONS_ERROR);
    }
}