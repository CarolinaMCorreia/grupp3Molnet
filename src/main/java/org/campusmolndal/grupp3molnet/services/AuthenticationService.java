package org.campusmolndal.grupp3molnet.services;


import org.campusmolndal.grupp3molnet.dtos.LoginUserDto;
import org.campusmolndal.grupp3molnet.dtos.RegisterUserDto;
import org.campusmolndal.grupp3molnet.dtos.UserDto;
import org.campusmolndal.grupp3molnet.exceptions.UserAuthenticationException;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class for handling authentication, such as registration and login.
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Creates an instance of AuthenticationService with necessary dependencies.
     *
     * @param userRepository Repository for accessing user data.
     * @param authenticationManager Handles the authentication process.
     * @param passwordEncoder Encrypts passwords.
     */
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     *
     * @return The newly created user.
     * @throws IllegalArgumentException if the input is null.
     */
    public UserDto signup(RegisterUserDto input) {
        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            throw new UserAuthenticationException(input.getUsername(), "User already exists");
        }
        //TODO: REMOVE THIS PRINT
        System.out.println("Is Admin: " + input.isAdmin());
        // Skapa ny användare
        Users users = Users.builder()
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .admin(input.isAdmin())
                .build();

        userRepository.save(users);

        return new UserDto(users);
    }


    /**
     * Authenticates a user based on login credentials.
     *
     * @return The authenticated user.
     * @throws NoSuchElementException if the user cannot be found or the password is incorrect.
     */
    /**
     * Method to authenticate a user
     * @param input LoginUserDto
     * @return User
     */
    public Users authenticate(LoginUserDto input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword())
            );
            Optional<Users> userToAuthenticate = userRepository.findByUsername(input.getUsername());
            if (!userToAuthenticate.isPresent()) {
                throw new NoSuchElementException("User not found");
            }
            return userToAuthenticate.get();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

}
