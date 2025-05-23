package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.*;
import com.ut1.miage.appRS.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d’intégration du contrôleur {@link IndexController}.
 * Couvre les publications, réactions, commentaires et interactions de l’utilisateur.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IndexControllerTest {

    /** Permet de simuler des requêtes HTTP dans les tests. */
    @Autowired
    private MockMvc mockMvc;

    /** Référentiel pour accéder aux données des étudiants. */
    @Autowired
    private EtudiantRepository etudiantRepository;

    /** Référentiel pour accéder aux publications (posts). */
    @Autowired
    private PostRepository postRepository;

    /** Référentiel pour gérer les republis des publications. */
    @Autowired
    private RepublierRepository republierRepository;

    /** Référentiel pour gérer les réactions aux publications (Like, Favori). */
    @Autowired
    private ReagirRepository reagirRepository;

    /** Référentiel pour gérer les commentaires sur les publications. */
    @Autowired
    private CommenterRepository commenterRepository;

    /** Étudiant simulé utilisé dans les tests. */
    private Etudiant etudiant;

    /** Session simulée représentant un étudiant connecté. */
    private MockHttpSession session;
    /**
     * Initialise les données de test avant chaque exécution :
     * crée un étudiant fictif et simule une session active avec cet étudiant.
     */
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
    public void testToggleLike_shouldAddAndRemoveLike() throws Exception {

        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Post à liker");
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        ReagirId likeId = new ReagirId(post.getIdPost(), etudiant.getIdEtudiant(), "Like");


        mockMvc.perform(get("/reaction/like")
                        .param("postId", post.getIdPost().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection());

        Optional<Reagir> ajout = reagirRepository.findById(likeId);
        assertTrue(ajout.isPresent(), "Like 应该已添加");


        mockMvc.perform(get("/reaction/like")
                        .param("postId", post.getIdPost().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection());

        Optional<Reagir> suppression = reagirRepository.findById(likeId);
        assertTrue(suppression.isEmpty(), "Like 应该已删除");
    }


    /**
     * Vérifie le comportement du bouton "favori".
     */
    @Test
    public void testToggleFavori_shouldAddAndRemoveFavori() throws Exception {

        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Post favori");
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        ReagirId favoriId = new ReagirId(post.getIdPost(), etudiant.getIdEtudiant(), "Favori");


        mockMvc.perform(get("/reaction/favori")
                        .param("postId", post.getIdPost().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection());

        Optional<Reagir> ajout = reagirRepository.findById(favoriId);
        assertTrue(ajout.isPresent(), "Favori 应该已添加");


        mockMvc.perform(get("/reaction/favori")
                        .param("postId", post.getIdPost().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection());

        Optional<Reagir> suppression = reagirRepository.findById(favoriId);
        assertTrue(suppression.isEmpty(), "Favori 应该已删除");
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
        /**
     * Vérifie qu’une publication vide (sans texte ni image) est rejetée.
     */
    @Test
    public void testPublierSansContenuNiImage() throws Exception {
        mockMvc.perform(multipart("/publier")
                        .param("contenu", "")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    /**
     * Vérifie qu’un utilisateur non connecté ne peut pas republier un post.
     */
    @Test
    public void testRepublierSansConnexion() throws Exception {
        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Post original");
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        mockMvc.perform(post("/republication")
                        .param("postId", post.getIdPost().toString())
                        .param("commentaire", "Républier test")
                        .param("estPublic", "true"))
                .andExpect(redirectedUrl("/"));
    }

    /**
     * Vérifie qu’un post inexistant ne peut pas être republié.
     */
    @Test
    public void testRepublierPostInexistant() throws Exception {
        mockMvc.perform(post("/republication")
                        .param("postId", "99999")
                        .param("commentaire", "Erreur post")
                        .param("estPublic", "true")
                        .session(session))
                .andExpect(redirectedUrl("/"));
    }

    /**
     * Vérifie qu’un like sur un post inexistant est bien redirigé.
     */
    @Test
    public void testLikePostInexistant() throws Exception {
        mockMvc.perform(get("/reaction/like")
                        .param("postId", "999999")
                        .session(session))
                .andExpect(redirectedUrl("/#post-999999"));
    }

    /**
     * Vérifie qu’un favori sur un post inexistant est bien redirigé.
     */
    @Test
    public void testFavoriPostInexistant() throws Exception {
        mockMvc.perform(get("/reaction/favori")
                        .param("postId", "999999")
                        .session(session))
                .andExpect(redirectedUrl("/#post-999999"));
    }

    /**
     * Vérifie qu’un utilisateur non connecté ne peut pas commenter.
     */
    @Test
    public void testCommenterSansConnexion() throws Exception {
        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Post test");
        post.setEstPublicPost(true);
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        mockMvc.perform(post("/commenter")
                        .param("postId", post.getIdPost().toString())
                        .param("commentaire", "Test anonyme"))
                .andExpect(redirectedUrl("/#post-" + post.getIdPost()));
    }

    /**
     * Vérifie qu’un commentaire sur un post inexistant est redirigé sans erreur.
     */
    @Test
    public void testCommenterPostInexistant() throws Exception {
        mockMvc.perform(post("/commenter")
                        .param("postId", "999999")
                        .param("commentaire", "Erreur post")
                        .session(session))
                .andExpect(redirectedUrl("/"));
    }

    /**
     * Vérifie qu’un étudiant ne peut pas supprimer le commentaire d’un autre.
     */
    @Test
    public void testSupprimerCommentaireNonAuteur() throws Exception {
        Etudiant autre = new Etudiant();
        autre.setNomEtudiant("Autre");
        autre.setPrenomEtudiant("Utilisateur");
        autre.setEmailEtudiant("autre@example.com");
        autre.setMotDePass("xyz");
        autre.setDateNaissanceEtudiant(LocalDate.of(1999, 1, 1));
        autre = etudiantRepository.save(autre);

        Post post = new Post();
        post.setContenuPost("Post autre");
        post.setEtudiant(autre);
        post.setEstPublicPost(true);
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        Commenter commentaire = new Commenter();
        commentaire.setEtudiant(autre);
        commentaire.setPost(post);
        commentaire.setCommentaire("À ne pas supprimer");
        commentaire.setDateHeureCommentaire(LocalDateTime.now());
        commentaire = commenterRepository.save(commentaire);

        mockMvc.perform(post("/commenter/supprimer")
                        .param("postId", post.getIdPost().toString())
                        .param("idCommentaire", commentaire.getIdCommentaire().toString())
                        .session(session)) // session = etudiant, pas auteur
                .andExpect(redirectedUrl("/#post-" + post.getIdPost()));
    }
        /**
     * Vérifie que la publication échoue si plus de 3 images sont envoyées.
     * ⚠️ Ici on simule la logique, pas de vrai fichier requis.
     */
    @Test
    public void testPublierAvecTropDImages() throws Exception {
        mockMvc.perform(multipart("/publier")
                        .param("contenu", "Post avec trop d'images")
                        .param("estPublic", "true")
                        .param("images", "img1.jpg", "img2.jpg", "img3.jpg", "img4.jpg")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    /**
     * Vérifie qu'un utilisateur non connecté ne peut pas aimer une publication.
     */
    @Test
    public void testLikeSansConnexion() throws Exception {
        Post post = new Post();
        post.setContenuPost("Post");
        post.setEtudiant(etudiant);
        post.setEstPublicPost(true);
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        mockMvc.perform(get("/reaction/like")
                        .param("postId", post.getIdPost().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/#post-" + post.getIdPost()));
    }

    /**
     * Vérifie qu'un utilisateur non connecté ne peut pas ajouter une publication aux favoris.
     */
    @Test
    public void testFavoriSansConnexion() throws Exception {
        Post post = new Post();
        post.setContenuPost("Post favori");
        post.setEtudiant(etudiant);
        post.setEstPublicPost(true);
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        mockMvc.perform(get("/reaction/favori")
                        .param("postId", post.getIdPost().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/#post-" + post.getIdPost()));
    }

    /**
     * Vérifie qu'un utilisateur ne peut pas supprimer un commentaire inexistant.
     */
    @Test
    public void testSupprimerCommentaireInexistant() throws Exception {
        mockMvc.perform(post("/commenter/supprimer")
                        .param("postId", "1")
                        .param("idCommentaire", "99999")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/#post-1"));
    }

    /**
     * Vérifie qu’un utilisateur non connecté ne peut pas supprimer de commentaire.
     */
    @Test
    public void testSupprimerCommentaireSansConnexion() throws Exception {
        Post post = new Post();
        post.setContenuPost("Post commentaire");
        post.setEtudiant(etudiant);
        post.setEstPublicPost(true);
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

        Commenter commentaire = new Commenter();
        commentaire.setEtudiant(etudiant);
        commentaire.setPost(post);
        commentaire.setCommentaire("À supprimer");
        commentaire.setDateHeureCommentaire(LocalDateTime.now());
        commentaire = commenterRepository.save(commentaire);

        mockMvc.perform(post("/commenter/supprimer")
                        .param("postId", post.getIdPost().toString())
                        .param("idCommentaire", commentaire.getIdCommentaire().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/#post-" + post.getIdPost()));
    }

    /**
     * Vérifie qu’un post sans date de publication ne cause pas d’erreur à l’accueil.
     */
    @Test
    public void testAccueilAvecPostSansDate() throws Exception {
        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Sans date");
        post.setEstPublicPost(true);
        post = postRepository.save(post); // pas de datePublicationPost

        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
    /**
 * Vérifie que lorsqu’aucun utilisateur n’est connecté,
 * les attributs de session "amis" et "mesGroupes" sont mis à null.
 */
@Test
public void testAccueilSansConnexion() throws Exception {
    MockHttpSession sessionSansLogin = new MockHttpSession();

    mockMvc.perform(get("/").session(sessionSansLogin))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("posts"))
            .andExpect(model().attributeDoesNotExist("etudiantConnecte"));

    // Validation indirecte : si aucun utilisateur connecté, les groupes et amis sont null
    assert sessionSansLogin.getAttribute("amis") == null;
    assert sessionSansLogin.getAttribute("mesGroupes") == null;
}



}
