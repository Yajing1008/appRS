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

    /** Nom de l'étudiant. */
    private String nomEtudiant;

    /** Prénom de l'étudiant. */
    private String prenomEtudiant;

    /** Date de naissance de l'étudiant. */
    private LocalDate dateNaissanceEtudiant;

    /** Sexe de l'étudiant (par exemple : "Homme", "Femme", "Autre"). */
    private String sexeEtudiant;

    /** Adresse e-mail de l'étudiant. */
    private String emailEtudiant;

    /** Mot de passe de l'étudiant (stocké de manière sécurisée). */
    private String motDePass;


    /**
     * Photo de profil de l'étudiant (stockée en tant que texte, par exemple URL ou base64).
     */
    @Lob
    private String photoEtudiant = "";
    
    /**
     * Description personnelle de l'étudiant.
     */
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
    
    /**
     * Demandes de rejoindre un groupe envoyées par l'étudiant.
     */
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
    
    /**
     * Demandes d'amitié envoyées par l'étudiant (en tant que demandeur).
     */
    @OneToMany(mappedBy = "demandeur", cascade = CascadeType.ALL)
    private List<DemandeAmi> demandesEnvoyees = new ArrayList<>();
    
    /**
     * Demandes d'amitié reçues par l'étudiant (en tant que receveur).
     */
    @OneToMany(mappedBy = "receveur", cascade = CascadeType.ALL)
    private List<DemandeAmi> demandesRecues = new ArrayList<>();

    
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

    /**
     * Retourne le nom de l'étudiant.
     *
     * @return le nom de l'étudiant
     */
    public String getNomEtudiant() {
        return nomEtudiant;
    }

    /**
     * Définit le nom de l'étudiant.
     *
     * @param nomEtudiant le nom de l'étudiant
     */
    public void setNomEtudiant(String nomEtudiant) {
        this.nomEtudiant = nomEtudiant;
    }

    /**
     * Retourne le prénom de l'étudiant.
     *
     * @return le prénom de l'étudiant
     */
    public String getPrenomEtudiant() {
        return prenomEtudiant;
    }

    /**
     * Définit le prénom de l'étudiant.
     *
     * @param prenomEtudiant le prénom de l'étudiant
     */
    public void setPrenomEtudiant(String prenomEtudiant) {
        this.prenomEtudiant = prenomEtudiant;
    }

    /**
     * Retourne la date de naissance de l'étudiant.
     *
     * @return la date de naissance de l'étudiant
     */
    public LocalDate getDateNaissanceEtudiant() {
        return dateNaissanceEtudiant;
    }

    /**
     * Définit la date de naissance de l'étudiant.
     *
     * @param dateNaissanceEtudiant la date de naissance de l'étudiant
     */
    public void setDateNaissanceEtudiant(LocalDate dateNaissanceEtudiant) {
        this.dateNaissanceEtudiant = dateNaissanceEtudiant;
    }

    /**
     * Retourne le sexe de l'étudiant.
     *
     * @return le sexe de l'étudiant
     */
    public String getSexeEtudiant() {
        return sexeEtudiant;
    }

    /**
     * Définit le sexe de l'étudiant.
     *
     * @param sexeEtudiant le sexe de l'étudiant
     */
    public void setSexeEtudiant(String sexeEtudiant) {
        this.sexeEtudiant = sexeEtudiant;
    }

    /**
     * Retourne l'adresse e-mail de l'étudiant.
     *
     * @return l'adresse e-mail de l'étudiant
     */
    public String getEmailEtudiant() {
        return emailEtudiant;
    }

    /**
     * Définit l'adresse e-mail de l'étudiant.
     *
     * @param emailEtudiant l'adresse e-mail de l'étudiant
     */
    public void setEmailEtudiant(String emailEtudiant) {
        this.emailEtudiant = emailEtudiant;
    }

    /**
     * Retourne le mot de passe de l'étudiant.
     *
     * @return le mot de passe de l'étudiant
     */
    public String getMotDePass() {
        return motDePass;
    }

    /**
     * Définit le mot de passe de l'étudiant.
     *
     * @param motDePass le mot de passe de l'étudiant
     */
    public void setMotDePass(String motDePass) {
        this.motDePass = motDePass;
    }

    /**
     * Retourne la photo de profil de l'étudiant.
     *
     * @return la photo de profil de l'étudiant
     */
    public String getPhotoEtudiant() {
        return photoEtudiant;
    }

    /**
     * Définit la photo de profil de l'étudiant.
     *
     * @param photoEtudiant la photo de profil de l'étudiant
     */
    public void setPhotoEtudiant(String photoEtudiant) {
        this.photoEtudiant = photoEtudiant;
    }

    /**
     * Retourne la description ou biographie de l'étudiant.
     *
     * @return la description ou biographie de l'étudiant
     */
    public String getDescriptionEtudiant() {
        return descriptionEtudiant;
    }

    /**
     * Définit la description ou biographie de l'étudiant.
     *
     * @param descriptionEtudiant la description ou biographie de l'étudiant
     */
    public void setDescriptionEtudiant(String descriptionEtudiant) {
        this.descriptionEtudiant = descriptionEtudiant;
    }

    /**
     * Retourne la liste des amis de l’étudiant.
     *
     * @return la liste des étudiants amis
     */
    public List<Etudiant> getAmis() {
        return amis;
    }

    /**
     * Définit la liste des amis de l’étudiant.
     *
     * @param amis la liste des étudiants à définir comme amis
     */
    public void setAmis(List<Etudiant> amis) {
        this.amis = amis;
    }

    /**
     * Retourne la liste des universités associées à l’étudiant.
     *
     * @return la liste des universités
     */
    public List<Universite> getUniversites() {
        return universites;
    }

    /**
     * Définit la liste des universités associées à l’étudiant.
     *
     * @param universites la liste des universités à associer
     */
    public void setUniversites(List<Universite> universites) {
        this.universites = universites;
    }

    /**
     * Retourne la liste des centres d’intérêt de l’étudiant.
     *
     * @return la liste des centres d’intérêt
     */
    public List<CentreInteret> getCentresInteret() {
        return centresInteret;
    }

    /**
     * Définit la liste des centres d’intérêt de l’étudiant.
     *
     * @param centresInteret la liste des centres d’intérêt à associer
     */
    public void setCentresInteret(List<CentreInteret> centresInteret) {
        this.centresInteret = centresInteret;
    }

    /**
     * Retourne la liste des publications créées par l’étudiant.
     *
     * @return la liste des posts publiés
     */
    public List<Post> getPostsPublies() {
        return postsPublies;
    }

    /**
     * Définit la liste des publications créées par l’étudiant.
     *
     * @param postsPublies la liste des posts à définir
     */
    public void setPostsPublies(List<Post> postsPublies) {
        this.postsPublies = postsPublies;
    }

    /**
     * Retourne la liste des publications repartagées par l’étudiant.
     *
     * @return la liste des posts republies
     */
    public List<Republier> getPostsRepublies() {
        return postsRepublies;
    }

    /**
     * Définit la liste des publications repartagées par l’étudiant.
     *
     * @param postsRepublies la liste des reposts à définir
     */
    public void setPostsRepublies(List<Republier> postsRepublies) {
        this.postsRepublies = postsRepublies;
    }

    /**
     * Retourne la liste des groupes créés par l’étudiant.
     *
     * @return la liste des groupes créés
     */
    public List<Groupe> getGroupesCrees() {
        return groupesCrees;
    }

    /**
     * Définit la liste des groupes créés par l’étudiant.
     *
     * @param groupesCrees la liste des groupes à définir
     */
    public void setGroupesCrees(List<Groupe> groupesCrees) {
        this.groupesCrees = groupesCrees;
    }

    /**
     * Retourne la liste des messages envoyés par l’étudiant.
     *
     * @return la liste des messages envoyés
     */
    public List<EtuMessConversation> getMessagesEnvoyes() {
        return messagesEnvoyes;
    }

    /**
     * Définit la liste des messages envoyés par l’étudiant.
     *
     * @param messagesEnvoyes la liste des messages à définir
     */
    public void setMessagesEnvoyes(List<EtuMessConversation> messagesEnvoyes) {
        this.messagesEnvoyes = messagesEnvoyes;
    }

    /**
     * Retourne la liste des participations de l’étudiant aux groupes.
     *
     * @return la liste des participations
     */
    public List<Participer> getParticipations() {
        return participations;
    }

    /**
     * Définit la liste des participations de l’étudiant aux groupes.
     *
     * @param participations la liste des participations à définir
     */
    public void setParticipations(List<Participer> participations) {
        this.participations = participations;
    }

    /**
     * Retourne la liste des commentaires rédigés par l’étudiant.
     *
     * @return la liste des commentaires
     */
    public List<Commenter> getCommentaires() {
        return commentaires;
    }

    /**
     * Définit la liste des commentaires rédigés par l’étudiant.
     *
     * @param commentaires la liste des commentaires à définir
     */
    public void setCommentaires(List<Commenter> commentaires) {
        this.commentaires = commentaires;
    }

    /**
     * Retourne la liste des réactions de l’étudiant sur les publications.
     *
     * @return la liste des réactions
     */
    public List<Reagir> getReactions() {
        return reactions;
    }

    /**
     * Définit la liste des réactions de l’étudiant sur les publications.
     *
     * @param reactions la liste des réactions à définir
     */
    public void setReactions(List<Reagir> reactions) {
        this.reactions = reactions;
    }

    /**
     * Retourne la liste des demandes de groupe envoyées par l’étudiant.
     *
     * @return la liste des demandes de groupe
     */
    public List<DemandeRejoindreGroupe> getDemandesGroupes() {
        return demandesGroupes;
    }

    /**
     * Définit la liste des demandes de groupe envoyées par l’étudiant.
     *
     * @param demandesGroupes la liste des demandes de groupe à définir
     */
    public void setDemandesGroupes(List<DemandeRejoindreGroupe> demandesGroupes) {
        this.demandesGroupes = demandesGroupes;
    }

    /**
     * Retourne la liste des événements auxquels l’étudiant participe.
     *
     * @return la liste des événements suivis
     */
    public List<Evenement> getEvenementsParticiper() {
        return evenementsParticiper;
    }

    /**
     * Définit la liste des événements auxquels l’étudiant participe.
     *
     * @param evenementsParticiper la liste des événements suivis
     */
    public void setEvenementsParticiper(List<Evenement> evenementsParticiper) {
        this.evenementsParticiper = evenementsParticiper;
    }

    /**
     * Retourne la liste des événements créés par l’étudiant.
     *
     * @return la liste des événements créés
     */
    public List<Evenement> getEvenementsCreer() {
        return evenementsCreer;
    }

    /**
     * Définit la liste des événements créés par l’étudiant.
     *
     * @param evenementsCreer la liste des événements à définir
     */
    public void setEvenementsCreer(List<Evenement> evenementsCreer) {
        this.evenementsCreer = evenementsCreer;
    }

    /**
     * Retourne la liste des demandes d’amis envoyées par l’étudiant.
     *
     * @return la liste des demandes envoyées
     */
    public List<DemandeAmi> getDemandesEnvoyees() {
        return demandesEnvoyees;
    }

    /**
     * Définit la liste des demandes d’amis envoyées par l’étudiant.
     *
     * @param demandesEnvoyees la liste des demandes envoyées à définir
     */
    public void setDemandesEnvoyees(List<DemandeAmi> demandesEnvoyees) {
        this.demandesEnvoyees = demandesEnvoyees;
    }

    /**
     * Retourne la liste des demandes d’amis reçues par l’étudiant.
     *
     * @return la liste des demandes reçues
     */
    public List<DemandeAmi> getDemandesRecues() {
        return demandesRecues;
    }

    /**
     * Définit la liste des demandes d’amis reçues par l’étudiant.
     *
     * @param demandesRecues la liste des demandes reçues à définir
     */
    public void setDemandesRecues(List<DemandeAmi> demandesRecues) {
        this.demandesRecues = demandesRecues;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        
        Etudiant etudiant = (Etudiant) o;
        return Objects.equals(getIdEtudiant(), etudiant.getIdEtudiant()) &&
                Objects.equals(getNomEtudiant(), etudiant.getNomEtudiant()) &&
                Objects.equals(getPrenomEtudiant(), etudiant.getPrenomEtudiant()) &&
                Objects.equals(getDateNaissanceEtudiant(), etudiant.getDateNaissanceEtudiant()) &&
                Objects.equals(getSexeEtudiant(), etudiant.getSexeEtudiant()) &&
                Objects.equals(getEmailEtudiant(), etudiant.getEmailEtudiant());
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hashCode(getIdEtudiant());
        result = 31 * result + Objects.hashCode(getNomEtudiant());
        result = 31 * result + Objects.hashCode(getPrenomEtudiant());
        result = 31 * result + Objects.hashCode(getDateNaissanceEtudiant());
        result = 31 * result + Objects.hashCode(getSexeEtudiant());
        result = 31 * result + Objects.hashCode(getEmailEtudiant());
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
}
