package org.campusmolndal.grupp3molnet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.campusmolndal.grupp3molnet.dtos.LoginUserDto;
import org.campusmolndal.grupp3molnet.dtos.RegisterUserDto;
import org.campusmolndal.grupp3molnet.dtos.UserDto;
import org.campusmolndal.grupp3molnet.models.LoginResponse;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.services.AuthenticationService;
import org.campusmolndal.grupp3molnet.services.JwtService;
import org.campusmolndal.grupp3molnet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller för att hantera autentisering-relaterade operationer såsom registrering och inloggning av användare.
 */
@RequestMapping("/auth")
@RestController
@Tag(name = "Authentication Controller", description = "Endpoints för användarregistrering och inloggning")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    /**
     * Konstruktor för att injicera beroenden för JwtService och AuthenticationService.
     *
     * @param jwtService JWT-tjänsten
     * @param authenticationService autentiseringstjänsten
     * @param userService användartjänsten
     */

    @Autowired
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     * Hanterar registreringen av en ny användare.
     *
     * @return en ResponseEntity som innehåller den registrerade användaren
     */
    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Creates and stores a new user in the database")
    @ApiResponse(responseCode = "200", description = "User successfully registered")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserDto registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Hanterar inloggningen av en användare.
     *
     * @return en ResponseEntity som innehåller JWT-token och utgångstid
     */
    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Allows an existing user to log in to the API")
    @ApiResponse(responseCode = "200", description = "User successfully logged in")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        Users authenticatedUsers = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUsers);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
}
