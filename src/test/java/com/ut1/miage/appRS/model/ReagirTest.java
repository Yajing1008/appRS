package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour la classe {@link Reagir}.
 * Elle vérifie le bon fonctionnement des accesseurs, mutateurs et des valeurs par défaut.
 */
class ReagirTest {

    /**
     * Vérifie que les getters et setters fonctionnent correctement
     * pour les attributs {@code etudiant}, {@code post} et {@code statut}.
     */
    @Test
    void testGettersAndSetters() {
        Reagir reagir = new Reagir();

        Etudiant etudiant = new Etudiant();
        Post post = new Post();
        String statut = "like";

        reagir.setEtudiant(etudiant);
        reagir.setPost(post);
        reagir.setStatut(statut);

        assertSame(etudiant, reagir.getEtudiant());
        assertSame(post, reagir.getPost());
        assertEquals("like", reagir.getStatut());
    }

    /**
     * Vérifie que les valeurs par défaut des attributs de {@link Reagir}
     * sont null juste après instanciation.
     */
    @Test
    void testValeursParDefaut() {
        Reagir reagir = new Reagir();

        assertNull(reagir.getEtudiant());
        assertNull(reagir.getPost());
        assertNull(reagir.getStatut());
    }
}