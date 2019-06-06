package au.com.mongodb;

import au.com.mongodb.model.JSONDataModel;
import au.com.mongodb.model.JSONSchemaModel;
import au.com.mongodb.models.TestPojo;
import au.com.mongodb.persistence.entities.DataEntity;
import au.com.mongodb.persistence.entities.SchemaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;

import java.lang.reflect.Field;
import java.util.Date;

public class HelperTest {

    private Date tempNow = new Date();

    /**
     * Genereate Schema from pojos
     *
     * @param generator
     * @param type
     * @param <T>
     * @return
     * @throws JsonMappingException
     */
    public static <T> ObjectSchema generateSchema(JsonSchemaGenerator generator, Class<T> type) throws JsonMappingException {
        ObjectSchema schema = generator.generateSchema(type).asObjectSchema();
        for (final Field field : type.getDeclaredFields()) {
            if (!field.getType().getName().startsWith("java") && !field.getType().isPrimitive()) {
                final ObjectSchema fieldSchema = generateSchema(generator, field.getType());
                fieldSchema.rejectAdditionalProperties();
                schema.putProperty(field.getName(), fieldSchema);
            }
        }
        return schema;
    }


    /**
     * Get schema from TestPojo as jsonSchema string
     *
     * @return
     * @throws JsonProcessingException
     */
    private String getSchemaFromTestPojoClass() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        final ObjectSchema schema = generateSchema(schemaGen, TestPojo.class);
        schema.rejectAdditionalProperties();
        final String schemaJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
        return schemaJson;
    }


    private TestPojo getTestPojoTestData() {
        final TestPojo pojo = new TestPojo();
        pojo.setName("junitTests");
        pojo.setVersion(1);
        pojo.setDob(tempNow);
        return pojo;
    }


    /**
     * Return the data model with test values
     *
     * @return
     * @throws JsonProcessingException
     */
    public JSONDataModel getDataModelTest() throws JsonProcessingException {
        final TestPojo tempPojo = getTestPojoTestData();

        final JSONDataModel model = new JSONDataModel();
        model.setSchemaName("schemaTest");
        model.setCreatedTS(tempNow);
        model.setCreatedBy("junitTests");
        model.setMajorVersion(0);
        model.setMinorVersion(1);
        model.setJson(tempPojo.toJSON());
        return model;
    }


    /**
     * Return data entity with test values
     *
     * @return
     * @throws JsonProcessingException
     */
    public DataEntity getDataEntityTest() throws JsonProcessingException {
        final TestPojo tempPojo = getTestPojoTestData();

        final DataEntity entity = new DataEntity();
        entity.setSchemaName("schemaTest");
        entity.setCreatedTS(tempNow);
        entity.setCreatedBy("junitTests");
        entity.setMajorVersion(0);
        entity.setMinorVersion(1);
        entity.setJson(tempPojo.toJSON());
        return entity;
    }


    /**
     * Return the schema model with test values
     *
     * @return
     * @throws JsonProcessingException
     */
    public JSONSchemaModel getSchemaModelTest() throws JsonProcessingException {
        final JSONSchemaModel model = new JSONSchemaModel();
        model.setSchemaName("schemaTest");
        model.setMajorVersion(0);
        model.setMinorVersion(1);
        model.setCreatedBy("junitTests");
        model.setCreatedTS(tempNow);
        model.setStartedTS(tempNow);
        model.setValid(true);
        final String jsonSchema = getSchemaFromTestPojoClass();
        model.setJson(jsonSchema);
        return model;
    }


    /**
     * Return the data model with test values
     *
     * @return
     * @throws JsonProcessingException
     */
    public SchemaEntity getSchemaEntityTest() throws JsonProcessingException {
        final SchemaEntity entity = new SchemaEntity();
        entity.setSchemaName("schemaTest");
        entity.setMajorVersion(0);
        entity.setMinorVersion(1);
        entity.setCreatedBy("junitTests");
        entity.setCreatedTS(tempNow);
        entity.setStartedTS(tempNow);
        entity.setValid(true);
        final String jsonSchema = getSchemaFromTestPojoClass();
        entity.setJson(jsonSchema);
        return entity;
    }
}
