package pl.otomczak.empik.githubreporter.adapters.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import pl.otomczak.empik.githubreporter.adapters.github.RateLimits;
import pl.otomczak.empik.githubreporter.core.ports.GithubReportService;

/**
 * Adapter for {@link GithubReportService} implemented as REST API endpoint.
 */
@RestController
public class GithubReportEndpoint {

    private final GithubReportService service;

    public GithubReportEndpoint(@Autowired GithubReportService service) {
        this.service = service;
    }

    @GetMapping("/users/{login}")
    public UserReportResponse getReport(@PathVariable("login") final String login) {
        try {
            return UserReportResponse.of(service.getUserReport(login));
        } catch (HttpStatusCodeException ex) {
            throw translateRestException(ex);
        }
    }

    ErrorResponseException translateRestException(HttpStatusCodeException ex) {
        final RateLimits rateLimits = RateLimits.fromResponseHeaders(ex.getResponseHeaders());
        HttpStatusCode code = ex.getStatusCode();
        if (ex instanceof HttpClientErrorException.Forbidden && rateLimits.isExceeded()) {
            code = HttpStatus.TOO_MANY_REQUESTS;
        }
        final ProblemDetail details = ProblemDetail.forStatusAndDetail(code, ex.getResponseBodyAsString());
        return new ErrorResponseException(code, details, ex);
    }
}
