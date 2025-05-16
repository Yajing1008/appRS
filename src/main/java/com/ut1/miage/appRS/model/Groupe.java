package com.ut1.miage.appRS.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 * Représente un groupe dans le réseau social.
 * Un groupe peut avoir un créateur, des membres, une conversation associée,
 * et être public ou privé.
 */
@Entity
@Table(name = "GROUPES")
public class Groupe {

    /** Identifiant unique du groupe. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGroupe;

    /** Nom du groupe. */
    private String nomGroupe;

    /** Date de création du groupe. */
    private LocalDate dateCreerGroupe;

    /** Description du groupe. */
    private String descriptionGroupe;

    /** Indique si le groupe est public ou privé. */
    private Boolean estPublicGroupe;

    /** Conversation associée au groupe. */
    @ManyToOne
    @JoinColumn(name = "id_conversation")
    private Conversation conversation;

    /** Étudiant créateur du groupe. */
    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant createur;

    /** Liste des participations au groupe (étudiants membres). */
    @OneToMany(mappedBy = "groupe")
    private List<Participer> membres = new ArrayList<>();

    /** @return l'identifiant du groupe */
    public Long getIdGroupe() {
        return idGroupe;
    }

    /** @return le nom du groupe */
    public String getNomGroupe() {
        return nomGroupe;
    }

    /** @return la date de création du groupe */
    public LocalDate getDateCreerGroupe() {
        return dateCreerGroupe;
    }

    /** @return la description du groupe */
    public String getDescriptionGroupe() {
        return descriptionGroupe;
    }

    /** @return true si le groupe est public, sinon false */
    public Boolean getEstPublicGroupe() {
        return estPublicGroupe;
    }

    /** @return la conversation liée au groupe */
    public Conversation getConversation() {
        return conversation;
    }

    /** @return l'étudiant créateur du groupe */
    public Etudiant getCreateur() {
        return createur;
    }

    /**
     * Définit l'identifiant du groupe.
     * @param idGroupe identifiant à définir
     */
    public void setIdGroupe(Long idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Définit le nom du groupe.
     * @param nomGroupe nom à définir
     */
    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    /**
     * Définit la date de création du groupe.
     * @param dateCreerGroupe date à définir
     */
    public void setDateCreerGroupe(LocalDate dateCreerGroupe) {
        this.dateCreerGroupe = dateCreerGroupe;
    }

    /**
     * Définit la description du groupe.
     * @param descriptionGroupe description à définir
     */
    public void setDescriptionGroupe(String descriptionGroupe) {
        this.descriptionGroupe = descriptionGroupe;
    }

    /**
     * Définit si le groupe est public.
     * @param estPublicGroupe true si public, sinon false
     */
    public void setEstPublicGroupe(Boolean estPublicGroupe) {
        this.estPublicGroupe = estPublicGroupe;
    }

    /**
     * Définit la conversation associée au groupe.
     * @param conversation la conversation à associer
     */
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    /**
     * Définit l'étudiant créateur du groupe.
     * @param createur l'étudiant créateur
     */
    public void setCreateur(Etudiant createur) {
        this.createur = createur;
    }

    /** @return la liste des membres du groupe */
    public List<Participer> getMembres() {
        return membres;
    }

    /**
     * Définit la liste des membres du groupe.
     * @param membres liste de participations à associer
     */
    public void setMembres(List<Participer> membres) {
        this.membres = membres;
    }
}