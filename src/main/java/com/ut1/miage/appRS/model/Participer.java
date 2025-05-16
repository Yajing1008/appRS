package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

/**
 * Représente la participation d'un étudiant à un groupe avec un rôle spécifique.
 * Utilise une clé composée pour référencer l'étudiant et le groupe.
 */
@Entity
@Table(name = "PARTICIPER")
public class Participer {

    /** Clé primaire composée représentant la relation entre étudiant et groupe. */
    @EmbeddedId
    private ParticiperId id = new ParticiperId();

    /** Étudiant participant au groupe. */
    @ManyToOne
    @MapsId("idEtudiant")
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;

    /** Groupe auquel l'étudiant participe. */
    @ManyToOne
    @MapsId("idGroupe")
    @JoinColumn(name = "id_groupe")
    private Groupe groupe;

    /** Rôle de l'étudiant dans le groupe (ex. : "admin", "membre"). */
    private String role;

    /**
     * @return l'identifiant composé de la participation
     */
    public ParticiperId getId() {
        return id;
    }

    /**
     * Définit l'identifiant composé de la participation.
     * @param id l'identifiant composé à définir
     */
    public void setId(ParticiperId id) {
        this.id = id;
    }

    /**
     * @return l'étudiant participant
     */
    public Etudiant getEtudiant() {
        return etudiant;
    }

    /**
     * Définit l'étudiant participant.
     * @param etudiant l'étudiant à associer
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * @return le groupe associé à la participation
     */
    public Groupe getGroupe() {
        return groupe;
    }

    /**
     * Définit le groupe auquel l'étudiant participe.
     * @param groupe le groupe à associer
     */
    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    /**
     * @return le rôle de l'étudiant dans le groupe
     */
    public String getRole() {
        return role;
    }

    /**
     * Définit le rôle de l'étudiant dans le groupe.
     * @param role le rôle à définir (ex. : "admin", "membre")
     */
    public void setRole(String role) {
        this.role = role;
    }
}