package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour {@link EtuMessConversation}.
 */
class EtuMessConversationTest {

    /**
     * Vérifie le bon fonctionnement des accesseurs (getters/setters) de la classe EtuMessConversation.
     */
    @Test
    void testGettersAndSetters() {
        Etudiant etudiant = new Etudiant();
        Conversation conversation = new Conversation();
        LocalDateTime now = LocalDateTime.now();

        EtuMessConversation emc = new EtuMessConversation();
        emc.setEtudiant(etudiant);
        emc.setConversation(conversation);
        emc.setMessage("Hello world");
        emc.setDateHeureMessage(now);

        assertSame(etudiant, emc.getEtudiant());
        assertSame(conversation, emc.getConversation());
        assertEquals("Hello world", emc.getMessage());
        assertEquals(now, emc.getDateHeureMessage());
    }

    /**
     * Vérifie que l'identifiant composite (EmbeddedId) est correctement initialisé à la création.
     */
    @Test
    void testEmbeddedIdInitialization() {
        EtuMessConversation emc = new EtuMessConversation();
        assertNotNull(emc.getIdEtuMessConversation());
    }
}