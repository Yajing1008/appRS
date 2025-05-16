package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    boolean existsByEmailEtudiant(String emailEtudiant);
    Optional<Etudiant> findByEmailEtudiant(String emailEtudiant);


}
