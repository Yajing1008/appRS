package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Etudiant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
   
    boolean existsByEmailEtudiant(String emailEtudiant);
    
    // Recherche par nom contenant (insensible à la casse), et exclusion de soi
    @Query("SELECT e FROM Etudiant e WHERE (LOWER(e.nomEtudiant) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(e.prenomEtudiant) LIKE LOWER(CONCAT('%', :search, '%'))) AND e.idEtudiant <> :idConnecte")
    List<Etudiant> searchEtudiantsExceptSelf(@Param("search") String search, @Param("idConnecte") Long idConnecte);
    
    // Recherche de tous les étudiants sauf soi (quand pas de filtre)
    @Query("SELECT e FROM Etudiant e WHERE e.idEtudiant <> :idConnecte")
    List<Etudiant> findAllExceptSelf(@Param("idConnecte") Long idConnecte);
    
    @Query("SELECT e.amis FROM Etudiant e WHERE e.idEtudiant = :idConnecte")
    List<Etudiant> findFriends(@Param("idConnecte") Long idConnecte);
    
    Optional<Etudiant> findByEmailEtudiant(String emailEtudiant);

    @EntityGraph(attributePaths = {"amis"})
    @Query("SELECT e FROM Etudiant e WHERE e.idEtudiant = :id")
    Optional<Etudiant> findEtudiantWithAmis(@Param("id") Long id);


}
