package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.DemandeRejoindreGroupe;
import com.ut1.miage.appRS.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRejoindreGroupeRepository extends JpaRepository<DemandeRejoindreGroupe, Long> {
    List<DemandeRejoindreGroupe> findByGroupeCreateurIdEtudiant(Long idCreateur);

    List<DemandeRejoindreGroupe> findByEtudiant(Etudiant etudiant);
}
