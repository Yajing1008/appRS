package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour la classe {@link Conversation}.
 */
class ConversationTest {

    /**
     * Vérifie le bon fonctionnement des getters et setters de base.
     */
    @Test
    void testGettersAndSetters() {
        Conversation conversation = new Conversation();

        Long id = 1L;
        LocalDateTime date = LocalDateTime.of(2024, 10, 1,1,1,1);

        conversation.setIdConversation(id);
        conversation.setDateCommenceConversation(date);

        assertEquals(id, conversation.getIdConversation());
        assertEquals(date, conversation.getDateCommenceConversation());
    }

    /**
     * Vérifie l'association entre une conversation et ses messages.
     */
    @Test
    void testAssociationAvecMessages() {
        EtuMessConversation m1 = new EtuMessConversation();
        EtuMessConversation m2 = new EtuMessConversation();

        List<EtuMessConversation> messages = new ArrayList<>();
        messages.add(m1);
        messages.add(m2);

        Conversation conversation = new Conversation();
        conversation.setMessagesDansConversation(messages);

        assertEquals(2, conversation.getMessagesDansConversation().size());
    }

    /**
     * Vérifie les valeurs par défaut lors de la création d'une nouvelle conversation.
     */
    @Test
    void testValeursParDefaut() {
        Conversation conversation = new Conversation();

        assertNull(conversation.getDateCommenceConversation());
        assertNotNull(conversation.getMessagesDansConversation());
    }
}