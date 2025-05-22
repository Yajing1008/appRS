package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.EtuMessConversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface de dépôt pour l'entité {@link EtuMessConversation}.
 * Permet de gérer les messages échangés dans une conversation entre étudiants.
 */
public interface EtuMessConversationRepository extends JpaRepository<EtuMessConversation, Long> {
	
	/**
	 * Recherche tous les messages associés à une conversation donnée.
	 *
	 * @param conversation la conversation concernée
	 * @return liste des messages de la conversation
	 */
	List<EtuMessConversation> findByConversation(Conversation conversation);
	
	/**
	 * Recherche tous les messages en utilisant l'identifiant d'une conversation.
	 * Utile pour vérifier l'existence ou charger les messages par ID de conversation uniquement.
	 *
	 * @param idConversation identifiant de la conversation
	 * @return liste des messages liés à cette conversation
	 */
	List<EtuMessConversation> findByConversationIdConversation(Long idConversation);
}
