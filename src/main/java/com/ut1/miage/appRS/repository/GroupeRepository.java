package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Conversation;
import com.ut1.miage.appRS.model.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {
    List<Groupe> findByNomGroupeContainingIgnoreCase(String nom);
    Optional<Groupe> findByConversation(Conversation conversation);
    
}
