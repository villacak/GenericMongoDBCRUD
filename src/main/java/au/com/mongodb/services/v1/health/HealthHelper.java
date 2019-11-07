package au.com.mongodb.services.v1.health;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public class HealthHelper {

    private final String DEFAULT_DATABASE_NAME = "jpaNoSQLTestDB";
    private final String DEFAULT_COLLECTION_NAME = "CRUDTest";
    private final String HOST = "localhost";
    private final int DEFAULT_PORT = 27017;


    /**
     * Check if the MongoDB is up and running, when running local.
     * IF MongoDB is running in a separate server/ Instance/ VM then this
     * service will not work and the application will fail to start.
     *
     * @return
     */
    public boolean checkifMongoIsRunning() {
        MongoClient client = null;
        boolean toReturn = false;
        try {
            client = new MongoClient(HOST, DEFAULT_PORT);
            final MongoDatabase mongoDB = client.getDatabase(DEFAULT_DATABASE_NAME);
            MongoCollection collection = mongoDB.getCollection(DEFAULT_COLLECTION_NAME);
            final List<String> tempCollections = client.getDatabaseNames();
            if (!tempCollections.contains(DEFAULT_COLLECTION_NAME)) {
                mongoDB.createCollection(DEFAULT_COLLECTION_NAME);
            }
            final Document ping = new Document("ping", "1");
            final Document serverStatus = mongoDB.runCommand(ping);
            System.out.println(serverStatus.toJson());
            toReturn = true;
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            client.close();
            client = null;
        }
        return toReturn;
    }
}
