package com.ut1.miage.appRS.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ETU_MESS_CONVERSATION")
public class EtuMessConversation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtuMessConversation;
    
    @ManyToOne
    @JoinColumn(name = "id_etudiant", nullable = false)
    private Etudiant etudiant;
    
    @ManyToOne
    @JoinColumn(name = "id_conversation", nullable = false)
    private Conversation conversation;
    
    private String message;
    
    private LocalDateTime dateHeureMessage;
    
    // --- Getters & Setters ---
    
    public Long getIdEtuMessConversation() {
        return idEtuMessConversation;
    }
    
    public void setIdEtuMessConversation(Long idMessage) {
        this.idEtuMessConversation = idMessage;
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
