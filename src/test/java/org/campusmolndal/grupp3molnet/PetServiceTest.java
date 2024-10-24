package org.campusmolndal.grupp3molnet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.campusmolndal.grupp3molnet.dtos.PetDto;
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

    @BeforeEach
    void setup() {
        petRepository = mock(PetRepository.class);
        petService = new PetService(petRepository);
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

        Pet pet = new Pet(
            1L,
            Species.DOG,
            "Schäfer",
            "namn",
            LocalDate.now(),
            null
        );
        
        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        }
        );

        PetDto actualDto = petService.addPet(caller, pet);

        assertTrue(actualDto.getName().contains(pet.getName()));
        assertEquals(caller.getUserId(), actualDto.getUserId());
    }
}
