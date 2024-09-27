package org.campusmolndal.grupp3molnet.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Tjänstklass för hantering av JSON Web Tokens (JWT), inklusive generering och verifiering av tokens.
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Hämtar användarnamnet från den angivna JWT:n.
     *
     * @param token JWT:n från vilken användarnamnet extraheras.
     * @return Användarnamnet som en sträng.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generisk metod för att extrahera en specifik uppgift (claim) från en JWT.
     *
     * @param token JWT:n från vilken uppgiften extraheras.
     * @param claimsResolver funktion som definierar hur uppgiften extraheras från JWT:n.
     * @param <T> Typen av uppgiften som extraheras.
     * @return Den extraherade uppgiften.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Genererar en JWT för en given användare.
     *
     * @param userDetails Användardetaljerna som JWT:n baseras på.
     * @return En JWT som en sträng.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genererar en JWT för en användare, inklusive extra uppgifter (claims).
     *
     * @param extraClaims Extra uppgifter som ska inkluderas i JWT:n.
     * @param userDetails Användardetaljerna som JWT:n baseras på.
     * @return En JWT som en sträng.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Hämtar den konfigurerade utgångstiden för JWT:er.
     *
     * @return Utgångstiden i millisekunder.
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Bygger en JWT med användardetaljer och extra uppgifter.
     *
     * @param extraClaims Extra uppgifter som ska inkluderas i JWT:n.
     * @param userDetails Användardetaljerna som JWT:n baseras på.
     * @param expiration Varaktigheten (i millisekunder) efter vilken JWT:n kommer att gå ut.
     * @return En JWT som en sträng.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Avgör om en JWT fortfarande är giltig för en viss användare.
     *
     * @param token JWT:n som ska kontrolleras.
     * @param userDetails Användardetaljerna som ska valideras mot token.
     * @return true om token är giltig och inte har gått ut; false annars.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Kontrollerar om en JWT har gått ut.
     *
     * @param token JWT:n som ska kontrolleras.
     * @return true om JWT:n har gått ut; false annars.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Hämtar utgångsdatumet från en JWT.
     *
     * @param token JWT:n från vilken utgångsdatumet extraheras.
     * @return Utgångsdatumet.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extraherar alla uppgifter (claims) från en JWT.
     *
     * @param token JWT:n som ska parsas.
     * @return Uppgifterna som finns i JWT:n.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Hämtar signeringsnyckeln för JWT-hantering.
     *
     * @return Signeringsnyckeln baserad på den konfigurerade hemliga nyckeln.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

