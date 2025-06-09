package org.campusmolndal.grupp3molnet.repositories;

import org.campusmolndal.grupp3molnet.models.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long>{
}
