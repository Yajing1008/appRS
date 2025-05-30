package com.ut1.miage.appRS.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entité représentant une conversation dans l'application.
 * Une conversation peut regrouper plusieurs groupes et plusieurs messages envoyés par des étudiants.
 */
@Entity
@Table(name = "CONVERSATIONS")
public class Conversation {

    /**
     * Identifiant unique de la conversation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConversation;

    /**
     * Date de début de la conversation.
     */
    private LocalDateTime dateCommenceConversation;

    /**
     * Messages envoyés dans cette conversation.
     */
    @OneToMany(mappedBy = "conversation")
    private List<EtuMessConversation> messagesDansConversation = new ArrayList<>();

    /**
     * Retourne l'identifiant de la conversation.
     * @return idConversation
     */
    public Long getIdConversation() {
        return idConversation;
    }

    /**
     * Définit l'identifiant de la conversation.
     * @param idConversation identifiant à définir
     */
    public void setIdConversation(Long idConversation) {
        this.idConversation = idConversation;
    }

    /**
     * Retourne la date de début de la conversation.
     * @return dateCommenceConversation
     */
    public LocalDateTime getDateCommenceConversation() {
        return dateCommenceConversation;
    }

    /**
     * Définit la date de début de la conversation.
     * @param dateCommenceConversation date à définir
     */
    public void setDateCommenceConversation(LocalDateTime dateCommenceConversation) {
        this.dateCommenceConversation = dateCommenceConversation;
    }
    
    /**
     * Retourne les messages envoyés dans cette conversation.
     * @return liste de messages
     */
    public List<EtuMessConversation> getMessagesDansConversation() {
        return messagesDansConversation;
    }

    /**
     * Définit les messages de cette conversation.
     * @param messagesDansConversation liste de messages
     */
    public void setMessagesDansConversation(List<EtuMessConversation> messagesDansConversation) {
        this.messagesDansConversation = messagesDansConversation;
    }
}