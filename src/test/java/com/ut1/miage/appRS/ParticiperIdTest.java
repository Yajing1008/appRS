package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.ParticiperId;

import static org.junit.jupiter.api.Assertions.*;

class ParticiperIdTest {

    @Test
    void testConstructeurParDefaut() {
        ParticiperId id = new ParticiperId();
        assertNull(id.getIdEtudiant());
        assertNull(id.getIdGroupe());
    }

    @Test
    void testConstructeurAvecParametres() {
        ParticiperId id = new ParticiperId(1L, 2L);
        assertEquals(1L, id.getIdEtudiant());
        assertEquals(2L, id.getIdGroupe());
    }

    @Test
    void testSettersEtGetters() {
        ParticiperId id = new ParticiperId();
        id.setIdEtudiant(10L);
        id.setIdGroupe(20L);

        assertEquals(10L, id.getIdEtudiant());
        assertEquals(20L, id.getIdGroupe());
    }

    @Test
    void testEqualsEtHashCode() {
        ParticiperId id1 = new ParticiperId(1L, 2L);
        ParticiperId id2 = new ParticiperId(1L, 2L);
        ParticiperId id3 = new ParticiperId(3L, 4L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());

        assertNotEquals(id1, id3);
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testEqualsAvecNullEtAutresTypes() {
        ParticiperId id = new ParticiperId(1L, 2L);

        assertNotEquals(id, null);
        assertNotEquals(id, "autre objet");
    }
}
