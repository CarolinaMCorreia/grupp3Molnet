package org.campusmolndal.grupp3molnet.services;

import org.campusmolndal.grupp3molnet.exceptions.ResourceNotFoundException;
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
        Pet petToUpdate = findPetById(id);
        if (!petToUpdate.getUser().getUserId() == user.getUserId() && !user.isAdmin()) { // TODO: kolla getUser() och isAdmin()
            throw new Exception(); // TODO: byt ut exception mot passande i global exceptionhandler
        }
        pet.setId(id); //säkerställa att id i pet och id dem skickar in är samma så rätt pet uppdateras
        return petRepository.save(pet);
    }

    public void deletePet(Users user, Long id) {
        Pet petToDelete = findPetById(id);
        if (!petToDelete.getUser().getUserId() == user.getUserId() && !user.isAdmin()) { // TODO: kolla getUser() och isAdmin()
            throw new Exception(); // TODO: byt ut exception mot passande i global exceptionhandler
        }
        petRepository.delete(petToDelete);
    }
}
