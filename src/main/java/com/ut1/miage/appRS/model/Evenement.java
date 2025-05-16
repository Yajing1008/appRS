package com.ut1.miage.appRS.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Représente un événement dans le réseau social.
 * Un événement est créé par un étudiant et peut avoir plusieurs membres (étudiants) qui y participent.
 */
@Entity
public class Evenement {

    /** Identifiant unique de l'événement. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvenement;

    /** Date et heure de l'événement. */
    @Column(name = "dateHeureEvenement", nullable = false)
    private LocalDateTime dateHeureEvenement;

    /** Nom de l'événement. */
    @Column(name = "nomEvenement", nullable = false)
    private String nomEvenement;

    /** Étudiant créateur de l'événement. */
    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant createur;

    /**
     * Liste des étudiants participant à l'événement.
     * Cette relation est mappée via la table de jointure PRENDRE_PART.
     */
    @ManyToMany
    @JoinTable(
        name = "PRENDRE_PART",
        joinColumns = @JoinColumn(name = "id_evenement"),
        inverseJoinColumns = @JoinColumn(name = "id_etudiant")
    )
    private List<Etudiant> membreGroupe = new ArrayList<>();

    /** Constructeur par défaut. */
    public Evenement() {}

    /**
     * Constructeur avec tous les champs.
     *
     * @param idEvenement identifiant de l'événement
     * @param dateHeureEvenement date et heure de l'événement
     * @param nomEvenement nom de l'événement
     * @param createur créateur de l'événement
     * @param membreGroupe liste des membres participants
     */
    public Evenement(Long idEvenement, LocalDateTime dateHeureEvenement, String nomEvenement, Etudiant createur,
                     List<Etudiant> membreGroupe) {
        this.idEvenement = idEvenement;
        this.dateHeureEvenement = dateHeureEvenement;
        this.nomEvenement = nomEvenement;
        this.createur = createur;
        this.membreGroupe = membreGroupe;
    }

    public Long getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(Long idEvenement) {
        this.idEvenement = idEvenement;
    }

    public LocalDateTime getDateHeureEvenement() {
        return dateHeureEvenement;
    }

    public void setDateHeureEvenement(LocalDateTime dateHeureEvenement) {
        this.dateHeureEvenement = dateHeureEvenement;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }

    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement;
    }

    public Etudiant getCreateur() {
        return createur;
    }

    public void setCreateur(Etudiant createur) {
        this.createur = createur;
    }

    public List<Etudiant> getMembreGroupe() {
        return membreGroupe;
    }

    public void setMembreGroupe(List<Etudiant> membreGroupe) {
        this.membreGroupe = membreGroupe;
    }

    /**
     * Génère un hashCode basé sur tous les champs.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idEvenement, dateHeureEvenement, nomEvenement, createur, membreGroupe);
    }

    /**
     * Compare deux événements pour vérifier s'ils sont égaux.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Evenement other = (Evenement) obj;
        return Objects.equals(idEvenement, other.idEvenement)
            && Objects.equals(dateHeureEvenement, other.dateHeureEvenement)
            && Objects.equals(nomEvenement, other.nomEvenement)
            && Objects.equals(createur, other.createur)
            && Objects.equals(membreGroupe, other.membreGroupe);
    }
}