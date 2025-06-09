package org.campusmolndal.grupp3molnet.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDto {
    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String confirmNewPassword;
}
