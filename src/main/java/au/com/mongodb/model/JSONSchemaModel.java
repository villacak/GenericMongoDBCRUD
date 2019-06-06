package au.com.mongodb.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;

public class JSONSchemaModel {

    private String id;
    private String schemaName;
    private int majorVersion;
    private int minorVersion;
    private String createdBy;
    private Date createdTS;
    private Date startedTS;
    private Date endTS;
    private boolean isValid;
    private String json;
    private int version;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTS() {
        return createdTS;
    }

    public void setCreatedTS(Date createdTS) {
        this.createdTS = createdTS;
    }

    public Date getStartedTS() {
        return startedTS;
    }

    public void setStartedTS(Date startedTS) {
        this.startedTS = startedTS;
    }

    public Date getEndTS() {
        return endTS;
    }

    public void setEndTS(Date endTS) {
        this.endTS = endTS;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
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
}
