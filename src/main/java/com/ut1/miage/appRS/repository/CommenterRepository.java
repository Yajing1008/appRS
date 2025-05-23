package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Commenter;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Référentiel Spring Data JPA pour la gestion des entités {@link Commenter}.
 *
 * Fournit les opérations CRUD de base ainsi que des méthodes personnalisées
 * pour gérer les commentaires liés aux publications et aux étudiants.
 */
@Repository
public interface CommenterRepository extends JpaRepository<Commenter, Long> {
    /**
     * Recherche les commentaires associés à une publication, triés par date décroissante.
     *
     * @param post la publication concernée
     * @return la liste des commentaires triés du plus récent au plus ancien
     */
    List<Commenter> findByPostOrderByDateHeureCommentaireDesc(Post post);
    /**
     * Supprime un commentaire spécifique lié à une publication et un étudiant donné.
     *
     * @param post la publication concernée
     * @param etudiant l'étudiant auteur du commentaire
     */
    void deleteByPostAndEtudiant(Post post, Etudiant etudiant);
    /**
     * Supprime tous les commentaires associés à une publication.
     *
     * @param post la publication concernée
     */
    void deleteAllByPost(Post post);

}
