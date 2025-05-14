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
    private Long IdUniv;
    @Column(name = "Nom_univ",nullable = false)
    private String NomUniv;
    
    @ManyToMany
    @JoinTable(
        name = "ETUDIER", 
        joinColumns = @JoinColumn(name = "Id_univ"),
        inverseJoinColumns = @JoinColumn(name = "Id_utudiant")
    )
    private List<Etudiant> etudiant = new ArrayList<>();

    public Universite() {}

    public Universite(Long idUniv, String nomUniv, List<Etudiant> etudiant) {
        IdUniv = idUniv;
        NomUniv = nomUniv;
        this.etudiant = etudiant;
    }

    public Long getIdUniv() {
        return IdUniv;
    }

    public void setIdUniv(Long idUniv) {
        IdUniv = idUniv;
    }

    public String getNomUniv() {
        return NomUniv;
    }

    public void setNomUniv(String nomUniv) {
        NomUniv = nomUniv;
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
        result = prime * result + ((IdUniv == null) ? 0 : IdUniv.hashCode());
        result = prime * result + ((NomUniv == null) ? 0 : NomUniv.hashCode());
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
        if (IdUniv == null) {
            if (other.IdUniv != null)
                return false;
        } else if (!IdUniv.equals(other.IdUniv))
            return false;
        if (NomUniv == null) {
            if (other.NomUniv != null)
                return false;
        } else if (!NomUniv.equals(other.NomUniv))
            return false;
        if (etudiant == null) {
            if (other.etudiant != null)
                return false;
        } else if (!etudiant.equals(other.etudiant))
            return false;
        return true;
    }


    
    
}
