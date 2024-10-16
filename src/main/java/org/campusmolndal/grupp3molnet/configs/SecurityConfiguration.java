package org.campusmolndal.grupp3molnet.configs;

import jakarta.servlet.http.HttpServletResponse;
import org.campusmolndal.grupp3molnet.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Konstruktor för att injicera beroenden för JwtService och UserDetailsService.
     *
     * @param jwtService JWT-tjänsten
     * @param userDetailsService användardetaljstjänsten
     */
    @Autowired
    public SecurityConfiguration(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Array som innehåller URI-sökvägar som definierar endpoints som inte kräver autentisering.
     */
    private static final String[] AUTH_WHITELIST = {
            "/auth/signup",
            "/auth/login",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui",
            "/webjars/**",
            "v2/api-docs/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/"
    };

    /**
     * Returns a SecurityFilterChain
     * @return SecurityFilterChain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Konfigurera CORS för React frontend
                .cors(withDefaults())
                // Konfigurera autentisering och auktorisering
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(AUTH_WHITELIST).permitAll() // Tillåt alla för de definierade whitelist-URL:erna
                        .requestMatchers("/auth/**").permitAll() // Tillåt alla att nå "/api/**"
                        .anyRequest().authenticated() // Kräver autentisering för alla andra begäranden
                )
                // Konfigurera CSRF-skydd
                .csrf(AbstractHttpConfigurer::disable) // Inaktivera CSRF-skydd för API:er om det är lämpligt
                // Konfigurera sessionhantering
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Använd stateless session
                )
                // Lägg till JwtAuthenticationFilter innan UsernamePasswordAuthenticationFilter
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                // Konfigurera HTTP Basic autentisering
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint((request, response, authException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                        )
                );
        return http.build();
    }

    /**
     * Konfigurera CORS-filtrering för att tillåta anrop från port 3000.
     *
     * @return UrlBasedCorsConfigurationSource
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}