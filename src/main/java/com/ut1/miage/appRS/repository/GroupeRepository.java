package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface d’accès aux données pour l'entité {@link Groupe}.
 * Fournit des méthodes personnalisées de recherche en plus des opérations CRUD héritées de {@link JpaRepository}.
 */
@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {

    /**
     * Recherche les groupes dont le nom contient la chaîne spécifiée (insensible à la casse).
     *
     * @param nom chaîne à rechercher partiellement dans le nom du groupe
     * @return liste des groupes correspondants
     */
    List<Groupe> findByNomGroupeContainingIgnoreCase(String nom);

    /**
     * Recherche un groupe associé à une conversation donnée.
     *
     * @param conversation la conversation à rechercher
     * @return un {@link Optional} contenant le groupe si trouvé, vide sinon
     */
    Optional<Groupe> findByConversation(Conversation conversation);
}
