package com.ut1.miage.appRS.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * Représente un commentaire posté par un étudiant sur un post.
 * Utilise une clé composite via la classe {@link CommenterId}.
 */
@Entity
@Table(name = "COMMENTER")
@IdClass(CommenterId.class)
public class Commenter {

    /**
     * Post sur lequel le commentaire a été fait.
     * Fait partie de la clé composite.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_post")
    private Post post;

    /**
     * Étudiant ayant rédigé le commentaire.
     * Fait partie de la clé composite.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;

    /** Contenu textuel du commentaire. */
    @Column(nullable = false)
    private String commentaire;

    /** Date et heure du commentaire. */
    @Column(name = "dateheure_commentaire", nullable = false)
    private LocalDateTime dateHeureCommentaire;

    /**
     * @return Date et heure du commentaire.
     */
    public LocalDateTime getDateHeureCommentaire() {
        return dateHeureCommentaire;
    }

    /**
     * Définit la date et l’heure du commentaire.
     * 
     * @param dateHeureCommentaire La date et heure à définir.
     */
    public void setDateHeureCommentaire(LocalDateTime dateHeureCommentaire) {
        this.dateHeureCommentaire = dateHeureCommentaire;
    }

    /**
     * @return Le post concerné par le commentaire.
     */
    public Post getPost() {
        return post;
    }

    /**
     * Définit le post commenté.
     * 
     * @param post Le post sur lequel porte le commentaire.
     */
    public void setPost(Post post) {
        this.post = post;
    }

    /**
     * @return L’étudiant ayant commenté.
     */
    public Etudiant getEtudiant() {
        return etudiant;
    }

    /**
     * Définit l’étudiant ayant commenté.
     * 
     * @param etudiant L’auteur du commentaire.
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * @return Le contenu du commentaire.
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * Définit le contenu du commentaire.
     * 
     * @param commentaire Texte du commentaire.
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}