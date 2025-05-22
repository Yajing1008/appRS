package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.EtuMessConversation;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.ConversationRepository;
import com.ut1.miage.appRS.repository.EtuMessConversationRepository;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour le contrôleur {@link ConversationController}.
 * Ces tests vérifient la création de conversation, l’envoi de messages
 * et le bon rendu des vues.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ConversationControllerTest {
    /** Objet permettant de simuler des requêtes HTTP dans les tests. */
    @Autowired
    private MockMvc mockMvc;

    /** Référentiel permettant l'accès aux données des étudiants pour les tests. */
    @Autowired
    private EtudiantRepository etudiantRepository;

    /** Référentiel pour accéder aux conversations de groupe ou privées dans les tests. */
    @Autowired
    private ConversationRepository conversationRepository;

    /** Référentiel pour accéder aux messages envoyés par les étudiants dans les tests. */
    @Autowired
    private EtuMessConversationRepository messageRepository;

    /** Premier étudiant utilisé dans les scénarios de test. */
    private Etudiant e1;

    /** Deuxième étudiant utilisé dans les scénarios de test. */
    private Etudiant e2;

    /** Session simulée représentant un étudiant connecté. */
    private MockHttpSession session;

    /**
     * Initialise deux étudiants et une session simulée pour les tests.
     */
    @BeforeEach
    public void setUp() {
        e1 = new Etudiant();
        e1.setNomEtudiant("Alice");
        e1.setPrenomEtudiant("A");
        e1.setEmailEtudiant("alice@example.com");
        e1.setDateNaissanceEtudiant(LocalDate.of(2000, 1, 1));
        etudiantRepository.save(e1);

        e2 = new Etudiant();
        e2.setNomEtudiant("Bob");
        e2.setPrenomEtudiant("B");
        e2.setEmailEtudiant("bob@example.com");
        e2.setDateNaissanceEtudiant(LocalDate.of(2000, 2, 2));
        etudiantRepository.save(e2);

        session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", e1);
    }

    /**
     * Vérifie que l'ouverture d'une conversation nouvelle crée bien une conversation et deux messages initiaux.
     */
    @Test
    public void testOpenConversation_createsNew() throws Exception {
        mockMvc.perform(get("/conversation/" + e2.getIdEtudiant()).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("conversation"))
                .andExpect(model().attributeExists("conversation"))
                .andExpect(model().attributeExists("messages"))
                .andExpect(model().attributeExists("ami"));
    }

    /**
     * Vérifie qu’un message peut être envoyé via POST /send.
     */
    @Test
    public void testSendMessage() throws Exception {
        // Créer une conversation manuellement
        Conversation conversation = new Conversation();
        conversation.setDateCommenceConversation(java.time.LocalDateTime.now());
        conversationRepository.save(conversation);

        // 插入初始消息
        EtuMessConversation m1 = new EtuMessConversation();
        m1.setConversation(conversation);
        m1.setEtudiant(e1);
        m1.setMessage("Hello");
        m1.setDateHeureMessage(java.time.LocalDateTime.now());
        messageRepository.save(m1);

        EtuMessConversation m2 = new EtuMessConversation();
        m2.setConversation(conversation);
        m2.setEtudiant(e2);
        m2.setMessage("Salut");
        m2.setDateHeureMessage(java.time.LocalDateTime.now());
        messageRepository.save(m2);

        mockMvc.perform(post("/send")
                        .session(session)
                        .param("idConversation", conversation.getIdConversation().toString())
                        .param("idEtudiant", e1.getIdEtudiant().toString())
                        .param("message", "Test message"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/conversation/*"));

        List<EtuMessConversation> messages = messageRepository.findByConversation(conversation);
        assert messages.stream().anyMatch(m -> m.getMessage().equals("Test message"));
    }

    /**
     * Vérifie que l’accès sans session redirige vers /connexion.
     */
    @Test
    public void testOpenConversation_noSession_redirects() throws Exception {
        mockMvc.perform(get("/conversation/" + e2.getIdEtudiant()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"));
    }

    /**
     * Vérifie qu’un utilisateur non autorisé ne peut pas envoyer de message.
     */
    @Test
    public void testSendMessage_invalidUser_redirects() throws Exception {
        // Faux utilisateur connecté
        Etudiant imposteur = new Etudiant();
        imposteur.setNomEtudiant("Fake");
        imposteur.setPrenomEtudiant("User");
        imposteur.setEmailEtudiant("imposteur@example.com");
        imposteur.setDateNaissanceEtudiant(LocalDate.of(1990, 1, 1));
        etudiantRepository.save(imposteur);

        MockHttpSession fakeSession = new MockHttpSession();
        fakeSession.setAttribute("etudiantConnecte", imposteur);

        Conversation conversation = new Conversation();
        conversation.setDateCommenceConversation(java.time.LocalDateTime.now());
        conversationRepository.save(conversation);

        mockMvc.perform(post("/send")
                        .session(fakeSession)
                        .param("idConversation", conversation.getIdConversation().toString())
                        .param("idEtudiant", e1.getIdEtudiant().toString())
                        .param("message", "Hacked"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"));
    }
}
