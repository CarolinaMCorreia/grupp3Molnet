package org.campusmolndal.grupp3molnet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.campusmolndal.grupp3molnet.dtos.PetDto;
import org.campusmolndal.grupp3molnet.exceptions.ResourceNotFoundException;
import org.campusmolndal.grupp3molnet.models.Pet;
import org.campusmolndal.grupp3molnet.repositories.PetRepository;
import org.campusmolndal.grupp3molnet.services.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.models.Pet.Species;

class PetServiceTest {
    PetRepository petRepository;
    PetService petService;
    Pet examplePet;

    @BeforeEach
    void setup() {
        petRepository = mock(PetRepository.class);
        petService = new PetService(petRepository);
        examplePet = new Pet(
            1L,
            Species.DOG,
            "Schäfer",
            "namn",
            LocalDate.now(),
            Users.builder().userId(1L).build()
        );
    }

    @Test
    void addPet() {
        Users caller = new Users(
            1L,
            "username",
            "password",
            false,
            null
        );
        
        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        }
        );

        PetDto petDto = petService.addPet(caller, examplePet);

        assertTrue(petDto.getName().contains(examplePet.getName()));
        assertEquals(caller.getUserId(), petDto.getUserId());
    }

    @Test
    void findPetById() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(examplePet));
        when(petRepository.findById(2L)).thenReturn(Optional.empty());

        PetDto actualDto = petService.findPetById(1L);
        Exception exception = assertThrowsExactly(ResourceNotFoundException.class, () -> {
            petService.findPetById(2L);
        });
        String expectedMsg = "No pet found";
        String actualMsg = exception.getMessage();

        assertTrue(actualMsg.contains(expectedMsg));
        assertEquals(examplePet.getName(), actualDto.getName());
    }

    @Test
    void findAllPetsHappy() {
        when(petRepository.findAll()).thenReturn(List.of(examplePet));
        Iterable<PetDto> result = petService.findAllPets();

        assertEquals(examplePet.getName(), result.iterator().next().getName());
    }

    @Test
    void findAllPetsBad() {
        when(petRepository.findAll()).thenReturn(List.of());
        
        Exception exception = assertThrowsExactly(
            ResourceNotFoundException.class,
            () -> petService.findAllPets()
        );
        String expectedMsg = "No pets found";
        String actualMsg = exception.getMessage();

        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    void updateOwnExistingPet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(examplePet));
        
        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> {
            Pet pet = invocation.getArgument(0);
            pet.setName("newName");
            return pet;
        });

        Users caller = new Users(
            1L,
            "username",
            "password",
            false,
            List.of(examplePet)
        );

        PetDto petDto = new PetDto(examplePet);
        petDto.setName("newName");

        PetDto actualDto = petService.updatePet(caller, 1L, petDto);

        assertEquals(petDto.getName(), actualDto.getName());
    }

    @Test
    void updateNonExistingPet() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrowsExactly(ResourceNotFoundException.class, () -> {
            petService.updatePet(
                Users.builder().userId(1L).admin(false).build(),
                1L,
                new PetDto(examplePet)
            );
        });
        String excpectedMsg = "No pet found";
        String actualMsg = exception.getMessage();

        assertTrue(actualMsg.contains(excpectedMsg));
    }

    @Test
    void updateOthersExistingPetNotAdmin() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(examplePet));

        Exception exception = assertThrowsExactly(AccessDeniedException.class, () -> {
            petService.updatePet(
                Users.builder().userId(2L).admin(false).build(),
                1L,
                new PetDto(examplePet)
            );
        });
        String expectedMsg = "Unauthorized Access";
        String actualMsg = exception.getMessage();

        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    void updateOthersExistingPetIsAdmin() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(examplePet));

        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> {
            Pet pet = invocation.getArgument(0);
            pet.setName("newName");
            return pet;
        });

        PetDto petDto = new PetDto(examplePet);
        petDto.setName("newName");

        PetDto actualDto = petService.updatePet(
            Users.builder().userId(2L).admin(true).build(),
            1L,
            petDto
        );

        assertEquals(petDto.getName(), actualDto.getName());
    }

    @Test
    void deleteOwnExistingPet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(examplePet));
        petService.deletePet(
            Users.builder().userId(1L).admin(false).build(),
            1L
        );

        verify(petRepository, times(1)).delete(any(Pet.class));
    }

    @Test
    void deleteNonExistingPet() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrowsExactly(ResourceNotFoundException.class, () -> {
            petService.deletePet(
                Users.builder().userId(1L).admin(false).build(),
                1L);
        });
        String expectedMsg = "No pet found";
        String actualMsg = exception.getMessage();

        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    void deleteOthersExistingPetNotAdmin() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(examplePet));

        Exception exception = assertThrowsExactly(AccessDeniedException.class, () -> {
            petService.deletePet(
                Users.builder().userId(2L).admin(false).build(),
                1L);
        });
        String expectedMsg = "Unauthorized Access";
        String actualMsg = exception.getMessage();

        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    void deleteOthersExistingPetIsAdmin() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(examplePet));
        petService.deletePet(
            Users.builder().userId(2L).admin(true).build(),
            1L
        );

        verify(petRepository, times(1)).delete(any(Pet.class));
    }
}
