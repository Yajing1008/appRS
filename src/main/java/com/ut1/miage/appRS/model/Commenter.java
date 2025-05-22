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

    /**
     * Retourne l'identifiant unique du commentaire.
     *
     * @return l'identifiant du commentaire
     */
    public Long getIdCommentaire() {
        return idCommentaire;
    }

    /**
     * Définit l'identifiant unique du commentaire.
     *
     * @param idCommentaire l'identifiant à attribuer
     */
    public void setIdCommentaire(Long idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    /**
     * Retourne la publication associée à ce commentaire.
     *
     * @return l'objet Post associé
     */
    public Post getPost() {
        return post;
    }

    /**
     * Définit la publication associée à ce commentaire.
     *
     * @param post l'objet Post à associer
     */
    public void setPost(Post post) {
        this.post = post;
    }

    /**
     * Retourne l'étudiant ayant rédigé ce commentaire.
     *
     * @return l'objet Etudiant auteur du commentaire
     */
    public Etudiant getEtudiant() {
        return etudiant;
    }

    /**
     * Définit l'étudiant ayant rédigé ce commentaire.
     *
     * @param etudiant l'objet Etudiant à associer
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * Retourne le contenu textuel du commentaire.
     *
     * @return le texte du commentaire
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * Définit le contenu textuel du commentaire.
     *
     * @param commentaire le texte à enregistrer
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    /**
     * Retourne la date et l'heure de publication du commentaire.
     *
     * @return la date et l'heure du commentaire
     */
    public LocalDateTime getDateHeureCommentaire() {
        return dateHeureCommentaire;
    }

    /**
     * Définit la date et l'heure de publication du commentaire.
     *
     * @param dateHeureCommentaire la date et l'heure à enregistrer
     */
    public void setDateHeureCommentaire(LocalDateTime dateHeureCommentaire) {
        this.dateHeureCommentaire = dateHeureCommentaire;
    }

}