package au.com.mongodb.business;

import au.com.mongodb.model.ResponseMessage;
import org.junit.Test;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReadyResponsesTest {

    @Test
    public void badRequestTest() {
        final ResponseMessage responseMessage = new ResponseMessage(Response.Status.BAD_REQUEST.getStatusCode(),
                "Basic Validation Failed. Please check your payload.");

        final Response badRequest = ReadyResponses.badRequest();
        assertNotNull(badRequest);
        assertEquals(badRequest.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        assertEquals(responseMessage.toJSON(), badRequest.getEntity().toString());
    }


    @Test
    public void serverErrorTest() {
        final ResponseMessage responseMessage = new ResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());

        final Response serverErrorResponse = ReadyResponses.serverError();
        assertNotNull(serverErrorResponse);
        assertEquals(serverErrorResponse.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertEquals(responseMessage.toJSON(), serverErrorResponse.getEntity().toString());
    }


    @Test
    public void successWithMessageTest() {
        final ResponseMessage responseMessage = new ResponseMessage(Response.Status.OK.getStatusCode(),
                "All Good!");

        final Response successWithMessageResponse = ReadyResponses.successWithMessage("All Good!");
        assertNotNull(successWithMessageResponse);
        assertEquals(successWithMessageResponse.getStatus(), Response.Status.OK.getStatusCode());
        assertEquals(responseMessage.toJSON(), successWithMessageResponse.getEntity().toString());
    }
}
