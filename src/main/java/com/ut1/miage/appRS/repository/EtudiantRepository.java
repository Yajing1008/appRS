package com.ut1.miage.appRS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ut1.miage.appRS.model.Etudiant;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByEmailEtudiant(String emailEtudiant);
    boolean existsByEmailEtudiant(String emailEtudiant);
}
