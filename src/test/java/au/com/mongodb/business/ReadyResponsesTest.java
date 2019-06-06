//package au.com.mongodb.business;
//
//import au.com.mongodb.HelperTest;
//import au.com.mongodb.enums.EventSearchField;
//import au.com.mongodb.mapper.SchemaMapper;
//import au.com.mongodb.model.EventModel;
//import au.com.mongodb.persistence.entities.Event;
//import au.com.mongodb.services.v1.health.HealthHelper;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.ws.rs.core.Response;
//
//import java.io.IOException;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class ReadyResponsesTest {
//
//    private Event event;
//    private EventModel model;
//    private ReadyResponses business;
//    private ObjectMapper mapper;
//    EventModel saved = null;
//    EventModel updated = null;
//
//
//    @Before
//    public void startup() throws Exception {
//        final HealthHelper healthHelper = new HealthHelper();
//        boolean isRunning = healthHelper.checkifMongoIsRunning();
//
//        if (isRunning) {
//            final HelperTest helperTest = new HelperTest();
//            event = helperTest.getEventEntityForTest();
//            model = SchemaMapper.MAPPER.mapSchemaEntityToSchemaModel(event);
//            business = new ReadyResponses();
//            mapper = new ObjectMapper();
//        } else {
//            throw new Exception("Local MongoDB not Running");
//        }
//
//    }
//
//    @After
//    public void cleanup() {
//        if (saved != null && updated == null) {
//            business.deleteSingleEvent(saved.getPrimaryKey());
//        }
//        if (updated != null) {
//            business.deleteSingleEvent(updated.getPrimaryKey());
//        }
//        event = null;
//        model = null;
//        business = null;
//        mapper = null;
//        saved = null;
//        updated = null;
//    }
//
//
//    @Test
//    public void persistSingleEventSuccessTest() throws IOException {
//        model.setPrimaryKey(null);
//        Response response = business.persistSingleEvent(model, true);
//        int httpCode = response.getStatus();
//        String jsonResponse = (String) response.getEntity();
//        saved = mapper.readValue(jsonResponse, EventModel.class);
//        assertTrue(httpCode == 200);
//        assertTrue(saved.getPrimaryKey() != null);
//    }
//
//    @Test
//    public void persistSingleEventFailTest() throws IOException {
//        model.setPrimaryKey(null);
//        Response response = business.persistSingleEvent(null, true);
//        int httpCode = response.getStatus();
//        String jsonResponse = (String) response.getEntity();
//        assertTrue(httpCode == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
//        assertTrue(jsonResponse != Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
//    }
//
//
//    @Test
//    public void updateSingleEventSuccessTest() throws IOException {
//        Response fromUpdate = null;
//        model.setPrimaryKey(null);
//        Response response = business.persistSingleEvent(model, true);
//        int httpCode = response.getStatus();
//        String jsonResponse = (String) response.getEntity();
//        saved = mapper.readValue(jsonResponse, EventModel.class);
//        assertTrue(httpCode == 200);
//        assertTrue(saved.getPrimaryKey() != null);
//
//        response = business.searchForEvents(saved.getPrimaryKey(), EventSearchField.ID);
//        httpCode = response.getStatus();
//        jsonResponse = (String) response.getEntity();
//
//        List<EventModel> models = mapper.readValue(jsonResponse, new TypeReference<List<EventModel>>() {
//        });
//        assertTrue(models != null && models.size() > 0);
//
//        updated = models.get(0);
//        updated.setAccountID("UpdatedAccountId");
//
//        fromUpdate = business.persistSingleEvent(updated, false);
//        httpCode = fromUpdate.getStatus();
//        jsonResponse = (String) fromUpdate.getEntity();
//        updated = mapper.readValue(jsonResponse, EventModel.class);
//        assertTrue(httpCode == 200);
//        assertTrue(updated.getAccountID().equals("UpdatedAccountId"));
//        assertEquals(updated.getPrimaryKey(), saved.getPrimaryKey());
//    }
//
//
//    @Test
//    public void searchFail() throws IOException {
//        Response response = business.searchForEvents("WrongKey", EventSearchField.ID);
//        int httpCode = response.getStatus();
//        String jsonResponse = (String) response.getEntity();
//        assertEquals(httpCode, Response.Status.OK.getStatusCode());
//        assertTrue(jsonResponse.contains("Nothing found."));
//
//        response = business.searchForEvents(null, EventSearchField.ID);
//        httpCode = response.getStatus();
//        jsonResponse = (String) response.getEntity();
//        assertEquals(httpCode, Response.Status.OK.getStatusCode());
//        assertTrue(jsonResponse.contains("Nothing found."));
//    }
//
//
//    @Test
//    public void deleteFail() {
//        Response response = business.deleteSingleEvent(null);
//        String jsonResponse = (String) response.getEntity();
//        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
//        assertTrue(jsonResponse.contains("Record not found for delete."));
//
//        response = business.deleteSingleEvent("TEST");
//        jsonResponse = (String) response.getEntity();
//        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
//        assertTrue(jsonResponse.contains("Record not found for delete."));
//    }
//
//
//    @Test
//    public void badRequest() {
//        Response resp = ReadyResponses.badRequest();
//        assertNotNull(resp);
//        assertEquals(resp.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
//    }
//
//
//    @Test
//    public void bserverError() {
//        Response resp = ReadyResponses.serverError();
//        assertNotNull(resp);
//        assertEquals(resp.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
//    }
//
//
//    @Test
//    public void successWithMessage() {
//        final String TEST = "TEST";
//        Response resp = ReadyResponses.successWithMessage(TEST);
//        assertNotNull(resp);
//        assertEquals(resp.getStatus(), Response.Status.OK.getStatusCode());
//        assertTrue(((String)resp.getEntity()).contains(TEST));
//    }
//}
