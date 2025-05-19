package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByCreateur(Etudiant createur);
    List<Evenement> findByMembreGroupeContains(Etudiant etudiant);
    // 获取未来活动并按时间升序排序
    List<Evenement> findByDateHeureFinEvenementAfterOrderByDateHeureDebutEvenementAsc(LocalDateTime now);
    // 关键词过滤 + 未来活动 + 时间升序排序
    List<Evenement> findByNomEvenementContainingIgnoreCaseAndDateHeureFinEvenementAfterOrderByDateHeureDebutEvenementAsc(String motCle, LocalDateTime now);


}
