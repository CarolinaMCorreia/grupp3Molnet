package org.campusmolndal.grupp3molnet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.campusmolndal.grupp3molnet.dtos.PetDto;
import org.campusmolndal.grupp3molnet.exceptions.ResourceNotFoundException;
import org.campusmolndal.grupp3molnet.models.Pet;
import org.campusmolndal.grupp3molnet.repositories.PetRepository;
import org.campusmolndal.grupp3molnet.services.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        PetDto actualDto = petService.addPet(caller, examplePet);

        assertTrue(actualDto.getName().contains(examplePet.getName()));
        assertEquals(caller.getUserId(), actualDto.getUserId());
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
}
