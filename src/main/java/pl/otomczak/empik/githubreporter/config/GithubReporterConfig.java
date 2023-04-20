package pl.otomczak.empik.githubreporter.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.otomczak.empik.githubreporter.core.ports.GithubFacade;
import pl.otomczak.empik.githubreporter.core.ports.GithubReportService;
import pl.otomczak.empik.githubreporter.core.ports.MetricsPublisher;

@Configuration
@EnableScheduling
@EnableCaching
public class GithubReporterConfig {

    @Bean
    public GithubReportService githubReportService(GithubFacade githubFacade, MetricsPublisher metricsPublisher) {
        return new GithubReportService(githubFacade, metricsPublisher);
    }
}
