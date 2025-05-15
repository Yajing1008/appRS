package com.ut1.miage.appRS.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 * Représente une publication (post) faite par un étudiant sur le réseau social.
 */
@Entity
@Table(name = "POSTS")
public class Post {

    /** Identifiant unique du post. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;

    /** Contenu textuel du post. */
    private String contenuPost;

    /** Indique si le post est public ou non. */
    private boolean estPublicPost;

    /** L'étudiant ayant publié ce post. */
    @ManyToOne
    @JoinColumn(name = "id_etudiant_publier", nullable = false)
    private Etudiant etudiant;

    /** Liste des étudiants ayant republié ce post. */
    @ManyToMany
    @JoinTable(
        name = "republier",
        joinColumns = @JoinColumn(name = "id_post"),
        inverseJoinColumns = @JoinColumn(name = "id_etudiant")
    )
    private List<Etudiant> republications = new ArrayList<>();

    /** Liste des commentaires associés à ce post. */
    @OneToMany(mappedBy = "post")
    private List<Commenter> commentaires = new ArrayList<>();

    /** Liste des réactions associées à ce post. */
    @OneToMany(mappedBy = "post")
    private List<Reagir> reactions = new ArrayList<>();

    /**
     * @return la liste des réactions sur ce post
     */
    public List<Reagir> getReactions() {
        return reactions;
    }

    /**
     * @return la liste des commentaires sur ce post
     */
    public List<Commenter> getCommentaires() {
        return commentaires;
    }

    /**
     * Définit la liste des commentaires.
     * @param commentaires la liste de commentaires à associer
     */
    public void setCommentaires(List<Commenter> commentaires) {
        this.commentaires = commentaires;
    }

    /**
     * Définit la liste des réactions.
     * @param reactions la liste de réactions à associer
     */
    public void setReactions(List<Reagir> reactions) {
        this.reactions = reactions;
    }

    /**
     * @return l'étudiant auteur du post
     */
    public Etudiant getEtudiant() {
        return etudiant;
    }

    /**
     * Définit l'étudiant auteur du post.
     * @param etudiant l'auteur du post
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    /**
     * @return la liste des étudiants ayant republié ce post
     */
    public List<Etudiant> getRepublications() {
        return republications;
    }

    /**
     * Définit la liste des étudiants ayant republié ce post.
     * @param republications la liste des étudiants
     */
    public void setRepublications(List<Etudiant> republications) {
        this.republications = republications;
    }

    /**
     * @return l'identifiant du post
     */
    public Long getIdPost() {
        return idPost;
    }

    /**
     * @return true si le post est public, false sinon
     */
    public boolean isEstPublicPost() {
        return estPublicPost;
    }

    /**
     * Définit si le post est public.
     * @param estPublicPost true si public, false sinon
     */
    public void setEstPublicPost(boolean estPublicPost) {
        this.estPublicPost = estPublicPost;
    }

    /**
     * Définit l'identifiant du post.
     * @param idPost l'identifiant
     */
    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    /**
     * @return le contenu textuel du post
     */
    public String getContenuPost() {
        return contenuPost;
    }

    /**
     * Définit le contenu textuel du post.
     * @param contenuPost le contenu du post
     */
    public void setContenuPost(String contenuPost) {
        this.contenuPost = contenuPost;
    }
}