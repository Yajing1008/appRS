package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente un groupe dans le réseau social étudiant.
 * Un groupe peut être public ou privé, avoir un créateur, des membres, des demandes d’adhésion
 * et une conversation de groupe.
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

    /** Indique si le groupe est public (true) ou privé (false). */
    private Boolean estPublicGroupe;

    /** Photo de couverture du groupe (base64 ou URL). */
    @Lob
    private String photoGroupe = "";

    /** Conversation liée à ce groupe (peut être null au début). */
    @ManyToOne
    @JoinColumn(name = "id_conversation")
    private Conversation conversation;

    /** Étudiant qui a créé le groupe. */
    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant createur;

    /** Liste des membres du groupe via la relation Participer. */
    @OneToMany(mappedBy = "groupe")
    private List<Participer> membres = new ArrayList<>();

    /** Liste des demandes pour rejoindre le groupe. */
    @OneToMany(mappedBy = "groupe", cascade = CascadeType.ALL)
    private List<DemandeRejoindreGroupe> demandes = new ArrayList<>();

    // ====== Getters ======

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

    /** @return true si le groupe est public, false s’il est privé */
    public Boolean getEstPublicGroupe() {
        return estPublicGroupe;
    }

    /** @return la conversation associée au groupe */
    public Conversation getConversation() {
        return conversation;
    }

    /** @return l’étudiant créateur du groupe */
    public Etudiant getCreateur() {
        return createur;
    }

    /** @return la liste des membres du groupe */
    public List<Participer> getMembres() {
        return membres;
    }

    /** @return la photo du groupe (URL ou base64) */
    public String getPhotoGroupe() {
        return photoGroupe;
    }

    /** @return les demandes d'adhésion au groupe */
    public List<DemandeRejoindreGroupe> getDemandes() {
        return demandes;
    }

    // ====== Setters ======

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
     * @param estPublicGroupe true si public, false sinon
     */
    public void setEstPublicGroupe(Boolean estPublicGroupe) {
        this.estPublicGroupe = estPublicGroupe;
    }

    /**
     * Définit la conversation associée.
     * @param conversation conversation à associer
     */
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    /**
     * Définit le créateur du groupe.
     * @param createur étudiant à définir comme créateur
     */
    public void setCreateur(Etudiant createur) {
        this.createur = createur;
    }

    /**
     * Définit la liste des membres.
     * @param membres liste des participations
     */
    public void setMembres(List<Participer> membres) {
        this.membres = membres;
    }

    /**
     * Définit la photo du groupe.
     * @param photoGroupe photo à définir
     */
    public void setPhotoGroupe(String photoGroupe) {
        this.photoGroupe = photoGroupe;
    }

    /**
     * Définit la liste des demandes d’adhésion.
     * @param demandes liste à définir
     */
    public void setDemandes(List<DemandeRejoindreGroupe> demandes) {
        this.demandes = demandes;
    }

    // ====== equals & hashCode (basés sur l'id) ======

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Groupe groupe)) return false;
        return Objects.equals(idGroupe, groupe.idGroupe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGroupe);
    }
}