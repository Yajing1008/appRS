package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DEMANDE_AMI")
public class DemandeAmi {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDemande;
	
	@ManyToOne
	@JoinColumn(name = "id_demandeur")
	private Etudiant demandeur;
	
	@ManyToOne
	@JoinColumn(name = "id_receveur")
	private Etudiant receveur;
	
	private String statut; // EN_ATTENTE, ACCEPTEE, REFUSEE
	
	private LocalDateTime dateDemande = LocalDateTime.now();
	
	public Long getIdDemande() {
		return idDemande;
	}
	
	public void setIdDemande(Long idDemande) {
		this.idDemande = idDemande;
	}
	
	public Etudiant getDemandeur() {
		return demandeur;
	}
	
	public void setDemandeur(Etudiant demandeur) {
		this.demandeur = demandeur;
	}
	
	public Etudiant getReceveur() {
		return receveur;
	}
	
	public void setReceveur(Etudiant receveur) {
		this.receveur = receveur;
	}
	
	public String getStatut() {
		return statut;
	}
	
	public void setStatut(String statut) {
		this.statut = statut;
	}
	
	public LocalDateTime getDateDemande() {
		return dateDemande;
	}
	
	public void setDateDemande(LocalDateTime dateDemande) {
		this.dateDemande = dateDemande;
	}
}

