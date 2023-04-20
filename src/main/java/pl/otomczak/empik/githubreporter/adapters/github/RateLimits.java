package pl.otomczak.empik.githubreporter.adapters.github;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static pl.otomczak.empik.githubreporter.adapters.github.Headers.*;

/**
 * Status of GitHub REST API rate limits (returned in every response). Built by parsing
 * response headers.
 */
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RateLimits {

    private final int limit;
    private final int remaining;
    private final int used;
    private final ZonedDateTime resetTime;

    private static ZonedDateTime getFirstEpochTime(HttpHeaders headers, String name) {
        final String value = headers.getFirst(name);
        if (value == null) {
            return null;
        }
        final ZoneId zoneId = LocaleContextHolder.getTimeZone().toZoneId();
        return Instant.ofEpochSecond(Integer.parseInt(value)).atZone(zoneId);
    }

    private static int getFirstInt(HttpHeaders headers, String name) {
        final String value = headers.getFirst(name);
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }

    public static RateLimits fromResponseHeaders(HttpHeaders headers) {
        final int limit = getFirstInt(headers, HEADER_RATELIMIT_LIMIT);
        final int remaining = getFirstInt(headers, HEADER_RATELIMIT_REMAINING);
        final int used = getFirstInt(headers, HEADER_RATELIMIT_USED);
        final ZonedDateTime resetOn = getFirstEpochTime(headers, HEADER_RATELIMIT_RESET);
        return RateLimits.builder()
                .limit(limit)
                .remaining(remaining)
                .used(used)
                .resetTime(resetOn)
                .build();
    }

    public boolean isExceeded() {
        return remaining == 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RateLimits(");
        sb.append(used).append("/").append(limit);
        sb.append(", resetTime: ").append(resetTime);
        sb.append(')');
        return sb.toString();
    }
}
