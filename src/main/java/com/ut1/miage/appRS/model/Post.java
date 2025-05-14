package com.ut1.miage.appRS.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;
    private String contenuPost;
    private boolean estPublicPost;

    @ManyToOne
    @JoinColumn(name = "id_etudiant_publier", nullable = false)
    private Etudiant etudiant;

    @ManyToMany
    @JoinTable(
    name = "republier",
    joinColumns = @JoinColumn(name = "id_post"),
    inverseJoinColumns = @JoinColumn(name = "id_etudiant")
    )
    private List<Etudiant> republications = new ArrayList<>();

    public Etudiant getEtudiant() {
        return etudiant;
    }
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }
    public List<Etudiant> getRepublications() {
        return republications;
    }
    public void setRepublications(List<Etudiant> republications) {
        this.republications = republications;
    }
    public Long getIdPost() {
        return idPost;
    }
    public boolean isEstPublicPost() {
        return estPublicPost;
    }
    public void setEstPublicPost(boolean estPublicPost) {
        this.estPublicPost = estPublicPost;
    }
    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }
    public String getContenuPost() {
        return contenuPost;
    }
    public void setContenuPost(String contenuPost) {
        this.contenuPost = contenuPost;
    }
    
}
