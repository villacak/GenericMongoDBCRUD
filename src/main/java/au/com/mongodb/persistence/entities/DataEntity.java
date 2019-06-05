package au.com.mongodb.persistence.entities;

import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.Field;
import org.eclipse.persistence.nosql.annotations.NoSql;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "JSON_DATA")
@NoSql(dataFormat = DataFormatType.MAPPED)
@NamedQueries({
        @NamedQuery(name = "data.findById", query = "SELECT d FROM JSON_DATA d WHERE d.dataId = :id"),
        @NamedQuery(name = "data.searchDataByUserIdSchemaAndMajorVersionRange",
                query = "SELECT d FROM JSON_DATA d WHERE d.createdBy= :userId AND d.schemaName = :schemaName AND d.majorVersion >= :majorVersionFrom AND d.majorVersion <= :majorVersionTo"),
        @NamedQuery(name = "data.searchDataByUserIdSchema",
                query = "SELECT d FROM JSON_DATA d WHERE d.createdBy= :userId AND d.schemaName = :schemaName")
})
public class DataEntity {

    @Id
    @GeneratedValue
    @Field(name = "id")
    private int dataId;

    @Column(name = "schemaName")
    private String schemaName;

    @Column(name = "majorVersion")
    private int majorVersion;

    @Column(name = "minorVersion")
    private int minorVersion;

    @Column(name = "createdTS")
    private LocalDateTime createdTS;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "json")
    private String json;

    @Version
    private long version;



    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
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

    public LocalDateTime getCreatedTS() {
        return createdTS;
    }

    public void setCreatedTS(LocalDateTime createdTS) {
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
}
