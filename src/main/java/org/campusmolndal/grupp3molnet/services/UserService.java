package org.campusmolndal.grupp3molnet.services;


import org.campusmolndal.grupp3molnet.dtos.RegisterUserDto;
import org.campusmolndal.grupp3molnet.dtos.UserDto;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Iterable<UserDto> findAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(user -> new UserDto(user))
                .collect(Collectors.toList());
    }

    /**
     * Method to find a user by id.
     *
     * @param id The id of the user.
     * @return The user with the given id.
     */
    public UserDto findUserById(Long id) {
        Users users = userRepository.findById(id).orElse(null);
        return users != null ? new UserDto(users) : null;
    }

    public UserDto findUserByUsername(String username) {
        Users users = userRepository.findByUsername(username).orElse(null);
        return users != null ? new UserDto(users) : null;
    }

    /**
     * Hämta användare baserat på en lista med ID:n.
     *
     * @param userIds En lista med användar-ID:n.
     * @return En lista med UserDto-objekt.
     */
    public Set<UserDto> findUsersByIds(Set<Long> userIds) {
        // Implementera metoden för att hämta användare baserat på ID:n
        List<Users> users = userRepository.findAllById(userIds);
        return users.stream()
                .map(UserDto::new)
                .collect(Collectors.toSet());
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kontrollera om autentisering är tillgänglig och korrekt
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // Hämta användaren från databasen
            Optional<Users> userOptional = userRepository.findByUsername(username);

            // Kontrollera om användaren finns och returnera användar-ID
            return userOptional.map(Users::getUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        }

        throw new AuthenticationCredentialsNotFoundException("Authentication credentials not found");
    }

    // Lägg till metoder för att hämta användare baserat på användarnamn
    public Set<UserDto> findUsersByUsernames(List<String> usernames) {
        List<Users> users = userRepository.findByUsernameIn(usernames);
        return users.stream()
                .map(UserDto::new)
                .collect(Collectors.toSet());
    }


    /**
     * Hämta användar-ID:n baserat på en lista med användarnamn.
     *
     * @param usernames En lista med användarnamn.
     * @return En lista med användar-ID:n.
     */
    public Set<Long> findUserIdsByUsernames(List<String> usernames) {
        // Hämta användare baserat på användarnamn
        List<Users> users = userRepository.findByUsernameIn(usernames);

        // Konvertera listan med användare till en lista med ID:n
        return users.stream()
                .map(Users::getUserId)
                .collect(Collectors.toSet());
    }

    private UserDto convertToUserDto(Users users) {
        return new UserDto(users.getUserId(), users.getUsername());
    }

    public UserDto registerUser(RegisterUserDto registerUserDto) {
        Users newUsers = new Users();
        newUsers.setUsername(registerUserDto.getUsername());
        newUsers.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        userRepository.save(newUsers);

        UserDto userDto = new UserDto();
        userDto.setUsername(newUsers.getUsername());
        return userDto;
    }
}