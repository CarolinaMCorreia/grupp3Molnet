package org.campusmolndal.grupp3molnet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private Optional<String> username = Optional.empty();
    private Optional<String> password = Optional.empty();
    private boolean isAdmin;
}
