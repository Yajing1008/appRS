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
import com.ut1.miage.appRS.model.Groupe;
import com.ut1.miage.appRS.repository.ConversationRepository;
import com.ut1.miage.appRS.repository.EtuMessConversationRepository;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import com.ut1.miage.appRS.repository.GroupeRepository;

import jakarta.servlet.http.HttpSession;

/**
 * Contrôleur permettant de gérer les conversations de groupe dans l'application.
 * Il permet d'afficher les messages d’un groupe et d’en envoyer de nouveaux.
 */
@Controller
public class ConversationGroupeController {

    @Autowired
    GroupeRepository groupeRepository;

    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    EtuMessConversationRepository messageRepository;

    @Autowired
    EtudiantRepository etudiantRepository;

    /**
     * Affiche la conversation associée à un groupe donné.
     *
     * @param idGroupe identifiant du groupe
     * @param model modèle pour transmettre les données à la vue
     * @param session session utilisateur
     * @return nom de la vue "conversationGroupe", ou redirection en cas d'erreur
     */
    @GetMapping("/conversationGroupe/{id}")
    public String afficherConversationGroupe(@PathVariable("id") Long idGroupe,
                                             Model model,
                                             HttpSession session) {

        Etudiant utilisateurConnecte = (Etudiant) session.getAttribute("etudiantConnecte");
        if (utilisateurConnecte == null) {
            return "redirect:/connexion";
        }

        Groupe groupe = groupeRepository.findById(idGroupe).orElse(null);
        if (groupe == null || groupe.getConversation() == null) {
            return "redirect:/groupe/groupes";
        }

        Conversation conversation = groupe.getConversation();
        List<EtuMessConversation> messages = messageRepository.findByConversation(conversation);

        model.addAttribute("groupe", groupe); // utilisé dans la vue pour afficher le nom et l’image du groupe
        model.addAttribute("conversation", conversation);
        model.addAttribute("messages", messages);
        model.addAttribute("utilisateurConnecte", utilisateurConnecte);

        return "conversationGroupe";
    }

    /**
     * Permet à un étudiant d'envoyer un message dans une conversation de groupe.
     *
     * @param idConversation identifiant de la conversation de groupe
     * @param idEtudiant identifiant de l’étudiant qui envoie le message
     * @param contenu contenu du message
     * @param session session utilisateur
     * @return redirection vers la page de conversation de groupe
     */
    @PostMapping("/sendGroupe")
    public String envoyerMessageGroupe(@RequestParam("idConversation") Long idConversation,
                                       @RequestParam("idEtudiant") Long idEtudiant,
                                       @RequestParam("message") String contenu,
                                       HttpSession session) {

        Etudiant etudiant = (Etudiant) session.getAttribute("etudiantConnecte");
        if (etudiant == null || !etudiant.getIdEtudiant().equals(idEtudiant)) {
            return "redirect:/connexion";
        }

        Conversation conversation = conversationRepository.findById(idConversation).orElse(null);
        if (conversation == null) {
            return "redirect:/groupe/groupes";
        }

        // Création et enregistrement du message
        EtuMessConversation message = new EtuMessConversation();
        message.setConversation(conversation);
        message.setEtudiant(etudiant);
        message.setMessage(contenu);
        message.setDateHeureMessage(LocalDateTime.now());

        messageRepository.save(message);

        // Redirection vers la conversation du groupe
        Groupe groupe = groupeRepository.findByConversation(conversation).orElse(null);
        if (groupe != null) {
            return "redirect:/conversationGroupe/" + groupe.getIdGroupe();
        } else {
            return "redirect:/groupe/groupes";
        }
    }
}
