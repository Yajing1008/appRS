package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;
import com.ut1.miage.appRS.model.Republier;
import com.ut1.miage.appRS.model.RepublierId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
/**
 * Référentiel Spring Data JPA pour la gestion des entités {@link Republier}.
 *
 * Permet de gérer les republications de posts par les étudiants,
 * en utilisant une clé composite {@link RepublierId}.
 */
public interface RepublierRepository extends JpaRepository<Republier, RepublierId> {
    /**
     * Recherche toutes les republications effectuées par un étudiant,
     * triées par date de republication décroissante.
     *
     * @param etudiant l'étudiant concerné
     * @return la liste des republications effectuées
     */
    List<Republier> findByEtudiantOrderByDateRepublicationDesc(Etudiant etudiant);
    /**
     * Recherche une republication spécifique d’un post par un étudiant.
     *
     * @param post le post concerné
     * @param etudiant l'étudiant ayant effectué la republication
     * @return une Optional contenant la republication si elle existe, sinon vide
     */
    Optional<Republier> findByPostAndEtudiant(Post post, Etudiant etudiant);
    /**
     * Supprime toutes les republications associées à un post.
     *
     * @param post le post concerné
     */
    void deleteAllByPost(Post post);
}
