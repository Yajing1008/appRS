package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Post;
import com.ut1.miage.appRS.model.Reagir;
import com.ut1.miage.appRS.model.ReagirId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReagirRepository extends JpaRepository<Reagir, ReagirId>{
    void deleteAllByPost(Post post);
}
