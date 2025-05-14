package com.ut1.miage.appRS.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONVERSATIONS")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConversation;

    private LocalDate dateCommenceConversation;
    @OneToMany(mappedBy = "conversation")
    private List<Groupe> groupes = new ArrayList<>();

    @OneToMany(mappedBy = "conversation")
    private List<EtuMessConversation> messagesDansConversation;



    public Long getIdConversation() {
        return idConversation;
    }

    public LocalDate getDateCommenceConversation() {
        return dateCommenceConversation;
    }

    public void setIdConversation(Long idConversation) {
        this.idConversation = idConversation;
    }

    public void setDateCommenceConversation(LocalDate dateCommenceConversation) {
        this.dateCommenceConversation = dateCommenceConversation;
    }

    public List<Groupe> getGroupes() {
        return groupes;
    }

    public void setGroupes(List<Groupe> groupes) {
        this.groupes = groupes;
    }

    public List<EtuMessConversation> getMessagesDansConversation() {
        return messagesDansConversation;
    }

    public void setMessagesDansConversation(List<EtuMessConversation> messagesDansConversation) {
        this.messagesDansConversation = messagesDansConversation;
    }

    
}

