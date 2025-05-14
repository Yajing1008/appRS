package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

@Entity
@Table(name = "REAGIR")
@IdClass(ReagirId.class)
public class Reagir {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_post")
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;

    @Column(nullable = false)
    private String statut;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
