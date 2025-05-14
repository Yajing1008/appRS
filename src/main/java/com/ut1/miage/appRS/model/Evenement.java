package com.ut1.miage.appRS.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdEvenement;
    @Column(name = "DateHeureEvenement",nullable = false)
    private LocalDateTime DateHeureEvenement;
    @Column(name = "NomEvenement",nullable = false)
    private String NomEvenement;

    @ManyToOne
    @JoinColumn(name = "Id_etudiant")
    private Etudiant createur;

    @ManyToMany
    @JoinTable(
        name = "PRENDRE_PART", 
        joinColumns = @JoinColumn(name = "Id_evenement"),
        inverseJoinColumns = @JoinColumn(name = "Id_etudiant")
    )
    private List<Etudiant> MembreGroupe = new ArrayList<>();

    
    public Evenement() {}


    public Evenement(Long idEvenement, LocalDateTime dateHeureEvenement, String nomEvenement, Etudiant createur,
            List<Etudiant> membreGroupe) {
        IdEvenement = idEvenement;
        DateHeureEvenement = dateHeureEvenement;
        NomEvenement = nomEvenement;
        this.createur = createur;
        MembreGroupe = membreGroupe;
    }


    public Long getIdEvenement() {
        return IdEvenement;
    }


    public void setIdEvenement(Long idEvenement) {
        IdEvenement = idEvenement;
    }


    public LocalDateTime getDateHeureEvenement() {
        return DateHeureEvenement;
    }


    public void setDateHeureEvenement(LocalDateTime dateHeureEvenement) {
        DateHeureEvenement = dateHeureEvenement;
    }


    public String getNomEvenement() {
        return NomEvenement;
    }


    public void setNomEvenement(String nomEvenement) {
        NomEvenement = nomEvenement;
    }


    public Etudiant getCreateur() {
        return createur;
    }


    public void setCreateur(Etudiant createur) {
        this.createur = createur;
    }


    public List<Etudiant> getMembreGroupe() {
        return MembreGroupe;
    }


    public void setMembreGroupe(List<Etudiant> membreGroupe) {
        MembreGroupe = membreGroupe;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((IdEvenement == null) ? 0 : IdEvenement.hashCode());
        result = prime * result + ((DateHeureEvenement == null) ? 0 : DateHeureEvenement.hashCode());
        result = prime * result + ((NomEvenement == null) ? 0 : NomEvenement.hashCode());
        result = prime * result + ((createur == null) ? 0 : createur.hashCode());
        result = prime * result + ((MembreGroupe == null) ? 0 : MembreGroupe.hashCode());
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
        Evenement other = (Evenement) obj;
        if (IdEvenement == null) {
            if (other.IdEvenement != null)
                return false;
        } else if (!IdEvenement.equals(other.IdEvenement))
            return false;
        if (DateHeureEvenement == null) {
            if (other.DateHeureEvenement != null)
                return false;
        } else if (!DateHeureEvenement.equals(other.DateHeureEvenement))
            return false;
        if (NomEvenement == null) {
            if (other.NomEvenement != null)
                return false;
        } else if (!NomEvenement.equals(other.NomEvenement))
            return false;
        if (createur == null) {
            if (other.createur != null)
                return false;
        } else if (!createur.equals(other.createur))
            return false;
        if (MembreGroupe == null) {
            if (other.MembreGroupe != null)
                return false;
        } else if (!MembreGroupe.equals(other.MembreGroupe))
            return false;
        return true;
    }
    

    

    
}
