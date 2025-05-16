package com.ut1.miage.appRS.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Représente un étudiant dans l'application de réseau social.
 * Un étudiant peut publier des posts, réagir, commenter, participer à des groupes,
 * à des événements, être ami avec d'autres étudiants, etc.
 */
@Entity
@Table(name = "ETUDIANTS")
public class Etudiant {

    /**
     * Identifiant unique de l'étudiant.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etudiant")
    private Long idEtudiant;

    private String nomEtudiant;
    private String prenomEtudiant;
    private LocalDate dateNaissanceEtudiant;
    private String sexeEtudiant;
    private String emailEtudiant;
    private String motDePass;

    /**
     * Liste des amis de l'étudiant.
     */
    @ManyToMany
    @JoinTable(
        name = "ETRE_AMI",
        joinColumns = @JoinColumn(name = "id_etudiant"),
        inverseJoinColumns = @JoinColumn(name = "id_etudiant_1")
    )
    private List<Etudiant> amis = new ArrayList<>();

    /**
     * Liste des posts publiés par l'étudiant.
     */
    @OneToMany(mappedBy = "etudiant")
    private List<Post> postsPublies = new ArrayList<>();

    /**
     * Liste des posts que l'étudiant a republiés.
     */
    @ManyToMany(mappedBy = "etudiant")
    private List<Post> postsRepublies = new ArrayList<>();

    /**
     * Groupes créés par l'étudiant.
     */
    @OneToMany(mappedBy = "createur")
    private List<Groupe> groupesCrees = new ArrayList<>();

    /**
     * Liste des participations de l'étudiant aux groupes.
     */
    @OneToMany(mappedBy = "etudiant")
    private List<Participer> participations = new ArrayList<>();

    /**
     * Messages envoyés dans les conversations.
     */
    @OneToMany(mappedBy = "etudiant")
    private List<EtuMessConversation> messagesEnvoyes;

    /**
     * Commentaires faits par l'étudiant.
     */
    @OneToMany(mappedBy = "etudiant")
    private List<Commenter> commentaires = new ArrayList<>();

    /**
     * Réactions de l'étudiant aux posts.
     */
    @OneToMany(mappedBy = "etudiant")
    private List<Reagir> reactions = new ArrayList<>();

    /**
     * Universités fréquentées par l'étudiant.
     */
    @ManyToMany(mappedBy = "etudiant")
    private List<Universite> universites = new ArrayList<>();

    /**
     * Centres d’intérêt de l’étudiant.
     */
    @ManyToMany(mappedBy = "etudiant")
    private List<CentreInteret> centresInteret = new ArrayList<>();

    /**
     * Événements auxquels l’étudiant participe.
     */
    @ManyToMany(mappedBy = "membreGroupe")
    private List<Evenement> evenementsParticiper = new ArrayList<>();

    /**
     * Événements créés par l’étudiant.
     */
    @OneToMany(mappedBy = "createur")
    private List<Evenement> evenementsCreer = new ArrayList<>();

    // --- Getters / Setters avec JavaDoc ---

    /**
     * @return identifiant de l'étudiant
     */
    public Long getIdEtudiant() {
        return idEtudiant;
    }

    /**
     * @param idEtudiant identifiant à définir
     */
    public void setIdEtudiant(Long idEtudiant) {
        this.idEtudiant = idEtudiant;
    }
    
    public String getNomEtudiant() {
        return nomEtudiant;
    }

    public void setNomEtudiant(String nomEtudiant) {
        this.nomEtudiant = nomEtudiant;
    }

    public String getPrenomEtudiant() {
        return prenomEtudiant;
    }

    public void setPrenomEtudiant(String prenomEtudiant) {
        this.prenomEtudiant = prenomEtudiant;
    }

    public LocalDate getDateNaissanceEtudiant() {
        return dateNaissanceEtudiant;
    }

    public void setDateNaissanceEtudiant(LocalDate dateNaissanceEtudiant) {
        this.dateNaissanceEtudiant = dateNaissanceEtudiant;
    }

    public String getSexeEtudiant() {
        return sexeEtudiant;
    }

    public void setSexeEtudiant(String sexeEtudiant) {
        this.sexeEtudiant = sexeEtudiant;
    }

    public String getEmailEtudiant() {
        return emailEtudiant;
    }

    public void setEmailEtudiant(String emailEtudiant) {
        this.emailEtudiant = emailEtudiant;
    }

    public String getMotDePass() {
        return motDePass;
    }

    public void setMotDePass(String motDePass) {
        this.motDePass = motDePass;
    }

    public List<Etudiant> getAmis() {
        return amis;
    }

    public void setAmis(List<Etudiant> amis) {
        this.amis = amis;
    }

    public List<Post> getPostsPublies() {
        return postsPublies;
    }

    public void setPostsPublies(List<Post> postsPublies) {
        this.postsPublies = postsPublies;
    }

    public List<Post> getPostsRepublies() {
        return postsRepublies;
    }

    public void setPostsRepublies(List<Post> postsRepublies) {
        this.postsRepublies = postsRepublies;
    }

    public List<Groupe> getGroupesCrees() {
        return groupesCrees;
    }

    public void setGroupesCrees(List<Groupe> groupesCrees) {
        this.groupesCrees = groupesCrees;
    }

    public List<Participer> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participer> participations) {
        this.participations = participations;
    }

    public List<EtuMessConversation> getMessagesEnvoyes() {
        return messagesEnvoyes;
    }

    public void setMessagesEnvoyes(List<EtuMessConversation> messagesEnvoyes) {
        this.messagesEnvoyes = messagesEnvoyes;
    }

    public List<Commenter> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commenter> commentaires) {
        this.commentaires = commentaires;
    }

    public List<Reagir> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reagir> reactions) {
        this.reactions = reactions;
    }

    public List<Universite> getUniversites() {
        return universites;
    }

    public void setUniversites(List<Universite> universites) {
        this.universites = universites;
    }

    public List<CentreInteret> getCentresInteret() {
        return centresInteret;
    }

    public void setCentresInteret(List<CentreInteret> centresInteret) {
        this.centresInteret = centresInteret;
    }

    public List<Evenement> getEvenementsParticiper() {
        return evenementsParticiper;
    }

    public void setEvenementsParticiper(List<Evenement> evenementsParticiper) {
        this.evenementsParticiper = evenementsParticiper;
    }

    public List<Evenement> getEvenementsCreer() {
        return evenementsCreer;
    }

    public void setEvenementsCreer(List<Evenement> evenementsCreer) {
        this.evenementsCreer = evenementsCreer;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Etudiant)) return false;
        Etudiant e = (Etudiant) o;
        return idEtudiant != null && idEtudiant.equals(e.getIdEtudiant());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idEtudiant);
    }
    
}