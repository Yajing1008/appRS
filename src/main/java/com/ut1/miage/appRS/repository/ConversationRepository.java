package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Conversation;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
	
	@Query("""
        SELECT c
        FROM Conversation c
        JOIN EtuMessConversation emc ON emc.conversation = c
        WHERE emc.etudiant.idEtudiant IN (:id1, :id2)
        GROUP BY c.idConversation
        HAVING COUNT(DISTINCT emc.etudiant.idEtudiant) = 2
    """)
	Optional<Conversation> findConversationBetween(@Param("id1") Long id1, @Param("id2") Long id2);
}
