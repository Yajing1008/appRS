package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.CentreInteret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Référentiel Spring Data JPA pour l'entité {@link CentreInteret}.
 *
 * Fournit des opérations de base de type CRUD ainsi qu'une méthode personnalisée
 * pour rechercher un centre d'intérêt par son nom (sans tenir compte de la casse).
 */
@Repository
public interface CentreInteretRepository extends JpaRepository<CentreInteret, Long> {
    /**
     * Recherche un centre d'intérêt par son nom, sans sensibilité à la casse.
     *
     * @param nomCentreInteret le nom du centre d'intérêt à rechercher
     * @return un Optional contenant le centre d'intérêt s'il est trouvé, sinon vide
     */
    Optional<CentreInteret> findByNomCentreInteretIgnoreCase(String nomCentreInteret);
}
