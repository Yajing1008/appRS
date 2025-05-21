package com.ut1.miage.appRS.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.EtuMessConversation;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.ConversationRepository;
import com.ut1.miage.appRS.repository.EtuMessConversationRepository;
import com.ut1.miage.appRS.repository.EtudiantRepository;

import jakarta.servlet.http.HttpSession;

/**
 * Contrôleur permettant de gérer les conversations privées entre deux étudiants,
 * incluant l'initialisation de conversation et l'envoi de messages.
 */
@Controller
public class ConversationController {

    @Autowired
    EtudiantRepository etudiantRepository;

    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    EtuMessConversationRepository messageRepository;

    /**
     * Ouvre une conversation existante entre l'étudiant connecté et un autre étudiant,
     * ou en crée une nouvelle si aucune conversation commune n’existe encore.
     *
     * @param idEtudiant identifiant de l’autre étudiant (destinataire)
     * @param model      modèle pour la vue
     * @param session    session HTTP pour récupérer l'étudiant connecté
     * @return nom de la vue "conversation"
     */
    @GetMapping("/conversation/{id}")
    public String openConversation(@PathVariable("id") Long idEtudiant,
                                   Model model,
                                   HttpSession session) {

        Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
        if (utilisateurConnecte == null) {
            return "redirect:/connexion";
        }

        // Recherche d'une conversation existante entre les deux étudiants (ils doivent tous deux y avoir posté un message)
        List<Conversation> allConversations = conversationRepository.findAll();
        Conversation conversation = null;

        for (Conversation c : allConversations) {
            List<EtuMessConversation> messages = messageRepository.findByConversation(c);
            boolean contientUtilisateur = messages.stream()
                    .anyMatch(m -> m.getEtudiant().getIdEtudiant().equals(utilisateurConnecte.getIdEtudiant()));
            boolean contientAmi = messages.stream()
                    .anyMatch(m -> m.getEtudiant().getIdEtudiant().equals(idEtudiant));
            if (contientUtilisateur && contientAmi) {
                conversation = c;
                break;
            }
        }

        if (conversation == null) {
            // Création d'une nouvelle conversation
            conversation = new Conversation();
            conversation.setDateCommenceConversation(LocalDateTime.now());
            conversationRepository.save(conversation);

            // Insertion de deux messages "Start to chat!" pour les deux participants
            EtuMessConversation msg1 = new EtuMessConversation();
            msg1.setEtudiant(utilisateurConnecte);
            msg1.setConversation(conversation);
            msg1.setMessage("Start to chat!");
            msg1.setDateHeureMessage(LocalDateTime.now());
            messageRepository.save(msg1);

            Etudiant amiInit = etudiantRepository.findById(idEtudiant).orElseThrow();

            EtuMessConversation msg2 = new EtuMessConversation();
            msg2.setEtudiant(amiInit);
            msg2.setConversation(conversation);
            msg2.setMessage("Start to chat!");
            msg2.setDateHeureMessage(LocalDateTime.now());
            messageRepository.save(msg2);
        }

        Etudiant ami = etudiantRepository.findById(idEtudiant).orElseThrow();
        List<EtuMessConversation> messages = messageRepository.findByConversation(conversation);

        model.addAttribute("ami", ami);
        model.addAttribute("conversation", conversation);
        model.addAttribute("messages", messages);
        model.addAttribute("utilisateurConnecte", utilisateurConnecte);

        return "conversation";
    }

    /**
     * Enregistre et envoie un nouveau message dans une conversation existante.
     *
     * @param idConversation identifiant de la conversation
     * @param idEtudiant     identifiant de l'étudiant qui envoie le message
     * @param message        contenu du message
     * @param session        session HTTP pour vérifier l’expéditeur
     * @return redirection vers la page de conversation
     */
    @PostMapping("/send")
    public String sendMessage(@RequestParam("idConversation") Long idConversation,
                              @RequestParam("idEtudiant") Long idEtudiant,
                              @RequestParam("message") String message,
                              HttpSession session) {

        Etudiant sender = (Etudiant) session.getAttribute("etudiantConnecte");

        if (sender == null || !sender.getIdEtudiant().equals(idEtudiant)) {
            return "redirect:/connexion";
        }

        Conversation conversation = conversationRepository.findById(idConversation).orElseThrow();

        EtuMessConversation msg = new EtuMessConversation();
        msg.setEtudiant(sender);
        msg.setConversation(conversation);
        msg.setMessage(message);
        msg.setDateHeureMessage(LocalDateTime.now());

        messageRepository.save(msg);

        return "redirect:/conversation/" + getOtherParticipantId(conversation, idEtudiant);
    }

    /**
     * Récupère l'identifiant de l'autre participant à la conversation.
     *
     * @param conv instance de la conversation
     * @param myId identifiant de l’étudiant actuel
     * @return identifiant de l'autre participant
     */
    private Long getOtherParticipantId(Conversation conv, Long myId) {
        List<EtuMessConversation> messages = messageRepository.findByConversation(conv);
        return messages.stream()
                .map(m -> m.getEtudiant().getIdEtudiant())
                .filter(id -> !id.equals(myId))
                .findFirst()
                .orElse(myId);
    }
}
