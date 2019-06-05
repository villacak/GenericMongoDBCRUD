package au.com.mongodb.persistence.entities;

import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.Field;
import org.eclipse.persistence.nosql.annotations.NoSql;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "JSON_SCHEMA")
@NoSql(dataFormat = DataFormatType.MAPPED)
@NamedQueries({
        @NamedQuery(name = "schema.findById", query = "SELECT s FROM JSON_SCHEMA s WHERE s.id = :id"),
        @NamedQuery(name = "schema.findByNameAndVersion", query = "SELECT s FROM JSON_SCHEMA s WHERE s.schemaName = :schemaName AND s.majorVersion = :majorVersion AND s.minorVersion = :minorVersion")
})
public class SchemaEntity implements Serializable {

    @Id
    @GeneratedValue
    @Field(name = "_id")
    private int id;

    @Column(name = "schemaName")
    private String schemaName;

    @Column(name = "majorVersion")
    private int majorVersion;

    @Column(name = "minorVersion")
    private int minorVersion;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "createdTS")
    private LocalDateTime createdTS;

    @Column(name = "startedTS")
    private LocalDateTime startedTS;

    @Column(name = "endTS")
    private LocalDateTime endTS;

    @Column(name = "isValid")
    private boolean isValid;

    @Column(name = "json")
    private String json;

    @Version
    private long version;


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public LocalDateTime getCreatedTS() {
        return createdTS;
    }

    public void setCreatedTS(LocalDateTime createdTS) {
        this.createdTS = createdTS;
    }

    public LocalDateTime getStartedTS() {
        return startedTS;
    }

    public void setStartedTS(LocalDateTime startedTS) {
        this.startedTS = startedTS;
    }

    public LocalDateTime getEndTS() {
        return endTS;
    }

    public void setEndTS(LocalDateTime endTS) {
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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
