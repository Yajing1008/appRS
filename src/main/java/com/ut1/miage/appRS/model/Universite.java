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
    private Long Id_Univ;
    @Column(name = "Nom_Univ",nullable = false)
    private String Nom_Univ;
    
    @ManyToMany
    @JoinTable(
        name = "ETUDIER", 
        joinColumns = @JoinColumn(name = "Id_Univ"),
        inverseJoinColumns = @JoinColumn(name = "Id_Etudiant")
    )
    private List<Etudiant> etudiant = new ArrayList<>();

    public Universite() {}

    public Universite(Long id_Univ, String nom_Univ, List<Etudiant> etudiant) {
        Id_Univ = id_Univ;
        Nom_Univ = nom_Univ;
        this.etudiant = etudiant;
    }

    public Long getId_Univ() {
        return Id_Univ;
    }

    public void setId_Univ(Long id_Univ) {
        Id_Univ = id_Univ;
    }

    public String getNom_Univ() {
        return Nom_Univ;
    }

    public void setNom_Univ(String nom_Univ) {
        Nom_Univ = nom_Univ;
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
        result = prime * result + ((Id_Univ == null) ? 0 : Id_Univ.hashCode());
        result = prime * result + ((Nom_Univ == null) ? 0 : Nom_Univ.hashCode());
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
        if (Id_Univ == null) {
            if (other.Id_Univ != null)
                return false;
        } else if (!Id_Univ.equals(other.Id_Univ))
            return false;
        if (Nom_Univ == null) {
            if (other.Nom_Univ != null)
                return false;
        } else if (!Nom_Univ.equals(other.Nom_Univ))
            return false;
        if (etudiant == null) {
            if (other.etudiant != null)
                return false;
        } else if (!etudiant.equals(other.etudiant))
            return false;
        return true;
    }

    
}
