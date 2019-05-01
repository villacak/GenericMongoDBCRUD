package au.com.mongodb.test.services.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/health")
@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
public class HealthService {


    @GET
    @Path("check")
    public Response checkHealth() {
        final String responseMessage = "Service working.";
        final Response response = Response.ok().entity(responseMessage).build();
        return response;
    }


}
