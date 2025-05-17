package com.ut1.miage.appRS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ut1.miage.appRS.model.Etudiant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    boolean existsByEmailEtudiant(String emailEtudiant);
    // Recherche par nom contenant (insensible à la casse), et exclusion de soi
    @Query("SELECT e FROM Etudiant e WHERE LOWER(e.nomEtudiant) LIKE LOWER(CONCAT('%', :search, '%')) AND e.idEtudiant <> :idConnecte")
    List<Etudiant> searchEtudiantsExceptSelf(@Param("search") String search, @Param("idConnecte") Long idConnecte);
    
    // Recherche de tous les étudiants sauf soi (quand pas de filtre)
    @Query("SELECT e FROM Etudiant e WHERE e.idEtudiant <> :idConnecte")
    List<Etudiant> findAllExceptSelf(@Param("idConnecte") Long idConnecte);
    Optional<Etudiant> findByEmailEtudiant(String emailEtudiant);


}
