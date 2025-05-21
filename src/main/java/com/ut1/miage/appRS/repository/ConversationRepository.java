package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Conversation;
import org.springframework.data.jpa.repository.*;

/**
 * Interface de dépôt pour l'entité {@link Conversation}.
 * Fournit des méthodes de base pour l'accès à la base de données via JPA.
 * Étend {@link JpaRepository} pour bénéficier des opérations CRUD standard.
 */
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

}
