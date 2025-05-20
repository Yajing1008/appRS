package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Conversation;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

}
