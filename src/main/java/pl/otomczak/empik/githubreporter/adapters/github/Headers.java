package pl.otomczak.empik.githubreporter.adapters.github;

/**
 * Custom headers used in GitHub REST API.
 */
public class Headers {
    public static final String HEADER_API_VERSION = "X-GitHub-Api-Version";
    public static final String HEADER_RATELIMIT_LIMIT = "X-RateLimit-Limit";
    public static final String HEADER_RATELIMIT_REMAINING = "X-RateLimit-Remaining";
    public static final String HEADER_RATELIMIT_USED = "X-RateLimit-Used";
    public static final String HEADER_RATELIMIT_RESET = "X-RateLimit-Reset";
}
