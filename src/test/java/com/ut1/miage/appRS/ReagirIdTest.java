package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.ReagirId;

import static org.junit.jupiter.api.Assertions.*;

class ReagirIdTest {

    @Test
    void testConstructeurEtGetters() {
        ReagirId id = new ReagirId(1L, 2L);

        assertEquals(1L, id.getEtudiant());
        assertEquals(2L, id.getPost());
    }

    @Test
    void testSetters() {
        ReagirId id = new ReagirId();
        id.setEtudiant(3L);
        id.setPost(4L);

        assertEquals(3L, id.getEtudiant());
        assertEquals(4L, id.getPost());
    }

    @Test
    void testEqualsEtHashCode() {
        ReagirId id1 = new ReagirId(5L, 6L);
        ReagirId id2 = new ReagirId(5L, 6L);
        ReagirId id3 = new ReagirId(7L, 8L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }

    @Test
    void testEqualsAvecNullEtAutreClasse() {
        ReagirId id = new ReagirId(1L, 1L);

        assertNotEquals(null, id);
        assertNotEquals("une chaîne", id);
    }

    @Test
    void testValeursParDefaut() {
        ReagirId id = new ReagirId();

        assertNull(id.getEtudiant());
        assertNull(id.getPost());
    }
}
