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




    /**
     * Retourne l'identifiant de l'événement.
     *
     * @return l'identifiant de l'événement
     */
    public Long getIdEvenement() {
        return idEvenement;
    }

    /**
     * Définit l'identifiant de l'événement.
     *
     * @param idEvenement l'identifiant de l'événement
     */
    public void setIdEvenement(Long idEvenement) {
        this.idEvenement = idEvenement;
    }

    /**
     * Retourne la date et l'heure de début de l'événement.
     *
     * @return la date et l'heure de début de l'événement
     */
    public LocalDateTime getDateHeureDebutEvenement() {
        return dateHeureDebutEvenement;
    }

    /**
     * Définit la date et l'heure de début de l'événement.
     *
     * @param dateHeureDebutEvenement la date et l'heure de début de l'événement
     */
    public void setDateHeureDebutEvenement(LocalDateTime dateHeureDebutEvenement) {
        this.dateHeureDebutEvenement = dateHeureDebutEvenement;
    }

    /**
     * Retourne la date et l'heure de fin de l'événement.
     *
     * @return la date et l'heure de fin de l'événement
     */
    public LocalDateTime getDateHeureFinEvenement() {
        return dateHeureFinEvenement;
    }

    /**
     * Définit la date et l'heure de fin de l'événement.
     *
     * @param dateHeureFinEvenement la date et l'heure de fin de l'événement
     */
    public void setDateHeureFinEvenement(LocalDateTime dateHeureFinEvenement) {
        this.dateHeureFinEvenement = dateHeureFinEvenement;
    }

    /**
     * Retourne le nom de l'événement.
     *
     * @return le nom de l'événement
     */
    public String getNomEvenement() {
        return nomEvenement;
    }

    /**
     * Définit le nom de l'événement.
     *
     * @param nomEvenement le nom de l'événement
     */
    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement;
    }

    /**
     * Retourne l'étudiant créateur de l'événement.
     *
     * @return l'étudiant créateur
     */
    public Etudiant getCreateur() {
        return createur;
    }

    /**
     * Définit l'étudiant créateur de l'événement.
     *
     * @param createur l'étudiant créateur
     */
    public void setCreateur(Etudiant createur) {
        this.createur = createur;
    }

    /**
     * Retourne la liste des membres du groupe participant à l'événement.
     *
     * @return la liste des membres du groupe
     */
    public List<Etudiant> getMembreGroupe() {
        return membreGroupe;
    }

    /**
     * Définit la liste des membres du groupe participant à l'événement.
     *
     * @param membreGroupe la liste des membres du groupe
     */
    public void setMembreGroupe(List<Etudiant> membreGroupe) {
        this.membreGroupe = membreGroupe;
    }

    /**
     * Retourne l'URL de l'image associée à l'événement.
     *
     * @return l'URL de l'image de l'événement
     */
    public String getImageUrlEvenement() {
        return imageUrlEvenement;
    }

    /**
     * Définit l'URL de l'image associée à l'événement.
     *
     * @param imageUrlEvenement l'URL de l'image de l'événement
     */
    public void setImageUrlEvenement(String imageUrlEvenement) {
        this.imageUrlEvenement = imageUrlEvenement;
    }

    /**
     * Retourne le lieu de l'événement.
     *
     * @return le lieu de l'événement
     */
    public String getLieuEvenement() {
        return lieuEvenement;
    }

    /**
     * Définit le lieu de l'événement.
     *
     * @param lieuEvenement le lieu de l'événement
     */
    public void setLieuEvenement(String lieuEvenement) {
        this.lieuEvenement = lieuEvenement;
    }

    /**
     * Retourne la description de l'événement.
     *
     * @return la description de l'événement
     */
    public String getDescriptionEvenement() {
        return descriptionEvenement;
    }

    /**
     * Définit la description de l'événement.
     *
     * @param descriptionEvenement la description de l'événement
     */
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