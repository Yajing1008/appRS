package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Universite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversiteRepository extends JpaRepository<Universite, Long> {
    Optional<Universite> findByNomUnivIgnoreCase(String nomUniv);
}
