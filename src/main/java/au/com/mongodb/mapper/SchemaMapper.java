package au.com.mongodb.mapper;

import au.com.mongodb.model.JSONSchemaModel;
import au.com.mongodb.persistence.entities.SchemaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * SchemaMapper
 */
@Mapper
public interface SchemaMapper {

    final SchemaMapper MAPPER = Mappers.getMapper(SchemaMapper.class);

    /**
     * mapSchemaModelToSchemaEntity
     *
     * @param model
     * @return
     */
    public SchemaEntity mapSchemaModelToSchemaEntity(final JSONSchemaModel model);

    /**
     * mapSchemaModelToSchemaEntity
     *
     * @param entity
     * @return
     */
    public JSONSchemaModel mapSchemaEntityToSchemaModel(final SchemaEntity entity);
}
