package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.CentreInteret;
import com.ut1.miage.appRS.model.Etudiant;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CentreInteretTest {
    
    @Test
    void testConstructeurEtGettersSetters() {
        CentreInteret centre = new CentreInteret();
        centre.setIdCentreInteret(1L);
        centre.setNomCentreInteret("Musique");

        assertEquals(1L, centre.getIdCentreInteret());
        assertEquals("Musique", centre.getNomCentreInteret());
    }

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
        centre.setEtudiant(etudiants);

        assertEquals(2, centre.getEtudiant().size());
        assertEquals(10L, centre.getEtudiant().get(0).getIdEtudiant());
    }

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

    @Test
    void testNonEquals() {
        CentreInteret c1 = new CentreInteret(1L, "Lecture", new ArrayList<>());
        CentreInteret c2 = new CentreInteret(2L, "Sport", new ArrayList<>());

        assertNotEquals(c1, c2);
    }

    @Test
    void testHashCodeConsistanceAvecId() {
        CentreInteret c1 = new CentreInteret();
        c1.setIdCentreInteret(42L);

        CentreInteret c2 = new CentreInteret();
        c2.setIdCentreInteret(42L);

        assertEquals(c1.hashCode(), c2.hashCode());
    }

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
