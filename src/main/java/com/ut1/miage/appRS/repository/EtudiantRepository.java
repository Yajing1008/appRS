package com.ut1.miage.appRS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ut1.miage.appRS.model.Etudiant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Interface de dépôt pour l'entité {@link Etudiant}.
 * Fournit des opérations de base pour gérer les étudiants dans la base de données,
 * ainsi que des requêtes personnalisées pour la recherche de contacts ou d'amis.
 */
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    
    /**
     * Vérifie si un étudiant existe avec une adresse e-mail donnée.
     *
     * @param emailEtudiant l'adresse e-mail à vérifier
     * @return true si l'adresse e-mail est déjà utilisée, sinon false
     */
    boolean existsByEmailEtudiant(String emailEtudiant);
    
    /**
     * Recherche les étudiants dont le nom ou prénom contient un mot-clé donné
     * (insensible à la casse), en excluant l'étudiant connecté.
     *
     * @param search mot-clé à rechercher
     * @param idConnecte identifiant de l'étudiant connecté (à exclure)
     * @return liste d'étudiants correspondant à la recherche
     */
    @Query("SELECT e FROM Etudiant e WHERE (LOWER(e.nomEtudiant) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(e.prenomEtudiant) LIKE LOWER(CONCAT('%', :search, '%'))) AND e.idEtudiant <> :idConnecte")
    List<Etudiant> searchEtudiantsExceptSelf(@Param("search") String search, @Param("idConnecte") Long idConnecte);
    
    /**
     * Récupère tous les étudiants à l'exception de l'étudiant connecté.
     *
     * @param idConnecte identifiant de l'étudiant connecté (à exclure)
     * @return liste de tous les autres étudiants
     */
    @Query("SELECT e FROM Etudiant e WHERE e.idEtudiant <> :idConnecte")
    List<Etudiant> findAllExceptSelf(@Param("idConnecte") Long idConnecte);
    
    /**
     * Récupère la liste des amis d’un étudiant.
     *
     * @param idConnecte identifiant de l'étudiant
     * @return liste des amis de l'étudiant
     */
    @Query("SELECT e.amis FROM Etudiant e WHERE e.idEtudiant = :idConnecte")
    List<Etudiant> findFriends(@Param("idConnecte") Long idConnecte);
    
    /**
     * Recherche un étudiant par son adresse e-mail.
     *
     * @param emailEtudiant l’adresse e-mail de l’étudiant
     * @return un Optional contenant l’étudiant s’il est trouvé, sinon vide
     */
    Optional<Etudiant> findByEmailEtudiant(String emailEtudiant);
}
