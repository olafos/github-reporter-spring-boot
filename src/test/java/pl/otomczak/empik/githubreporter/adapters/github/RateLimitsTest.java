package pl.otomczak.empik.githubreporter.adapters.github;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimitsTest {

    @Test
    void shouldBeParsedFromResponseHeaders() {
        final HttpHeaders headers = new HttpHeaders();

        headers.add("x-ratelimit-limit", "60");
        headers.add("x-ratelimit-remaining", "0");
        headers.add("x-ratelimit-reset", "1682024920");
        headers.add("x-ratelimit-used", "60");

        final RateLimits limits = RateLimits.fromResponseHeaders(headers);
        assertThat(limits.getLimit()).isEqualTo(60);
        assertThat(limits.getRemaining()).isEqualTo(0);
        assertThat(limits.getUsed()).isEqualTo(60);
        assertThat(limits.getResetTime()).isEqualTo(ZonedDateTime.parse("2023-04-20T21:08:40Z"));
    }

    @Test
    void shouldBeExceededWhenRemainingRequestsIsZero() {
        final HttpHeaders headers = new HttpHeaders();

        headers.add("x-ratelimit-limit", "60");
        headers.add("x-ratelimit-remaining", "0");
        headers.add("x-ratelimit-reset", "1682024920");
        headers.add("x-ratelimit-used", "60");

        final RateLimits limits = RateLimits.fromResponseHeaders(headers);
        assertThat(limits.isExceeded()).isTrue();
    }

    @Test
    void shouldNotBeExceededWhenRemainingRequestsIsGreaterThanZero() {
        final HttpHeaders headers = new HttpHeaders();

        headers.add("x-ratelimit-limit", "60");
        headers.add("x-ratelimit-remaining", "10");
        headers.add("x-ratelimit-reset", "1682024920");
        headers.add("x-ratelimit-used", "50");

        final RateLimits limits = RateLimits.fromResponseHeaders(headers);
        assertThat(limits.isExceeded()).isFalse();
    }
}