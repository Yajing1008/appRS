package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.*;
import com.ut1.miage.appRS.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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



@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RepublierRepository republierRepository;

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private CentreInteretRepository centreInteretRepository;

    private Etudiant etudiant;
    private Etudiant cible;
    private Post post;

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

    }

    @Test
    void testAfficherProfil_ConnecteEtProprietaire() throws Exception {
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        mockMvc.perform(get("/profil").sessionAttr("etudiantConnecte", savedEtudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attribute("etudiant", hasProperty("idEtudiant", is(savedEtudiant.getIdEtudiant()))))
                .andExpect(model().attribute("isOwner", true));
    }

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


    @Test
    void testAfficherProfilParId_NonAmi() throws Exception {
        mockMvc.perform(get("/profil/" + cible.getIdEtudiant())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attribute("isOwner", false))
                .andExpect(model().attribute("isFriend", false));
    }

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

    @Test
    void testShowEditForm_ConnecteAvecDonnees() throws Exception {
        // 添加帖文
        Post post = new Post();
        post.setEtudiant(etudiant);
        post.setContenuPost("Bonjour !");
        post.setDatePublicationPost(LocalDateTime.of(2025, 5, 1, 10, 0));
        post = postRepository.save(post);

        // 添加大学
        Universite universite = new Universite();
        universite.setNomUniv("Université de Test");
        universite = universiteRepository.save(universite);

        // 添加兴趣
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


    @Test
    void testPublierPost_NonConnecte() throws Exception {
        mockMvc.perform(multipart("/profil/publier")
                        .param("contenu", "Test sans login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Veuillez vous connecter pour publier."));
    }

    @Test
    void testPublierPost_ContenuVideEtSansImage() throws Exception {
        mockMvc.perform(multipart("/profil/publier")
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("contenu", " ")) // 空格内容
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Le contenu ne peut pas être vide."));
    }

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












}


