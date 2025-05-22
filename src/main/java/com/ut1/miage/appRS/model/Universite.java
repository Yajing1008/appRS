package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente une université dans le système.  
 * Une université peut être liée à plusieurs étudiants via la relation "ETUDIER".
 */
@Entity
public class Universite {

    /** Identifiant unique de l'université (clé primaire). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUniv;

    /** Nom de l'université (champ obligatoire). */
    @Column(name = "nom_univ", nullable = false)
    private String nomUniv;

    /**
     * Liste des étudiants associés à cette université via la table de jointure "ETUDIER".
     */
    @ManyToMany(mappedBy = "universites")
    private List<Etudiant> etudiants = new ArrayList<>();

    /** Constructeur sans argument requis pour JPA. */
    public Universite() {}

    /**
     * Constructeur avec paramètres.
     * 
     * @param idUniv l'identifiant de l'université
     * @param nomUniv le nom de l'université
     * @param etudiants la liste des étudiants associés
     */
    public Universite(Long idUniv, String nomUniv, List<Etudiant> etudiants) {
        this.idUniv = idUniv;
        this.nomUniv = nomUniv;
        this.etudiants = etudiants;
    }

    /**
     * Retourne l'identifiant de l'université.
     * 
     * @return identifiant de l'université
     */
    public Long getIdUniv() {
        return idUniv;
    }

    /**
     * Définit l'identifiant de l'université.
     * 
     * @param idUniv identifiant de l'université
     */
    public void setIdUniv(Long idUniv) {
        this.idUniv = idUniv;
    }

    /**
     * Retourne le nom de l'université.
     * 
     * @return nom de l'université
     */
    public String getNomUniv() {
        return nomUniv;
    }

    /**
     * Définit le nom de l'université.
     * 
     * @param nomUniv nom de l'université
     */
    public void setNomUniv(String nomUniv) {
        this.nomUniv = nomUniv;
    }

    /**
     * Retourne la liste des étudiants associés à cette université.
     * 
     * @return liste des étudiants
     */
    public List<Etudiant> getEtudiants() {
        return etudiants;
    }



    /**
     * Définit la liste des étudiants associés à cette université.
     * 
     * @param etudiants liste des étudiants
     */
    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    /**
     * Calcule le hash code de l'objet Universite.
     * 
     * @return le hash code basé sur l'id, le nom et les étudiants
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idUniv == null) ? 0 : idUniv.hashCode());
        result = prime * result + ((nomUniv == null) ? 0 : nomUniv.hashCode());
        result = prime * result + ((etudiants == null) ? 0 : etudiants.hashCode());
        return result;
    }

    /**
     * Compare cette université à un autre objet.
     * 
     * @param obj l'objet à comparer
     * @return true si les objets sont égaux, sinon false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Universite other = (Universite) obj;
        return Objects.equals(idUniv, other.idUniv)
            && Objects.equals(nomUniv, other.nomUniv)
            && Objects.equals(etudiants, other.etudiants);
    }
}