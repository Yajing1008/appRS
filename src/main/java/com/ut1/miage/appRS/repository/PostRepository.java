package com.ut1.miage.appRS.repository;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 所有公开帖子
    List<Post> findByEstPublicPostTrueOrderByDatePublicationPostDesc();
    // 🔹 查找某位学生转发的所有帖子（按时间倒序）
    List<Post> findByEtudiantOrderByDatePublicationPostDesc(Etudiant etudiant);


    // ✅ 未登录用户和已登录用户都需要：获取所有公开的帖子（包含公开转发）
    @Query("""
            SELECT DISTINCT p FROM Post p
            WHERE p.estPublicPost = true
               OR p.idPost IN (
                   SELECT r.post.idPost 
                   FROM Republier r
                   WHERE r.estPublic = true
                     AND r.post.estPublicPost = true
               )
            ORDER BY p.datePublicationPost DESC
            """)
    List<Post> findAllPublicPostsWithPublicReposts();


    // ✅ 已登录用户：自己、朋友的帖子，朋友的转发（包括原帖是公开的或原帖是朋友的私帖）
//    @Query("""
//            SELECT DISTINCT p FROM Post p
//            LEFT JOIN FETCH p.republications r
//            WHERE
//                p.etudiant = :etudiant
//                OR p.estPublicPost = true
//                OR (p.etudiant IN :amis)
//                OR (
//                    (r.etudiant IN :amis AND r.estPublic = true) OR
//                    (r.etudiant IN :amis AND r.post.etudiant IN :amis)
//                )
//            ORDER BY p.datePublicationPost DESC
//            """)
//    List<Post> findRelevantPostsForUser(
//            @Param("etudiant") Etudiant etudiant,
//            @Param("amis") List<Etudiant> amis
//    );

    @Query("""
            SELECT p 
            FROM Post p 
            WHERE p.estPublicPost = true OR (p.estPublicPost = false AND p.etudiant IN :amis)           
            """)
    List<Post> findRelativePosts(List<Etudiant> amis);




}
