package au.com.mongodb.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class JSONDataModel {

    private String dataId;
    private String schemaName;
    private int majorVersion;
    private int minorVersion;
    private Date createdTS;
    private String createdBy;
    private String json;
    private int version;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public Date getCreatedTS() {
        return createdTS;
    }

    public void setCreatedTS(Date createdTS) {
        this.createdTS = createdTS;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    public String toJSON() {
        final ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(this);
        } catch (IOException ioe) {
            json = null;
        }
        return json;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSONDataModel)) return false;
        JSONDataModel dataModel = (JSONDataModel) o;
        return majorVersion == dataModel.majorVersion &&
                minorVersion == dataModel.minorVersion &&
                Objects.equals(dataId, dataModel.dataId) &&
                Objects.equals(schemaName, dataModel.schemaName) &&
                Objects.equals(createdTS, dataModel.createdTS) &&
                Objects.equals(createdBy, dataModel.createdBy) &&
                json.equals(dataModel.json);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, schemaName, majorVersion, minorVersion, createdTS, createdBy, json);
    }
}
