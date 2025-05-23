package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Référentiel Spring Data JPA pour la gestion des entités {@link Post}.
 *
 * Fournit les opérations de base (CRUD) ainsi que des méthodes personnalisées
 * pour interroger les publications selon leur visibilité, leur auteur ou les relations d'amitié.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * Recherche toutes les publications publiques, triées de la plus récente à la plus ancienne.
     *
     * @return la liste des publications publiques triées par date de publication décroissante
     */
    List<Post> findByEstPublicPostTrueOrderByDatePublicationPostDesc();

    /**
     * Recherche toutes les publications d’un étudiant, triées par date de publication décroissante.
     *
     * @param etudiant l'étudiant auteur des publications
     * @return la liste des publications de l'étudiant
     */
    List<Post> findByEtudiantOrderByDatePublicationPostDesc(Etudiant etudiant);

    /**
     * Recherche toutes les publications publiques d’un étudiant, triées par date décroissante.
     *
     * @param etudiant l'étudiant concerné
     * @return la liste des publications publiques de l'étudiant
     */
    List<Post> findByEtudiantAndEstPublicPostTrueOrderByDatePublicationPostDesc(Etudiant etudiant);

    /**
     * Recherche toutes les publications publiques, incluant celles repartagées publiquement,
     * triées par date de publication décroissante.
     *
     * @return la liste des publications publiques et repartagées
     */
    @Query("""
            SELECT DISTINCT p FROM Post p
            WHERE p.estPublicPost = true
               OR p.idPost IN (
                   SELECT r.post.idPost 
                   FROM Republier r
                   WHERE r.estPublic = true
                     AND r.post.estPublicPost = true
               )
            ORDER BY p.datePublicationPost DESC
           """)
    List<Post> findAllPublicPostsWithPublicReposts();

    /**
     * Recherche les publications visibles pour un étudiant en fonction de ses amis :
     * soit les publications publiques, soit celles privées mais postées par ses amis.
     *
     * @param amis la liste des amis de l'étudiant connecté
     * @return la liste des publications visibles (publiques ou amicales)
     */
    @Query("""
       SELECT p
       FROM Post p
       WHERE p.estPublicPost = true OR (p.estPublicPost = false AND p.etudiant IN :amis)
       ORDER BY p.datePublicationPost DESC
       """)
    List<Post> findRelativePosts(List<Etudiant> amis);

}
