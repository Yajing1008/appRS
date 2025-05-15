package com.ut1.miage.appRS.model;

import java.util.*;
import jakarta.persistence.*;

/**
 * Représente un centre d'intérêt auquel un ou plusieurs étudiants peuvent être associés.
 */
@Entity
public class CentreInteret {

    /** Identifiant unique du centre d'intérêt. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCentreInteret;

    /** Nom du centre d'intérêt. */
    @Column(name = "nom_centre_interet", nullable = false)
    private String nomCentreInteret;

    /**
     * Liste des étudiants ayant ce centre d'intérêt.
     * Relation ManyToMany avec la table de jointure AVOIR.
     */
    @ManyToMany
    @JoinTable(
        name = "AVOIR", 
        joinColumns = @JoinColumn(name = "nom_centre_interet"),
        inverseJoinColumns = @JoinColumn(name = "id_etudiant")
    )
    private List<Etudiant> etudiant = new ArrayList<>();

    /** Constructeur par défaut. */
    public CentreInteret() {}

    /**
     * Constructeur avec paramètres.
     * 
     * @param idCentreInteret L'identifiant du centre d'intérêt.
     * @param nomCentreInteret Le nom du centre d'intérêt.
     * @param etudiant La liste des étudiants associés à ce centre d'intérêt.
     */
    public CentreInteret(Long idCentreInteret, String nomCentreInteret, List<Etudiant> etudiant) {
        this.idCentreInteret = idCentreInteret;
        this.nomCentreInteret = nomCentreInteret;
        this.etudiant = etudiant;
    }

    /**
     * @return L'identifiant du centre d'intérêt.
     */
    public Long getIdCentreInteret() {
        return idCentreInteret;
    }

    /**
     * @param idCentreInteret L'identifiant à définir.
     */
    public void setIdCentreInteret(Long idCentreInteret) {
        this.idCentreInteret = idCentreInteret;
    }

    /**
     * @return Le nom du centre d'intérêt.
     */
    public String getNomCentreInteret() {
        return nomCentreInteret;
    }

    /**
     * @param nomCentreInteret Le nom à définir pour le centre d'intérêt.
     */
    public void setNomCentreInteret(String nomCentreInteret) {
        this.nomCentreInteret = nomCentreInteret;
    }

    /**
     * @return La liste des étudiants associés à ce centre d'intérêt.
     */
    public List<Etudiant> getEtudiant() {
        return etudiant;
    }

    /**
     * @param etudiant La liste des étudiants à associer.
     */
    public void setEtudiant(List<Etudiant> etudiant) {
        this.etudiant = etudiant;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCentreInteret, nomCentreInteret, etudiant);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CentreInteret)) return false;
        CentreInteret other = (CentreInteret) obj;
        return Objects.equals(idCentreInteret, other.idCentreInteret) &&
               Objects.equals(nomCentreInteret, other.nomCentreInteret) &&
               Objects.equals(etudiant, other.etudiant);
    }
}