package au.com.mongodb.business;

import au.com.mongodb.cache.BasicProperties;
import au.com.mongodb.cache.MongoDBCRUDCacheUtils;
import au.com.mongodb.constants.Constant;
import au.com.mongodb.mapper.SchemaMapper;
import au.com.mongodb.model.JSONSchemaModel;
import au.com.mongodb.model.ResponseMessage;
import au.com.mongodb.persistence.NoSQLCRUDMaster;
import au.com.mongodb.persistence.entities.SchemaEntity;
import au.com.mongodb.services.v1.validations.CrudMongoDbValidations;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchemaBusiness {

    private final int FIRST = 0;
    private MongoDBCRUDCacheUtils cacheUtils;

    public SchemaBusiness() {
        cacheUtils = new MongoDBCRUDCacheUtils(Constant.CACHE_NAME_DEFAULT);
    }


    /**
     * convertRequestToModel
     *
     * @param jsonSchema
     * @return
     */
    public JSONSchemaModel convertRequestToModel(final String jsonSchema) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        final ObjectMapper mapper = new ObjectMapper();
        JSONSchemaModel schemaModel;
        try {
            schemaModel = mapper.readValue(jsonSchema, JSONSchemaModel.class);
            if (!validations.validateMandatoryString(schemaModel.getSchemaName())) {
                schemaModel = null;
            }

            if (!validations.validateMandatoryString(schemaModel.getCreatedBy())) {
                // Capture the user id and add to the setCreatedBy
                schemaModel.setCreatedBy(cacheUtils.getCachedValueFromKey(BasicProperties.USER_LOGGED_ID.toString()));
            }
        } catch (IOException e) {
            schemaModel = null;
        }

        return schemaModel;
    }


    /**
     * persistSingleJSONData
     *
     * @param model
     * @param isSave
     * @return
     */
    public Response persistSingleSchema(final JSONSchemaModel model, final boolean isSave) {
        Response response = null;
        try {
            final SchemaEntity entity = SchemaMapper.MAPPER.mapSchemaModelToSchemaEntity(model);

            final NoSQLCRUDMaster crud = new NoSQLCRUDMaster();
            final SchemaEntity savedEventEntity;
            if (isSave) {
                savedEventEntity = crud.saveData(entity);
            } else {
                savedEventEntity = crud.mergeData(entity);
            }
            final JSONSchemaModel modelToReturn = SchemaMapper.MAPPER.mapSchemaEntityToSchemaModel(savedEventEntity);
            response = Response.ok().entity(modelToReturn.toJSON()).build();
        } catch (Exception e) {
            response = ReadyResponses.serverError();
        }
        return response;
    }


    /**
     * deleteSingleSchema
     *
     * @param id
     * @return
     */
    public Response deleteSingleSchema(final String id) {
        Response response = null;
        try {
            final ResponseMessage responseMessage;
            final NoSQLCRUDMaster crud = new NoSQLCRUDMaster();
            final List<SchemaEntity> schemaEntities = crud.searchById(Constant.ENTITY_ID,
                    id,
                    cacheUtils.getCachedValueFromKey(BasicProperties.SCHEMA_FIND_BY_ID.toString()));
            if (schemaEntities == null || schemaEntities.size() == 0) {
                response = ReadyResponses.successWithMessage(cacheUtils.getCachedValueFromKey(BasicProperties.RECORD_NOT_FOUND_FOR_DELETE.toString()));
            } else if (schemaEntities.size() > 1) {
                response = ReadyResponses.successWithMessage(cacheUtils.getCachedValueFromKey(BasicProperties.DUPLICATED_IDS.toString()));
            } else {
                crud.delete(schemaEntities.get(FIRST));
                response = ReadyResponses.successWithMessage(cacheUtils.getCachedValueFromKey(BasicProperties.RECORD_DELETED.toString()));
            }
        } catch (Exception e) {
            response = ReadyResponses.serverError();
        }
        return response;
    }


    /**
     * SearchSchemas
     *
     * @param id
     * @param field
     * @return
     */
    public Response searchSchemasById(final String id, final String field) {
        Response response = null;
        try {
            final NoSQLCRUDMaster crud = new NoSQLCRUDMaster();
            final List<SchemaEntity> schemaEntities = crud.searchById(id,
                    field,
                    cacheUtils.getCachedValueFromKey(BasicProperties.SCHEMA_FIND_BY_ID.toString()));
            if (schemaEntities == null || schemaEntities.size() == 0) {
                response = ReadyResponses.successWithMessage(cacheUtils.getCachedValueFromKey(BasicProperties.NOTHING_FOUND.toString()));
            } else {
                final ObjectMapper mapper = new ObjectMapper();
                final List<JSONSchemaModel> models = new ArrayList<>(schemaEntities.size());
                for (SchemaEntity tempEvent : schemaEntities) {
                    final JSONSchemaModel modelToReturn = SchemaMapper.MAPPER.mapSchemaEntityToSchemaModel(tempEvent);
                    models.add(modelToReturn);
                }
                final String json = mapper.writeValueAsString(models);
                response = Response.ok().entity(json).build();
            }
        } catch (Exception e) {
            response = ReadyResponses.serverError();
        }
        return response;

    }


    /**
     * searchSchemasByNameAndVersion
     *
     * @param name
     * @param majorVersion
     * @param minorVersion
     * @return
     */
    public Response searchSchemasByNameAndVersion(final String name, final int majorVersion, final int minorVersion) {
        Response response = null;
        try {
            final NoSQLCRUDMaster crud = new NoSQLCRUDMaster();
            final List<SchemaEntity> schemaEntities = crud.searchByNameAndVersion(name,
                    majorVersion,
                    minorVersion,
                    "schema.findByNameAndVersion");
            if (schemaEntities == null || schemaEntities.size() == 0) {
                response = ReadyResponses.successWithMessage(cacheUtils.getCachedValueFromKey(BasicProperties.NOTHING_FOUND.toString()));
            } else {
                final ObjectMapper mapper = new ObjectMapper();
                final List<JSONSchemaModel> models = new ArrayList<>(schemaEntities.size());
                for (SchemaEntity tempSchema : schemaEntities) {
                    final JSONSchemaModel modelToReturn = SchemaMapper.MAPPER.mapSchemaEntityToSchemaModel(tempSchema);
                    models.add(modelToReturn);
                }
                final String json = mapper.writeValueAsString(models);
                response = Response.ok().entity(json).build();
            }
        } catch (Exception e) {
            response = ReadyResponses.serverError();
        }
        return response;
    }



    /**
     * searchSchemaByNameAndVersion
     *
     * @param name
     * @param majorVersion
     * @param minorVersion
     * @return
     */
    public JSONSchemaModel searchSchemaByNameAndVersion(final String name, final int majorVersion, final int minorVersion) {
        final NoSQLCRUDMaster crud = new NoSQLCRUDMaster();
        final List<SchemaEntity> schemaEntities = crud.searchByNameAndVersion(name,
                majorVersion,
                minorVersion,
                "schema.findByNameAndVersion");
        final JSONSchemaModel modelToReturn;
        if (schemaEntities != null || schemaEntities.size() == 1) {
            final SchemaEntity tempSchema = schemaEntities.get(FIRST);
            modelToReturn = SchemaMapper.MAPPER.mapSchemaEntityToSchemaModel(tempSchema);
        } else {
            modelToReturn = null;
        }
        return modelToReturn;
    }
}

