package au.com.mongodb.services.v1.validations;


import au.com.mongodb.HelperTest;
import au.com.mongodb.business.SchemaBusiness;
import au.com.mongodb.model.JSONDataModel;
import au.com.mongodb.model.JSONSchemaModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

public class CrudMongoDbValidationsTest {

    private HelperTest helperTest;
    private CrudMongoDbValidations validations;
    private String schemaJson;
    private String dataJson;
    private Response resp;
    private boolean isSavedOrUpdated;
    private JSONSchemaModel tempSchemaModel;
    private SchemaBusiness business;

    @Before
    public void setup() throws JsonProcessingException {
        helperTest = new HelperTest();
        validations = new CrudMongoDbValidations();
        business = new SchemaBusiness();

        tempSchemaModel = helperTest.getSchemaModelTest();
        final JSONDataModel tempDataModel = helperTest.getDataModelTest();

        schemaJson = tempSchemaModel.toJSON();
        dataJson = tempDataModel.toJSON();
    }


    @After
    public void cleanup() {
        if (isSavedOrUpdated) {
            business.deleteSingleSchema(tempSchemaModel.getId());
            isSavedOrUpdated = false;
        }
        validations = null;
        business = null;
        schemaJson = null;
        dataJson = null;
        resp = null;
        tempSchemaModel = null;
    }




    @Test
    public void validateSchemaTest() {
        resp = validations.validateSchema(schemaJson);
        assertNull(resp);


        resp = validations.validateSchema("{\"test\": \"blah\"}");
        assertNotNull(resp);
        assertTrue(resp.getStatus() == Response.Status.BAD_REQUEST.getStatusCode());


        resp = validations.validateSchema(null);
        assertNotNull(resp);
        assertTrue(resp.getStatus() == Response.Status.BAD_REQUEST.getStatusCode());
    }


    @Test
    public void validateDataTest() {
        resp = business.persistSingleSchema(tempSchemaModel, true);
        assertTrue(resp.getStatus() == 200);
        isSavedOrUpdated = true;
        final String respJSON = resp.getEntity().toString();
        final JSONSchemaModel schemaModel = business.convertRequestToModel(respJSON);
        assertNotNull(schemaModel);
        assertTrue(schemaModel.getId() != null);
        tempSchemaModel.setId(schemaModel.getId());


        resp = validations.validateData(true, dataJson);
        assertNull(resp);

        resp = validations.validateData(true,"{\"test\": \"blah\"}");
        assertNotNull(resp);
        assertTrue(resp.getStatus() == Response.Status.BAD_REQUEST.getStatusCode());

        resp = validations.validateData(false,"{\"test\": \"blah\"}");
        assertNotNull(resp);
        assertTrue(resp.getStatus() == Response.Status.BAD_REQUEST.getStatusCode());

        resp = validations.validateSchema(null);
        assertNotNull(resp);
        assertTrue(resp.getStatus() == Response.Status.BAD_REQUEST.getStatusCode());
    }


    @Test
    public void validateMandatoryStringTest() {
        boolean isValid = validations.validateMandatoryString("Test");
        assertTrue(isValid);

        isValid = validations.validateMandatoryString(null);
        assertFalse(isValid);

        isValid = validations.validateMandatoryString("");
        assertFalse(isValid);
    }
}
