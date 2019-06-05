package au.com.mongodb.services.v1.crud;

import au.com.mongodb.business.DataBusiness;
import au.com.mongodb.business.ReadyResponses;
import au.com.mongodb.business.SchemaBusiness;
import au.com.mongodb.constants.Constant;
import au.com.mongodb.model.JSONDataModel;
import au.com.mongodb.model.JSONSchemaModel;
import au.com.mongodb.services.v1.validations.CrudMongoDbValidations;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/data")
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
public class CrudData {

    @PUT
    @Path("insert")
    public Response insertDataRecord(@QueryParam("validateSchema") final boolean validateSchema, final String jsonData) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        Response response = validations.validateData(validateSchema, jsonData);
        if (response == null) {
            final DataBusiness business = new DataBusiness();
            final JSONDataModel tempModel = business.convertRequestToModel(jsonData);
            response = business.persistSingleJSONData(tempModel, true);
        }
        return response;
    }


    @PATCH
    @Path("update")
    public Response updateDataRecord(@QueryParam("validateSchema") final boolean validateSchema, final String jsonData) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        Response response = validations.validateData(validateSchema, jsonData);
        if (response == null) {
            final DataBusiness business = new DataBusiness();
            final JSONDataModel tempModel = business.convertRequestToModel(jsonData);
            response = business.persistSingleJSONData(tempModel, false);
        }
        return response;
    }


    @DELETE
    @Path("delete/{id}")
    public Response deleteRecord(@PathParam("id") final String id) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        final Response response;
        if (validations.validateMandatoryString(id)) {
            final DataBusiness business = new DataBusiness();
            response = business.deleteSingleData(id);
        } else {
            response = ReadyResponses.badRequest();
        }
        return response;
    }


    @GET
    @Path("search/{id}")
    public Response searchRecord(@PathParam("id") final String id) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        final Response response;
        if (validations.validateMandatoryString(id)) {
            final DataBusiness business = new DataBusiness();
            response = business.searchDataById(Constant.ENTITY_ID, id);
        } else {
            response = ReadyResponses.badRequest();
        }
        return response;
    }


    @GET
    @Path("search/{userId}/{schemaName}/{majorVersionFrom}/{majorVersionTo}")
    public Response searchRecord(@PathParam("userId") final String userId, @PathParam("schemaName") final String schemaName,
                                 @PathParam("majorVersionFrom") final int majorVersionFrom, @PathParam("majorVersionTo") final int majorVersionTo) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        final Response response;
        if (validations.validateMandatoryString(userId) && validations.validateMandatoryString(schemaName)) {
            final DataBusiness business = new DataBusiness();
            response = business.searchDataByUserIdSchemaAndMajorVersionRange(userId, schemaName, majorVersionFrom, majorVersionTo);
        } else {
            response = ReadyResponses.badRequest();
        }
        return response;
    }


    @GET
    @Path("search/{userId}/{schemaName}")
    public Response searchRecord(@PathParam("userId") final String userId, @PathParam("schemaName") final String schemaName) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        final Response response;
        if (validations.validateMandatoryString(userId) && validations.validateMandatoryString(schemaName)) {
            final DataBusiness business = new DataBusiness();
            response = business.searchDataByUserIdSchemaAndMajorVersionRange(userId, schemaName, 0, 0);
        } else {
            response = ReadyResponses.badRequest();
        }
        return response;
    }
}
