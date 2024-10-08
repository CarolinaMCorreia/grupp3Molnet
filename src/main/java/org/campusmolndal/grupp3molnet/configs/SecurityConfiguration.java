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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;

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
            "/swagger-ui/**"
    };

    /**
     * Returns a SecurityFilterChain
     * @return SecurityFilterChain
     * @throws Exception if an error occurs
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
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
}
