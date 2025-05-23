package com.ut1.miage.appRS.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Représente un message envoyé par un étudiant dans une conversation.
 * Ce message est lié à un étudiant (auteur) et à une conversation spécifique.
 */
@Entity
@Table(name = "ETU_MESS_CONVERSATION")
public class EtuMessConversation {
    
    /**
     * Identifiant unique du message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtuMessConversation;
    
    /**
     * Étudiant ayant envoyé le message.
     */
    @ManyToOne
    @JoinColumn(name = "id_etudiant", nullable = false)
    private Etudiant etudiant;
    
    /**
     * Conversation dans laquelle le message a été envoyé.
     */
    @ManyToOne
    @JoinColumn(name = "id_conversation", nullable = false)
    private Conversation conversation;
    
    /**
     * Contenu textuel du message.
     */
    private String message;
    
    /**
     * Date et heure d'envoi du message.
     */
    private LocalDateTime dateHeureMessage;

    
    /**
     * Retourne l'identifiant du message.
     * @return identifiant du message
     */
    public Long getIdEtuMessConversation() {
        return idEtuMessConversation;
    }
    
    /**
     * Définit l'identifiant du message.
     * @param idMessage identifiant à définir
     */
    public void setIdEtuMessConversation(Long idMessage) {
        this.idEtuMessConversation = idMessage;
    }
    
    /**
     * Retourne l'étudiant ayant envoyé le message.
     * @return l'étudiant auteur du message
     */
    public Etudiant getEtudiant() {
        return etudiant;
    }
    
    /**
     * Définit l'étudiant auteur du message.
     * @param etudiant étudiant à définir
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }
    
    /**
     * Retourne la conversation concernée.
     * @return la conversation liée
     */
    public Conversation getConversation() {
        return conversation;
    }
    
    /**
     * Définit la conversation liée au message.
     * @param conversation conversation à définir
     */
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
    
    /**
     * Retourne le contenu du message.
     * @return contenu textuel
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Définit le contenu du message.
     * @param message texte à définir
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Retourne la date et l'heure d'envoi du message.
     * @return date et heure d'envoi
     */
    public LocalDateTime getDateHeureMessage() {
        return dateHeureMessage;
    }
    
    /**
     * Définit la date et l'heure d'envoi du message.
     * @param dateHeureMessage date à définir
     */
    public void setDateHeureMessage(LocalDateTime dateHeureMessage) {
        this.dateHeureMessage = dateHeureMessage;
    }
}
