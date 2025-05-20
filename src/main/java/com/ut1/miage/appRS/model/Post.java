package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    /** Date de publication du post. */
    @Column(nullable = false)
    private LocalDateTime datePublicationPost = LocalDateTime.now();

    /** Liste des URLs des photos du post. */
    @ElementCollection
    @CollectionTable(name = "post_photos", joinColumns = @JoinColumn(name = "id_post"))
    @Column(name = "url_photo_post")
    private List<String> urlsPhotosPost = new ArrayList<>();

    /** L'étudiant ayant publié ce post. */
    @ManyToOne
    @JoinColumn(name = "id_etudiant_publier", nullable = false)
    private Etudiant etudiant;

    /** Liste des étudiants ayant republié ce post. */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Republier> republications = new ArrayList<>();


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

    public List<Republier> getRepublications() {
        return republications.stream()
                .sorted((r1, r2) -> r2.getDateRepublication().compareTo(r1.getDateRepublication()))
                .toList();
    }

    public void setRepublications(List<Republier> republications) {
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

    /**
     * @return la date de publication du post
     */
    public LocalDateTime getDatePublicationPost() {
        return datePublicationPost;
    }

    /**
     * Définit la date de publication du post.
     * @param datePublicationPost la date de publication
     */

    public void setDatePublicationPost(LocalDateTime datePublicationPost) {
        this.datePublicationPost = datePublicationPost;
    }

    /**
     * @return la liste des URLs des photos du post
     */
    public List<String> getUrlsPhotosPost() {
        return urlsPhotosPost;
    }

    /**
     * Définit la liste des URLs des photos du post.
     * @param urlsPhotosPost la liste des URLs à associer au post
     */
    public void setUrlsPhotosPost(List<String> urlsPhotosPost) {
        this.urlsPhotosPost = urlsPhotosPost;
    }


    public List<Reagir> getFavoris() {
        List<Reagir> favoris = new ArrayList<>();
        for (Reagir r : reactions) {
            if ("Favori".equalsIgnoreCase(r.getStatut())) {
                favoris.add(r);
            }
        }
        return favoris;
    }

    public List<Reagir> getLikes() {
        List<Reagir> likes = new ArrayList<>();
        for (Reagir r : reactions) {
            if ("Like".equalsIgnoreCase(r.getStatut())) {
                likes.add(r);
            }
        }
        return likes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "idPost=" + idPost +
                ", contenuPost='" + contenuPost + '\'' +
                ", estPublicPost=" + estPublicPost +
                '}';
    }
}