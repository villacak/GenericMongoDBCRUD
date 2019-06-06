package au.com.mongodb.business;

import au.com.mongodb.HelperTest;
import au.com.mongodb.model.JSONDataModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
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



    @Before
    public void setup() throws JsonProcessingException {
        helperTest = new HelperTest();
        business = new DataBusiness();
        tempDataModel = helperTest.getDataModelTest();
        jsonDataModel = tempDataModel.toJSON();
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

}
