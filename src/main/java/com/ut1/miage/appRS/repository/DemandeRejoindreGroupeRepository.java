package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.DemandeRejoindreGroupe;
import com.ut1.miage.appRS.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface d'accès aux données pour l'entité {@link DemandeRejoindreGroupe}.
 * Fournit des méthodes pour récupérer les demandes d’adhésion en fonction du créateur du groupe
 * ou de l’étudiant demandeur.
 */
@Repository
public interface DemandeRejoindreGroupeRepository extends JpaRepository<DemandeRejoindreGroupe, Long> {

    /**
     * Recherche toutes les demandes d’adhésion pour les groupes créés par un étudiant donné.
     *
     * @param idCreateur l'identifiant de l'étudiant créateur du groupe
     * @return liste des demandes liées aux groupes créés par cet étudiant
     */
    List<DemandeRejoindreGroupe> findByGroupeCreateurIdEtudiant(Long idCreateur);

    /**
     * Recherche toutes les demandes effectuées par un étudiant donné.
     *
     * @param etudiant l'étudiant demandeur
     * @return liste des demandes envoyées par cet étudiant
     */
    List<DemandeRejoindreGroupe> findByEtudiant(Etudiant etudiant);
}