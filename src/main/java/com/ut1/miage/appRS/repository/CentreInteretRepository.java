package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.CentreInteret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CentreInteretRepository extends JpaRepository<CentreInteret, Long> {
    Optional<CentreInteret> findByNomCentreInteretIgnoreCase(String nomCentreInteret);
}
