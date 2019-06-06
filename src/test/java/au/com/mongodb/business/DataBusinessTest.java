package au.com.mongodb.business;

import au.com.mongodb.HelperTest;
import au.com.mongodb.model.JSONDataModel;
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

public class DataBusinessTest {


    private HelperTest helperTest;
    private JSONDataModel dataModel;
    private JSONDataModel tempDataModel;
    private List<JSONDataModel> dataModels;
    private DataBusiness business;
    private String jsonDataModel;
    private Response resp;
    private boolean savedOrUpdated;
    private ObjectMapper mapper;



    @Before
    public void setup() throws JsonProcessingException {
        helperTest = new HelperTest();
        business = new DataBusiness();
        tempDataModel = helperTest.getDataModelTest();
        jsonDataModel = tempDataModel.toJSON();
        mapper = new ObjectMapper();
    }


    @After
    public void cleanuo() {
        helperTest = null;
        if (dataModel != null && savedOrUpdated) {
            business.deleteSingleData(dataModel.getDataId());
            dataModel = null;
        }
        dataModels = null;
        business = null;
        tempDataModel = null;
        jsonDataModel = null;
        resp = null;
        savedOrUpdated = false;
        mapper = null;
    }


    @Test
    public void convertRequestToModelSuccess() {
        business = new DataBusiness();
        dataModel = business.convertRequestToModel(jsonDataModel);
        assertNotNull(dataModel);
        assertEquals(dataModel, tempDataModel);

        tempDataModel.setCreatedBy(null);
        jsonDataModel = tempDataModel.toJSON();
        dataModel = business.convertRequestToModel(jsonDataModel);
        assertNotNull(dataModel);
        assertTrue(dataModel.getCreatedBy() != null);
    }


    @Test
    public void convertRequestToModelFail() {
        business = new DataBusiness();
        dataModel = business.convertRequestToModel("toFail");
        assertNull(dataModel);
    }



    @Test
    public void persistAndUpdateSingleJSONDataSuccess() {
        // New records
        resp = business.persistSingleJSONData(tempDataModel, true);
        assertTrue(resp.getStatus() == 200);
        savedOrUpdated = true;

        final String respJSON = resp.getEntity().toString();
        dataModel = business.convertRequestToModel(respJSON);
        assertNotNull(dataModel);
        assertTrue(dataModel.getDataId() != null);

        // Update record
        dataModel.setCreatedBy("UpdateId");
        resp = business.persistSingleJSONData(dataModel, false);

        assertTrue(resp.getStatus() == 200);
        savedOrUpdated = true;
        final String respUpdatedJSON = resp.getEntity().toString();
        dataModel = business.convertRequestToModel(respUpdatedJSON);
        assertNotNull(dataModel);
        assertTrue(dataModel.getCreatedBy().equals("UpdateId"));
        assertTrue(dataModel.getVersion() == 2);
    }


    @Test
    public void deleteNotFound() throws IOException {
        final String dummyId = "saw12323wesdasd";
        resp = business.deleteSingleData(dummyId);
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        final String jsonRes = resp.getEntity().toString();
        final ResponseMessage responseMessage = mapper.readValue(jsonRes, ResponseMessage.class);
        assertEquals(responseMessage.getHttpCode(), 200);
        assertEquals(responseMessage.getMessage(), "Record not found for delete.");
    }


    @Test
    public void searchDataByIdSuccess() throws IOException {
        resp = business.persistSingleJSONData(tempDataModel, true);
        assertTrue(resp.getStatus() == 200);
        savedOrUpdated = true;

        final String respJSON = resp.getEntity().toString();
        dataModel = business.convertRequestToModel(respJSON);
        assertNotNull(dataModel);
        assertTrue(dataModel.getDataId() != null);

        resp = business.searchDataById("id", dataModel.getDataId());
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        jsonDataModel = resp.getEntity().toString();
        dataModels = mapper.readValue(jsonDataModel, new TypeReference<List<JSONDataModel>>() {});
        assertTrue(dataModels.size() > 0);

        tempDataModel = dataModels.get(0);
        assertEquals(dataModel.getDataId(), tempDataModel.getDataId());
        assertEquals(dataModel.getJson(), tempDataModel.getJson());
        assertTrue(dataModel.getMajorVersion() == tempDataModel.getMajorVersion());
        assertTrue(dataModel.getMinorVersion() == tempDataModel.getMinorVersion());


        resp = business.searchDataById("id", "nonExistingId");
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        final String jsonRes = resp.getEntity().toString();
        final ResponseMessage responseMessage = mapper.readValue(jsonRes, ResponseMessage.class);
        assertEquals(responseMessage.getHttpCode(), 200);
        assertEquals(responseMessage.getMessage(), "Nothing found.");
    }


    @Test
    public void searchDataByUserIdSchemaAndMajorVersionRangeSuccess() throws IOException {
        resp = business.persistSingleJSONData(tempDataModel, true);
        assertTrue(resp.getStatus() == 200);
        savedOrUpdated = true;

        final String respJSON = resp.getEntity().toString();
        dataModel = business.convertRequestToModel(respJSON);
        assertNotNull(dataModel);
        assertTrue(dataModel.getDataId() != null);

        resp = business.searchDataByUserIdSchemaAndMajorVersionRange(dataModel.getCreatedBy(), dataModel.getSchemaName(),
                                dataModel.getMajorVersion(), dataModel.getMinorVersion());
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        jsonDataModel = resp.getEntity().toString();
        dataModels = mapper.readValue(jsonDataModel, new TypeReference<List<JSONDataModel>>() {});
        assertTrue(dataModels.size() > 0);

        tempDataModel = dataModels.get(0);
        assertEquals(dataModel.getDataId(), tempDataModel.getDataId());
        assertEquals(dataModel.getJson(), tempDataModel.getJson());
        assertTrue(dataModel.getMajorVersion() == tempDataModel.getMajorVersion());
        assertTrue(dataModel.getMinorVersion() == tempDataModel.getMinorVersion());



        resp = business.searchDataByUserIdSchemaAndMajorVersionRange(dataModel.getCreatedBy(), dataModel.getSchemaName(),
                0, 0);
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        jsonDataModel = resp.getEntity().toString();
        dataModels = mapper.readValue(jsonDataModel, new TypeReference<List<JSONDataModel>>() {});
        assertTrue(dataModels.size() > 0);

        tempDataModel = dataModels.get(0);
        assertEquals(dataModel.getDataId(), tempDataModel.getDataId());
        assertEquals(dataModel.getJson(), tempDataModel.getJson());
        assertTrue(dataModel.getMajorVersion() == tempDataModel.getMajorVersion());
        assertTrue(dataModel.getMinorVersion() == tempDataModel.getMinorVersion());



        resp = resp = business.searchDataByUserIdSchemaAndMajorVersionRange("nonExistingUserId", dataModel.getSchemaName(),
                0, 0);
        assertNotNull(resp);
        assertTrue(resp.getStatus() == 200);

        final String jsonRes = resp.getEntity().toString();
        final ResponseMessage responseMessage = mapper.readValue(jsonRes, ResponseMessage.class);
        assertEquals(responseMessage.getHttpCode(), 200);
        assertEquals(responseMessage.getMessage(), "Nothing found.");

    }
}
