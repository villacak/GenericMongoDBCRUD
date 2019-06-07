package au.com.mongodb.services.v1.validations;

import au.com.mongodb.business.ReadyResponses;
import au.com.mongodb.business.SchemaBusiness;
import au.com.mongodb.model.JSONDataModel;
import au.com.mongodb.model.JSONSchemaModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import javax.ws.rs.core.Response;

public class CrudMongoDbValidations {

    private final String EMPTY = "";

    /**
     * validateSchema
     *
     * @param json
     * @return
     */
    public Response validateSchema(final String json) {
        Response resp;
        try {
            if (validateMandatoryString(json)) {
                // Validate the JSON Schema
                final ObjectMapper mapper = new ObjectMapper();
                final JSONSchemaModel model = mapper.readValue(json, JSONSchemaModel.class);
                mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
                mapper.readTree(json);
                mapper.readTree(model.getJson());

                resp = null;
            } else {
                resp = ReadyResponses.badRequest();
            }
        } catch (Exception ioe) {
            resp = ReadyResponses.badRequest();
        }
        return resp;
    }


    /**
     * validateData
     *
     * @param validateSchema
     * @param json
     * @return
     */
    public Response validateData(final boolean validateSchema, final String json) {
        Response resp = null;
        try {
            if (validateMandatoryString(json)) {
                // Validate the JSON Schema
                final ObjectMapper mapper = new ObjectMapper();
                final JSONDataModel model = mapper.readValue(json, JSONDataModel.class);

                if (validateSchema) {
                    final SchemaBusiness business = new SchemaBusiness();
                    final JSONSchemaModel schemaModel = business.
                            searchSchemaByNameAndVersion(model.getSchemaName(), model.getMajorVersion(), model.getMinorVersion());
                    final JsonNode jsonNodeSchema = mapper.readTree(model.getJson());
                    final JsonNode jsonNodeData = mapper.readTree(json);
                    final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
                    final JsonSchema schema = factory.getJsonSchema(jsonNodeSchema);
                    final ProcessingReport validator = schema.validateUnchecked(jsonNodeData);
                    if (validator.isSuccess()) {
                        resp = null;
                    } else {
                        resp = ReadyResponses.badRequest();
                    }
                } else {
                    resp = null;
                }
            } else {
                resp = ReadyResponses.badRequest();
            }
        } catch (Exception ioe) {
            resp = ReadyResponses.badRequest();
        }
        return resp;
    }


    /**
     * validateMandatoryString
     *
     * @param value
     * @return
     */
    public boolean validateMandatoryString(final String value) {
        final boolean isValid;
        if (value != null && !value.equals(EMPTY)) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

}
