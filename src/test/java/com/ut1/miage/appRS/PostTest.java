package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.Commenter;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;
import com.ut1.miage.appRS.model.Reagir;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void testGettersAndSetters() {
        Post post = new Post();

        post.setIdPost(1L);
        post.setContenuPost("Contenu de test");
        post.setEstPublicPost(true);

        Etudiant etudiant = new Etudiant();
        post.setEtudiant(etudiant);

        List<Etudiant> republications = new ArrayList<>();
        republications.add(new Etudiant());
        post.setRepublications(republications);

        List<Commenter> commentaires = new ArrayList<>();
        commentaires.add(new Commenter());
        post.setCommentaires(commentaires);

        List<Reagir> reactions = new ArrayList<>();
        reactions.add(new Reagir());
        post.setReactions(reactions);

        assertEquals(1L, post.getIdPost());
        assertEquals("Contenu de test", post.getContenuPost());
        assertTrue(post.isEstPublicPost());
        assertSame(etudiant, post.getEtudiant());
        assertEquals(1, post.getRepublications().size());
        assertEquals(1, post.getCommentaires().size());
        assertEquals(1, post.getReactions().size());
    }

    @Test
    void testValeursParDefaut() {
        Post post = new Post();

        assertNull(post.getIdPost());
        assertNull(post.getContenuPost());
        assertFalse(post.isEstPublicPost());
        assertNull(post.getEtudiant());
        assertNotNull(post.getRepublications());
        assertTrue(post.getRepublications().isEmpty());
        assertNotNull(post.getCommentaires());
        assertTrue(post.getCommentaires().isEmpty());
        assertNotNull(post.getReactions());
        assertTrue(post.getReactions().isEmpty());
    }
}
