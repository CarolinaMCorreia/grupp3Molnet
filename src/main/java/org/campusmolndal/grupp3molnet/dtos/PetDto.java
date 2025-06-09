package org.campusmolndal.grupp3molnet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.campusmolndal.grupp3molnet.models.Pet;
import org.campusmolndal.grupp3molnet.models.Pet.Species;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDto {
    private Species species;
    private String breed;
    private String name;
    private String birthdate;
    private Long userId;

    public PetDto(Pet pet) {
        this.species = pet.getSpecies();
        this.breed = pet.getBreed();
        this.name = pet.getName();
        this.birthdate = pet.getBirthDate().toString();
        this.userId = pet.getUser().getUserId();
    }


}
