package pl.otomczak.empik.githubreporter.adapters.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.otomczak.empik.githubreporter.core.ports.MetricsRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class MongoMetricsRepositoryTest {

    @Autowired
    MetricsRepository repository;

    @Test
    void shouldIncrementUserRequestCount() {
        long aliceCount = repository.getRequestCount("alice");
        repository.incrementRequestCountBy("alice", 3);
        long newAliceCount = repository.getRequestCount("alice");
        assertThat(newAliceCount).isEqualTo(aliceCount + 3);
    }
}