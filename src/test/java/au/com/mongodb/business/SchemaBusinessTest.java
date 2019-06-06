package au.com.mongodb.business;

import au.com.mongodb.HelperTest;
import au.com.mongodb.model.JSONSchemaModel;
import au.com.mongodb.model.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class SchemaBusinessTest {


    private HelperTest helperTest;
    private JSONSchemaModel schemaModel;
    private JSONSchemaModel tempSchemaModel;
    private List<JSONSchemaModel> schemaModels;
    private SchemaBusiness business;
    private String jsonSchemaModel;
    private Response resp;
    private boolean savedOrUpdated;
    private ObjectMapper mapper;



    @Before
    public void setup() throws JsonProcessingException {
        helperTest = new HelperTest();
        business = new SchemaBusiness();
        tempSchemaModel = helperTest.getSchemaModelTest();
        jsonSchemaModel = tempSchemaModel.toJSON();
        mapper = new ObjectMapper();
    }


    @After
    public void cleanuo() {
        helperTest = null;
        if (schemaModel != null && savedOrUpdated) {
            business.deleteSingleSchema(schemaModel.getId());
            schemaModel = null;
        }
        schemaModels = null;
        business = null;
        tempSchemaModel = null;
        jsonSchemaModel = null;
        resp = null;
        savedOrUpdated = false;
        mapper = null;
    }


    @Test
    public void convertRequestToModelSuccess() {
        business = new SchemaBusiness();
        schemaModel = business.convertRequestToModel(jsonSchemaModel);
        assertNotNull(schemaModel);
        assertEquals(schemaModel, tempSchemaModel);

        tempSchemaModel.setCreatedBy(null);
        jsonSchemaModel = tempSchemaModel.toJSON();
        schemaModel = business.convertRequestToModel(jsonSchemaModel);
        assertNotNull(schemaModel);
        assertTrue(schemaModel.getCreatedBy() != null);
    }


    @Test
    public void convertRequestToModelFail() {
        business = new SchemaBusiness();
        schemaModel = business.convertRequestToModel("toFail");
        assertNull(schemaModel);
    }



    @Test
    public void persistAndUpdateSingleJSONDataSuccess() {
        // New records
        resp = business.persistSingleSchema(tempSchemaModel, true);
        assertTrue(resp.getStatus() == 200);
        savedOrUpdated = true;

        final String respJSON = resp.getEntity().toString();
        schemaModel = business.convertRequestToModel(respJSON);
        assertNotNull(schemaModel);
        assertTrue(schemaModel.getId() != null);

        // Update record
        schemaModel.setCreatedBy("UpdateId");
        resp = business.persistSingleSchema(schemaModel, false);

        assertTrue(resp.getStatus() == 200);
        savedOrUpdated = true;
        final String respUpdatedJSON = resp.getEntity().toString();
        schemaModel = business.convertRequestToModel(respUpdatedJSON);
        assertNotNull(schemaModel);
        assertTrue(schemaModel.getCreatedBy().equals("UpdateId"));
        assertTrue(schemaModel.getVersion() == 2);
    }


    @Test
    public void deleteNotFound() throws IOException {
        final String dummyId = "saw12323wesdasd";
        resp = business.deleteSingleSchema(dummyId);
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        final String jsonRes = resp.getEntity().toString();
        final ResponseMessage responseMessage = mapper.readValue(jsonRes, ResponseMessage.class);
        assertEquals(responseMessage.getHttpCode(), 200);
        assertEquals(responseMessage.getMessage(), "Record not found for delete.");
    }


    @Test
    public void searchSchemaByIdSuccess() throws IOException {
        resp = business.persistSingleSchema(tempSchemaModel, true);
        assertTrue(resp.getStatus() == 200);
        savedOrUpdated = true;

        final String respJSON = resp.getEntity().toString();
        schemaModel = business.convertRequestToModel(respJSON);
        assertNotNull(schemaModel);
        assertTrue(schemaModel.getId() != null);

        resp = business.searchSchemasById("id", schemaModel.getId());
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        jsonSchemaModel = resp.getEntity().toString();
        schemaModels = mapper.readValue(jsonSchemaModel, new TypeReference<List<JSONSchemaModel>>() {});
        assertTrue(schemaModels.size() > 0);

        tempSchemaModel = schemaModels.get(0);
        assertEquals(schemaModel.getId(), tempSchemaModel.getId());
        assertEquals(schemaModel.getJson(), tempSchemaModel.getJson());
        assertTrue(schemaModel.getMajorVersion() == tempSchemaModel.getMajorVersion());
        assertTrue(schemaModel.getMinorVersion() == tempSchemaModel.getMinorVersion());


        resp = business.searchSchemasById("id", "nonExistingId");
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        final String jsonRes = resp.getEntity().toString();
        final ResponseMessage responseMessage = mapper.readValue(jsonRes, ResponseMessage.class);
        assertEquals(responseMessage.getHttpCode(), 200);
        assertEquals(responseMessage.getMessage(), "Nothing found.");
    }


    @Test
    public void searchSchemaBySchemaNameAndMajorMinorVersionSuccess() throws IOException {
        resp = business.persistSingleSchema(tempSchemaModel, true);
        assertTrue(resp.getStatus() == 200);
        savedOrUpdated = true;

        final String respJSON = resp.getEntity().toString();
        schemaModel = business.convertRequestToModel(respJSON);
        assertNotNull(schemaModel);
        assertTrue(schemaModel.getId() != null);

        resp = business.searchSchemasByNameAndVersion(schemaModel.getSchemaName(),
                                schemaModel.getMajorVersion(), schemaModel.getMinorVersion());
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        jsonSchemaModel = resp.getEntity().toString();
        schemaModels = mapper.readValue(jsonSchemaModel, new TypeReference<List<JSONSchemaModel>>() {});
        assertTrue(schemaModels.size() > 0);

        tempSchemaModel = schemaModels.get(0);
        assertEquals(schemaModel.getId(), tempSchemaModel.getId());
        assertEquals(schemaModel.getJson(), tempSchemaModel.getJson());
        assertTrue(schemaModel.getMajorVersion() == tempSchemaModel.getMajorVersion());
        assertTrue(schemaModel.getMinorVersion() == tempSchemaModel.getMinorVersion());


        resp = resp = business.searchSchemasByNameAndVersion("nonExistingUserId",
                0, 0);
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        final String jsonRes = resp.getEntity().toString();
        final ResponseMessage responseMessage = mapper.readValue(jsonRes, ResponseMessage.class);
        assertEquals(responseMessage.getHttpCode(), 200);
        assertEquals(responseMessage.getMessage(), "Nothing found.");
    }
}
