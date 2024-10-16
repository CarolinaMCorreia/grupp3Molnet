# Husdjursregistert

### Gruppmedlemmar:
- Carolina Correia
- Joakim Bagge
- Pontus Kävrestad
- Louise Siesing
 

### Endpoints:

AuthenticationController:

- GET /auth/signup - Registrerar ny användare
- POST /auth/login - Logga in befintlig användare

PetController:
- POST /api/pet - Lägga till nytt djur
- GET /api/pet/{id} - Hämta ett husdjur 
- GET /api/pet/all - Hämta alla husdjur
- PUT /api/pet/{id} - Uppdatera husdjur
- DELETE /api/pet/{id} - Radera ett husdjur

UserController:
- GET /api/users/id/{id} - Hämta en användare med id
- GET /api/users/usernames/{username} - Hämta användare med användarnamn
- GET /api/users/all - Hämta alla användare
- DELETE /api/users/{userId} - Radera användare
- PUT /api/users/id/{userId} - Uppdatera användare med id
- PUT /api/users/password - Uppdatera användares lösenord

