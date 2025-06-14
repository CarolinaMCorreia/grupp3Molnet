package org.campusmolndal.grupp3molnet.services;

import jakarta.validation.Valid;
import org.campusmolndal.grupp3molnet.dtos.UpdatePasswordDto;
import org.campusmolndal.grupp3molnet.dtos.UpdateUserDto;
import org.campusmolndal.grupp3molnet.dtos.UserDto;
import org.campusmolndal.grupp3molnet.exceptions.InvalidPasswordException;
import org.campusmolndal.grupp3molnet.exceptions.UserNotFoundException;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
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
     * @param user        Användaren som ska uppdateras.
     * @param passwordDto En UpdatePasswordDto som representerar det nya lösenordet.
     * @return Användarinformationen.
     */
    public UserDto updateUserPassword(Users user, UpdatePasswordDto passwordDto) {
        if (passwordDto.getNewPassword().isEmpty() || passwordDto.getConfirmNewPassword().isEmpty()) {
            throw new InvalidPasswordException("Password cannot be empty");
        }
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmNewPassword())) {
            throw new InvalidPasswordException("Passwords do not match");
        }
        if (passwordEncoder.matches(passwordDto.getNewPassword(), user.getPassword())) {
            throw new InvalidPasswordException("New password cannot be the same as the current password");
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        Users updatedUser = userRepository.save(user);
        return new UserDto(updatedUser);
    }

    /**
     * Metod för att uppdatera en användare baserat på ID.
     *
     * @param userId        Användar-ID:t.
     * @param updateUserDto En UpdateUserDto som representerar ny data av den nya användaren.
     * @return Användarinformationen.
     */
    public UserDto updateUserById(Long userId, @Valid UpdateUserDto updateUserDto) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(String.format("User with ID: %d not found", userId));
        } else {
            updateUserDto.getUsername().ifPresent(user::setUsername);
            updateUserDto.getPassword().ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
            user.setAdmin(updateUserDto.isAdmin());
            Users updatedUser = userRepository.save(user);
            return new UserDto(updatedUser);
        }
    }

}