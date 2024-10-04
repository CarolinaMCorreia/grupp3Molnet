package org.campusmolndal.grupp3molnet.controllers;


import lombok.RequiredArgsConstructor;

import org.campusmolndal.grupp3molnet.models.Pet;
import org.campusmolndal.grupp3molnet.models.Users;
import org.campusmolndal.grupp3molnet.services.PetService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @PostMapping("")
    public ResponseEntity<Pet> addPet(@AuthenticationPrincipal Users user, @RequestBody Pet pet) {
        return ResponseEntity.ok(petService.addPet(user, pet));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.findPetById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.findAllPets());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@AuthenticationPrincipal Users user, @PathVariable Long id, @RequestBody Pet pet) {
        return ResponseEntity.ok(petService.updatePet(user, id, pet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@AuthenticationPrincipal Users user, @PathVariable Long id) {
        petService.deletePet(user, id);
        return ResponseEntity.noContent().build();
    }
}
