package au.com.mongodb.business;

import au.com.mongodb.model.ResponseMessage;
import javax.ws.rs.core.Response;

public class ReadyResponses {

    /**
     * badRequest - 400
     *
     * @return
     */
    public static Response badRequest() {
        final ResponseMessage responseMessage = new ResponseMessage(Response.Status.BAD_REQUEST.getStatusCode(),
                "Basic Validation Failed. Please check your payload.");
        final Response response = Response.status(Response.Status.BAD_REQUEST).entity(responseMessage.toJSON()).build();
        return response;
    }


    /**
     * serverError - 500
     * @return
     */
    public static Response serverError() {
        final ResponseMessage responseMessage = new ResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
        final Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseMessage.toJSON()).build();
        return response;
    }


    /**
     * successWithMessage - 200
     * @param message
     * @return
     */
    public static Response successWithMessage(final String message) {
        final ResponseMessage responseMessage = new ResponseMessage(Response.Status.OK.getStatusCode(), message);
        final Response response = Response.ok().entity(responseMessage.toJSON()).build();
        return response;
    }

}
