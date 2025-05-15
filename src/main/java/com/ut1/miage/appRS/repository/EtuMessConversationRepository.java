package com.ut1.miage.appRS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ut1.miage.appRS.model.EtuMessConversation;

@Repository
public interface EtuMessConversationRepository extends JpaRepository<EtuMessConversation, Long> {
}
