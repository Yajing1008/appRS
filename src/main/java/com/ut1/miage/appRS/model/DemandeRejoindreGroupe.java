package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Représente une demande d’un étudiant pour rejoindre un groupe privé.
 * La demande contient l'étudiant concerné, le groupe visé, la date,
 * le statut (EN_ATTENTE, ACCEPTEE, REFUSEE), ainsi que des indicateurs de validation.
 */
@Entity
@Table(name = "DEMANDES_REJOINDRE_GROUPE")
public class DemandeRejoindreGroupe {

    /** Identifiant unique de la demande. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDemande;

    /** Étudiant qui a fait la demande. */
    @ManyToOne
    @JoinColumn(name = "id_etudiant", nullable = false)
    private Etudiant etudiant;

    /** Groupe que l'étudiant souhaite rejoindre. */
    @ManyToOne
    @JoinColumn(name = "id_groupe", nullable = false)
    private Groupe groupe;

    /** Date à laquelle la demande a été effectuée. */
    private LocalDate dateDemande;

    /**
     * Statut de la demande :
     * - EN_ATTENTE
     * - ACCEPTEE
     * - REFUSEE
     */
    private String statut;

    /** Indique si la demande a été approuvée (true/false). */
    private Boolean approuvee;

    /** Indique si la demande a été explicitement refusée. */
    private boolean refusee = false;

    // --- Getters & Setters ---

    /**
     * @return l'identifiant unique de la demande
     */
    public Long getIdDemande() {
        return idDemande;
    }

    /**
     * Définit l'identifiant de la demande.
     * @param idDemande identifiant à définir
     */
    public void setIdDemande(Long idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @return l'étudiant ayant effectué la demande
     */
    public Etudiant getEtudiant() {
        return etudiant;
    }

    /**
     * Définit l'étudiant à l'origine de la demande.
     * @param etudiant étudiant concerné
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * @return le groupe ciblé par la demande
     */
    public Groupe getGroupe() {
        return groupe;
    }

    /**
     * Définit le groupe à rejoindre.
     * @param groupe groupe visé
     */
    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    /**
     * @return la date de la demande
     */
    public LocalDate getDateDemande() {
        return dateDemande;
    }

    /**
     * Définit la date de la demande.
     * @param dateDemande date à définir
     */
    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    /**
     * @return le statut de la demande (EN_ATTENTE, ACCEPTEE, REFUSEE)
     */
    public String getStatut() {
        return statut;
    }

    /**
     * Définit le statut de la demande.
     * @param statut statut à définir
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * @return true si la demande est approuvée
     */
    public Boolean getApprouvee() {
        return approuvee;
    }

    /**
     * Définit si la demande est approuvée.
     * @param approuvee valeur à définir
     */
    public void setApprouvee(Boolean approuvee) {
        this.approuvee = approuvee;
    }

    /**
     * @return true si la demande est refusée
     */
    public boolean isRefusee() {
        return refusee;
    }

    /**
     * Définit si la demande est refusée.
     * @param refusee true si refusée, sinon false
     */
    public void setRefusee(boolean refusee) {
        this.refusee = refusee;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DemandeRejoindreGroupe that)) return false;
        return Objects.equals(idDemande, that.idDemande);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDemande);
    }
}