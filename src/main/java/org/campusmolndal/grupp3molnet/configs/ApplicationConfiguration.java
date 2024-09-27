package org.campusmolndal.grupp3molnet.configs;

import org.campusmolndal.grupp3molnet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {

    private final UserRepository userRepository;

    /**
     * Konstruerar en ApplicationConfiguration med beroendet UserRepository.
     * @param userRepository En UserRepository för att hämta användardata.
     */
    @Autowired
    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Implementerar en UserDetailsService för att ladda användaren från databasen.
     *
     * @return En UserDetailsService för att hämta användardata.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    /**
     * Implementerar en BCryptPasswordEncoder för att hantera lösenordskryptering.
     *
     * @return En BCryptPasswordEncoder.
     * Hanterar bara inloggningsdelen, jwt används under resten av kommunikationen.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Konfigurerar och tillhandahåller en AuthenticationManager.
     *
     * @param config Grundkonfiguration för autentisering.
     * @return Om det lyckas returneras ett autentiseringsobjekt som innehåller information om den autentiserade användaren.
     * @throws Exception Om det uppstår ett fel vid konfiguration.
     * Ansvarar för att validera användarens uppgifter.
     * Denna gör jobbet den nedanför..
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        try {
            return config.getAuthenticationManager();
        } catch (Exception e) {
            throw new UsernameNotFoundException("AuthenticationManager is null");
        }
    }

    /**
     * Skapar en AuthenticationProvider som använder DAO för autentisering.
     *
     * @return En instans av AuthenticationProvider.
     * Hämtar information från databasen för att jämföra med den som försöker logga in.
     * ...Hämtar bara data.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
