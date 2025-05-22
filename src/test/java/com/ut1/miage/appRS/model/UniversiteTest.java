package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour {@link Universite}.
 * Elle couvre les constructeurs, les accesseurs (getters/setters),
 * et les méthodes {@code equals()} et {@code hashCode()}.
 */
class UniversiteTest {

    /**
     * Vérifie que le constructeur avec paramètres initialise
     * correctement les attributs.
     */
    @Test
    void testConstructeurAvecParametres() {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant());
        Universite u = new Universite(1L, "Université de Toulouse", etudiants);

        assertEquals(1L, u.getIdUniv());
        assertEquals("Université de Toulouse", u.getNomUniv());
        assertEquals(etudiants, u.getEtudiants());
    }

    /**
     * Vérifie que le constructeur par défaut initialise correctement
     * la liste d'étudiants et laisse les autres champs à {@code null}.
     */
    @Test
    void testConstructeurParDefaut() {
        Universite u = new Universite();
        assertNull(u.getIdUniv());
        assertNull(u.getNomUniv());
        assertNotNull(u.getEtudiants());
        assertTrue(u.getEtudiants().isEmpty());
    }

    /**
     * Vérifie que les accesseurs et mutateurs fonctionnent comme prévu.
     */
    @Test
    void testSettersEtGetters() {
        Universite u = new Universite();
        List<Etudiant> etudiants = new ArrayList<>();
        Etudiant e = new Etudiant();
        etudiants.add(e);

        u.setIdUniv(10L);
        u.setNomUniv("UT1");
        u.setEtudiants(etudiants);

        assertEquals(10L, u.getIdUniv());
        assertEquals("UT1", u.getNomUniv());
        assertEquals(etudiants, u.getEtudiants());
    }

    /**
     * Vérifie l'implémentation de {@code equals()} et {@code hashCode()}
     * avec deux objets identiques et un différent.
     */
    @Test
    void testEqualsEtHashCode() {
        List<Etudiant> etudiants = new ArrayList<>();
        Universite u1 = new Universite(1L, "UT1", etudiants);
        Universite u2 = new Universite(1L, "UT1", etudiants);
        Universite u3 = new Universite(2L, "Autre", new ArrayList<>());

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotEquals(u1, u3);
    }

    /**
     * Vérifie le comportement de {@code equals()} avec {@code null}
     * et un objet d'une autre classe.
     */
    @Test
    void testEqualsAvecNullEtAutreClasse() {
        Universite u = new Universite(1L, "UT1", new ArrayList<>());

        assertNotEquals(null, u);
        assertNotEquals("Université", u);
    }
}