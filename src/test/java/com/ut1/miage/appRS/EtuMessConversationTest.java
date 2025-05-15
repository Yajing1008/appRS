
package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.EtuMessConversation;
import com.ut1.miage.appRS.model.Etudiant;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EtuMessConversationTest {

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

    @Test
    void testEmbeddedIdInitialization() {
        EtuMessConversation emc = new EtuMessConversation();
        assertNotNull(emc.getId());
    }
}
