package com.ut1.miage.appRS.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Représente une demande envoyée par un étudiant pour rejoindre un groupe privé.
 */
@Entity
@Table(name = "DEMANDES_REJOINDRE_GROUPE")
public class DemandeRejoindreGroupe {

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

    /** Date de la demande. */
    private LocalDate dateDemande;

    /**
     * Statut de la demande :
     * - EN_ATTENTE
     * - ACCEPTEE
     * - REFUSEE
     */
    private String statut;

    private Boolean approuvee;

    private boolean refusee = false;


    // --- Getters & Setters ---

    public Long getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(Long idDemande) {
        this.idDemande = idDemande;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Boolean getApprouvee() {
        return approuvee;
    }

    public void setApprouvee(Boolean approuvee) {
        this.approuvee = approuvee;
    }

    public boolean isRefusee() {
        return refusee;
    }

    public void setRefusee(boolean refusee) {
        this.refusee = refusee;
    }
}
