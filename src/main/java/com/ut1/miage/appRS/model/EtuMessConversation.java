package com.ut1.miage.appRS.model;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * Représente un message envoyé par un étudiant dans une conversation.
 * 
 * Cette entité utilise une clé composite {@link EtuMessConversationId} composée des identifiants
 * de l'étudiant et de la conversation. Elle contient également le contenu du message et
 * la date/heure à laquelle il a été envoyé.
 */
@Entity
@Table(name = "ETU_MESS_CONVERSATION")
public class EtuMessConversation {

    /**
     * Clé primaire composite liant un étudiant à une conversation.
     */
    @EmbeddedId
    private EtuMessConversationId id = new EtuMessConversationId();

    /**
     * Étudiant qui a envoyé le message.
     */
    @ManyToOne
    @MapsId("idEtudiant")
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;

    /**
     * Conversation dans laquelle le message a été envoyé.
     */
    @ManyToOne
    @MapsId("idConversation")
    @JoinColumn(name = "id_conversation")
    private Conversation conversation;

    /**
     * Contenu textuel du message.
     */
    private String message;

    /**
     * Date et heure d'envoi du message.
     */
    private LocalDateTime dateHeureMessage;

    // --- Getters & Setters ---

    /**
     * Retourne la clé composite de ce message.
     * 
     * @return identifiant composite
     */
    public EtuMessConversationId getId() {
        return id;
    }

    /**
     * Définit la clé composite du message.
     * 
     * @param id identifiant composite
     */
    public void setId(EtuMessConversationId id) {
        this.id = id;
    }

    /**
     * Retourne l'étudiant ayant envoyé le message.
     * 
     * @return l'étudiant
     */
    public Etudiant getEtudiant() {
        return etudiant;
    }

    /**
     * Définit l'étudiant ayant envoyé le message.
     * 
     * @param etudiant l'étudiant
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * Retourne la conversation à laquelle appartient le message.
     * 
     * @return la conversation
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     * Définit la conversation dans laquelle le message a été envoyé.
     * 
     * @param conversation la conversation
     */
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    /**
     * Retourne le contenu textuel du message.
     * 
     * @return le message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Définit le contenu du message.
     * 
     * @param message contenu textuel
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retourne la date et l'heure de l'envoi du message.
     * 
     * @return date et heure
     */
    public LocalDateTime getDateHeureMessage() {
        return dateHeureMessage;
    }

    /**
     * Définit la date et l'heure d'envoi du message.
     * 
     * @param dateHeureMessage date et heure
     */
    public void setDateHeureMessage(LocalDateTime dateHeureMessage) {
        this.dateHeureMessage = dateHeureMessage;
    }
}