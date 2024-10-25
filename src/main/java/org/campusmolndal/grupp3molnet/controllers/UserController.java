package org.campusmolndal.grupp3molnet.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.campusmolndal.grupp3molnet.dtos.UpdatePasswordDto;
import org.campusmolndal.grupp3molnet.dtos.UpdateUserDto;
import org.campusmolndal.grupp3molnet.dtos.UserDto;
import org.campusmolndal.grupp3molnet.exceptions.UserNotFoundException;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Endpoints for user management")
public class UserController {

    private final UserService userService;

    //@Autowired
    //public UserController(UserService userService) {this.userService = userService;}

    @GetMapping("/id/{userId}")
    @Operation(summary = "Find user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and returned"),
            @ApiResponse(responseCode = "204", description = "No user found")
    })
    public ResponseEntity<UserDto> findUserById(
            @Parameter(description = "The id of the user to be returned")
            @PathVariable Long userId) {
        UserDto userDto = userService.findUserById(userId);
        if (userDto == null) {
            throw new UserNotFoundException(String.format("No user was found with id %d", userId));
        }
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/ids")
    @Operation(summary = "Find users by ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found and returned"),
            @ApiResponse(responseCode = "204", description = "No users found")
    })
    public ResponseEntity<Set<UserDto>> findUsersByIds(
            @Parameter(description = "The ids of the users to be returned")
            @RequestParam Set<Long> ids) {
        Set<UserDto> users = userService.findUsersByIds(ids);
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users were found with the provided ids");
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/usernames")
    @Operation(summary = "Find users by multiple usernames")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found and returned"),
            @ApiResponse(responseCode = "204", description = "No users found")
    })
    public ResponseEntity<Set<UserDto>> findUsersByUsernames(
            @Parameter(description = "The username of the user to be returned")
            @RequestParam List<String> usernames) {
        Set<UserDto> userDtos = userService.findUsersByUsernames(usernames);
        if (userDtos.isEmpty()) {
            throw new UserNotFoundException("No users were found with the provided usernames");
        }
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/usernames/{username}")
    @Operation(summary = "Find user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and returned"),
            @ApiResponse(responseCode = "204", description = "User not found")
    })
    public ResponseEntity<UserDto> findUserByUsername(
            @Parameter(description = "The username of the user to be returned")
            @PathVariable String username) {
        UserDto userDto = userService.findUserByUsername(username);
        if (userDto == null) {
            throw new UserNotFoundException(String.format("No user was found with username %s", username));
        }
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/all")
    @Operation(summary = "Find all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found and returned"),
            @ApiResponse(responseCode = "204", description = "No users found")
    })
    public ResponseEntity<Iterable<UserDto>> findAllUsers() {
        Iterable<UserDto> users = userService.findAllUsers();
        if (!users.iterator().hasNext()) {
            throw new UserNotFoundException("No users were found");
        }
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/id/{userId}")
    @Operation(summary = "Delete user by id")
    @Secured("ROLE_ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUserById(
            @Parameter(description = "The id of the user to be deleted")
            @PathVariable Long userId) {
        if (userService.deleteUserById(userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/id/{userId}")
    @Operation(summary = "Update user by id")
    @Secured("ROLE_ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDto> updateUserById(
            @Parameter(description = "The id of the user to be updated")
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserDto updateUserDto) {
        try {
            UserDto updatedUser = userService.updateUserById(userId, updateUserDto);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/password")
    @Operation(summary = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated"),
            @ApiResponse(responseCode = "400", description = "Invalid password")
    })
    public ResponseEntity<UserDto> updateUserPassword(
            @AuthenticationPrincipal Users user,
            @Valid @RequestBody UpdatePasswordDto passwordDto) {
        UserDto updatedUser = userService.updateUserPassword(user, passwordDto);
        return ResponseEntity.ok(updatedUser);
    }
}
