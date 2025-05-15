package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;
import com.ut1.miage.appRS.model.Reagir;

import static org.junit.jupiter.api.Assertions.*;

class ReagirTest {

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

    @Test
    void testValeursParDefaut() {
        Reagir reagir = new Reagir();

        assertNull(reagir.getEtudiant());
        assertNull(reagir.getPost());
        assertNull(reagir.getStatut());
    }
}
