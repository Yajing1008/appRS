package com.ut1.miage.appRS.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "PARTICIPER")
public class Participer {

    @EmbeddedId
    private ParticiperId id = new ParticiperId();

    @ManyToOne
    @MapsId("idEtudiant")
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;

    @ManyToOne
    @MapsId("idGroupe")
    @JoinColumn(name = "id_groupe")
    private Groupe groupe;

    private String role;

    public ParticiperId getId() {
        return id;
    }

    public void setId(ParticiperId id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
