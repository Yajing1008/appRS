package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;
import com.ut1.miage.appRS.model.Republier;
import com.ut1.miage.appRS.model.RepublierId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepublierRepository extends JpaRepository<Republier, RepublierId> {
    List<Republier> findByEtudiantOrderByDateRepublicationDesc(Etudiant etudiant);
    Optional<Republier> findByPostAndEtudiant(Post post, Etudiant etudiant);
    void deleteAllByPost(Post post);
}
