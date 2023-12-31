package com.safetynet.safetynetalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Classe principale pour initier l'exécution de l'application Spring Boot.
 * Configure et démarre l'application, servant de point d'entrée principal.
 */

@SpringBootApplication(scanBasePackages = "com.safetynet.safetynetalerts")
@ComponentScan(basePackages = "com.safetynet.safetynetalerts")
public class SafetyNetAlertsApplication {

	public static void main(String[] args) {

		SpringApplication.run(SafetyNetAlertsApplication.class, args);
	}
}
