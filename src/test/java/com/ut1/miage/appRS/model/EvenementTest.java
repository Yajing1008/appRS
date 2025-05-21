package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour la classe {@link Evenement}.
 */
class EvenementTest {

    /**
     * Teste les accesseurs (getters/setters) de la classe {@link Evenement}.
     */
    @Test
    void testGettersAndSetters() {
        Etudiant createur = new Etudiant();
        LocalDateTime date = LocalDateTime.of(2025, 5, 15, 14, 30);
        List<Etudiant> membres = new ArrayList<>();
        membres.add(new Etudiant());

        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1L);
        evenement.setDateHeureDebutEvenement(date);
        evenement.setNomEvenement("Réunion projet");
        evenement.setCreateur(createur);
        evenement.setMembreGroupe(membres);

        assertEquals(1L, evenement.getIdEvenement());
        assertEquals(date, evenement.getDateHeureDebutEvenement());
        assertEquals("Réunion projet", evenement.getNomEvenement());
        assertSame(createur, evenement.getCreateur());
        assertEquals(1, evenement.getMembreGroupe().size());
    }

    /**
     * Vérifie que le constructeur avec arguments initialise correctement tous les champs.
     */
    @Test
    void testConstructeurAvecArguments() {
        Etudiant createur = new Etudiant();
        List<Etudiant> membres = new ArrayList<>();
        membres.add(new Etudiant());

        LocalDateTime debut = LocalDateTime.of(2025, 12, 1, 10, 0);
        LocalDateTime fin = LocalDateTime.of(2025, 12, 1, 12, 0);
        String nom = "Conférence";
        String imageUrl = "image.jpg";
        String lieu = "Salle 101";
        String description = "Une conférence importante.";

        Evenement evenement = new Evenement(2L, debut, fin, nom, imageUrl, lieu, description, createur, membres);

        assertEquals(2L, evenement.getIdEvenement());
        assertEquals(debut, evenement.getDateHeureDebutEvenement());
        assertEquals(fin, evenement.getDateHeureFinEvenement());
        assertEquals(nom, evenement.getNomEvenement());
        assertEquals(imageUrl, evenement.getImageUrlEvenement());
        assertEquals(lieu, evenement.getLieuEvenement());
        assertEquals(description, evenement.getDescriptionEvenement());
        assertSame(createur, evenement.getCreateur());
        assertSame(membres, evenement.getMembreGroupe());
    }



    /**
     * Vérifie les valeurs par défaut d'une instance de {@link Evenement}.
     */
    @Test
    void testDefaultValues() {
        Evenement evenement = new Evenement();

        assertNull(evenement.getIdEvenement());
        assertNull(evenement.getDateHeureDebutEvenement());
        assertNull(evenement.getNomEvenement());
        assertNotNull(evenement.getMembreGroupe());
        assertTrue(evenement.getMembreGroupe().isEmpty());
    }

    /**
     * Vérifie la méthode equals et hashCode avec deux objets identiques.
     */
    @Test
    void testEqualsEtHashCode() {
        LocalDateTime date = LocalDateTime.now();
        Etudiant createur = new Etudiant();
        List<Etudiant> membres = new ArrayList<>();

        Evenement e1 = new Evenement();
        e1.setIdEvenement(1L);
        e1.setDateHeureDebutEvenement(date);
        e1.setNomEvenement("Nom");
        e1.setCreateur(createur);
        e1.setMembreGroupe(membres);

        Evenement e2 = new Evenement();
        e2.setIdEvenement(1L);
        e2.setDateHeureDebutEvenement(date);
        e2.setNomEvenement("Nom");
        e2.setCreateur(createur);
        e2.setMembreGroupe(membres);

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }
}