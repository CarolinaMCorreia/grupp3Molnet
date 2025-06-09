package org.campusmolndal.grupp3molnet.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to PetManager API by Grupp 3. " +
                "For more information visit: " +
                "http://husdjursregister1-env.eba-gzkbcjgw.eu-north-1.elasticbeanstalk.com/Swagger-UI.html");
    }
}