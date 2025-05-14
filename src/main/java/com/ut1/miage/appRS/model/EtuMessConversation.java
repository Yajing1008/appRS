package com.ut1.miage.appRS.model;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "ETU_MESS_CONVERSATION")
public class EtuMessConversation {

    @EmbeddedId
    private EtuMessConversationId id = new EtuMessConversationId();

    @ManyToOne
    @MapsId("idEtudiant")
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;

    @ManyToOne
    @MapsId("idConversation")
    @JoinColumn(name = "id_conversation")
    private Conversation conversation;

    private String message;

    private LocalDateTime dateHeureMessage;

    // --- Getters & Setters ---

    public EtuMessConversationId getId() {
        return id;
    }

    public void setId(EtuMessConversationId id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateHeureMessage() {
        return dateHeureMessage;
    }

    public void setDateHeureMessage(LocalDateTime dateHeureMessage) {
        this.dateHeureMessage = dateHeureMessage;
    }
}
