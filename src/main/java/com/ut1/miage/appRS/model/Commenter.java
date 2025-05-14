package com.ut1.miage.appRS.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "COMMENTER")
@IdClass(CommenterId.class)
public class Commenter {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_post")
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;

    @Column(nullable = false)
    private String commentaire;

    @Column(name = "dateheure_commentaire", nullable = false)
    private LocalDateTime dateHeureCommentaire;

    public LocalDateTime getDateHeureCommentaire() {
        return dateHeureCommentaire;
    }

    public void setDateHeureCommentaire(LocalDateTime dateHeureCommentaire) {
        this.dateHeureCommentaire = dateHeureCommentaire;
    }

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

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

}

