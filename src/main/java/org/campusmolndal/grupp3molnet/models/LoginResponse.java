package org.campusmolndal.grupp3molnet.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klass som representerar svaret som returneras vid en lyckad inloggning.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    /**
     * JWT-token som utfärdas vid lyckad autentisering.
     */
    private String token;
    /**
     * Utgångstiden för JWT-token.
     */
    private long expiresIn;
}
