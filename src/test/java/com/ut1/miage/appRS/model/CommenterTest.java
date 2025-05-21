package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe {@link Commenter}.
 */
public class CommenterTest {

    /**
     * Vérifie que les getters et setters de tous les attributs fonctionnent correctement.
     */
    @Test
    void testGettersAndSetters() {
        Post post = new Post();
        post.setIdPost(100L);

        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(200L);

        String texte = "Ceci est un commentaire.";
        LocalDateTime date = LocalDateTime.now();

        Commenter commenter = new Commenter();
        commenter.setPost(post);
        commenter.setEtudiant(etudiant);
        commenter.setCommentaire(texte);
        commenter.setDateHeureCommentaire(date);

        assertEquals(post, commenter.getPost());
        assertEquals(etudiant, commenter.getEtudiant());
        assertEquals(texte, commenter.getCommentaire());
        assertEquals(date, commenter.getDateHeureCommentaire());
    }

    /**
     * Vérifie que la date/heure de commentaire n'est pas null après affectation.
     */
    @Test
    void testDateHeureIsNotNull() {
        Commenter commenter = new Commenter();
        commenter.setDateHeureCommentaire(LocalDateTime.now());

        assertNotNull(commenter.getDateHeureCommentaire());
    }

    /**
     * Vérifie que les champs sont bien initialisés à null par défaut.
     */
    @Test
    void testChampsVidesParDefaut() {
        Commenter commenter = new Commenter();

        assertNull(commenter.getPost());
        assertNull(commenter.getEtudiant());
        assertNull(commenter.getCommentaire());
        assertNull(commenter.getDateHeureCommentaire());
    }
}