package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.DemandeAmi;
import com.ut1.miage.appRS.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DemandeAmiRepository extends JpaRepository<DemandeAmi, Long> {
	
	List<DemandeAmi> findByReceveurAndStatut(Etudiant receveur, String statut);
	
	boolean existsByDemandeurAndReceveurAndStatut(Etudiant demandeur, Etudiant receveur, String statut);
	
	Optional<DemandeAmi> findById(Long idDemande);
}

