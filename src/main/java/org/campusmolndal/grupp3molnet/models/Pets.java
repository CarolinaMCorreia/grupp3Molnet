package org.campusmolndal.grupp3molnet.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pets {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the pet", example = "1")
    private Long id;

    @Enumerated(EnumType.STRING)  // Begränsad av enum
    @Schema(description = "Enums to limit the choice of the user", example = "Dog, Cat, Bird, Rodent")
    private Species species;

    @Column(name = "breed")
    @Schema(description = "Breed of the pet is set from input of the user, moderated by admin", example = "Pomeranian")
    @Size(max = 50)
    private String breed; // Checkas av admin

    @Column(name = "name")
    @Schema(description = "Name of the pet", example = "Teddy")
    @Size(max = 50)
    private String name;

    @Column(name = "birthdate")
    @Schema(description = "Date of birth of the pet", example = "2024-10-01") // Date will be set automatically
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owners owner;


    // Custom all-args constructor in order to set date automatically
    public Pets(Species species, String breed, String name, Owners owner) {
        this.species = species;
        this.breed = breed;
        this.name = name;
        this.birthDate = LocalDate.now(); // Set birthDate to current date
        this.owner = owner;
    }


    // Enum inuti Pet-klassen
    public enum Species {
        DOG,
        CAT,
        BIRD,
        RODENT
    }
}
