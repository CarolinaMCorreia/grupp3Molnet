# Pet Registry

## Table of Contents

- [Description](#description)
- [Group Members](#group-members)
- [Installation](#installation)
- [API Documentation](#api-documentation)
- [Database](#database)
- [Deployment and CI/CD](#deployment-and-cicd)
- [Testing and Code Quality](#testing-and-code-quality)
- [Environment Variables](#environment-variables)
- [Technologies](#technologies)

## Description

The repository "grupp3Molnet" contains an API we chose to call the "Pet Registry". The API is intended to function as a registry for, for example, a club, charity organization, veterinary clinic, or private individuals to keep track of a large number of animals along with their respective owners (users).

The API provides full CRUD operations to manage information about pets and their owners. The application is built using Spring Boot and utilizes a MySQL database hosted on AWS RDS. It runs on an AWS Elastic Beanstalk server and is integrated with AWS CodeBuild and CodePipeline for CI/CD.

## Group Members:
- Carolina Correia
- Joakim Bagge
- Pontus Kävrestad
- Louise Siesing

## Installation

To run the application, follow these steps:

1. Clone this repo:
   ```bash
   git clone https://github.com/CarolinaMCorreia/grupp3Molnet.git
   ```

2. Build and run the application using Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

The application will now run connected to the database at:  
`husdjursregister.chc6mukasloa.eu-north-1.rds.amazonaws.com`

### Deployment to AWS Elastic Beanstalk

This application is configured to automatically deploy to AWS Elastic Beanstalk when a push occurs via a CI/CD pipeline that uses AWS CodeBuild and AWS CodePipeline.

## API Documentation

The API is documented using Swagger. When the application is running, you can visit Swagger-UI to explore and test the API.

### Accessing Swagger-UI

- **Local run**: `http://localhost:5000/swagger-ui/index.html`
- **AWS Elastic Beanstalk domain**: `http://husdjursregister1-env.eba-gzkbcjgw.eu-north-1.elasticbeanstalk.com/swagger-ui/index.html`

Test the various endpoints available in Swagger-UI or ask Group 4 for their corresponding API client.

There is also a `swagger.json` file in the root of the project.

### API Endpoints:

## HomeController:

- GET / - Application entrypoint

## AuthenticationController:

- POST /auth/signup - Register a new user  
- POST /auth/login - Log in an existing user

## PetController:
- POST /api/pet - Add a new pet  
- GET /api/pet/{id} - Retrieve a pet  
- GET /api/pet/all - Retrieve all pets  
- PUT /api/pet/{id} - Update a pet  
- DELETE /api/pet/{id} - Delete a pet

## UserController:
- GET /api/users/id/{id} - Retrieve a user by ID  
- GET /api/users/usernames/{username} - Retrieve a user by username  
- GET /api/users/all - Retrieve all users  
- DELETE /api/users/{userId} - Delete a user  
- PUT /api/users/id/{userId} - Update a user by ID

## Database

The application is connected to a MySQL database hosted on AWS RDS.

**Database Setup:**
- To run the application for the first time, the database `husdjursregister` must be manually created in MySQL. This is done by connecting to your MySQL instance and running the command:

  ```sql
  CREATE DATABASE husdjursregister;
  USE husdjursregister;
  ```

Once the database has been created, Hibernate will automatically generate the necessary tables when the application starts.

When Hibernate initializes, it automatically generates an admin user with the following credentials:

```json
{
  "username": "admin",
  "password": "adminpassword"
}
```

To perform CRUD operations on users, you need to be logged in as an admin.

### Database Structure

The database schema consists of two main tables:

- **Users**: Stores information about users/owners of pets  
- **Pets**: Stores information about pets linked to users

## Deployment and CI/CD

The application is automatically built and deployed through a CI/CD pipeline that includes the following steps:

- **AWS CodeBuild**: When a commit is pushed to the main branch, a CodeBuild process is triggered to build the project and run any tests.  
- **AWS CodePipeline**: Manages the deployment process. After a successful build, the application is automatically deployed to AWS Elastic Beanstalk.  
- **AWS Elastic Beanstalk**: Used to manage the application's runtime environment. Elastic Beanstalk handles distribution, scaling, and monitoring of the application.  
- **AWS RDS (Relational Database Service)**: The application uses an RDS instance to manage the database, providing a scalable and secure database solution that integrates with the application on Elastic Beanstalk.

## Testing and Code Quality

The project includes tests and tools to maintain high code quality:

- **Service Class Testing with JUnit**: JUnit is used to test all service classes, ensuring that the business logic functions as expected. These tests run as part of the CI process, reducing the risk of bugs in the production environment.
    - To run the tests:
      ```bash
      mvn test
      ```

## Environment Variables

The application's environment variables are located in the `application.properties` file.

## Technologies

The application uses the following technologies and tools:

- **Spring Boot**: Backend framework  
- **MySQL**: Relational database (hosted on AWS RDS)  
- **Swagger**: API documentation  
- **AWS Elastic Beanstalk**: Hosting and deployment  
- **AWS CodeBuild & CodePipeline**: CI/CD management  
- **JUnit**: Unit testing for service classes
