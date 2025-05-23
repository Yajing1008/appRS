package com.ut1.miage.appRS.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe représentant la clé composite pour l'entité {@link Reagir}.
 * Composée des identifiants d'un étudiant et d'un post.
 */
@Embeddable
public class ReagirId implements Serializable {

    /** Identifiant de l'étudiant ayant réagi. */
    private Long idEtudiant;

    /** Identifiant du post concerné. */
    private Long idPost;

    private String statut;

    /** Constructeur par défaut requis pour JPA. */
    public ReagirId() {}

    /**
     * Construit une clé composée avec l'identifiant de l'étudiant et l'identifiant du post.
     *
     * @param etudiant l'identifiant de l'étudiant
     * @param post l'identifiant du post
     */
    public ReagirId(Long etudiant, Long post) {
        this.idEtudiant = etudiant;
        this.idPost = post;
    }
    /**
     * Constructeur avec tous les champs.
     * 
     * @param etudiant identifiant de l'étudiant
     * @param post identifiant du post
     */
    public ReagirId(Long etudiant, Long post, String statut) {
        this.idEtudiant = etudiant;
        this.idPost = post;
        this.statut = statut;
    }

    /**
     * Retourne l'identifiant de l'étudiant.
     * 
     * @return identifiant de l'étudiant
     */
    public Long getEtudiant() {
        return idEtudiant;
    }

    /**
     * Définit l'identifiant de l'étudiant.
     * 
     * @param etudiant identifiant de l'étudiant
     */
    public void setEtudiant(Long etudiant) {
        this.idEtudiant = etudiant;
    }

    /**
     * Retourne l'identifiant du post.
     * 
     * @return identifiant du post
     */
    public Long getPost() {
        return idPost;
    }

    /**
     * Définit l'identifiant du post.
     * 
     * @param post identifiant du post
     */
    public void setPost(Long post) {
        this.idPost = post;
    }
    /**
     * Retourne le statut de la réaction (par exemple : "Like", "Favori").
     *
     * @return le statut de la réaction
     */
    public String getStatut() {
        return statut;
    }
    /**
     * Définit le statut de la réaction (par exemple : "Like", "Favori").
     *
     * @param statut le statut de la réaction à définir
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReagirId)) return false;
        ReagirId that = (ReagirId) o;
        return Objects.equals(idEtudiant, that.idEtudiant) &&
                Objects.equals(idPost, that.idPost) &&
                Objects.equals(statut, that.statut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEtudiant, idPost, statut);
    }
}