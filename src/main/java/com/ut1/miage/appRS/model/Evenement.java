package com.ut1.miage.appRS.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvenement;

    @Column(name = "dateHeureEvenement", nullable = false)
    private LocalDateTime dateHeureEvenement;

    @Column(name = "nomEvenement", nullable = false)
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
        this.idEvenement = idEvenement;
        this.dateHeureEvenement = dateHeureEvenement;
        this.nomEvenement = nomEvenement;
        this.createur = createur;
        this.membreGroupe = membreGroupe;
    }

    public Long getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(Long idEvenement) {
        this.idEvenement = idEvenement;
    }

    public LocalDateTime getDateHeureEvenement() {
        return dateHeureEvenement;
    }

    public void setDateHeureEvenement(LocalDateTime dateHeureEvenement) {
        this.dateHeureEvenement = dateHeureEvenement;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }

    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement;
    }

    public Etudiant getCreateur() {
        return createur;
    }

    public void setCreateur(Etudiant createur) {
        this.createur = createur;
    }

    public List<Etudiant> getMembreGroupe() {
        return membreGroupe;
    }

    public void setMembreGroupe(List<Etudiant> membreGroupe) {
        this.membreGroupe = membreGroupe;
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
        if (obj == null || getClass() != obj.getClass())
            return false;

        Evenement other = (Evenement) obj;

        return Objects.equals(idEvenement, other.idEvenement)
            && Objects.equals(dateHeureEvenement, other.dateHeureEvenement)
            && Objects.equals(nomEvenement, other.nomEvenement)
            && Objects.equals(createur, other.createur)
            && Objects.equals(membreGroupe, other.membreGroupe);
    }
}