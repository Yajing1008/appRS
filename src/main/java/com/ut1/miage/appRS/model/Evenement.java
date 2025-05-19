package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    /** Date et heure de début de l'événement. */
    @Column(name = "dateHeureDebutEvenement", nullable = false)
    private LocalDateTime dateHeureDebutEvenement;

    /** Date et heure de fin de l'événement. */
    @Column(name = "dateHeureFinEvenement", nullable = false)
    private LocalDateTime dateHeureFinEvenement;

    /** Nom de l'événement. */
    @Column(name = "nomEvenement", nullable = false)
    private String nomEvenement;

    /**URL de l'image illustrant l'événement.*/
    @Column(name = "imageUrlEvenement")
    private String imageUrlEvenement;

    /**Lieu où se déroule l'événement.*/
    @Column(name = "lieuEvenement")
    private String lieuEvenement;

    /** Description détaillée de l'événement.*/
    @Column(name = "descriptionEvenement", length = 1000)
    private String descriptionEvenement;


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
     * @param dateHeureDebutEvenement date et heure de début de l'événement
     * @param dateHeureFinEvenement date et heure de fin de l'événement
     * @param nomEvenement nom de l'événement
     * @param imageUrlEvenement URL de l'image illustrant l'événement
     * @param lieuEvenement lieu où se déroule l'événement
     * @param descriptionEvenement description détaillée de l'événement
     * @param createur créateur de l'événement
     * @param membreGroupe liste des membres participants
     */
    public Evenement(Long idEvenement,
                     LocalDateTime dateHeureDebutEvenement,
                     LocalDateTime dateHeureFinEvenement,
                     String nomEvenement,
                     String imageUrlEvenement,
                     String lieuEvenement,
                     String descriptionEvenement,
                     Etudiant createur,
                     List<Etudiant> membreGroupe) {
        this.idEvenement = idEvenement;
        this.dateHeureDebutEvenement = dateHeureDebutEvenement;
        this.dateHeureFinEvenement = dateHeureFinEvenement;
        this.nomEvenement = nomEvenement;
        this.imageUrlEvenement = imageUrlEvenement;
        this.lieuEvenement = lieuEvenement;
        this.descriptionEvenement = descriptionEvenement;
        this.createur = createur;
        this.membreGroupe = membreGroupe;
    }




    public Long getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(Long idEvenement) {
        this.idEvenement = idEvenement;
    }

    public LocalDateTime getDateHeureDebutEvenement() {
        return dateHeureDebutEvenement;
    }

    public void setDateHeureDebutEvenement(LocalDateTime dateHeureDebutEvenement) {
        this.dateHeureDebutEvenement = dateHeureDebutEvenement;
    }

    public LocalDateTime getDateHeureFinEvenement() {
        return dateHeureFinEvenement;
    }

    public void setDateHeureFinEvenement(LocalDateTime dateHeureFinEvenement) {
        this.dateHeureFinEvenement = dateHeureFinEvenement;
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

    public String getImageUrlEvenement() {
        return imageUrlEvenement;
    }

    public void setImageUrlEvenement(String imageUrlEvenement) {
        this.imageUrlEvenement = imageUrlEvenement;
    }

    public String getLieuEvenement() {
        return lieuEvenement;
    }

    public void setLieuEvenement(String lieuEvenement) {
        this.lieuEvenement = lieuEvenement;
    }

    public String getDescriptionEvenement() {
        return descriptionEvenement;
    }

    public void setDescriptionEvenement(String descriptionEvenement) {
        this.descriptionEvenement = descriptionEvenement;
    }

    /**
     * Génère un hashCode basé sur tous les champs.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getIdEvenement(), dateHeureDebutEvenement, getDateHeureFinEvenement(), getNomEvenement(), getImageUrlEvenement(), getLieuEvenement(), getDescriptionEvenement(), getCreateur(), getMembreGroupe());
    }

    /**
     * Compare deux événements pour vérifier s'ils sont égaux.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Evenement evenement = (Evenement) o;
        return Objects.equals(getIdEvenement(), evenement.getIdEvenement()) && Objects.equals(dateHeureDebutEvenement, evenement.dateHeureDebutEvenement) && Objects.equals(getDateHeureFinEvenement(), evenement.getDateHeureFinEvenement()) && Objects.equals(getNomEvenement(), evenement.getNomEvenement()) && Objects.equals(getImageUrlEvenement(), evenement.getImageUrlEvenement()) && Objects.equals(getLieuEvenement(), evenement.getLieuEvenement()) && Objects.equals(getDescriptionEvenement(), evenement.getDescriptionEvenement()) && Objects.equals(getCreateur(), evenement.getCreateur()) && Objects.equals(getMembreGroupe(), evenement.getMembreGroupe());
    }
}