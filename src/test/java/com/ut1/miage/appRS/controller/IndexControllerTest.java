package com.ut1.miage.appRS.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.transaction.annotation.Transactional;

import com.ut1.miage.appRS.model.Commenter;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;
import com.ut1.miage.appRS.model.Reagir;
import com.ut1.miage.appRS.model.ReagirId;
import com.ut1.miage.appRS.repository.CommenterRepository;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import com.ut1.miage.appRS.repository.PostRepository;
import com.ut1.miage.appRS.repository.ReagirRepository;
import com.ut1.miage.appRS.repository.RepublierRepository;

/**
 * Tests d’intégration du contrôleur {@link IndexController}.
 * Couvre les publications, réactions, commentaires et interactions de l’utilisateur.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IndexControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private RepublierRepository republierRepository;
    @Autowired private ReagirRepository reagirRepository;
    @Autowired private CommenterRepository commenterRepository;

    private Etudiant etudiant;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setNomEtudiant("Dupont");
        etudiant.setPrenomEtudiant("Jean");
        etudiant.setEmailEtudiant("jean@example.com");
        etudiant.setDateNaissanceEtudiant(LocalDate.of(2000, 1, 1));
        etudiant = etudiantRepository.save(etudiant);

        session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);
    }

    /**
     * Vérifie que la page d'accueil s'affiche correctement avec un utilisateur connecté.
     */
    @Test
    public void testAccueilConnecte() throws Exception {
        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("etudiantConnecte"));
    }

    /**
     * Vérifie qu'une publication peut être créée avec contenu texte uniquement.
     */
    @Test
    public void testPublierTexte() throws Exception {
        mockMvc.perform(multipart("/publier")
                        .param("contenu", "Mon premier post")
                        .param("estPublic", "true")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    /**
     * Vérifie qu'une républication est bien enregistrée.
     */
    @Test
    public void testRepublierPost() throws Exception {
        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Contenu original");
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        mockMvc.perform(post("/republication")
                        .param("postId", post.getIdPost().toString())
                        .param("commentaire", "Je republie !")
                        .param("estPublic", "true")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    /**
     * Vérifie le comportement du bouton "like".
     */
    @Test
    public void testToggleLike() throws Exception {
        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Post à liker");
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        mockMvc.perform(get("/reaction/like")
                        .param("postId", post.getIdPost().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection());

        Optional<Reagir> r = reagirRepository.findById(new ReagirId(post.getIdPost(), etudiant.getIdEtudiant()));
        assertTrue(r.isPresent());
    }

    /**
     * Vérifie le comportement du bouton "favori".
     */
    @Test
    public void testToggleFavori() throws Exception {
        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Post favori");
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        mockMvc.perform(get("/reaction/favori")
                        .param("postId", post.getIdPost().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection());

        Optional<Reagir> r = reagirRepository.findById(new ReagirId(post.getIdPost(), etudiant.getIdEtudiant()));
        assertTrue(r.isPresent());
    }

    /**
     * Vérifie qu’un commentaire est enregistré avec succès.
     */
    @Test
    public void testCommenterPost() throws Exception {
        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Post commentable");
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        mockMvc.perform(post("/commenter")
                        .param("postId", post.getIdPost().toString())
                        .param("commentaire", "Bravo !")
                        .session(session))
                .andExpect(status().is3xxRedirection());

        assertTrue(commenterRepository.findAll().stream()
                .anyMatch(c -> c.getCommentaire().equals("Bravo !")));
    }

    /**
     * Vérifie qu’un commentaire peut être supprimé par son auteur.
     */
   @Test
    public void testSupprimerCommentaire() throws Exception {
    Post post = new Post();
    post.setContenuPost("Post");
    post.setEtudiant(etudiant);
    post.setEstPublicPost(true);
    post.setDatePublicationPost(LocalDateTime.now());
    post = postRepository.save(post);

    Commenter commentaire = new Commenter();
    commentaire.setCommentaire("À supprimer");
    commentaire.setEtudiant(etudiant);
    commentaire.setPost(post);
    commentaire.setDateHeureCommentaire(LocalDateTime.now());
    commentaire = commenterRepository.save(commentaire);

    mockMvc.perform(post("/commenter/supprimer")
                    .param("postId", post.getIdPost().toString())
                    .param("idCommentaire", commentaire.getIdCommentaire().toString())
                    .session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/#post-" + post.getIdPost()));
    }
}
