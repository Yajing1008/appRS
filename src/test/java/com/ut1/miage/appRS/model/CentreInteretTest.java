package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour l'entité {@link CentreInteret}.
 * Vérifie les getters, setters, associations, égalité et cohérence des hashCodes.
 */
class CentreInteretTest {

    /**
     * Vérifie que les getters et setters fonctionnent correctement.
     */
    @Test
    void testConstructeurEtGettersSetters() {
        CentreInteret centre = new CentreInteret();
        centre.setIdCentreInteret(1L);
        centre.setNomCentreInteret("Musique");

        assertEquals(1L, centre.getIdCentreInteret());
        assertEquals("Musique", centre.getNomCentreInteret());
    }

    /**
     * Vérifie que l'association avec une liste d'étudiants fonctionne correctement.
     */
    @Test
    void testAssociationEtudiants() {
        Etudiant e1 = new Etudiant();
        e1.setIdEtudiant(10L);

        Etudiant e2 = new Etudiant();
        e2.setIdEtudiant(20L);

        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(e1);
        etudiants.add(e2);

        CentreInteret centre = new CentreInteret();
        centre.setEtudiants(etudiants);

        assertEquals(2, centre.getEtudiants().size());
        assertEquals(10L, centre.getEtudiants().get(0).getIdEtudiant());
    }

    /**
     * Vérifie que deux objets {@link CentreInteret} avec les mêmes attributs sont considérés comme égaux.
     */
    @Test
    void testEqualsEtHashCode() {
        CentreInteret c1 = new CentreInteret();
        c1.setIdCentreInteret(1L);
        c1.setNomCentreInteret("Lecture");

        CentreInteret c2 = new CentreInteret();
        c2.setIdCentreInteret(1L);
        c2.setNomCentreInteret("Lecture");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    /**
     * Vérifie que deux objets {@link CentreInteret} différents ne sont pas égaux.
     */
    @Test
    void testNonEquals() {
        CentreInteret c1 = new CentreInteret(1L, "Lecture", new ArrayList<>());
        CentreInteret c2 = new CentreInteret(2L, "Sport", new ArrayList<>());

        assertNotEquals(c1, c2);
    }

    /**
     * Vérifie que deux objets ayant le même id ont le même hashCode.
     */
    @Test
    void testHashCodeConsistanceAvecId() {
        CentreInteret c1 = new CentreInteret();
        c1.setIdCentreInteret(42L);

        CentreInteret c2 = new CentreInteret();
        c2.setIdCentreInteret(42L);

        assertEquals(c1.hashCode(), c2.hashCode());
    }

    /**
     * Vérifie que le hashCode change si le nom du centre d'intérêt change.
     */
    @Test
    void testHashCodeChangeAvecNom() {
        CentreInteret c1 = new CentreInteret();
        c1.setIdCentreInteret(1L);
        c1.setNomCentreInteret("Sport");

        CentreInteret c2 = new CentreInteret();
        c2.setIdCentreInteret(1L);
        c2.setNomCentreInteret("Musique");

        assertNotEquals(c1.hashCode(), c2.hashCode());
    }
}