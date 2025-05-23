package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Référentiel Spring Data JPA pour la gestion des entités {@link Evenement}.
 *
 * Fournit les opérations CRUD de base ainsi que des méthodes personnalisées
 * pour interroger les événements par créateur, participants, date ou mot-clé.
 */
@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    /**
     * Recherche les événements créés par un étudiant spécifique.
     *
     * @param createur l'étudiant créateur des événements
     * @return la liste des événements créés par cet étudiant
     */
    List<Evenement> findByCreateur(Etudiant createur);
    /**
     * Recherche les événements auxquels un étudiant participe.
     *
     * @param etudiant l'étudiant participant
     * @return la liste des événements où l'étudiant est membre du groupe
     */
    List<Evenement> findByMembreGroupeContains(Etudiant etudiant);
    /**
     * Recherche les événements futurs (non encore terminés), triés par date de début croissante.
     *
     * @param now la date/heure actuelle servant de référence
     * @return la liste des événements à venir
     */
    List<Evenement> findByDateHeureFinEvenementAfterOrderByDateHeureDebutEvenementAsc(LocalDateTime now);
    /**
     * Recherche les événements futurs contenant un mot-clé dans le nom, triés par date de début croissante.
     *
     * @param motCle le mot-clé à rechercher dans le nom de l'événement
     * @param now la date/heure actuelle servant de référence
     * @return la liste des événements correspondants
     */
    List<Evenement> findByNomEvenementContainingIgnoreCaseAndDateHeureFinEvenementAfterOrderByDateHeureDebutEvenementAsc(String motCle, LocalDateTime now);


}
