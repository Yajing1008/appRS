package com.ut1.miage.appRS.model;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id_Evenement;
    @Column(name = "DateHeure_Evenement",nullable = false)
    private LocalDateTime DateHeure_Evenement;
    @Column(name = "Nom_Evenement",nullable = false)
    private String Nom_Evenement;

    @ManyToOne
    @JoinColumn(name = "Id_Etudiant")
    private Etudiant createur;

    @ManyToMany
    @JoinTable(
        name = "PRENDRE_PART", 
        joinColumns = @JoinColumn(name = "Id_Evenement"),
        inverseJoinColumns = @JoinColumn(name = "Id_Etudiant")
    )
    private List<Etudiant> MembreGroupe = new ArrayList<>();

    // 构造方法
    public Evenement() {}
    

    public Evenement(Long id_Evenement, LocalDateTime dateHeure_Evenement, String nom_Evenement,
            List<Etudiant> etudiant) {
        Id_Evenement = id_Evenement;
        DateHeure_Evenement = dateHeure_Evenement;
        Nom_Evenement = nom_Evenement;
        this.etudiant = etudiant;
    }


    public Long getId_Evenement() {
        return Id_Evenement;
    }

    public void setId_Evenement(Long id_Evenement) {
        Id_Evenement = id_Evenement;
    }

    public LocalDateTime getDateHeure_Evenement() {
        return DateHeure_Evenement;
    }

    public void setDateHeure_Evenement(LocalDateTime dateHeure_Evenement) {
        DateHeure_Evenement = dateHeure_Evenement;
    }

    public String getNom_Evenement() {
        return Nom_Evenement;
    }

    public void setNom_Evenement(String nom_Evenement) {
        Nom_Evenement = nom_Evenement;
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
        result = prime * result + ((Id_Evenement == null) ? 0 : Id_Evenement.hashCode());
        result = prime * result + ((DateHeure_Evenement == null) ? 0 : DateHeure_Evenement.hashCode());
        result = prime * result + ((Nom_Evenement == null) ? 0 : Nom_Evenement.hashCode());
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
        Evenement other = (Evenement) obj;
        if (Id_Evenement == null) {
            if (other.Id_Evenement != null)
                return false;
        } else if (!Id_Evenement.equals(other.Id_Evenement))
            return false;
        if (DateHeure_Evenement == null) {
            if (other.DateHeure_Evenement != null)
                return false;
        } else if (!DateHeure_Evenement.equals(other.DateHeure_Evenement))
            return false;
        if (Nom_Evenement == null) {
            if (other.Nom_Evenement != null)
                return false;
        } else if (!Nom_Evenement.equals(other.Nom_Evenement))
            return false;
        if (etudiant == null) {
            if (other.etudiant != null)
                return false;
        } else if (!etudiant.equals(other.etudiant))
            return false;
        return true;
    }

    

    
}
