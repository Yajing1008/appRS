package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Post;
import com.ut1.miage.appRS.model.Reagir;
import com.ut1.miage.appRS.model.ReagirId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReagirRepository extends JpaRepository<Reagir, ReagirId>{
    void deleteAllByPost(Post post);
    @Query("SELECT r FROM Reagir r WHERE r.post.idPost = :postId AND r.etudiant.idEtudiant = :etudiantId AND r.statut = :statut")
    Optional<Reagir> findByPostIdAndEtudiantIdAndStatut(
            @Param("postId") Long postId,
            @Param("etudiantId") Long etudiantId,
            @Param("statut") String statut);


}
