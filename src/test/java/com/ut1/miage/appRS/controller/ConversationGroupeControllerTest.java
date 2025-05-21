package com.ut1.miage.appRS.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.transaction.annotation.Transactional;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.EtuMessConversation;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Groupe;
import com.ut1.miage.appRS.repository.ConversationRepository;
import com.ut1.miage.appRS.repository.EtuMessConversationRepository;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import com.ut1.miage.appRS.repository.GroupeRepository;

/**
 * Tests d'intégration du contrôleur {@link ConversationGroupeController}.
 * Vérifie l'accès aux conversations de groupe et l'envoi de messages.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ConversationGroupeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private EtuMessConversationRepository messageRepository;

    private Etudiant etudiant;
    private Groupe groupe;
    private Conversation conversation;
    private MockHttpSession session;

    /**
     * Prépare les données de base : étudiant, groupe, conversation et session simulée.
     */
    @BeforeEach
    public void setUp() {
        etudiant = new Etudiant();
        etudiant.setNomEtudiant("ChatGroupe");
        etudiant.setPrenomEtudiant("User");
        etudiant.setEmailEtudiant("chatgroupe@example.com");
        etudiant.setDateNaissanceEtudiant(LocalDate.of(2000, 5, 5));
        etudiant = etudiantRepository.save(etudiant);

        conversation = new Conversation();
        conversation.setDateCommenceConversation(LocalDateTime.now());
        conversation = conversationRepository.save(conversation);

        groupe = new Groupe();
        groupe.setNomGroupe("Mon Groupe");
        groupe.setConversation(conversation);
        groupe = groupeRepository.save(groupe);

        session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);
    }

    /**
     * Vérifie que la page de conversation de groupe s'affiche correctement.
     */
    @Test
    public void testAfficherConversationGroupe() throws Exception {
        mockMvc.perform(get("/conversationGroupe/" + groupe.getIdGroupe()).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("conversationGroupe"))
                .andExpect(model().attributeExists("groupe"))
                .andExpect(model().attributeExists("messages"))
                .andExpect(model().attributeExists("conversation"));
    }

    /**
     * Vérifie qu’un étudiant peut envoyer un message dans un groupe.
     */
    @Test
    public void testEnvoyerMessageGroupe() throws Exception {
        mockMvc.perform(post("/sendGroupe")
                        .param("idConversation", conversation.getIdConversation().toString())
                        .param("idEtudiant", etudiant.getIdEtudiant().toString())
                        .param("message", "Bonjour le groupe !")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/conversationGroupe/" + groupe.getIdGroupe()));

        List<EtuMessConversation> messages = messageRepository.findByConversation(conversation);
        assert messages.stream().anyMatch(m -> m.getMessage().contains("Bonjour"));
    }

    /**
     * Vérifie que si le groupe est inexistant, on est redirigé.
     */
    @Test
    public void testAfficherConversationGroupeInvalide() throws Exception {
        mockMvc.perform(get("/conversationGroupe/999999").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/groupes"));
    }

    /**
     * Vérifie qu’un étudiant non connecté est redirigé vers la page de connexion.
     */
    @Test
    public void testAccesSansSession() throws Exception {
        mockMvc.perform(get("/conversationGroupe/" + groupe.getIdGroupe()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"));
    }

    /**
     * Vérifie qu’un message ne peut être envoyé que par l’auteur légitime.
     */
    @Test
    public void testEnvoyerMessageNonAutorise() throws Exception {
        MockHttpSession fakeSession = new MockHttpSession();
        fakeSession.setAttribute("etudiantConnecte", null);  // pas connecté

        mockMvc.perform(post("/sendGroupe")
                        .param("idConversation", conversation.getIdConversation().toString())
                        .param("idEtudiant", etudiant.getIdEtudiant().toString())
                        .param("message", "Tentative")
                        .session(fakeSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"));
    }

    /**
     * Vérifie qu’on est redirigé si la conversation est introuvable.
     */
    @Test
    public void testEnvoyerMessageConversationIntrouvable() throws Exception {
        mockMvc.perform(post("/sendGroupe")
                        .param("idConversation", "9999999")
                        .param("idEtudiant", etudiant.getIdEtudiant().toString())
                        .param("message", "Oops")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/groupes"));
    }
}
