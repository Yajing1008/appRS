package com.ut1.miage.appRS.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Représente un commentaire posté par un étudiant sur un post.
 */
@Entity
@Table(name = "COMMENTER")
public class Commenter {

    /** Identifiant unique du commentaire. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCommentaire;

    /** Post sur lequel le commentaire a été fait. */
    @ManyToOne
    @JoinColumn(name = "id_post", nullable = false)
    private Post post;

    /** Étudiant ayant rédigé le commentaire. */
    @ManyToOne
    @JoinColumn(name = "id_etudiant", nullable = false)
    private Etudiant etudiant;

    /** Contenu textuel du commentaire. */
    @Column(nullable = false)
    private String commentaire;

    /** Date et heure du commentaire. */
    @Column(name = "dateheure_commentaire", nullable = false)
    private LocalDateTime dateHeureCommentaire;

    // Getters et setters

    public Long getIdCommentaire() {
        return idCommentaire;
    }

    public void setIdCommentaire(Long idCommentaire) {
        this.idCommentaire = idCommentaire;
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

    public LocalDateTime getDateHeureCommentaire() {
        return dateHeureCommentaire;
    }

    public void setDateHeureCommentaire(LocalDateTime dateHeureCommentaire) {
        this.dateHeureCommentaire = dateHeureCommentaire;
    }
}