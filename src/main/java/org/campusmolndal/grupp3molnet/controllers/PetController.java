package org.campusmolndal.grupp3molnet.controllers;


import lombok.RequiredArgsConstructor;

import org.campusmolndal.grupp3molnet.models.Pets;
import org.campusmolndal.grupp3molnet.services.PetService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @PostMapping("")
    public ResponseEntity<Pets> addPet(@RequestBody Pets pet) {
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pets> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.findById(id));
    }

    @GetMapping("")
    public ResponseEntity<Iterable<Pets>> getAllPets() {

    }

    @PutMapping("/{id}")
    public ResponseEntity<Pets> updatePet(@PathVariable Long id, @RequestBody Pets pet) {

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        
    }
}
