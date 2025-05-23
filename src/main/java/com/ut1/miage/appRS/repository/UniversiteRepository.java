package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Universite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Référentiel Spring Data JPA pour la gestion des entités {@link Universite}.
 *
 * Fournit les opérations CRUD de base ainsi qu'une méthode personnalisée
 * pour rechercher une université par son nom sans tenir compte de la casse.
 */
@Repository
public interface UniversiteRepository extends JpaRepository<Universite, Long> {
    /**
     * Recherche une université par son nom, sans distinction de casse.
     *
     * @param nomUniv le nom de l'université à rechercher
     * @return un Optional contenant l'université si elle est trouvée, sinon vide
     */
    Optional<Universite> findByNomUnivIgnoreCase(String nomUniv);
}
