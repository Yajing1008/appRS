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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "POSTS")
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

    @OneToMany(mappedBy = "post")
    private List<Commenter> commentaires = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Reagir> reactions = new ArrayList<>();

    public List<Reagir> getReactions() {
        return reactions;
    }

    public List<Commenter> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commenter> commentaires) {
        this.commentaires = commentaires;
    }

    public void setReactions(List<Reagir> reactions) {
        this.reactions = reactions;
    }

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
