package org.campusmolndal.grupp3molnet;

import static org.mockito.Mockito.mock;

import org.campusmolndal.grupp3molnet.repositories.PetRepository;
import org.campusmolndal.grupp3molnet.services.PetService;
import org.junit.jupiter.api.BeforeEach;

public class PetServiceTest {
    PetRepository petRepository;
    PetService petService;

    @BeforeEach
    void setup() {
        petRepository = mock(PetRepository.class);
        petService = new PetService(petRepository);
    }
}
