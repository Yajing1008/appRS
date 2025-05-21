package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Lob
    private String photoEtudiant = "";
    private String descriptionEtudiant;

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
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Republier> postsRepublies = new ArrayList<>();


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


    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    private List<DemandeRejoindreGroupe> demandesGroupes = new ArrayList<>();

    /**
     * Universités fréquentées par l'étudiant.
     */
    @ManyToMany
    @JoinTable(
            name = "ETUDIER",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_univ")
    )
    private List<Universite> universites = new ArrayList<>();


    /**
     * Centres d’intérêt de l’étudiant.
     */
    @ManyToMany
    @JoinTable(
            name = "AVOIR",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_centre_interet")
    )
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
    
    //en tant que demandeur
    @OneToMany(mappedBy = "demandeur", cascade = CascadeType.ALL)
    private List<DemandeAmi> demandesEnvoyees = new ArrayList<>();
    
    // en tant que receveur
    @OneToMany(mappedBy = "receveur", cascade = CascadeType.ALL)
    private List<DemandeAmi> demandesRecues = new ArrayList<>();
    
    
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

    public List<Republier> getPostsRepublies() {
        return postsRepublies;
    }

    public void setPostsRepublies(List<Republier> postsRepublies) {
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

    
    public List<DemandeAmi> getDemandesEnvoyees() {
        return demandesEnvoyees;
    }
    
    public void setDemandesEnvoyees(List<DemandeAmi> demandesEnvoyees) {
        this.demandesEnvoyees = demandesEnvoyees;
    }
    
    public List<DemandeAmi> getDemandesRecues() {
        return demandesRecues;
    }
    
    public void setDemandesRecues(List<DemandeAmi> demandesRecues) {
        this.demandesRecues = demandesRecues;
    }

    public List<DemandeRejoindreGroupe> getDemandesGroupes() {
        return demandesGroupes;
    }

    public void setDemandesGroupes(List<DemandeRejoindreGroupe> demandesGroupes) {
        this.demandesGroupes = demandesGroupes;
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
        int result = Objects.hashCode(getIdEtudiant());
        result = 31 * result + Objects.hashCode(getNomEtudiant());
        result = 31 * result + Objects.hashCode(getPrenomEtudiant());
        result = 31 * result + Objects.hashCode(getDateNaissanceEtudiant());
        result = 31 * result + Objects.hashCode(getSexeEtudiant());
        result = 31 * result + Objects.hashCode(getEmailEtudiant());
        result = 31 * result + Objects.hashCode(getMotDePass());
        result = 31 * result + Objects.hashCode(getPhotoEtudiant());
        result = 31 * result + Objects.hashCode(getDescriptionEtudiant());
        result = 31 * result + Objects.hashCode(getAmis());
        result = 31 * result + Objects.hashCode(getPostsPublies());
        result = 31 * result + Objects.hashCode(getPostsRepublies());
        result = 31 * result + Objects.hashCode(getGroupesCrees());
        result = 31 * result + Objects.hashCode(getParticipations());
        result = 31 * result + Objects.hashCode(getMessagesEnvoyes());
        result = 31 * result + Objects.hashCode(getCommentaires());
        result = 31 * result + Objects.hashCode(getReactions());
        result = 31 * result + Objects.hashCode(getUniversites());
        result = 31 * result + Objects.hashCode(getCentresInteret());
        result = 31 * result + Objects.hashCode(getEvenementsParticiper());
        result = 31 * result + Objects.hashCode(getEvenementsCreer());
        return result;
    }
    
    @Override
    public String toString() {
        return "Etudiant{" +
                "idEtudiant=" + idEtudiant +
                ", nomEtudiant='" + nomEtudiant + '\'' +
                ", prenomEtudiant='" + prenomEtudiant + '\'' +
                '}';
    }
    
    public String getPhotoEtudiant() {
        return photoEtudiant;
    }

    public void setPhotoEtudiant(String photoEtudiant) {
        this.photoEtudiant = photoEtudiant;
    }

    public String getDescriptionEtudiant() {
        return descriptionEtudiant;
    }

    public void setDescriptionEtudiant(String descriptionEtudiant) {
        this.descriptionEtudiant = descriptionEtudiant;
    }
}