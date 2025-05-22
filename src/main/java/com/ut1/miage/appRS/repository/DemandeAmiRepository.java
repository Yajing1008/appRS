package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.DemandeAmi;
import com.ut1.miage.appRS.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interface de dépôt pour l'entité {@link DemandeAmi}.
 * Permet d'accéder aux demandes d'amitié et d'effectuer des recherches personnalisées
 * selon le demandeur, le receveur ou le statut de la demande.
 */
public interface DemandeAmiRepository extends JpaRepository<DemandeAmi, Long> {
	
	/**
	 * Recherche les demandes d'amitié reçues par un étudiant ayant un statut donné.
	 *
	 * @param receveur l'étudiant qui a reçu la demande
	 * @param statut le statut de la demande (par exemple : "EN_ATTENTE")
	 * @return liste des demandes correspondantes
	 */
	List<DemandeAmi> findByReceveurAndStatut(Etudiant receveur, String statut);
	
	/**
	 * Recherche les demandes d'amitié envoyées par un étudiant ayant un statut donné.
	 *
	 * @param demandeur l'étudiant qui a envoyé la demande
	 * @param statut le statut de la demande (par exemple : "ACCEPTEE")
	 * @return liste des demandes correspondantes
	 */
	List<DemandeAmi> findByDemandeurAndStatut(Etudiant demandeur, String statut);
	
	/**
	 * Vérifie si une demande d'amitié existe entre deux étudiants avec un statut donné.
	 *
	 * @param demandeur l'étudiant qui a envoyé la demande
	 * @param receveur l'étudiant qui a reçu la demande
	 * @param statut le statut de la demande
	 * @return true si une telle demande existe, sinon false
	 */
	boolean existsByDemandeurAndReceveurAndStatut(Etudiant demandeur, Etudiant receveur, String statut);
	
	/**
	 * Recherche une demande d'amitié par son identifiant.
	 *
	 * @param idDemande identifiant de la demande
	 * @return une option contenant la demande si elle existe, sinon vide
	 */
	Optional<DemandeAmi> findById(Long idDemande);
}
