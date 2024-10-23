# Husdjursregistret

## Innehållsförteckning

- [Beskrivning](#beskrivning)
- [Gruppmedlemmar](#gruppmedlemmar) 
- [Installation](#installation)
- [API-dokumentation](#api-dokumentation)
- [Databas](#databas)
- [Deployment och CI/CD](#deployment-och-cicd)
- [Testning och Kodkvalitet](#testning-och-kodkvalitet)
- [Miljövariabler](#miljövariabler)
- [Teknologier](#teknologier)
- [Kontakt](#kontakt)

## Beskrivning

Repot "grupp3Molnet" innehåller ett API som vi har valt att kalla för "Husdjursregistret". API:et är tänkt att fungera som ett register för tex. en klubb, välgörenhetsorganisation, veterinärmottagning eller privatpersoner för att kunna hålla koll på ett omfattade antal djur medd dess respektive ägare (users).
API:et erbjuder CRUD-operationer för att hantera information om dessa husdjur och ägare. Applikationen är byggd med Spring Boot och använder en MySQL-databas som är hostad i AWS databastjänst RDS. Applikationen körs på en AWS Elastic Beanstalk-server och är integrerad med AWS CodeBuild och CodePipeline för CI/CD.

## Gruppmedlemmar:
- Carolina Correia
- Joakim Bagge
- Pontus Kävrestad
- Louise Siesing

## Installation

För att köra applikationen, följ dessa steg:

1. Klona detta repo:
   ```bash
   git clone https://github.com/CarolinaMCorreia/grupp3Molnet.git
   ```

2. Bygg och kör applikationen med Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

Applikationen kommer nu att köras mot databasen på `husdjursregister.chc6mukasloa.eu-north-1.rds.amazonaws.com`. 

### Deployment till AWS Elastic Beanstalk

Den här applikationen är konfigurerad för att automatiskt deployas till AWS Elastic Beanstalk vid händelse av en push via en CI/CD-pipeline som använder GitHub Actions, AWS CodeBuild och AWS CodePipeline.

## API-dokumentation

API:et är dokumenterat med Swagger. När applikationen körs kan du besöka Swagger-UI för att utforska och testa API:et.

### Åtkomst till Swagger-UI

- **Lokal körning**: `http://localhost:5000/swagger-ui/index.html`
- **AWS Elastic Beanstalk-domänen**: `http://husdjursregister1-env.eba-gzkbcjgw.eu-north-1.elasticbeanstalk.com/swagger-ui/index.html`

Testa med de olika endpoints som finns i Swagger-UI genom att lägga till /swagger-ui/index.html i slutet av URL:en.

Det finns även en swagger.json-fil i roten av projektet.

### API Endpoints:

## AuthenticationController:

- GET /auth/signup - Registrerar ny användare
- POST /auth/login - Logga in befintlig användare

## PetController:
- POST /api/pet - Lägga till nytt djur
- GET /api/pet/{id} - Hämta ett husdjur 
- GET /api/pet/all - Hämta alla husdjur
- PUT /api/pet/{id} - Uppdatera husdjur
- DELETE /api/pet/{id} - Radera ett husdjur

## UserController:
- GET /api/users/id/{id} - Hämta en användare med id
- GET /api/users/usernames/{username} - Hämta användare med användarnamn
- GET /api/users/all - Hämta alla användare
- DELETE /api/users/{userId} - Radera användare
- PUT /api/users/id/{userId} - Uppdatera användare med id
- PUT /api/users/password - Uppdatera användares lösenord

## Databas

Applikationen är ansluten till en MySQL-databas som är hostad på AWS RDS.

**Databasens uppsättning:**
- För att köra applikationen första gången behöver databasen `husdjursregister` skapas manuellt i MySQL. Detta görs genom att ansluta till din MySQL-instans och köra kommandot:

  ```sql
  CREATE DATABASE husdjursregister;
  USE husdjursregister;

Efter att databasen har skapats kommer Hibernate automatiskt att generera de nödvändiga tabellerna när applikationen startas.

I samband med att Hibernate startas upp genereras automatiskt en adminanvändare med följande uppgifter:

{
  "username": "admin",
  "password": "adminpassword"
}

För att utföra CRUD-operationer på Users behöver man vara inloggad som admin.

### Databasstruktur

Databasens schema består av två huvudsakliga tabeller:

- **Users**: Lagrar information om användare/ägare till husdjur.
- **Pets**: Lagrar information om husdjur kopplade till användare.

## Deployment och CI/CD

Applikationen byggs och deployas automatiskt genom en CI/CD-pipeline som inkluderar följande steg:

- **AWS CodeBuild**: När en commit pushas till huvudgrenen, triggas en CodeBuild-process som bygger projektet och kör eventuella tester.
- **AWS CodePipeline**: Hanterar deployprocessen. Efter en lyckad build deployas applikationen automatiskt till AWS Elastic Beanstalk.
- **AWS Elastic Beanstalk**: Används för att hantera applikationens driftmiljö. Elastic Beanstalk sköter distributionen, skalningen och övervakningen av applikationen.
-**AWS RDS (Relational Database Service)**: Applikationen använder en RDS-instans för att hantera databasen, vilket ger en skalbar och säker databaslösning som integreras med applikationen på Elastic Beanstalk.

## Testning och Kodkvalitet

Projektet inkluderar tester och verktyg för att upprätthålla hög kodkvalitet:

- **Serviceklass-tester med JUnit**: JUnit används för att testa alla serviceklasser, vilket säkerställer att affärslogiken i applikationen fungerar som förväntat. Dessa tester körs som en del av CI-processen, vilket minskar risken för buggar i produktionsmiljön.
    - För att köra testerna:
      ```bash
      mvn test
      ```
## Miljövariabler

Applikationens miljövariabler hittar du i `application.properties`.

## Teknologier

Applikationen använder följande teknologier och verktyg:

- **Spring Boot**: Backend-ramverk
- **MySQL**: Relationsdatabas (hostad på AWS RDS)
- **Swagger**: API-dokumentation
- **AWS Elastic Beanstalk**: Hosting och deployment
- **AWS CodeBuild & CodePipeline**: CI/CD-hantering
- **JUnit**: För enhetstester av serviceklasser

