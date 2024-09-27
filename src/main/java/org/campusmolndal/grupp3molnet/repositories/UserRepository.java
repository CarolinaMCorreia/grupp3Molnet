package org.campusmolndal.grupp3molnet.repositories;


import org.campusmolndal.grupp3molnet.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String userName);

    List<Users> findByUsernameIn(List<String> usernames);
}
