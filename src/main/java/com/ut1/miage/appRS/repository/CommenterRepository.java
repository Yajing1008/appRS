package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Commenter;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommenterRepository extends JpaRepository<Commenter, Long> {
    List<Commenter> findByPostOrderByDateHeureCommentaireDesc(Post post);
    void deleteByPostAndEtudiant(Post post, Etudiant etudiant);
    void deleteAllByPost(Post post);

}
