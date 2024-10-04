package org.campusmolndal.grupp3molnet.services;

import org.campusmolndal.grupp3molnet.models.Pet;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.repositories.PetRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {
    PetRepository petRepository;

    public Pet addPet(Users user, Pet pet) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPet'");
    }

    public Pet findPetById(Long id) {
        return petRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No pet found"));
    }

    public Iterable<Pet> findAllPets() {
        Iterable<Pet> list = petRepository.findAll();
        if (!list.iterator().hasNext()) throw new ResourceNotFoundException("No pets found");
        return list;
    }

    public Pet updatePet(Users user, Long id, Pet pet) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePet'");
    }

    public void deletePet(Users user, Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePet'");
    }
}
