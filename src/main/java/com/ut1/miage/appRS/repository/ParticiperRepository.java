package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.ParticiperId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ut1.miage.appRS.model.Participer;

@Repository
public interface ParticiperRepository extends JpaRepository<Participer, Long> {
    void deleteById(ParticiperId participerId);
}
