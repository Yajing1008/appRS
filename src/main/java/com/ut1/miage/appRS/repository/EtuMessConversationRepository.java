package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.EtuMessConversation;
import com.ut1.miage.appRS.model.EtuMessConversationId;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtuMessConversationRepository extends JpaRepository<EtuMessConversation, EtuMessConversationId> {
	
	@Query("SELECT m FROM EtuMessConversation m WHERE m.conversation.idConversation = :idConversation ORDER BY m.dateHeureMessage ASC")
	List<EtuMessConversation> findByConversationOrderByDate(@Param("idConversation") Long idConversation);
	List<EtuMessConversation> findByConversation(Conversation conversation);
	
}
