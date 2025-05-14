package com.ut1.miage.appRS.model;
import java.util.*;

import jakarta.persistence.*;

@Entity
public class CentreInteret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id_CentreInteret;
    @Column(name = "Nom_CentreInteret",nullable = false)
    private String Nom_CentreInteret;
    
    @ManyToMany
    @JoinTable(
        name = "AVOIR", 
        joinColumns = @JoinColumn(name = "Id_CentreInteret"),
        inverseJoinColumns = @JoinColumn(name = "Id_Etudiant")
    )
    private List<Etudiant> etudiant = new ArrayList<>();

    public CentreInteret() {}

    public CentreInteret(Long id_CentreInteret, String nom_CentreInteret, List<Etudiant> etudiant) {
        Id_CentreInteret = id_CentreInteret;
        Nom_CentreInteret = nom_CentreInteret;
        this.etudiant = etudiant;
    }

    public Long getId_CentreInteret() {
        return Id_CentreInteret;
    }

    public void setId_CentreInteret(Long id_CentreInteret) {
        Id_CentreInteret = id_CentreInteret;
    }

    public String getNom_CentreInteret() {
        return Nom_CentreInteret;
    }

    public void setNom_CentreInteret(String nom_CentreInteret) {
        Nom_CentreInteret = nom_CentreInteret;
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
        result = prime * result + ((Id_CentreInteret == null) ? 0 : Id_CentreInteret.hashCode());
        result = prime * result + ((Nom_CentreInteret == null) ? 0 : Nom_CentreInteret.hashCode());
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
        CentreInteret other = (CentreInteret) obj;
        if (Id_CentreInteret == null) {
            if (other.Id_CentreInteret != null)
                return false;
        } else if (!Id_CentreInteret.equals(other.Id_CentreInteret))
            return false;
        if (Nom_CentreInteret == null) {
            if (other.Nom_CentreInteret != null)
                return false;
        } else if (!Nom_CentreInteret.equals(other.Nom_CentreInteret))
            return false;
        if (etudiant == null) {
            if (other.etudiant != null)
                return false;
        } else if (!etudiant.equals(other.etudiant))
            return false;
        return true;
    }

    
}
