package au.com.mongodb.business;

import au.com.mongodb.cache.BasicProperties;
import au.com.mongodb.cache.MongoDBCRUDCacheUtils;
import au.com.mongodb.constants.Constant;
import au.com.mongodb.mapper.DataMapper;
import au.com.mongodb.model.JSONDataModel;
import au.com.mongodb.model.ResponseMessage;
import au.com.mongodb.persistence.NoSQLCRUDMaster;
import au.com.mongodb.persistence.entities.DataEntity;
import au.com.mongodb.services.v1.validations.CrudMongoDbValidations;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBusiness {

    private MongoDBCRUDCacheUtils cacheUtils;

    public DataBusiness() {
        cacheUtils = new MongoDBCRUDCacheUtils(Constant.CACHE_NAME_DEFAULT);
    }

    /**
     * convertRequestToModel
     *
     * @param jsonData
     * @return
     */
    public JSONDataModel convertRequestToModel(final String jsonData) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        final ObjectMapper mapper = new ObjectMapper();
        JSONDataModel dataModel;
        try {
            dataModel = mapper.readValue(jsonData, JSONDataModel.class);
            if (!validations.validateMandatoryString(dataModel.getSchemaName())) {
                dataModel = null;
            }

            if (!validations.validateMandatoryString(dataModel.getCreatedBy())) {
                // Capture the user id and add to the setCreatedBy
                dataModel.setCreatedBy(cacheUtils.getCachedValueFromKey(BasicProperties.USER_LOGGED_ID.toString()));
            }
        } catch (IOException e) {
            dataModel = null;
        }

        return dataModel;
    }


    /**
     * persistSingleJSONData
     *
     * @param model
     * @param isSave
     * @return
     */
    public Response persistSingleJSONData(final JSONDataModel model, final boolean isSave) {
        Response response = null;
        try {
            final DataEntity entity = DataMapper.MAPPER.mapDataModelToDataEntity(model);

            final NoSQLCRUDMaster crud = new NoSQLCRUDMaster();
            final DataEntity savedEntity;
            if (isSave) {
                savedEntity = crud.saveData(entity);
            } else {
                savedEntity = crud.mergeData(entity);
            }
            final JSONDataModel modelToReturn = DataMapper.MAPPER.mapDataEntityToDataModel(savedEntity);
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
    public Response deleteSingleData(final String id) {
        final int FIRST = 0;
        Response response = null;

        try {
            final ResponseMessage responseMessage;
            final NoSQLCRUDMaster crud = new NoSQLCRUDMaster();
            final List<DataEntity> dataEntities = crud.searchById(Constant.ENTITY_ID,
                    id,
                    cacheUtils.getCachedValueFromKey(BasicProperties.DATA_FIND_BY_ID.toString()));
            if (dataEntities == null || dataEntities.size() == 0) {
                response = ReadyResponses.successWithMessage(cacheUtils.getCachedValueFromKey(BasicProperties.RECORD_NOT_FOUND_FOR_DELETE.toString()));
            } else if (dataEntities.size() > 1) {
                response = ReadyResponses.successWithMessage(cacheUtils.getCachedValueFromKey(BasicProperties.DUPLICATED_IDS.toString()));
            } else {
                crud.delete(dataEntities.get(FIRST));
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
    public Response searchDataById(final String id, final String field) {
        Response response = null;
        try {
            final NoSQLCRUDMaster crud = new NoSQLCRUDMaster();
            final List<DataEntity> dataEntities = crud.searchById(id,
                    field,
                    cacheUtils.getCachedValueFromKey(BasicProperties.DATA_FIND_BY_ID.toString()));
            if (dataEntities == null || dataEntities.size() == 0) {
                response = ReadyResponses.successWithMessage(cacheUtils.getCachedValueFromKey(BasicProperties.NOTHING_FOUND.toString()));
            } else {
                final ObjectMapper mapper = new ObjectMapper();
                final List<JSONDataModel> models = new ArrayList<>(dataEntities.size());
                for (DataEntity tempData : dataEntities) {
                    final JSONDataModel modelToReturn = DataMapper.MAPPER.mapDataEntityToDataModel(tempData);
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
     * searchDataByUserIdSchemaAndMajorVersionRange
     *
     * @param userId
     * @param schemaName
     * @param majorVersionFrom
     * @param majorVersionTo
     * @return
     */
    public Response searchDataByUserIdSchemaAndMajorVersionRange(final String userId, final String schemaName,
                                                                 final int majorVersionFrom, final int majorVersionTo) {
        Response response = null;
        try {
            final NoSQLCRUDMaster crud = new NoSQLCRUDMaster();
            final List<DataEntity> dataEntities;
            if (majorVersionFrom == 0 && majorVersionTo == 0) {
                dataEntities = crud.searchDataByUserIdSchema(userId, schemaName,"data.searchDataByUserIdSchema");
            } else {
                dataEntities = crud.searchDataByUserIdSchemaAndMajorVersionRange(userId, schemaName,
                        majorVersionFrom, majorVersionTo, "data.searchDataByUserIdSchemaAndMajorVersionRange");
            }

            if (dataEntities == null || dataEntities.size() == 0) {
                response = ReadyResponses.successWithMessage(cacheUtils.getCachedValueFromKey(BasicProperties.NOTHING_FOUND.toString()));
            } else {
                final ObjectMapper mapper = new ObjectMapper();
                final List<JSONDataModel> models = new ArrayList<>(dataEntities.size());
                for (DataEntity tempData : dataEntities) {
                    final JSONDataModel modelToReturn = DataMapper.MAPPER.mapDataEntityToDataModel(tempData);
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
}
