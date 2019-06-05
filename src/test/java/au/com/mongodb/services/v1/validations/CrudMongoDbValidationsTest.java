package au.com.mongodb.services.v1.validations;

import au.com.mongodb.HelperTest;
import au.com.mongodb.enums.EventSearchField;
import au.com.mongodb.mapper.SchemaMapper;
import au.com.mongodb.model.EventModel;
import au.com.mongodb.persistence.entities.Event;
import au.com.mongodb.services.v1.health.HealthHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CrudMongoDbValidationsTest {

    private Event event;
    private EventModel model;
    private CrudMongoDbValidations validations;


    @Before
    public void startup() throws Exception {
        final HealthHelper healthHelper = new HealthHelper();
        boolean isRunning = healthHelper.checkifMongoIsRunning();

        if (isRunning) {
            final HelperTest helperTest = new HelperTest();
            event = helperTest.getEventEntityForTest();
            model = SchemaMapper.MAPPER.mapSchemaEntityToSchemaModel(event);
            validations = new CrudMongoDbValidations();
        } else {
            throw new Exception("Local MongoDB not Running");
        }

    }

    @After
    public void cleanup() {
        event = null;
        model = null;
        validations = null;
    }


    @Test
    public void validateEventModelInsertSuccessTest() {
        boolean isValid = validations.validateEventModelInsert(model);
        assertTrue(isValid);
    }

    @Test
    public void validateEventModelInsertFailTest() {
        model.setAccountID(null);
        boolean isValid = validations.validateEventModelInsert(model);
        assertFalse(isValid);

        model.setAccountID("");
        isValid = validations.validateEventModelInsert(model);
        assertFalse(isValid);
    }


    @Test
    public void validateMandatoryStringSuccessTest() {
        boolean isValid = validations.validateMandatoryString("Test");
        assertTrue(isValid);
    }

    @Test
    public void validateMandatoryStringFailTest() {
        boolean isValid = validations.validateMandatoryString("");
        assertFalse(isValid);

        isValid = validations.validateMandatoryString(null);
        assertFalse(isValid);
    }


    @Test
    public void getSearchFieldEnumSuccessTest() {
        EventSearchField field = validations.getSearchFieldEnum(EventSearchField.ACCOUNT_ID.getField());
        assertEquals(field, EventSearchField.ACCOUNT_ID);
    }

    @Test
    public void getSearchFieldEnumFailTest() {
        EventSearchField field = validations.getSearchFieldEnum("test");
        assertNull(field);

        field = validations.getSearchFieldEnum("");
        assertNull(field);

        field = validations.getSearchFieldEnum(null);
        assertNull(field);
    }
}
