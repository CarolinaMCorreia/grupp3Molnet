package org.campusmolndal.grupp3molnet.services;

import jakarta.validation.Valid;
import org.campusmolndal.grupp3molnet.dtos.UpdateUserDto;
import org.campusmolndal.grupp3molnet.dtos.UserDto;
import org.campusmolndal.grupp3molnet.exceptions.UserNotFoundException;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Hämtar alla användare för tjänsten.
     *
     * @return En lista med alla registrerade användare.
     */
    public Set<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toSet());
    }

    /**
     * Hämtar användare baserat på ID.
     *
     * @param id Användar-ID:t.
     * @return Användaren med det angivna ID:t.
     */
    public UserDto findUserById(Long id) {
        Users users = userRepository.findById(id).orElse(null);
        return users != null ? new UserDto(users) : null;
    }

    /**
     * Hämta användare baserat på användarnamn.
     *
     * @param username Användarnamnet.
     * @return Användaren med det angivna användarnamnet.
     */
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

    /**
     * Hämta användare baserat på en lista med användarnamn.
     *
     * @param usernames En lista med användarnamn.
     * @return En lista med UserDto-objekt.
     */
    public Set<UserDto> findUsersByUsernames(List<String> usernames) {
        List<Users> users = userRepository.findByUsernameIn(usernames);
        return users.stream()
                .map(UserDto::new)
                .collect(Collectors.toSet());
    }

    /**
     * Metod för att ta bort en användare utifrån användar-ID:t.
     *
     * @param userId Användar-ID:t.
     * @return sant om användaren har tagits bort, annars falskt.
     */
    public boolean deleteUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User IDs cannot be empty");
        } else {
            userRepository.deleteById(userId);
            return true;
        }
    }

    /**
     * Metod för att uppdatera en användares lösenord.
     *
     * @param user Användaren som ska uppdateras.
     * @param password Det nya lösenordet.
     * @return Användarinformationen.
     */
    public UserDto updateUserPassword(Users user, String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        } else {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return new UserDto(user);
        }
    }

    /**
     * Metod för att uppdatera en användare baserat på ID.
     *
     * @param userId Användar-ID:t.
     * @param updateUserDto En UpdateUserDto som representerar ny data av den nya användaren.
     * @return Användarinformationen.
     */
    public UserDto updateUserById(Long userId, @Valid UpdateUserDto updateUserDto) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(String.format("User with ID: %d not found",userId));
        } else {
            updateUserDto.getUsername().ifPresent(user::setUsername);
            updateUserDto.getPassword().ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
            user.setAdmin(updateUserDto.isAdmin());
            Users updatedUser = userRepository.save(user);
            return new UserDto(updatedUser);
        }
    }
}