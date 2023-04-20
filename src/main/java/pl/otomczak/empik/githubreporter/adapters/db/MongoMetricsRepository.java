package pl.otomczak.empik.githubreporter.adapters.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.otomczak.empik.githubreporter.core.ports.MetricsRepository;

/**
 * {@link MetricsRepository} adapter using MongoDB. Database connection can be configured
 * using standard Spring Boot configuration in <code>spring.data.mongodb.*</code>.
 * Database, collection used as the repository can be configured using:
 * <code>application.metrics.repository.database</code> and
 * <code>application.metrics.repository.collection</code> respectively.
 * Data is stored in {@link MongoMetricsRepository#FIELD_LOGIN} and {@link MongoMetricsRepository#FIELD_REQUEST_COUNT} fields.
 * <p>
 * This repository uses only atomic operations.
 */
@Component
public class MongoMetricsRepository implements MetricsRepository {

    public static final String FIELD_ID = "_id";
    public static final String FIELD_LOGIN = "LOGIN";
    public static final String FIELD_REQUEST_COUNT = "REQUEST_COUNT";
    protected final UpdateOptions UPDATE_OPTIONS = new UpdateOptions().upsert(true);
    protected final String databaseName;
    protected final String collectionName;
    protected final MongoClient mongoClient;


    public MongoMetricsRepository(@Autowired MongoClient client,
                                  @Value("${application.metrics.repository.database}") String databaseName,
                                  @Value("${application.metrics.repository.collection}") String collectionName) {
        this.mongoClient = client;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }


    @Override
    public void incrementRequestCountBy(String login, long count) {

        final Bson filter = Filters.eq(FIELD_ID, login);
        final Bson update = Updates.combine(
                Updates.inc(FIELD_REQUEST_COUNT, count),
                Updates.set(FIELD_LOGIN, login)
        );
        mongoClient.getDatabase(databaseName).getCollection(collectionName)
                .updateOne(filter, update, UPDATE_OPTIONS)
                .getModifiedCount();
    }

    @Override
    public long getRequestCount(String login) {

        final Bson filter = Filters.eq(FIELD_ID, login);
        final Bson select = Projections.include(FIELD_REQUEST_COUNT);
        final Document result = mongoClient.getDatabase(databaseName).getCollection(collectionName)
                .find(filter)
                .projection(select)
                .first();
        return result == null ? 0L : result.getLong(FIELD_REQUEST_COUNT);
    }
}
