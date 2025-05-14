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
    private Long idEvenement;
    @Column(name = "dateHeureEvenement",nullable = false)
    private LocalDateTime dateHeureEvenement;
    @Column(name = "nomEvenement",nullable = false)
    private String nomEvenement;

    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant createur;

    @ManyToMany
    @JoinTable(
        name = "PRENDRE_PART", 
        joinColumns = @JoinColumn(name = "id_evenement"),
        inverseJoinColumns = @JoinColumn(name = "id_etudiant")
    )
    private List<Etudiant> membreGroupe = new ArrayList<>();

    
    public Evenement() {}


    public Evenement(Long idEvenement, LocalDateTime dateHeureEvenement, String nomEvenement, Etudiant createur,
            List<Etudiant> membreGroupe) {
        idEvenement = idEvenement;
        dateHeureEvenement = dateHeureEvenement;
        nomEvenement = nomEvenement;
        this.createur = createur;
        membreGroupe = membreGroupe;
    }


    public Long getidEvenement() {
        return idEvenement;
    }


    public void setidEvenement(Long idEvenement) {
        idEvenement = idEvenement;
    }


    public LocalDateTime getdateHeureEvenement() {
        return dateHeureEvenement;
    }


    public void setdateHeureEvenement(LocalDateTime dateHeureEvenement) {
        dateHeureEvenement = dateHeureEvenement;
    }


    public String getnomEvenement() {
        return nomEvenement;
    }


    public void setnomEvenement(String nomEvenement) {
        nomEvenement = nomEvenement;
    }


    public Etudiant getCreateur() {
        return createur;
    }


    public void setCreateur(Etudiant createur) {
        this.createur = createur;
    }


    public List<Etudiant> getmembreGroupe() {
        return membreGroupe;
    }


    public void setmembreGroupe(List<Etudiant> membreGroupe) {
        membreGroupe = membreGroupe;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idEvenement == null) ? 0 : idEvenement.hashCode());
        result = prime * result + ((dateHeureEvenement == null) ? 0 : dateHeureEvenement.hashCode());
        result = prime * result + ((nomEvenement == null) ? 0 : nomEvenement.hashCode());
        result = prime * result + ((createur == null) ? 0 : createur.hashCode());
        result = prime * result + ((membreGroupe == null) ? 0 : membreGroupe.hashCode());
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
        if (idEvenement == null) {
            if (other.idEvenement != null)
                return false;
        } else if (!idEvenement.equals(other.idEvenement))
            return false;
        if (dateHeureEvenement == null) {
            if (other.dateHeureEvenement != null)
                return false;
        } else if (!dateHeureEvenement.equals(other.dateHeureEvenement))
            return false;
        if (nomEvenement == null) {
            if (other.nomEvenement != null)
                return false;
        } else if (!nomEvenement.equals(other.nomEvenement))
            return false;
        if (createur == null) {
            if (other.createur != null)
                return false;
        } else if (!createur.equals(other.createur))
            return false;
        if (membreGroupe == null) {
            if (other.membreGroupe != null)
                return false;
        } else if (!membreGroupe.equals(other.membreGroupe))
            return false;
        return true;
    }
    

    

    
}
