package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "REPUBLIER")
public class Republier {

    @EmbeddedId
    private RepublierId id = new RepublierId();

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "id_post")
    private Post post;

    @ManyToOne
    @MapsId("etudiantId")
    @JoinColumn(name = "id_etudiant")
    private Etudiant etudiant;

    @Column(name = "date_republication", nullable = false)
    private LocalDateTime dateRepublication = LocalDateTime.now();

    @Column(name = "commentaire_republication")
    private String commentaireRepublication;

    @Column(name = "est_public")
    private boolean estPublic;

    public Republier() {

    }

    public Republier(RepublierId id, Post post, Etudiant etudiant, LocalDateTime dateRepublication, boolean estPublic, String commentaireRepublication) {
        this.id = id;
        this.post = post;
        this.etudiant = etudiant;
        this.dateRepublication = dateRepublication;
        this.estPublic = estPublic;
        this.commentaireRepublication = commentaireRepublication;
    }

    public RepublierId getId() {
        return id;
    }

    public void setId(RepublierId id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public LocalDateTime getDateRepublication() {
        return dateRepublication;
    }

    public void setDateRepublication(LocalDateTime dateRepublication) {
        this.dateRepublication = dateRepublication;
    }

    public String getCommentaireRepublication() {
        return commentaireRepublication;
    }

    public void setCommentaireRepublication(String commentaireRepublication) {
        this.commentaireRepublication = commentaireRepublication;
    }


    public boolean isEstPublic() {
        return estPublic;
    }

    public void setEstPublic(boolean estPublic) {
        this.estPublic = estPublic;
    }
}
