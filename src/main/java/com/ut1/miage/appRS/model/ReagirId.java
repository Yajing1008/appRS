package com.ut1.miage.appRS.model;

import java.io.Serializable;

/**
 * Classe représentant la clé composite pour l'entité {@link Reagir}.
 * Composée des identifiants d'un étudiant et d'un post.
 */
public class ReagirId implements Serializable {

    /** Identifiant de l'étudiant ayant réagi. */
    private Long etudiant;

    /** Identifiant du post concerné. */
    private Long post;

    /** Constructeur par défaut requis pour JPA. */
    public ReagirId() {}

    /**
     * Constructeur avec tous les champs.
     * 
     * @param etudiant identifiant de l'étudiant
     * @param post identifiant du post
     */
    public ReagirId(Long etudiant, Long post) {
        this.etudiant = etudiant;
        this.post = post;
    }

    /**
     * Retourne l'identifiant de l'étudiant.
     * 
     * @return identifiant de l'étudiant
     */
    public Long getEtudiant() {
        return etudiant;
    }

    /**
     * Définit l'identifiant de l'étudiant.
     * 
     * @param etudiant identifiant de l'étudiant
     */
    public void setEtudiant(Long etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * Retourne l'identifiant du post.
     * 
     * @return identifiant du post
     */
    public Long getPost() {
        return post;
    }

    /**
     * Définit l'identifiant du post.
     * 
     * @param post identifiant du post
     */
    public void setPost(Long post) {
        this.post = post;
    }

    /**
     * Compare deux objets ReagirId pour vérifier s'ils sont égaux.
     * 
     * @param obj l'objet à comparer
     * @return true si les deux objets sont égaux, sinon false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ReagirId other = (ReagirId) obj;
        return (etudiant != null && etudiant.equals(other.etudiant)) &&
               (post != null && post.equals(other.post));
    }

    /**
     * Calcule le hash code de la clé composite.
     * 
     * @return le hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((etudiant == null) ? 0 : etudiant.hashCode());
        result = prime * result + ((post == null) ? 0 : post.hashCode());
        return result;
    }
}