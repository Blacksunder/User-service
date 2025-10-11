# User-service
REST API with basic CRUD operations using Spring. It provides the application for getting, updating, saving and removing users from/to the database. Notification and User services are connected with Kafka server, that provides updating and deleting operations notification mechanism by sending mail messages. 
Api gateway provides the single application entry point, all services get their properties from config-server and be published on eureka-server.

Before the launch you should configure configs/notification-service.yaml for Notification-service (mail connection settings) and launch run.sh script.

If docker images are running and mail settings are correct, you can use the application with all provided features.
