package com.ut1.miage.appRS;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classe de test pour vérifier le bon démarrage de l'application Spring Boot.
 */
@SpringBootTest
class AppRsApplicationTests {

    /**
     * Teste que le contexte Spring se charge sans erreur.
     * Ce test permet de s'assurer que l'application est bien configurée (fichiers, beans, dépendances).
     */
    @Test
    void contextLoads() {
        // Ce test échoue si le contexte ne peut pas être chargé.
    }

}