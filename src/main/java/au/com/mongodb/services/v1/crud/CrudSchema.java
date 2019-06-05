package au.com.mongodb.services.v1.crud;

import au.com.mongodb.business.ReadyResponses;
import au.com.mongodb.business.SchemaBusiness;
import au.com.mongodb.model.JSONSchemaModel;
import au.com.mongodb.services.v1.validations.CrudMongoDbValidations;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/schema")
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
public class CrudSchema {

    @PUT
    @Path("insert")
    public Response insertSchemaRecord(final String jsonSchema) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        Response response = validations.validateSchema(jsonSchema);
        if (response == null) {
            final SchemaBusiness business = new SchemaBusiness();
            final JSONSchemaModel tempModel = business.convertRequestToModel(jsonSchema);
            response = business.persistSingleSchema(tempModel, true);
        }
        return response;
    }

    @DELETE
    @Path("delete/{id}")
    public Response deleteRecord(@PathParam("id") final String id) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        final Response response;
        if (validations.validateMandatoryString(id)) {
            final SchemaBusiness business = new SchemaBusiness();
            response = business.deleteSingleSchema(id);
        } else {
            response = ReadyResponses.badRequest();
        }
        return response;
    }


    @DELETE
    @Path("delete/{schemaName}/{majorVersion}/{minorVersion}")
    public Response deleteRecord(@PathParam("schemaName") final String schemaName,
                                 @PathParam("majorVersion") final int majorVersion, @PathParam("minorVersion") final int minorVersion) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        final Response response;
        if (validations.validateMandatoryString(schemaName)) {
            final SchemaBusiness business = new SchemaBusiness();
            final JSONSchemaModel model = business.searchSchemaByNameAndVersion(schemaName, majorVersion, minorVersion);
            response = business.deleteSingleSchema(String.valueOf(model.getSchemaId()));
        } else {
            response = ReadyResponses.badRequest();
        }
        return response;
    }


    @GET
    @Path("search/{schemaName}/{majorVersion}/{minorVersion}")
    public Response searchRecord(@PathParam("schemaName") final String schemaName,
                                 @PathParam("majorVersion") final int majorVersion, @PathParam("minorVersion") final int minorVersion) {
        final CrudMongoDbValidations validations = new CrudMongoDbValidations();
        final Response response;
        if (validations.validateMandatoryString(schemaName)) {
            final SchemaBusiness business = new SchemaBusiness();
            response = business.searchSchemasByNameAndVersion(schemaName, majorVersion, minorVersion);
        } else {
            response = ReadyResponses.badRequest();
        }
        return response;
    }


//    @PATCH
//    @Path("update")
//    public Response updateRecord(final EventModel event) {
//        Response response = simpleCRUDEventValidations(event);
//        if (response == null) {
//            final ReadyResponses business = new ReadyResponses();
//            response = business.persistSingleJSONData(event, false);
//        }
//        return response;
//    }







}
