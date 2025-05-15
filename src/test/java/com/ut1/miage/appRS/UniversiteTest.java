package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Universite;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UniversiteTest {

    @Test
    void testConstructeurAvecParametres() {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant());
        Universite u = new Universite(1L, "Université de Toulouse", etudiants);

        assertEquals(1L, u.getIdUniv());
        assertEquals("Université de Toulouse", u.getNomUniv());
        assertEquals(etudiants, u.getEtudiant());
    }

    @Test
    void testConstructeurParDefaut() {
        Universite u = new Universite();
        assertNull(u.getIdUniv());
        assertNull(u.getNomUniv());
        assertNotNull(u.getEtudiant()); // initialisé dans la déclaration
        assertTrue(u.getEtudiant().isEmpty());
    }

    @Test
    void testSettersEtGetters() {
        Universite u = new Universite();
        List<Etudiant> etudiants = new ArrayList<>();
        Etudiant e = new Etudiant();
        etudiants.add(e);

        u.setIdUniv(10L);
        u.setNomUniv("UT1");
        u.setEtudiant(etudiants);

        assertEquals(10L, u.getIdUniv());
        assertEquals("UT1", u.getNomUniv());
        assertEquals(etudiants, u.getEtudiant());
    }

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

    @Test
    void testEqualsAvecNullEtAutreClasse() {
        Universite u = new Universite(1L, "UT1", new ArrayList<>());

        assertNotEquals(null, u);
        assertNotEquals("Université", u);
    }
}
