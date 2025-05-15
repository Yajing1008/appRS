package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.Etudiant;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EtudiantTest {

    @Test
    void testGettersAndSetters() {
        Etudiant etudiant = new Etudiant();

        etudiant.setIdEtudiant(1L);
        etudiant.setNomEtudiant("Durand");
        etudiant.setPrenomEtudiant("Alice");
        etudiant.setDateNaissanceEtudiant(LocalDate.of(2000, 5, 10));
        etudiant.setSexeEtudiant("F");
        etudiant.setEmailEtudiant("alice.durand@example.com");
        etudiant.setMotDePass("secret123");

        assertEquals(1L, etudiant.getIdEtudiant());
        assertEquals("Durand", etudiant.getNomEtudiant());
        assertEquals("Alice", etudiant.getPrenomEtudiant());
        assertEquals(LocalDate.of(2000, 5, 10), etudiant.getDateNaissanceEtudiant());
        assertEquals("F", etudiant.getSexeEtudiant());
        assertEquals("alice.durand@example.com", etudiant.getEmailEtudiant());
        assertEquals("secret123", etudiant.getMotDePass());
    }

    @Test
    void testInitialListsNotNull() {
        Etudiant etudiant = new Etudiant();

        assertNotNull(etudiant.getAmis());
        assertNotNull(etudiant.getPostsPublies());
        assertNotNull(etudiant.getPostsRepublies());
        assertNotNull(etudiant.getGroupesCrees());
        assertNotNull(etudiant.getParticipations());
        assertNotNull(etudiant.getCommentaires());
        assertNotNull(etudiant.getReactions());
        assertNotNull(etudiant.getUniversites());
        assertNotNull(etudiant.getCentresInteret());
        assertNotNull(etudiant.getEvenementsParticiper());
        assertNotNull(etudiant.getEvenementsCreer());
    }

    @Test
    void testAddAmi() {
        Etudiant e1 = new Etudiant();
        Etudiant e2 = new Etudiant();

        e1.getAmis().add(e2);

        assertEquals(1, e1.getAmis().size());
        assertSame(e2, e1.getAmis().get(0));
    }
}
