package pl.otomczak.empik.githubreporter.adapters.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.otomczak.empik.githubreporter.core.model.User;
import pl.otomczak.empik.githubreporter.core.ports.GithubFacade;

import java.util.Collections;
import java.util.Map;

import static pl.otomczak.empik.githubreporter.adapters.github.Headers.HEADER_API_VERSION;

/**
 * {@link GithubFacade} adapter using Spring {@link RestTemplate}. This adapter optionally
 * caches API calls. The cache is configured by <code>spring.cache.caffeine.spec.*</code>
 * properties. Set <code>spring.cache.type=none</code> to disable it completely.
 */
@Component
public class RestFacade implements GithubFacade {

    public static final String TEST_USER = "octocat";

    private final RestTemplate restTemplate;

    @Autowired
    public RestFacade(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .rootUri("https://api.github.com")
                .defaultHeader(HEADER_API_VERSION, "2022-11-28")
                .build();
    }

    @Override
    @Cacheable("github-users")
    public User getUser(String login) {

        return getUserResponse(login).getBody().toUser();
    }

    ResponseEntity<UserResponse> getUserResponse(String login) {

        final Map<String, String> params = Collections.singletonMap("login", login);

        return restTemplate.getForEntity("/users/{login}", UserResponse.class, params);
    }

    public RateLimits getRateLimits() {
        HttpHeaders headers;
        try {
            headers = getUserResponse(TEST_USER).getHeaders();
        } catch (HttpClientErrorException.Forbidden ex) {
            headers = ex.getResponseHeaders();
        }
        return RateLimits.fromResponseHeaders(headers);
    }
}
