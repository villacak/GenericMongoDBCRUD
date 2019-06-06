package au.com.mongodb.services.v1.health;

import au.com.mongodb.model.ResponseMessage;
import jdk.net.SocketFlow;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/health")
@Consumes(MediaType.WILDCARD)
@Produces({MediaType.APPLICATION_JSON})
public class HealthService {


    @GET
    @Path("check")
    public Response checkHealth() {
        final ResponseMessage rs = new ResponseMessage(Response.Status.OK.getStatusCode(), "Service working.");
        final Response response = Response.ok().entity(rs.toJSON()).build();
        return response;
    }


    @GET
    @Path("checkMongoDBConnection")
    public Response checkMongoDBConnection() {
        final String responseMessage;
        final Response response;
        final HealthHelper helper = new HealthHelper();
        if (helper.checkifMongoIsRunning()) {
            final ResponseMessage rs = new ResponseMessage(Response.Status.OK.getStatusCode(), "MongoDB is Up&Running.");
            response = Response.ok().entity(rs.toJSON()).build();
        } else {
            final ResponseMessage rs = new ResponseMessage(Response.Status.OK.getStatusCode(), "MongoDB is down.");
            response = Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(rs.toJSON()).build();
        }
        return response;
    }
}
