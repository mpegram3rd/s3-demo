## Spring Boot + S3 Standalone Client

This is just a simple bit of code to fetch the list of files available in an S3 bucket.  
 
It's here just to get familiar with tying all these pieces together and to serve as a reference for our interns.

This uses 2 approaches to pull S3 lists...

1. Uses Spring to wire up standard AWS S3 Library dependencies (REST endpoint: /storage/list)
2. The other approach is the more Spring like way of doing things by using a ResourcePatternResolver that understands URIs of the form s3://bucket/** (REST endpoint: /storage/resources)

----
## Usage:
- Configure AWS connection information in "application.properties"
- Execute the app: ./gradlew bootRun

