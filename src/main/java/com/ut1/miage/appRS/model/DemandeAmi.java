package com.ut1.miage.appRS.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Représente une demande d'amitié entre deux étudiants dans le réseau social.
 * Une demande est initiée par un étudiant (demandeur) et envoyée à un autre étudiant (receveur).
 * Elle peut être en attente, acceptée ou refusée.
 */
@Entity
@Table(name = "DEMANDE_AMI")
public class DemandeAmi {
	
	/**
	 * Identifiant unique de la demande d'amitié.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDemande;
	
	/**
	 * Étudiant qui envoie la demande.
	 */
	@ManyToOne
	@JoinColumn(name = "id_demandeur")
	private Etudiant demandeur;
	
	/**
	 * Étudiant qui reçoit la demande.
	 */
	@ManyToOne
	@JoinColumn(name = "id_receveur")
	private Etudiant receveur;
	
	/**
	 * Statut de la demande : EN_ATTENTE, ACCEPTEE ou REFUSEE.
	 */
	private String statut;
	
	/**
	 * Date et heure à laquelle la demande a été envoyée.
	 * Initialisée à la date et l'heure actuelles par défaut.
	 */
	private LocalDateTime dateDemande = LocalDateTime.now();
	
	/**
	 * Retourne l'identifiant de la demande.
	 * @return l'identifiant de la demande
	 */
	public Long getIdDemande() {
		return idDemande;
	}
	
	/**
	 * Définit l'identifiant de la demande.
	 * @param idDemande identifiant à définir
	 */
	public void setIdDemande(Long idDemande) {
		this.idDemande = idDemande;
	}
	
	/**
	 * Retourne l'étudiant qui a envoyé la demande.
	 * @return le demandeur
	 */
	public Etudiant getDemandeur() {
		return demandeur;
	}
	
	/**
	 * Définit l'étudiant qui envoie la demande.
	 * @param demandeur l'étudiant demandeur
	 */
	public void setDemandeur(Etudiant demandeur) {
		this.demandeur = demandeur;
	}
	
	/**
	 * Retourne l'étudiant qui a reçu la demande.
	 * @return le receveur
	 */
	public Etudiant getReceveur() {
		return receveur;
	}
	
	/**
	 * Définit l'étudiant qui reçoit la demande.
	 * @param receveur l'étudiant receveur
	 */
	public void setReceveur(Etudiant receveur) {
		this.receveur = receveur;
	}
	
	/**
	 * Retourne le statut de la demande (EN_ATTENTE, ACCEPTEE, REFUSEE).
	 * @return le statut de la demande
	 */
	public String getStatut() {
		return statut;
	}
	
	/**
	 * Définit le statut de la demande.
	 * @param statut le statut à définir
	 */
	public void setStatut(String statut) {
		this.statut = statut;
	}
	
	/**
	 * Retourne la date et l'heure de la demande.
	 * @return la date de la demande
	 */
	public LocalDateTime getDateDemande() {
		return dateDemande;
	}
	
	/**
	 * Définit la date et l'heure de la demande.
	 * @param dateDemande date à définir
	 */
	public void setDateDemande(LocalDateTime dateDemande) {
		this.dateDemande = dateDemande;
	}
}
