package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.*;
import com.ut1.miage.appRS.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Classe de test d’intégration pour le contrôleur {@link ProfilController}.
 *
 * Vérifie les fonctionnalités du profil étudiant telles que l’affichage, les publications,
 * les commentaires, les interactions sociales et les données associées (universités, centres d’intérêt).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfilControllerTest {
    /** Objet permettant de simuler des requêtes HTTP dans les tests. */
    @Autowired
    private MockMvc mockMvc;

    /** Référentiel pour les entités Étudiant. */
    @Autowired
    private EtudiantRepository etudiantRepository;

    /** Référentiel pour les entités Post. */
    @Autowired
    private PostRepository postRepository;

    /** Référentiel pour les republis de publications. */
    @Autowired
    private RepublierRepository republierRepository;

    /** Référentiel pour les universités liées aux étudiants. */
    @Autowired
    private UniversiteRepository universiteRepository;

    /** Référentiel pour les centres d’intérêt des étudiants. */
    @Autowired
    private CentreInteretRepository centreInteretRepository;

    /** Référentiel pour les commentaires sur les publications. */
    @Autowired
    private CommenterRepository commenterRepository;

    /** Référentiel pour les réactions sur les publications. */
    @Autowired
    private ReagirRepository reagirRepository;

    /** Étudiant principal simulé dans les tests. */
    private Etudiant etudiant;

    /** Étudiant cible utilisé pour les interactions de test. */
    private Etudiant cible;

    /** Publication créée pour les tests. */
    private Post post;

    /** Commentaire associé à une publication. */
    private Commenter commentaire;

    /** Session simulée représentant un étudiant connecté. */
    private MockHttpSession session;
    /**
     * Initialise les données nécessaires avant chaque test :
     * crée deux étudiants, un post, un commentaire et une session simulée.
     */
    @BeforeEach
    void setUp() {

        etudiant = new Etudiant();
        etudiant.setNomEtudiant("Dupont");
        etudiant.setPrenomEtudiant("Jean");
        etudiant.setEmailEtudiant("jean.dupont@example.com");
        etudiant.setDateNaissanceEtudiant(LocalDate.of(2000, 1, 1));
        etudiant = etudiantRepository.save(etudiant);

        cible = new Etudiant();
        cible.setNomEtudiant("Cible");
        cible.setPrenomEtudiant("Etudiant");
        cible.setEmailEtudiant("cible@example.com");
        cible.setDateNaissanceEtudiant(LocalDate.of(2000, 2, 2));
        cible = etudiantRepository.save(cible);


        post = new Post();
        post.setContenuPost("Contenu original");
        post.setEtudiant(etudiant);
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);


        commentaire = new Commenter();
        commentaire.setEtudiant(etudiant);
        commentaire.setPost(post);
        commentaire.setCommentaire("Un commentaire.");
        commentaire.setDateHeureCommentaire(LocalDateTime.now());
        commentaire = commenterRepository.save(commentaire);


        session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);


        post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Post de test");
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);

    }
    /** Vérifie l'affichage du profil pour un étudiant connecté sur son propre compte. */
    @Test
    void testAfficherProfil_ConnecteEtProprietaire() throws Exception {
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        mockMvc.perform(get("/profil").sessionAttr("etudiantConnecte", savedEtudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attribute("etudiant", hasProperty("idEtudiant", is(savedEtudiant.getIdEtudiant()))))
                .andExpect(model().attribute("isOwner", true));
    }
    /** Vérifie l'affichage du profil lorsqu'aucun utilisateur n'est connecté. */
    @Test
    void testAfficherProfil_NonConnecte() throws Exception {
        mockMvc.perform(get("/profil"))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attribute("messageConnexion", "Veuillez vous connecter pour voir vos publications."))
                .andExpect(model().attribute("etudiant", is((Object) null)))
                .andExpect(model().attribute("posts", is(Collections.emptyList())))
                .andExpect(model().attribute("postDates", is(Collections.emptyMap())))
                .andExpect(model().attribute("isOwner", false));
    }
    /** Vérifie le comportement lorsqu'un étudiant connecté n'existe pas en base. */
    @Test
    void testAfficherProfil_EtudiantIntrouvable() throws Exception {
        Etudiant ghost = new Etudiant();
        ghost.setIdEtudiant(999L);
        ghost.setPrenomEtudiant("Ghost");

        mockMvc.perform(get("/profil").sessionAttr("etudiantConnecte", ghost))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attribute("messageConnexion", "Profil introuvable."))
                .andExpect(model().attribute("isOwner", false));
    }

    /** Vérifie l'affichage du profil avec des posts et republications. */
    @Test
    void testAfficherProfil_AvecPostsEtRepublications() throws Exception {

        Etudiant savedEtudiant = etudiantRepository.save(etudiant);


        Post post1 = new Post();
        post1.setEtudiant(savedEtudiant);
        post1.setContenuPost("Mon premier post");
        post1.setDatePublicationPost(LocalDateTime.of(2024, 4, 1, 15, 30));
        post1 = postRepository.save(post1);


        Post post2 = new Post();
        post2.setContenuPost("Un autre post");
        post2.setEtudiant(savedEtudiant);
        post2.setDatePublicationPost(LocalDateTime.of(2024, 3, 1, 10, 0));
        post2 = postRepository.save(post2);


        Republier republication = new Republier();
        republication.setEtudiant(savedEtudiant);
        republication.setPost(post2);
        republication.setDateRepublication(LocalDateTime.of(2024, 4, 2, 9, 0));
        republierRepository.save(republication);


        mockMvc.perform(get("/profil").sessionAttr("etudiantConnecte", savedEtudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attribute("etudiant", hasProperty("idEtudiant", is(savedEtudiant.getIdEtudiant()))))
                .andExpect(model().attribute("isOwner", true))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("postDates"));
    }
    /** Vérifie l'affichage du profil par ID lorsqu'il est inexistant. */
    @Test
    void testAfficherProfilParId_ProfilInexistant() throws Exception {
        mockMvc.perform(get("/profil/999999"))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attribute("messageConnexion", "Profil introuvable."))
                .andExpect(model().attribute("etudiant", is((Object) null)))
                .andExpect(model().attribute("isOwner", false))
                .andExpect(model().attribute("isFriend", false));
    }
    /** Vérifie l'affichage du profil d'un autre étudiant sans être connecté. */
    @Test
    void testAfficherProfilParId_SansConnexion() throws Exception {
        mockMvc.perform(get("/profil/" + cible.getIdEtudiant()))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attribute("etudiant", hasProperty("idEtudiant", is(cible.getIdEtudiant()))))
                .andExpect(model().attribute("isOwner", false))
                .andExpect(model().attribute("isFriend", false))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("postDates"));
    }
    /** Vérifie que le visiteur connecté est ami avec le propriétaire du profil. */
    @Test
    void testAfficherProfilParId_EstAmi() throws Exception {

        etudiant.getAmis().add(cible);
        etudiant = etudiantRepository.save(etudiant);

        mockMvc.perform(get("/profil/" + cible.getIdEtudiant())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attribute("isOwner", false))
                .andExpect(model().attribute("isFriend", true));
    }

    /** Vérifie que le visiteur connecté n'est pas ami avec le propriétaire du profil. */
    @Test
    void testAfficherProfilParId_NonAmi() throws Exception {
        mockMvc.perform(get("/profil/" + cible.getIdEtudiant())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attribute("isOwner", false))
                .andExpect(model().attribute("isFriend", false));
    }
    /** Vérifie l'accès au formulaire de modification sans être connecté. */
    @Test
    void testShowEditForm_SansConnexion() throws Exception {
        mockMvc.perform(get("/profil/modifier"))
                .andExpect(status().isOk())
                .andExpect(view().name("profil_modifier"))
                .andExpect(model().attribute("etudiant", nullValue()))
                .andExpect(model().attribute("posts", is(Collections.emptyList())))
                .andExpect(model().attribute("postDates", is(Collections.emptyMap())))
                .andExpect(model().attribute("toutesUniversites", is(Collections.emptyList())))
                .andExpect(model().attribute("tousCentresInteret", is(Collections.emptyList())));
    }
    /** Vérifie l'accès au formulaire de modification pour un étudiant connecté. */
    @Test
    void testShowEditForm_ConnecteAvecDonnees() throws Exception {

        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Bonjour !");
        post.setDatePublicationPost(LocalDateTime.of(2025, 5, 1, 10, 0));
        post = postRepository.save(post);


        Universite universite = new Universite();
        universite.setNomUniv("Université de Test");
        universite = universiteRepository.save(universite);


        CentreInteret interet = new CentreInteret();
        interet.setNomCentreInteret("Informatique");
        interet = centreInteretRepository.save(interet);

        mockMvc.perform(get("/profil/modifier")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("profil_modifier"))
                .andExpect(model().attribute("etudiant", hasProperty("idEtudiant", is(etudiant.getIdEtudiant()))))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("postDates"))
                .andExpect(model().attribute("toutesUniversites", hasItem(hasProperty("nomUniv", is("Université de Test")))))
                .andExpect(model().attribute("tousCentresInteret", hasItem(hasProperty("nomCentreInteret", is("Informatique")))));

    }
    /** Vérifie la modification du profil avec université, centre d’intérêt et photo. */
    @Test
    void testSaveProfile_ModifieAvecUniversitesEtInterets() throws Exception {

        etudiant = etudiantRepository.save(etudiant);


        MockMultipartFile photoFile = new MockMultipartFile(
                "photo", "photo.jpg", "image/jpeg", "fake-image-data".getBytes()
        );


        String nouvelleUniversite = "Université Paris-Saclay";
        String nouvelInteret = "Intelligence Artificielle";

        mockMvc.perform(multipart("/profil/modifier")
                        .file(photoFile)
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("nomEtudiant", "Durand")
                        .param("prenomEtudiant", "Alice")
                        .param("emailEtudiant", "alice.durand@example.com")
                        .param("dateNaissanceEtudiant", "2001-05-21")
                        .param("sexeEtudiant", "F")
                        .param("descriptionEtudiant", "Passionnée de tech")
                        .param("universites", nouvelleUniversite)
                        .param("centresInteret", nouvelInteret)
                        .flashAttr("etudiantForm", new Etudiant())) // 模拟 @ModelAttribute
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("success", "Profil mis à jour avec succès."));


        Etudiant updated = etudiantRepository.findById(etudiant.getIdEtudiant()).orElseThrow();

        assertEquals("Durand", updated.getNomEtudiant());
        assertEquals("Alice", updated.getPrenomEtudiant());
        assertEquals("alice.durand@example.com", updated.getEmailEtudiant());
        assertEquals(LocalDate.of(2001, 5, 21), updated.getDateNaissanceEtudiant());
        assertEquals("F", updated.getSexeEtudiant());
        assertEquals("Passionnée de tech", updated.getDescriptionEtudiant());
        assertTrue(updated.getPhotoEtudiant().startsWith("data:image/jpeg;base64,"));
        assertTrue(updated.getUniversites().stream()
                .anyMatch(u -> u.getNomUniv().equalsIgnoreCase(nouvelleUniversite)));
        assertTrue(updated.getCentresInteret().stream()
                .anyMatch(c -> c.getNomCentreInteret().equalsIgnoreCase(nouvelInteret)));
    }
    /** Vérifie la modification du profil sans université ni centre d’intérêt. */
    @Test
    void testSaveProfile_SansUniversitesNiInterets() throws Exception {

        Universite u = new Universite();
        u.setNomUniv("Ancienne Université");
        u = universiteRepository.save(u);

        CentreInteret c = new CentreInteret();
        c.setNomCentreInteret("Ancien Intérêt");
        c = centreInteretRepository.save(c);


        etudiant.setUniversites(new ArrayList<>(List.of(u)));
        etudiant.setCentresInteret(new ArrayList<>(List.of(c)));
        etudiant = etudiantRepository.save(etudiant);

        MockMultipartFile photoFile = new MockMultipartFile("photo", "", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/profil/modifier")
                        .file(photoFile)
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("nomEtudiant", "Nouvel")
                        .param("prenomEtudiant", "Utilisateur")
                        .param("emailEtudiant", "nouveau@example.com")
                        .param("dateNaissanceEtudiant", "2002-02-02")
                        .param("sexeEtudiant", "M")
                        .param("descriptionEtudiant", "Remise à zéro")
                        .flashAttr("etudiantForm", new Etudiant()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("success", "Profil mis à jour avec succès."));

        Etudiant updated = etudiantRepository.findById(etudiant.getIdEtudiant()).orElseThrow();


        assertEquals(0, updated.getUniversites().size(), "Universités doivent être vidées");
        assertEquals(0, updated.getCentresInteret().size(), "Centres d’intérêt doivent être vidés");
    }

    /** Vérifie que publier un post échoue si l’utilisateur n’est pas connecté. */
    @Test
    void testPublierPost_NonConnecte() throws Exception {
        mockMvc.perform(multipart("/profil/publier")
                        .param("contenu", "Test sans login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Veuillez vous connecter pour publier."));
    }
    /** Vérifie que le contenu vide sans image empêche la publication. */
    @Test
    void testPublierPost_ContenuVideEtSansImage() throws Exception {
        mockMvc.perform(multipart("/profil/publier")
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("contenu", " ")) // 空格内容
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Le contenu ne peut pas être vide."));
    }
    /** Vérifie que publier avec plus de 3 images est refusé. */
    @Test
    void testPublierPost_TropDImages() throws Exception {

        MockMultipartFile file1 = new MockMultipartFile("images", "img1.jpg", "image/jpeg", "data1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "img2.jpg", "image/jpeg", "data2".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "img3.jpg", "image/jpeg", "data3".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("images", "img4.jpg", "image/jpeg", "data4".getBytes());


        MockMultipartHttpServletRequestBuilder request = multipart("/profil/publier");


        request.file(file1).file(file2).file(file3).file(file4);


        request.param("contenu", "Post avec trop d’images");
        request.sessionAttr("etudiantConnecte", etudiant);


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Maximum 3 images autorisées."));
    }

    /** Vérifie qu’un post est publié avec succès avec contenu et image. */
    @Test
    void testPublierPost_SuccesAvecImageEtContenu() throws Exception {
        MockMultipartFile image = new MockMultipartFile("images", "test.jpg", "image/jpeg", "image-data".getBytes());

        mockMvc.perform(multipart("/profil/publier")
                        .file(image)
                        .param("contenu", "Mon premier post")
                        .param("estPublic", "true")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("success", "Publication réussie !"));


        List<Post> posts = postRepository.findByEtudiantOrderByDatePublicationPostDesc(etudiant);
        assertFalse(posts.isEmpty());
        assertEquals("Mon premier post", posts.get(0).getContenuPost());
        assertTrue(posts.get(0).isEstPublicPost());
        assertFalse(posts.get(0).getUrlsPhotosPost().isEmpty());
    }
    /** Vérifie la gestion des erreurs lors de l’envoi des images. */
    @Test
    void testPublierPost_ErreurUploadImage() throws Exception {

        MultipartFile realFile = new MockMultipartFile("images", "img.jpg", "image/jpeg", "data".getBytes());


        MultipartFile spyFile = spy(realFile);
        doThrow(new IOException("Fake I/O error")).when(spyFile).transferTo(any(File.class));


        mockMvc.perform(multipart("/profil/publier")
                        .file((MockMultipartFile) spyFile)
                        .param("contenu", "Test image erreur")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Erreur lors de l'envoi des images."));
    }
    /** Vérifie qu’un utilisateur non connecté ne peut pas republier. */
    @Test
    void testRepublier_NonConnecte() throws Exception {
        mockMvc.perform(post("/profil/republication")
                        .param("postId", post.getIdPost().toString())
                        .param("commentaire", "Super !")
                        .param("estPublic", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Veuillez vous connecter pour republier."));
    }
    /** Vérifie que republier un post inexistant affiche une erreur. */
    @Test
    void testRepublier_PostInexistant() throws Exception {
        mockMvc.perform(post("/profil/republication")
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("postId", "999999")
                        .param("commentaire", "Je republie !")
                        .param("estPublic", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Publication introuvable."));
    }
    /** Vérifie qu’un post peut être republicé avec succès. */
    @Test
    void testRepublier_Succes() throws Exception {
        mockMvc.perform(post("/profil/republication")
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("postId", post.getIdPost().toString())
                        .param("commentaire", "Je republie ce super post !")
                        .param("estPublic", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("success", "Républication réussie !"));


        Optional<Republier> opt = republierRepository.findById(new RepublierId(post.getIdPost(), etudiant.getIdEtudiant()));
        assertTrue(opt.isPresent(), "La républication doit être présente en base.");
        assertEquals("Je republie ce super post !", opt.get().getCommentaireRepublication());
    }
    /** Vérifie qu’un utilisateur non connecté ne peut pas commenter. */
    @Test
    void testCommenter_NonConnecte() throws Exception {
        mockMvc.perform(post("/profil/commenter")
                        .param("postId", post.getIdPost().toString())
                        .param("commentaire", "Super !"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()))
                .andExpect(flash().attribute("error", "Veuillez vous connecter pour commenter."));
    }
    /** Vérifie que commenter un post inexistant affiche une erreur. */
    @Test
    void testCommenter_PostInexistant() throws Exception {
        mockMvc.perform(post("/profil/commenter")
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("postId", "999999")
                        .param("commentaire", "Trop bien !"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Publication introuvable."));
    }
    /** Vérifie qu’un étudiant connecté peut commenter un post. */
    @Test
    void testCommenter_Succes() throws Exception {
        mockMvc.perform(post("/profil/commenter")
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("postId", post.getIdPost().toString())
                        .param("commentaire", "Très bon post !"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()))
                .andExpect(flash().attribute("success", "Commentaire publié !"));


        List<Commenter> commentaires = commenterRepository.findByPostOrderByDateHeureCommentaireDesc(post);

        boolean trouve = commentaires.stream().anyMatch(c ->
                c.getCommentaire().equals("Très bon post !") &&
                        c.getEtudiant().getIdEtudiant().equals(etudiant.getIdEtudiant())
        );

        assertTrue(trouve, "Le commentaire 'Très bon post !' devrait être enregistré.");
    }

    /** Vérifie que seul un utilisateur connecté peut supprimer un commentaire. */
    @Test
    void testSupprimerCommentaire_NonConnecte() throws Exception {
        mockMvc.perform(post("/profil/commenter/supprimer")
                        .param("postId", post.getIdPost().toString())
                        .param("idCommentaire", commentaire.getIdCommentaire().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()))
                .andExpect(flash().attribute("error", "Veuillez vous connecter pour supprimer un commentaire."));
    }

    /** Vérifie que la suppression échoue si le commentaire n’existe pas. */
    @Test
    void testSupprimerCommentaire_CommentaireInexistant() throws Exception {
        mockMvc.perform(post("/profil/commenter/supprimer")
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("postId", post.getIdPost().toString())
                        .param("idCommentaire", "999999")) // 不存在的评论ID
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()))
                .andExpect(flash().attribute("error", "Commentaire introuvable."));
    }
    /** Vérifie qu’un utilisateur ne peut supprimer que ses propres commentaires. */
    @Test
    void testSupprimerCommentaire_PasProprietaire() throws Exception {
        mockMvc.perform(post("/profil/commenter/supprimer")
                        .sessionAttr("etudiantConnecte", cible) // cible ≠ commentaire 作者
                        .param("postId", post.getIdPost().toString())
                        .param("idCommentaire", commentaire.getIdCommentaire().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()))
                .andExpect(flash().attribute("error", "Vous ne pouvez supprimer que vos propres commentaires."));
    }
    /** Vérifie qu’un commentaire est supprimé avec succès. */
    @Test
    void testSupprimerCommentaire_Succes() throws Exception {
        mockMvc.perform(post("/profil/commenter/supprimer")
                        .sessionAttr("etudiantConnecte", etudiant) // etudiant 是评论作者
                        .param("postId", post.getIdPost().toString())
                        .param("idCommentaire", commentaire.getIdCommentaire().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()))
                .andExpect(flash().attribute("success", "Commentaire supprimé !"));

        assertFalse(commenterRepository.findById(commentaire.getIdCommentaire()).isPresent(),
                "Le commentaire devrait être supprimé.");
    }
    /** Vérifie que seul un utilisateur connecté peut aimer une publication. */
    @Test
    void testToggleLike_NonConnecte() throws Exception {
        mockMvc.perform(get("/profil/reaction/like")
                        .param("postId", post.getIdPost().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()))
                .andExpect(flash().attribute("error", "Veuillez vous connecter pour aimer une publication."));
    }
    /** Vérifie que liker un post inexistant affiche une erreur. */
    @Test
    void testToggleLike_PublicationIntrouvable() throws Exception {
        mockMvc.perform(get("/profil/reaction/like")
                        .param("postId", "9999")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-9999"))
                .andExpect(flash().attribute("error", "Publication introuvable."));
    }
    /** Vérifie que l’ajout d’un like fonctionne. */
    @Test
    void testToggleLike_AjoutLike() throws Exception {
        mockMvc.perform(get("/profil/reaction/like")
                        .param("postId", post.getIdPost().toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()))
                .andExpect(flash().attribute("success", "Publication aimée !"));

        Optional<Reagir> like = reagirRepository.findByPostIdAndEtudiantIdAndStatut(
                post.getIdPost(), etudiant.getIdEtudiant(), "Like");
        assertTrue(like.isPresent());
    }

    /** Vérifie que le retrait d’un like fonctionne. */
    @Test
    void testToggleLike_RetraitLike() throws Exception {

        Reagir like = new Reagir();
        like.setPost(post);
        like.setEtudiant(etudiant);
        like.getReagirId().setStatut("Like");
        reagirRepository.save(like);

        mockMvc.perform(get("/profil/reaction/like")
                        .param("postId", post.getIdPost().toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()))
                .andExpect(flash().attribute("success", "Like retiré."));

        Optional<Reagir> deleted = reagirRepository.findByPostIdAndEtudiantIdAndStatut(
                post.getIdPost(), etudiant.getIdEtudiant(), "Like");
        assertTrue(deleted.isEmpty());
    }
    /** Vérifie qu’un favori peut être ajouté puis supprimé. */
    @Test
    void testToggleFavori_AjouterEtSupprimer() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);


        Optional<Reagir> avant = reagirRepository.findByPostIdAndEtudiantIdAndStatut(post.getIdPost(), etudiant.getIdEtudiant(), "Favori");
        assertTrue(avant.isEmpty());


        mockMvc.perform(get("/profil/reaction/favori")
                        .param("postId", post.getIdPost().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()));

        Optional<Reagir> apresAjout = reagirRepository.findByPostIdAndEtudiantIdAndStatut(post.getIdPost(), etudiant.getIdEtudiant(), "Favori");
        assertTrue(apresAjout.isPresent());


        mockMvc.perform(get("/profil/reaction/favori")
                        .param("postId", post.getIdPost().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()));

        Optional<Reagir> apresSuppression = reagirRepository.findByPostIdAndEtudiantIdAndStatut(post.getIdPost(), etudiant.getIdEtudiant(), "Favori");
        assertTrue(apresSuppression.isEmpty());
    }
    /** Vérifie que seul un utilisateur connecté peut ajouter aux favoris. */
    @Test
    void testToggleFavori_EtudiantNonConnecte() throws Exception {
        mockMvc.perform(get("/profil/reaction/favori")
                        .param("postId", post.getIdPost().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-" + post.getIdPost()));
    }
    /** Vérifie que l’ajout échoue si la publication est introuvable. */
    @Test
    void testToggleFavori_PostIntrouvable() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);

        mockMvc.perform(get("/profil/reaction/favori")
                        .param("postId", "999999") // 假设不存在的ID
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil#post-999999"));
    }
    /** Vérifie que l’auteur peut supprimer son post avec tous les liens associés. */
    @Test
    public void testSupprimerPost_parAuteur() throws Exception {

        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Post à supprimer");
        post.setDatePublicationPost(LocalDateTime.now());
        post = postRepository.save(post);


        Commenter c = new Commenter();
        c.setEtudiant(etudiant);
        c.setPost(post);
        c.setCommentaire("Commentaire test");
        c.setDateHeureCommentaire(LocalDateTime.now());
        commenterRepository.save(c);

        Reagir r = new Reagir();
        r.setPost(post);
        r.setEtudiant(etudiant);
        r.getReagirId().setStatut("Like");
        reagirRepository.save(r);

        Republier republier = new Republier();
        republier.setEtudiant(etudiant);
        republier.setPost(post);
        republier.setDateRepublication(LocalDateTime.now());
        republierRepository.save(republier);


        mockMvc.perform(post("/profil/post/supprimer")
                        .param("postId", post.getIdPost().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"));


        assertFalse(postRepository.findById(post.getIdPost()).isPresent());

        assertTrue(commenterRepository.findByPostOrderByDateHeureCommentaireDesc(post).isEmpty());

        assertTrue(reagirRepository.findByPostIdAndEtudiantIdAndStatut(
                post.getIdPost(), etudiant.getIdEtudiant(), "Like"
        ).isEmpty());

        assertFalse(republierRepository.findByPostAndEtudiant(post, etudiant).isPresent());
    }













}


