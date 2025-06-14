package org.campusmolndal.grupp3molnet.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.campusmolndal.grupp3molnet.models.Users;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @Schema(description = "The user id", example = "1")
    private Long id;

    @Schema(description = "The username", example = "johnDoe")
    private String username;

    public UserDto(Users user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
    }
}
