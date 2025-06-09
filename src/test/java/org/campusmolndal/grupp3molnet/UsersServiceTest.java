package org.campusmolndal.grupp3molnet;

import org.campusmolndal.grupp3molnet.dtos.UpdateUserDto;
import org.campusmolndal.grupp3molnet.dtos.UserDto;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.repositories.UserRepository;
import org.campusmolndal.grupp3molnet.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getting all users
    @Test
    void testGetAllUsers() {
        // Create a list of users for testing
        List<Users> usersList = new ArrayList<>();
        Users user1 = new Users();
        user1.setUserId(1L);
        user1.setUsername("user1");
        user1.setPassword("password");

        Users user2 = new Users();
        user2.setUserId(2L);
        user2.setUsername("user2");
        user2.setPassword("password2");

        usersList.add(user1);
        usersList.add(user2);

        // Mock the repository to return the users list
        when(userRepository.findAll()).thenReturn(usersList);

        // Call the service method
        Set<UserDto> result = userService.findAllUsers();

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify the usernames in the returned set
        assertTrue(result.stream().anyMatch(user -> "user1".equals(user.getUsername())));
        assertTrue(result.stream().anyMatch(user -> "user2".equals(user.getUsername())));

        verify(userRepository, times(1)).findAll();
    }

    // Test for finding user by ID
    @Test
    void testFindUserById_UserExists() {
        Long userId = 1L;
        Users user = new Users();
        user.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindUserById_UserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserDto result = userService.findUserById(userId);

        assertNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    // Test for deleting a user by ID
    @Test
    void testDeleteUserById_ValidId() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        boolean result = userService.deleteUserById(userId);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUserById_NullId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.deleteUserById(null)
        );

        assertEquals("User IDs cannot be empty", exception.getMessage());
        verify(userRepository, never()).deleteById(any());
    }

    // Test for updating user to admin by ID
    @Test
    void updateUserToAdmin() {
        Long userId = 1L;
        Users user = new Users();
        user.setUserId(userId);

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setAdmin(true); // Only changing admin status

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.updateUserById(userId, updateUserDto);

        assertNotNull(result);
        assertTrue(user.isAdmin());
        verify(userRepository, times(1)).save(user);
    }
}
