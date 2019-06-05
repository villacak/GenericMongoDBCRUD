package au.com.mongodb.mapper;

import au.com.mongodb.HelperTest;
import au.com.mongodb.model.SystemMetadataModel;
import au.com.mongodb.model.EventModel;
import au.com.mongodb.persistence.entities.Event;
import au.com.mongodb.persistence.entities.SystemMetadata;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchemaMapperTest {


    private Event event;
    private SystemMetadataModel systemMetadataModel;
    private EventModel model;
    private SystemMetadata systemMetadata;
    private HelperTest helperTest;


    @Before
    public void startup() {
        helperTest = new HelperTest();
    }


    @After
    public void cleanup() {
        event = null;
        systemMetadata = null;
        model = null;
        systemMetadataModel = null;
        helperTest = null;
    }


    @Test
    public void mapFromEntityToModelTest() {
        event = helperTest.getEventEntityForTest();
        assertNotNull(event);
        assertNull(model);

        model = SchemaMapper.MAPPER.mapSchemaEntityToSchemaModel(event);
        assertEquals(model.getPrimaryKey(), event.get_id());
        assertEquals(model.getAccNumber(), event.getAccountNumber());
        assertEquals(model.getAccountID(), event.getAccountId());
        assertTrue(model.getTotalAmount() == event.getTotalAmount());
    }

    @Test
    public void mapFromModelToEntityTest() {
        model = helperTest.getEventModelForTest();
        assertNotNull(model);
        assertNull(event);

        event = SchemaMapper.MAPPER.mapSchemaModelToSchemaEntity(model);
        assertEquals(event.get_id(), model.getPrimaryKey());
        assertEquals(event.getAccountNumber(), model.getAccNumber());
        assertEquals(event.getAccountId(), model.getAccountID());
        assertTrue(event.getTotalAmount() == model.getTotalAmount());
    }
}
