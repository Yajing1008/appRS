package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Post;
import com.ut1.miage.appRS.model.Reagir;
import com.ut1.miage.appRS.model.ReagirId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Référentiel Spring Data JPA pour la gestion des entités {@link Reagir}.
 *
 * Permet de gérer les réactions (like, favori, etc.) des étudiants sur les publications,
 * en utilisant une clé primaire composée {@link com.ut1.miage.appRS.model.ReagirId}.
 */
@Repository
public interface ReagirRepository extends JpaRepository<Reagir, ReagirId>{
    /**
     * Supprime toutes les réactions associées à une publication donnée.
     *
     * @param post la publication concernée
     */
    void deleteAllByPost(Post post);
    /**
     * Recherche une réaction spécifique selon l’identifiant du post, de l’étudiant et le type de réaction.
     *
     * @param postId l'identifiant du post
     * @param etudiantId l'identifiant de l'étudiant
     * @param statut le type de réaction (par exemple : "Like", "Favori")
     * @return une réaction correspondante si elle existe, sinon un Optional vide
     */
    @Query("SELECT r FROM Reagir r WHERE r.post.idPost = :postId AND r.etudiant.idEtudiant = :etudiantId AND r.reagirId.statut = :statut")
    Optional<Reagir> findByPostIdAndEtudiantIdAndStatut(
            @Param("postId") Long postId,
            @Param("etudiantId") Long etudiantId,
            @Param("statut") String statut);


}
