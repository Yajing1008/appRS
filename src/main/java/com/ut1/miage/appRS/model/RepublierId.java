package com.ut1.miage.appRS.model;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
/**
 * Classe représentant la clé composite pour l'entité Republier,
 * composée de l'identifiant du post et de l'étudiant.
 */
@Embeddable
public class RepublierId implements Serializable {

    /** Identifiant du post republicé. */
    @Column(name = "id_post")
    private Long postId;

    /** Identifiant de l'étudiant ayant republicé le post. */
    @Column(name = "id_etudiant")
    private Long etudiantId;

    /**
     * Constructeur par défaut.
     */
    public RepublierId() {
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param postId l'identifiant du post
     * @param etudiantId l'identifiant de l'étudiant
     */
    public RepublierId(Long postId, Long etudiantId) {
        this.postId = postId;
        this.etudiantId = etudiantId;
    }

    /**
     * Retourne l'identifiant du post.
     *
     * @return l'identifiant du post
     */
    public Long getPostId() {
        return postId;
    }

    /**
     * Définit l'identifiant du post.
     *
     * @param postId l'identifiant du post
     */
    public void setPostId(Long postId) {
        this.postId = postId;
    }

    /**
     * Retourne l'identifiant de l'étudiant.
     *
     * @return l'identifiant de l'étudiant
     */
    public Long getEtudiantId() {
        return etudiantId;
    }

    /**
     * Définit l'identifiant de l'étudiant.
     *
     * @param etudiantId l'identifiant de l'étudiant
     */
    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    /**
     * Compare deux objets RepublierId pour vérifier leur égalité.
     *
     * @param o l'objet à comparer
     * @return true si les deux objets sont égaux, sinon false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RepublierId)) return false;
        RepublierId that = (RepublierId) o;
        return Objects.equals(postId, that.postId) && Objects.equals(etudiantId, that.etudiantId);
    }

    /**
     * Calcule le hashcode basé sur les champs postId et etudiantId.
     *
     * @return le hashcode de cet objet
     */
    @Override
    public int hashCode() {
        return Objects.hash(postId, etudiantId);
    }

}

