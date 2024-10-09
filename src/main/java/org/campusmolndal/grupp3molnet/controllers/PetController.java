package org.campusmolndal.grupp3molnet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.campusmolndal.grupp3molnet.dtos.PetDto;
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

    @Operation(summary = "Add new pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added the pet"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("")
    public ResponseEntity<PetDto> addPet(@AuthenticationPrincipal Users user, @RequestBody Pet pet) {
        return ResponseEntity.ok(petService.addPet(user, pet));
    }

    @Operation(summary = "Retrieve pet by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the pet"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.findPetById(id));
    }

    @Operation(summary = "Retrieve all pets")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all pets")
    @GetMapping("/all")
    public ResponseEntity<Iterable<PetDto>> getAllPets() {
        return ResponseEntity.ok(petService.findAllPets());
    }

    @Operation(summary = "Update existing pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the pet"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@AuthenticationPrincipal Users user, @PathVariable Long id, @RequestBody PetDto pet) {
        return ResponseEntity.ok(petService.updatePet(user, id, pet));
    }

    @Operation(summary = "Delete pet by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the pet"),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@AuthenticationPrincipal Users user, @PathVariable Long id) {
        petService.deletePet(user, id);
        return ResponseEntity.noContent().build();
    }
}
