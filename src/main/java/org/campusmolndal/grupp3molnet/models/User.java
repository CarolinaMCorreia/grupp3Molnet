package org.campusmolndal.grupp3molnet.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.List;

/**
 * Represents a user in the application. Implements {@link UserDetails} for integration with Spring Security.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User implements UserDetails {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Schema(description = "The user's ID", example = "1")
    private Long userId;

    /**
     * The username of the user. Must be unique.
     */
    @Column(name = "username", nullable = false, unique = true)
    @Schema(description = "The user's username", example = "johnDoe")
    @Size(max = 50)
    private String username;

    /**
     * The password of the user. Must be at least 6 characters long.
     */
    @Column(name = "password", nullable = false)
    @JsonIgnore
    @Schema(description = "The user's password", example = "safestPassword123!")
    @Size(min = 6, max = 100)
    private String password;

    /**
     * Indicates whether the user has admin privileges.
     */
    @Column(name = "admin")
    @Schema(description = "Indicates if the user has admin privileges", example = "false")
    private boolean admin;

    /**
     * A list of pets owned by the user. The relationship is managed by the {@link Pets} entity.
     * If a User is deleted, all their associated pets will also be deleted from the Pets table.
     * If a Pet is removed from a User's list of pets, it will be deleted from the database.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of pets owned by the user")
    private List<Pet> pets;

    /**
     * Returns the authorities granted to the user.
     * If the user is an admin, they receive the "ROLE_ADMIN" authority. Otherwise, they receive "ROLE_USER".
     *
     * @return a collection of authorities granted to the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return admin ? List.of(() -> "ROLE_ADMIN") : List.of(() -> "ROLE_USER");
    }

    /**
     * Checks if the user's account is expired. This is currently always true, meaning the account is not expired.
     *
     * @return true, if the account is not expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Checks if the user's account is locked. This is currently always true, meaning the account is not locked.
     *
     * @return true, if the account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Checks if the user's credentials are expired. This is currently always true, meaning the credentials are valid.
     *
     * @return true, if the credentials are not expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Checks if the user is enabled. This is currently always true, meaning the user is enabled.
     *
     * @return true, if the user is enabled
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
