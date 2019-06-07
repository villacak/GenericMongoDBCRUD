# GenericMongoDBCRUD
PoC to have micro-service Java persistence with MongoDB. The project works with JSON-Schemas or without.

If running this locally with Tomcat at port 8080, and the configuration for the persistence.xml also assume MongoDB is running locally with default configurations on port 27017.

<h2>URLs for endpoints</h2>
<p></p>
<b>Health: </b> 

GET: http://localhost:8080/yourContextRoot/api/v1/health/check
Simple health check to see if the application is up and running.

GET: http://localhost:8080/yourContextRoot/api/v1/health/checkMongoDBConnection
Simple MongoDB connection check. If the MongoDB have any problem or hans't been started yet, it will fail


<b>Data:  </b>  
PUT: http://localhost:8080/yourContextRoot/api/v1/data/insert?validateSchema=<true|false>
and the payload with the json.
Create a new record.

PATCH: http://localhost:8080/yourContextRoot/api/v1/data/update?validateSchema=<true|false>
The payload with the json.
Update already existing record

DELETE: http://localhost:8080/yourContextRoot/api/v1/data/delete/{id}
Delete the id record.

GET: http://localhost:8080/yourContextRoot/api/v1/data/search/{id}
Retrieve the record from the id.

GET: http://localhost:8080/yourContextRoot/api/v1/data/search/{userId}/{schemaName}/{majorVersionFrom}/{majorVersionTo}
Retrieve a list/ collection of data model (refer Data Model bellow).

GET: http://localhost:8080/yourContextRoot/api/v1/data/search/{userId}/{schemaName}
Retrive a list/ collection of data model (refer Data Model bellow)


<b>Schema:  </b>  
PUT: http://localhost:8080/yourContextRoot/api/v1/schema/insert
and the payload with the json.
Create a new record.

DELETE: http://localhost:8080/yourContextRoot/api/v1/schema/delete/{id}
Delete the id record.

DELETE: http://localhost:8080/yourContextRoot/api/v1/schema/delete/{schemaName}/{majorVersion}/{minorVersion}
Delete the record filtering by schema name, major version and minor version.

GET: http://localhost:8080/yourContextRoot/api/v1/schema/search/{schemaName}/{majorVersion}/{minorVersion}
Retrieve the schema named as schemaName that contain the same major and minor versions.

   
   
<h2>Models</h2>
     
<b>Schema</b>  

         private String id;
         private String schemaName;
         private int majorVersion;
         private int minorVersion;
         private String createdBy;
         private Date createdTS;
         private Date startedTS;
         private Date endTS;
         private boolean isValid;
                 
         // The json-schema
         private String json;
         private int version;
       
       
<b>Data</b>  

         private String dataId;
         private String schemaName;
         private int majorVersion;
         private int minorVersion;
         private Date createdTS;
         private String createdBy;
                 
         // The json containing the data
         private String json;
         private int version;

Please feel free to fork and create your own custom version.
As it's a Maven project, you can build it typing <b><i>mvn clean install</i></b>
then get the .war file and deploy in Tomcat or any other Application Server.
