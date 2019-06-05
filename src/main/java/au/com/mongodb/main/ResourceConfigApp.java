package au.com.mongodb.main;

import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.EncodingFilter;

import javax.ws.rs.ApplicationPath;

public class ResourceConfigApp {

    @ApplicationPath("/rest")
    public class Application extends ResourceConfig {
        public Application() {
            packages("au.com.mongodb.services.v1.health", "au.com.mongodb.services.v1.crud");
            register(EntityFilteringFeature.class);

            // Some logs to see request been received
//            register(new LoggingFilter(java.util.logging.Logger.getLogger("STANDARD_MESSAGE"), true));
            EncodingFilter.enableFor(this, GZipEncoder.class);
            property(ServerProperties.TRACING, "ALL");
        }
    }
}
