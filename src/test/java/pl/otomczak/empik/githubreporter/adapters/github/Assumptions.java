package pl.otomczak.empik.githubreporter.adapters.github;

import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public abstract class Assumptions {

    public static boolean rateLimitExceeded(RestFacade github) {
        return github.getRateLimits().isExceeded();
    }

    public static void assumeRateLimitExceeded(RestFacade github) {
        assumeTrue(rateLimitExceeded(github));
    }

    public static void assumeRateLimitNotExceeded(RestFacade github) {
        assumeFalse(rateLimitExceeded(github));
    }
}
