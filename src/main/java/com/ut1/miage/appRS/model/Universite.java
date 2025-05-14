package com.ut1.miage.appRS.model;

import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Universite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUniv;
    @Column(name = "nom_univ",nullable = false)
    private String nomUniv;
    
    @ManyToMany
    @JoinTable(
        name = "ETUDIER", 
        joinColumns = @JoinColumn(name = "id_univ"),
        inverseJoinColumns = @JoinColumn(name = "id_etudiant")
    )
    private List<Etudiant> etudiant = new ArrayList<>();

    public Universite() {}

    public Universite(Long idUniv, String nomUniv, List<Etudiant> etudiant) {
        this.idUniv = idUniv;
        this.nomUniv = nomUniv;
        this.etudiant = etudiant;
    }

    public Long getIdUniv() {
        return idUniv;
    }

    public void setIdUniv(Long idUniv) {
        this.idUniv = idUniv;
    }

    public String getNomUniv() {
        return nomUniv;
    }

    public void setNomUniv(String nomUniv) {
        this.nomUniv = nomUniv;
    }

    public List<Etudiant> getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(List<Etudiant> etudiant) {
        this.etudiant = etudiant;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idUniv == null) ? 0 : idUniv.hashCode());
        result = prime * result + ((nomUniv == null) ? 0 : nomUniv.hashCode());
        result = prime * result + ((etudiant == null) ? 0 : etudiant.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Universite other = (Universite) obj;
        if (idUniv == null) {
            if (other.idUniv != null)
                return false;
        } else if (!idUniv.equals(other.idUniv))
            return false;
        if (nomUniv == null) {
            if (other.nomUniv != null)
                return false;
        } else if (!nomUniv.equals(other.nomUniv))
            return false;
        if (etudiant == null) {
            if (other.etudiant != null)
                return false;
        } else if (!etudiant.equals(other.etudiant))
            return false;
        return true;
    }

   
    
    
    
}
