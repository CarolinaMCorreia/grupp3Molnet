package org.campusmolndal.grupp3molnet.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class LoginUserDto {
    private String username;
    private String password;
}
