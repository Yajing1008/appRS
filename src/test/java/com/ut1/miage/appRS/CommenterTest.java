package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.Commenter;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommenterTest {

    @Test
    void testGettersAndSetters() {
        // Données de base
        Post post = new Post();
        post.setIdPost(100L);

        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(200L);

        String texte = "Ceci est un commentaire.";
        LocalDateTime date = LocalDateTime.now();

        // Création
        Commenter commenter = new Commenter();
        commenter.setPost(post);
        commenter.setEtudiant(etudiant);
        commenter.setCommentaire(texte);
        commenter.setDateHeureCommentaire(date);

        // Vérifications
        assertEquals(post, commenter.getPost());
        assertEquals(etudiant, commenter.getEtudiant());
        assertEquals(texte, commenter.getCommentaire());
        assertEquals(date, commenter.getDateHeureCommentaire());
    }

    @Test
    void testDateHeureIsNotNull() {
        Commenter commenter = new Commenter();
        commenter.setDateHeureCommentaire(LocalDateTime.now());

        assertNotNull(commenter.getDateHeureCommentaire());
    }

    @Test
    void testChampsVidesParDefaut() {
        Commenter commenter = new Commenter();

        assertNull(commenter.getPost());
        assertNull(commenter.getEtudiant());
        assertNull(commenter.getCommentaire());
        assertNull(commenter.getDateHeureCommentaire());
    }
}
