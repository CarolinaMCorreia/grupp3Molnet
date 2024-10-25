package org.campusmolndal.grupp3molnet.services;


import org.campusmolndal.grupp3molnet.dtos.LoginUserDto;
import org.campusmolndal.grupp3molnet.dtos.RegisterUserDto;
import org.campusmolndal.grupp3molnet.dtos.UserDto;
import org.campusmolndal.grupp3molnet.services.JwtService;
import org.campusmolndal.grupp3molnet.exceptions.UserAuthenticationException;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final JwtService jwtService;

    /**
     * Creates an instance of AuthenticationService with necessary dependencies.
     *
     * @param userRepository Repository for accessing user data.
     * @param authenticationManager Handles the authentication process.
     * @param passwordEncoder Encrypts passwords.
     */
    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

        // Skapa ny användare
        boolean isAdmin = Boolean.parseBoolean((input.getIsAdmin()));
        Users users = Users.builder()
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .admin(isAdmin)
                .build();

        userRepository.save(users);

        return new UserDto(users);
    }

    /**
     * Method to authenticate a user
     * @param input LoginUserDto
     * @return User
     */
    public String authenticate(LoginUserDto input) {
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
            if (passwordEncoder.matches(input.getPassword(), userToAuthenticate.get().getPassword())) {
                return jwtService.generateToken(userToAuthenticate.get());
            }
        } catch (AuthenticationException e) {
            throw new UserAuthenticationException("Invalid credentials");
        }
        return null;
    }
}
