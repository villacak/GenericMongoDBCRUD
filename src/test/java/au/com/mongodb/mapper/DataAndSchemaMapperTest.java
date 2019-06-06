package au.com.mongodb.mapper;


import au.com.mongodb.HelperTest;
import au.com.mongodb.model.JSONDataModel;
import au.com.mongodb.model.JSONSchemaModel;
import au.com.mongodb.persistence.entities.DataEntity;
import au.com.mongodb.persistence.entities.SchemaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataAndSchemaMapperTest {



    private HelperTest helperTest;
    private JSONDataModel dataModel;
    private DataEntity dataEntity;
    private JSONSchemaModel schemaModel;
    private SchemaEntity schemaEntity;


    @Before
    public void startup() {
        helperTest = new HelperTest();
    }


    @After
    public void cleanup() {
        dataModel = null;
        dataEntity = null;
        schemaModel = null;
        schemaEntity = null;
        helperTest = null;
    }


    @Test
    public void mapFromDataEntityToModelTest() throws JsonProcessingException {
        dataEntity = helperTest.getDataEntityTest();
        assertNotNull(dataEntity);

        dataModel = DataMapper.MAPPER.mapDataEntityToDataModel(dataEntity);
        assertEquals(dataModel.getCreatedBy(), dataEntity.getCreatedBy());
        assertEquals(dataModel.getSchemaName(), dataEntity.getSchemaName());
        assertEquals(dataModel.getJson(), dataEntity.getJson());
        assertTrue(dataModel.getMajorVersion() == dataEntity.getMajorVersion());
        assertTrue(dataModel.getMinorVersion() == dataEntity.getMinorVersion());
    }


    @Test
    public void mapFromSchemaEntityToModelTest() throws JsonProcessingException {
        schemaEntity = helperTest.getSchemaEntityTest();
        assertNotNull(schemaEntity);

        schemaModel = SchemaMapper.MAPPER.mapSchemaEntityToSchemaModel(schemaEntity);
        assertEquals(schemaModel.getCreatedBy(), schemaEntity.getCreatedBy());
        assertEquals(schemaModel.getSchemaName(), schemaEntity.getSchemaName());
        assertEquals(schemaModel.getJson(), schemaEntity.getJson());
        assertTrue(schemaModel.getMajorVersion() == schemaEntity.getMajorVersion());
        assertTrue(schemaModel.getMinorVersion() == schemaEntity.getMinorVersion());
    }


    @Test
    public void mapFromDataModelToEntityTest() throws JsonProcessingException {
        dataModel = helperTest.getDataModelTest();
        assertNotNull(dataModel);

        dataEntity = DataMapper.MAPPER.mapDataModelToDataEntity(dataModel);
        assertEquals(dataModel.getCreatedBy(), dataEntity.getCreatedBy());
        assertEquals(dataModel.getSchemaName(), dataEntity.getSchemaName());
        assertEquals(dataModel.getJson(), dataEntity.getJson());
        assertTrue(dataModel.getMajorVersion() == dataEntity.getMajorVersion());
        assertTrue(dataModel.getMinorVersion() == dataEntity.getMinorVersion());
    }


    @Test
    public void mapFromSchemaModelToEntityTest() throws JsonProcessingException {
        schemaModel = helperTest.getSchemaModelTest();
        assertNotNull(schemaModel);

        schemaEntity = SchemaMapper.MAPPER.mapSchemaModelToSchemaEntity(schemaModel);
        assertEquals(schemaModel.getCreatedBy(), schemaEntity.getCreatedBy());
        assertEquals(schemaModel.getSchemaName(), schemaEntity.getSchemaName());
        assertEquals(schemaModel.getJson(), schemaEntity.getJson());
        assertTrue(schemaModel.getMajorVersion() == schemaEntity.getMajorVersion());
        assertTrue(schemaModel.getMinorVersion() == schemaEntity.getMinorVersion());
    }

}
