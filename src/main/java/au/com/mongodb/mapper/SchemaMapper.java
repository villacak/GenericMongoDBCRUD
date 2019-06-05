package au.com.mongodb.mapper;

import au.com.mongodb.model.JSONSchemaModel;
import au.com.mongodb.persistence.entities.SchemaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SchemaMapper {

    final SchemaMapper MAPPER = Mappers.getMapper(SchemaMapper.class);

    public SchemaEntity mapSchemaModelToSchemaEntity(final JSONSchemaModel model);
    public JSONSchemaModel mapSchemaEntityToSchemaModel(final SchemaEntity entity);
}
