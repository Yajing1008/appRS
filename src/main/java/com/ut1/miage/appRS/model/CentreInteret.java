package com.ut1.miage.appRS.model;
import java.util.*;

import jakarta.persistence.*;

@Entity
public class CentreInteret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdCentreInteret;
    @Column(name = "NomCentreInteret",nullable = false)
    private String NomCentreInteret;
    
    @ManyToMany
    @JoinTable(
        name = "AVOIR", 
        joinColumns = @JoinColumn(name = "IdCentreInteret"),
        inverseJoinColumns = @JoinColumn(name = "IdEtudiant")
    )
    private List<Etudiant> etudiant = new ArrayList<>();

    public CentreInteret() {}

    public CentreInteret(Long idCentreInteret, String nomCentreInteret, List<Etudiant> etudiant) {
        IdCentreInteret = idCentreInteret;
        NomCentreInteret = nomCentreInteret;
        this.etudiant = etudiant;
    }

    public Long getIdCentreInteret() {
        return IdCentreInteret;
    }

    public void setIdCentreInteret(Long idCentreInteret) {
        IdCentreInteret = idCentreInteret;
    }

    public String getNomCentreInteret() {
        return NomCentreInteret;
    }

    public void setNomCentreInteret(String nomCentreInteret) {
        NomCentreInteret = nomCentreInteret;
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
        result = prime * result + ((IdCentreInteret == null) ? 0 : IdCentreInteret.hashCode());
        result = prime * result + ((NomCentreInteret == null) ? 0 : NomCentreInteret.hashCode());
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
        if (IdCentreInteret == null) {
            if (other.IdCentreInteret != null)
                return false;
        } else if (!IdCentreInteret.equals(other.IdCentreInteret))
            return false;
        if (NomCentreInteret == null) {
            if (other.NomCentreInteret != null)
                return false;
        } else if (!NomCentreInteret.equals(other.NomCentreInteret))
            return false;
        if (etudiant == null) {
            if (other.etudiant != null)
                return false;
        } else if (!etudiant.equals(other.etudiant))
            return false;
        return true;
    }

    
}
