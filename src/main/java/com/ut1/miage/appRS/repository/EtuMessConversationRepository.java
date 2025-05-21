package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.EtuMessConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EtuMessConversationRepository extends JpaRepository<EtuMessConversation, Long> {
	List<EtuMessConversation> findByConversation(Conversation conversation);
	
	// 查找是否已有两个人的共同会话
	List<EtuMessConversation> findByConversationIdConversation(Long idConversation);
}
