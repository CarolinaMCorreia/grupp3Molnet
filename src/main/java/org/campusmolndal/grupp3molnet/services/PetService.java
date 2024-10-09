package org.campusmolndal.grupp3molnet.services;

import org.campusmolndal.grupp3molnet.dtos.PetDto;
import org.campusmolndal.grupp3molnet.exceptions.ResourceNotFoundException;
import org.campusmolndal.grupp3molnet.models.Pet;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.repositories.PetRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;

    public PetDto addPet(Users user, Pet pet) {
        pet.setUser(user); // owner ska vara personen som gjorde anropet
        pet.setId(null); // null för att förhindra att existerande pet uppdateras
        return new PetDto(petRepository.save(pet));
    }

    public PetDto findPetById(Long id) {
        if(!petRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("No pet found");
        return new PetDto(petRepository.findById(id).get());
    }

    public Iterable<PetDto> findAllPets() {
        Iterable<Pet> list = petRepository.findAll();
        List<PetDto> petDtos = new ArrayList<>();
        if (!list.iterator().hasNext()) throw new ResourceNotFoundException("No pets found");
        for (Pet pet : list) {
            petDtos.add(new PetDto(pet));
        }
        return petDtos;
    }

    public PetDto updatePet(Users user, Long id, PetDto pet) {
        Pet petToUpdate = petRepository.findById(id).get();
        if (petToUpdate.getUser().getUserId() != user.getUserId() && !user.isAdmin()) { // TODO: kolla getOwner() och isAdmin()
            throw new AccessDeniedException("Unauthorized Access");
        }
        petToUpdate.setName(pet.getName());
        petToUpdate.setBirthDate(pet.getBirthdate());
        petToUpdate.setBreed(pet.getBreed());
        petToUpdate.setSpecies(pet.getSpecies());
        return new PetDto(petRepository.save(petToUpdate));
    }

    public void deletePet(Users user, Long id) {
        Pet petToDelete = petRepository.findById(id).get();
        if (petToDelete.getUser().getUserId() != user.getUserId() && !user.isAdmin()) { // TODO: kolla getOwner() och isAdmin()
            throw new AccessDeniedException("Unauthorized Access");
        }
        petRepository.delete(petToDelete);
    }
}
