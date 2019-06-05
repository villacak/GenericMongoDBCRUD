package au.com.mongodb.mapper;

import au.com.mongodb.model.JSONDataModel;
import au.com.mongodb.model.JSONSchemaModel;
import au.com.mongodb.persistence.entities.DataEntity;
import au.com.mongodb.persistence.entities.SchemaEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DataMapper {

    final DataMapper MAPPER = Mappers.getMapper(DataMapper.class);

    public DataEntity mapDataModelToDataEntity(final JSONDataModel model);
    public JSONDataModel mapDataEntityToDataModel(final DataEntity entity);
}
