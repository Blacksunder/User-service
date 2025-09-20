# User-service
REST API with basic CRUD operations using Spring. It provides the server with url's for getting, updating, saving and removing users from/to the database. Notification and User services are connected with Kafka server, that provides updating and deleting operations notification mechanism by sending mail messages. 

Before the launch you should run docker-compose.yaml files in User-service and Notification-service packages and configure application.properties for Notification-service (mail connection settings).

If docker images are running and mail settings are correct, you can launch User and Notification services by running Main for each other.
