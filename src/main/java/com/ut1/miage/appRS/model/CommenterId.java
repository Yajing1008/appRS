package com.ut1.miage.appRS.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clé composite représentant l’identifiant d’un commentaire.
 * Utilisée pour la relation entre {@link Etudiant} et {@link Post}
 * dans l’entité {@link Commenter}.
 */
public class CommenterId implements Serializable {

    /**
     * Identifiant de l’étudiant ayant commenté.
     */
    private Long etudiant;

    /**
     * Identifiant du post commenté.
     */
    private Long post;

    /**
     * Constructeur par défaut requis pour JPA.
     */
    public CommenterId() {}

    /**
     * Constructeur avec tous les attributs.
     *
     * @param etudiant identifiant de l’étudiant
     * @param post identifiant du post
     */
    public CommenterId(Long etudiant, Long post) {
        this.etudiant = etudiant;
        this.post = post;
    }

    /**
     * @return l’identifiant de l’étudiant
     */
    public Long getEtudiant() {
        return etudiant;
    }

    /**
     * Définit l’identifiant de l’étudiant.
     * @param etudiant identifiant de l’étudiant
     */
    public void setEtudiant(Long etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * @return l’identifiant du post
     */
    public Long getPost() {
        return post;
    }

    /**
     * Définit l’identifiant du post.
     * @param post identifiant du post
     */
    public void setPost(Long post) {
        this.post = post;
    }

    /**
     * Détermine si deux objets CommenterId sont égaux.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        CommenterId other = (CommenterId) obj;
        return Objects.equals(etudiant, other.etudiant) &&
            Objects.equals(post, other.post);
    }



    /**
     * Génère le hashcode basé sur les champs etudiant et post.
     */
    @Override
    public int hashCode() {
        return Objects.hash(etudiant, post);
    }
}