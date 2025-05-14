package com.ut1.miage.appRS.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ETUDIANTS")
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtudiant;

    private String nomEtudiant;
    private String prenomEtudiant;

    private LocalDate dateNaissanceEtudiant;

    private String sexeEtudiant;

    private String emailEtudiant;

    private String motDePass;

    @OneToMany(mappedBy = "etudiant")
    private List<Post> postsPublies = new ArrayList<>();

    @ManyToMany(mappedBy = "republications")
    private List<Post> postsRepublies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "idEtudiant")
    private Etudiant createur;

    @OneToMany(mappedBy = "etudiant")
    private List<Participer> participations = new ArrayList<>();

    public Long getIdEtudiant() {
        return idEtudiant;
    }

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

    public Etudiant getCreateur() {
        return createur;
    }

    public void setCreateur(Etudiant createur) {
        this.createur = createur;
    }

    public List<Participer> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participer> participations) {
        this.participations = participations;
    }
    
}