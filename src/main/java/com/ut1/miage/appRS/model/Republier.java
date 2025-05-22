package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
/**
 * Entité représentant la republication d'un post par un étudiant.
 * Contient des informations sur la date, le commentaire et la visibilité de la republication.
 */
@Entity
@Table(name = "REPUBLIER")
public class Republier {
    /** Clé primaire composée de l'étudiant et du post. */
    @EmbeddedId
    private RepublierId id = new RepublierId();

    /** Le post qui est republicé. */
    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "id_post")
    private Post post;

    /** L'étudiant qui republie le post. */
    @ManyToOne
    @MapsId("etudiantId")
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;

    /** Date et heure de la republication. */
    @Column(name = "date_republication", nullable = false)
    private LocalDateTime dateRepublication = LocalDateTime.now();

    /** Commentaire ajouté lors de la republication (facultatif). */
    @Column(name = "commentaire_republication")
    private String commentaireRepublication;

    /** Indique si la republication est publique ou non. */
    @Column(name = "est_public")
    private boolean estPublic;

    /** Constructeur par défaut. */
    public Republier() {

    }

    /**
     * Constructeur avec paramètres.
     *
     * @param id l'identifiant composite
     * @param post le post republicé
     * @param etudiant l'étudiant qui republie
     * @param dateRepublication la date de republication
     * @param estPublic visibilité de la republication
     * @param commentaireRepublication commentaire éventuel
     */
    public Republier(RepublierId id, Post post, Etudiant etudiant, LocalDateTime dateRepublication, boolean estPublic, String commentaireRepublication) {
        this.id = id;
        this.post = post;
        this.etudiant = etudiant;
        this.dateRepublication = dateRepublication;
        this.estPublic = estPublic;
        this.commentaireRepublication = commentaireRepublication;
    }

    /**
     * Retourne l'identifiant composite.
     *
     * @return l'identifiant composite
     */
    public RepublierId getId() {
        return id;
    }

    /**
     * Définit l'identifiant composite.
     *
     * @param id l'identifiant composite à définir
     */
    public void setId(RepublierId id) {
        this.id = id;
    }

    /**
     * Retourne le post republicé.
     *
     * @return le post republicé
     */
    public Post getPost() {
        return post;
    }

    /**
     * Définit le post à republication.
     *
     * @param post le post à définir
     */
    public void setPost(Post post) {
        this.post = post;
    }

    /**
     * Retourne l'étudiant qui republie le post.
     *
     * @return l'étudiant concerné
     */
    public Etudiant getEtudiant() {
        return etudiant;
    }

    /**
     * Définit l'étudiant qui republie le post.
     *
     * @param etudiant l'étudiant concerné
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * Retourne la date de republication.
     *
     * @return la date de republication
     */
    public LocalDateTime getDateRepublication() {
        return dateRepublication;
    }

    /**
     * Définit la date de republication.
     *
     * @param dateRepublication la date de republication à définir
     */
    public void setDateRepublication(LocalDateTime dateRepublication) {
        this.dateRepublication = dateRepublication;
    }

    /**
     * Retourne le commentaire lié à la republication.
     *
     * @return le commentaire de republication
     */
    public String getCommentaireRepublication() {
        return commentaireRepublication;
    }

    /**
     * Définit le commentaire de republication.
     *
     * @param commentaireRepublication le commentaire à définir
     */
    public void setCommentaireRepublication(String commentaireRepublication) {
        this.commentaireRepublication = commentaireRepublication;
    }

    /**
     * Retourne si la republication est publique.
     *
     * @return true si elle est publique, sinon false
     */
    public boolean isEstPublic() {
        return estPublic;
    }

    /**
     * Définit si la republication est publique.
     *
     * @param estPublic true si la republication est publique
     */
    public void setEstPublic(boolean estPublic) {
        this.estPublic = estPublic;
    }
}
