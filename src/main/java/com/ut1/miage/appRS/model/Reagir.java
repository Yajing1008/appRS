package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

/**
 * Entité représentant une réaction d’un étudiant sur un post (ex : like, dislike, etc.).
 * Utilise une clé composée (id_post + id_etudiant).
 */
@Entity
@Table(name = "REAGIR")
public class Reagir {
    /**
     * Identifiant composé de la réaction, combinant l'étudiant, le post et le statut de réaction.
     * Utilisé comme clé primaire embarquée.
     */
    @EmbeddedId
    private ReagirId reagirId = new ReagirId();

    /** Le post concerné par la réaction. */

    @ManyToOne
    @MapsId("idPost")
    @JoinColumn(name = "id_post")
    private Post post;

    /** L'étudiant ayant réagi au post. */

    @ManyToOne
    @MapsId("idEtudiant")
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;





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
     * Retourne la clé composée de la réaction.
     *
     * @return l'identifiant composite de la réaction
     */
    public ReagirId getReagirId() {
        return reagirId;
    }
    /**
     * Définit la clé composée de la réaction.
     *
     * @param reagirId l'identifiant composite à définir
     */
    public void setReagirId(ReagirId reagirId) {
        this.reagirId = reagirId;
    }


}