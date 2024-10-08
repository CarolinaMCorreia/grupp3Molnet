package org.campusmolndal.grupp3molnet.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.campusmolndal.grupp3molnet.models.Users;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @Schema(description = "The user id", example = "1")
    private Long id;

    @Schema(description = "The username", example = "johnDoe")
    private String username;

    @Schema(description = "The password", example = "safestPassword123!")
    private String password;

    @Schema(description = "Indicates if the user has admin privileges", example = "false")
    private boolean admin;

    public UserDto(Users user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.admin = user.isAdmin();
    }
}
