package com.ut1.miage.appRS.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "GROUPES")
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGroupe;

    private String nomGroupe;

    private LocalDate dateCreerGroupe;

    private String descriptionGroupe;

    private Boolean estPublicGroupe;

    @ManyToOne
    @JoinColumn(name = "idConversation")
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "idEtudiant")
    private Etudiant createur;

    @OneToMany(mappedBy = "groupe")
    private List<Participer> membres = new ArrayList<>();


    public Long getIdGroupe() {
        return idGroupe;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public LocalDate getDateCreerGroupe() {
        return dateCreerGroupe;
    }

    public String getDescriptionGroupe() {
        return descriptionGroupe;
    }

    public Boolean getEstPublicGroupe() {
        return estPublicGroupe;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public Etudiant getCreateur() {
        return createur;
    }

    public void setIdGroupe(Long idGroupe) {
        this.idGroupe = idGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public void setDateCreerGroupe(LocalDate dateCreerGroupe) {
        this.dateCreerGroupe = dateCreerGroupe;
    }

    public void setDescriptionGroupe(String descriptionGroupe) {
        this.descriptionGroupe = descriptionGroupe;
    }

    public void setEstPublicGroupe(Boolean estPublicGroupe) {
        this.estPublicGroupe = estPublicGroupe;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public void setCreateur(Etudiant createur) {
        this.createur = createur;
    }

    public List<Participer> getMembres() {
        return membres;
    }

    public void setMembres(List<Participer> membres) {
        this.membres = membres;
    }

}

