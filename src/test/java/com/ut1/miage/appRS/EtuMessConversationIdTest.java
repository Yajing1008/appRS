package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.EtuMessConversationId;

import static org.junit.jupiter.api.Assertions.*;

class EtuMessConversationIdTest {

    @Test
    void testConstructeursEtGettersSetters() {
        EtuMessConversationId id = new EtuMessConversationId();
        id.setIdEtudiant(1L);
        id.setIdConversation(2L);

        assertEquals(1L, id.getIdEtudiant());
        assertEquals(2L, id.getIdConversation());

        EtuMessConversationId id2 = new EtuMessConversationId(1L, 2L);
        assertEquals(1L, id2.getIdEtudiant());
        assertEquals(2L, id2.getIdConversation());
    }

    @Test
    void testEqualsEtHashCode() {
        EtuMessConversationId id1 = new EtuMessConversationId(1L, 2L);
        EtuMessConversationId id2 = new EtuMessConversationId(1L, 2L);
        EtuMessConversationId id3 = new EtuMessConversationId(3L, 4L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }

    @Test
    void testEqualsAvecNullEtAutreClasse() {
        EtuMessConversationId id = new EtuMessConversationId(1L, 2L);
        assertNotEquals(null, id);
        assertNotEquals("une chaine", id);
    }
}
