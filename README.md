# SWEN301 Assignment 3
## Background
The aim of this assignment is to develop HTTP-based services to store log information. This could then for instance be used as a backend for a log4j appender that stores logs remotely. These services is specified in a document using the Swapper / OpenAPI format:
https://app.swaggerhub.com/apis/jensdietrich/resthome4logs/1.2.0
## Server
The server is startable with the single mvn command mvn jetty:run, and stoppable with the single command mvn jetty:stop. When the server is running, the services are accessible at http://localhost:8080/resthome4logs/ (for instance: http://localhost:8080/resthome4logs/logs ) respectively.
## Client
The program will connect to a server running on http://localhost:8080/resthome4logs/ *(Note: this program will not start the server.)*
It will connect to statscsv or statsxls depending on the value of the first argument (type), and store the data in a local file named fileName.
Example: “java -jar resthome4logs-client.jar excel logs.xls” will download log stats in excel format from http://localhost:8080/resthome4logs/statsxls using a GET request, and store the data in an excel file logs.xls .
