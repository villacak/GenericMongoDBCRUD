package au.com.mongodb.main;

import au.com.mongodb.cache.MongoDBCRUDCacheUtils;
import au.com.mongodb.constants.Constant;
import au.com.mongodb.services.v1.filters.BasicFilter;
import au.com.mongodb.services.v1.health.HealthHelper;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.EncodingFilter;

import javax.cache.Cache;
import javax.ws.rs.ApplicationPath;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationPath("api")
public class Application extends ResourceConfig {

    private Cache<String, String> cache;

    public Application() {
        packages("au.com.mongodb.services.v1.health", "au.com.mongodb.services.v1.crud", "au.com.mongodb.services.v1.filters");
        register(EntityFilteringFeature.class);
        register(BasicFilter.class);

        // Some logs to see request been received
        register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));
        EncodingFilter.enableFor(this, GZipEncoder.class);
        property(ServerProperties.TRACING, "ALL");


        final MongoDBCRUDCacheUtils cacheUtils = new MongoDBCRUDCacheUtils(Constant.CACHE_NAME_DEFAULT);
        cacheUtils.loadBasicCache();

        final HealthHelper healthHelper = new HealthHelper();
        healthHelper.checkifMongoIsRunning();
    }
}

