package com.ut1.miage.appRS.model;
import java.util.*;

import jakarta.persistence.*;

@Entity
public class CentreInteret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCentreInteret;
    @Column(name = "nom_centre_interet",nullable = false)
    private String nomCentreInteret;
    
    @ManyToMany
    @JoinTable(
        name = "AVOIR", 
        joinColumns = @JoinColumn(name = "nom_centre_interet"),
        inverseJoinColumns = @JoinColumn(name = "id_etudiant")
    )
    private List<Etudiant> etudiant = new ArrayList<>();

    public CentreInteret() {}

    public CentreInteret(Long idCentreInteret, String nomCentreInteret, List<Etudiant> etudiant) {
        this.idCentreInteret = idCentreInteret;
        this.nomCentreInteret = nomCentreInteret;
        this.etudiant = etudiant;
    }

    public Long getIdCentreInteret() {
        return idCentreInteret;
    }

    public void setIdCentreInteret(Long idCentreInteret) {
        this.idCentreInteret = idCentreInteret;
    }

    public String getNomCentreInteret() {
        return nomCentreInteret;
    }

    public void setNomCentreInteret(String nomCentreInteret) {
        this.nomCentreInteret = nomCentreInteret;
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
        result = prime * result + ((idCentreInteret == null) ? 0 : idCentreInteret.hashCode());
        result = prime * result + ((nomCentreInteret == null) ? 0 : nomCentreInteret.hashCode());
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
        if (idCentreInteret == null) {
            if (other.idCentreInteret != null)
                return false;
        } else if (!idCentreInteret.equals(other.idCentreInteret))
            return false;
        if (nomCentreInteret == null) {
            if (other.nomCentreInteret != null)
                return false;
        } else if (!nomCentreInteret.equals(other.nomCentreInteret))
            return false;
        if (etudiant == null) {
            if (other.etudiant != null)
                return false;
        } else if (!etudiant.equals(other.etudiant))
            return false;
        return true;
    }

    
    
}
