package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Participer;
import com.ut1.miage.appRS.model.ParticiperId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface de gestion des accès à la table de jointure PARTICIPER.
 * Cette table représente l’association entre les étudiants et les groupes (participations).
 * 
 * Elle utilise une clé composite {@link ParticiperId}.
 */
@Repository
public interface ParticiperRepository extends JpaRepository<Participer, Long> {

    /**
     * Supprime une participation à un groupe via sa clé composite.
     *
     * @param participerId la clé composite contenant l'ID de l'étudiant et l'ID du groupe
     */
    void deleteById(ParticiperId participerId);

    /**
     * Recherche toutes les participations d’un étudiant donné.
     *
     * @param idEtudiant l’identifiant de l’étudiant
     * @return la liste des participations de cet étudiant
     */
    List<Participer> findByEtudiant_IdEtudiant(Long idEtudiant);
}