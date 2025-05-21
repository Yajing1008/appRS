package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour {@link Participer}.
 */
class ParticiperTest {

    /**
     * Vérifie que les getters et setters fonctionnent correctement.
     */
    @Test
    void testGettersAndSetters() {
        Etudiant etudiant = new Etudiant();
        Groupe groupe = new Groupe();
        ParticiperId id = new ParticiperId(1L, 2L);
        String role = "Administrateur";

        Participer participer = new Participer();
        participer.setId(id);
        participer.setEtudiant(etudiant);
        participer.setGroupe(groupe);
        participer.setRole(role);

        assertEquals(id, participer.getId());
        assertSame(etudiant, participer.getEtudiant());
        assertSame(groupe, participer.getGroupe());
        assertEquals("Administrateur", participer.getRole());
    }

    /**
     * Vérifie les valeurs par défaut après instanciation avec le constructeur vide.
     */
    @Test
    void testDefaultConstructorValues() {
        Participer participer = new Participer();

        assertNotNull(participer.getId()); // id est initialisé automatiquement
        assertNull(participer.getEtudiant());
        assertNull(participer.getGroupe());
        assertNull(participer.getRole());
    }
}