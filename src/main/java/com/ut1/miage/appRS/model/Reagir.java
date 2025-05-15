package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

/**
 * Entité représentant une réaction d’un étudiant sur un post (ex : like, dislike, etc.).
 * Utilise une clé composée (id_post + id_etudiant).
 */
@Entity
@Table(name = "REAGIR")
@IdClass(ReagirId.class)
public class Reagir {

    /** Le post concerné par la réaction. */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_post")
    private Post post;

    /** L'étudiant ayant réagi au post. */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;

    /** Type de réaction (ex : "Like", "Dislike", "Love", etc.). */
    @Column(nullable = false)
    private String statut;

    /**
     * Récupère le post concerné par la réaction.
     * @return le post réagi
     */
    public Post getPost() {
        return post;
    }

    /**
     * Définit le post concerné par la réaction.
     * @param post le post associé
     */
    public void setPost(Post post) {
        this.post = post;
    }

    /**
     * Récupère l'étudiant ayant effectué la réaction.
     * @return l'étudiant réactif
     */
    public Etudiant getEtudiant() {
        return etudiant;
    }

    /**
     * Définit l'étudiant ayant effectué la réaction.
     * @param etudiant l'étudiant réactif
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * Récupère le statut de la réaction (like, love, etc.).
     * @return le type de réaction
     */
    public String getStatut() {
        return statut;
    }

    /**
     * Définit le type de réaction.
     * @param statut le statut (type) de la réaction
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }
}