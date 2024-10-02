package org.campusmolndal.grupp3molnet.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a pet entity with attributes such as species, breed, name, birth date, and associated user.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pets {

    /**
     * The unique identifier of the pet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the pet", example = "1")
    private Long id;

    /**
     * The species of the pet, limited to predefined enums.
     */
    @Enumerated(EnumType.STRING)  // Begränsad av enum
    @Column(nullable = false, name = "species")
    @Schema(description = "Enums to limit the choice of the user", example = "Dog, Cat, Bird, Rodent")
    private Species species;

    /**
     * The breed of the pet, set from user input and moderated by admin.
     */
    @Column(nullable = false, name = "breed")
    @Schema(description = "Breed of the pet is set from input of the user, moderated by admin", example = "Pomeranian")
    @Size(max = 50)
    private String breed; // Checkas av admin

    /**
     * The name of the pet.
     */
    @Column(name = "name", nullable = false)
    @Schema(description = "Name of the pet", example = "Teddy")
    @Size(max = 50)
    private String name;

    /**
     * The birthdate of the pet.
     * Must follow the ISO date format (yyyy-MM-dd) and cannot be a future date.
     */
    @Column(name = "birthdate", nullable = false)
    @Schema(description = "The birthdate of the pet", example = "2024-10-01")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // ISO date format: yyyy-MM-dd
    @Setter(AccessLevel.NONE)  // Prevent Lombok from generating a setter for birthDate
    private LocalDate birthDate;

    /**
     * The user who owns the pet.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "The user the pet belongs to", example = "1")
    private Users user;

    /**
     * Custom setter for birthDate with validation.
     * Prevents user from setting a future birthdate for their pet and ensures the date is in the correct format.
     *
     * @param birthDateStr the birthdate as a string in the format yyyy-MM-dd
     * @throws IllegalArgumentException if the date format is invalid or if the date is in the future
     */
    public void setBirthDate(String birthDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate parsedDate = LocalDate.parse(birthDateStr, formatter);
            if (parsedDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Birthdate cannot be in the future.");
            }
            this.birthDate = parsedDate; // Sätt födelsedatumet till det validerade datumet
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    /**
     * Enum representing the species of the pet.
     */
    public enum Species {
        DOG,
        CAT,
        BIRD,
        RODENT
    }
}
