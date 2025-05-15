package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.CommenterId;

import static org.junit.jupiter.api.Assertions.*;

class CommenterIdTest {

    @Test
    void testConstructeurEtGetters() {
        CommenterId id = new CommenterId(1L, 2L);

        assertEquals(1L, id.getEtudiant());
        assertEquals(2L, id.getPost());
    }

    @Test
    void testEqualsTrue() {
        CommenterId id1 = new CommenterId(1L, 2L);
        CommenterId id2 = new CommenterId(1L, 2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testEqualsFalse() {
        CommenterId id1 = new CommenterId(1L, 2L);
        CommenterId id2 = new CommenterId(2L, 3L);

        assertNotEquals(id1, id2);
    }

    @Test
    void testEqualsAvecNull() {
        CommenterId id1 = new CommenterId(1L, null);
        CommenterId id2 = new CommenterId(1L, null);

        assertEquals(id1, id2);
    }

    @Test
    void testEqualsAutreClasse() {
        CommenterId id = new CommenterId(1L, 2L);
        String autre = "pas une instance";

        assertNotEquals(id, autre);
    }

    @Test
    void testEqualsAvecLuiMeme() {
        CommenterId id = new CommenterId(5L, 10L);
        assertEquals(id, id);
    }
}

