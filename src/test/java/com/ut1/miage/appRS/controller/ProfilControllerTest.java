package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Post;
import com.ut1.miage.appRS.model.Republier;
import com.ut1.miage.appRS.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.empty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(controllers = ProfilController.class)
class ProfilControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RepublierRepository republierRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    private Etudiant etudiant;

    /**
     * Fournit des beans mock pour remplacer les dépendances de ProfilController.
     */
    @TestConfiguration
    static class MockConfig {
        @Bean
        public PostRepository postRepository() {
            return Mockito.mock(PostRepository.class);
        }

        @Bean
        public RepublierRepository republierRepository() {
            return Mockito.mock(RepublierRepository.class);
        }

        @Bean
        public EtudiantRepository etudiantRepository() {
            return Mockito.mock(EtudiantRepository.class);
        }

        @Bean
        public ReagirRepository reagirRepository() {
            return Mockito.mock(ReagirRepository.class);
        }

        @Bean
        public CommenterRepository commenterRepository() {
            return Mockito.mock(CommenterRepository.class);
        }
    }


    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEtudiant("Dupont");
        etudiant.setPrenomEtudiant("Jean");
        etudiant.setDateNaissanceEtudiant(LocalDate.of(2000, 1, 1));
        etudiant.setEmailEtudiant("jean.dupont@example.com");
    }

    /**
     * Teste le scénario où l'étudiant n'est pas connecté.
     * On s'attend à ce que la page de profil soit affichée avec un message de connexion.
     */
    @Test
    void testAfficherProfil_EtudiantNonConnecte() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/profil").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("messageConnexion"))
                .andExpect(view().name("profil"));
    }

    /**
     * Teste le scénario où l'étudiant est connecté mais n'a publié aucun post ni republication.
     */
    @Test
    void testAfficherProfil_SansPostsNiRepublications() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);

        when(postRepository.findByEtudiantOrderByDatePublicationPostDesc(etudiant)).thenReturn(List.of());
        when(republierRepository.findByEtudiantOrderByDateRepublicationDesc(etudiant)).thenReturn(List.of());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/profil").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("etudiant", etudiant))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("postDates"))
                .andExpect(view().name("profil"));
    }

    /**
     * Teste le scénario où l'étudiant est connecté avec des posts et des republications.
     */
    @Test
    void testAfficherProfil_AvecPostsEtRepublications() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);

        Post post1 = new Post();
        post1.setIdPost(1L);
        post1.setDatePublicationPost(LocalDateTime.now().minusDays(1));
        post1.setEtudiant(etudiant);

        Post post2 = new Post();
        post2.setIdPost(2L);
        post2.setDatePublicationPost(LocalDateTime.now().minusDays(2));
        post2.setEtudiant(etudiant);

        Republier republication = new Republier();
        republication.setPost(post2);
        republication.setEtudiant(etudiant);
        republication.setDateRepublication(LocalDateTime.now());

        when(postRepository.findByEtudiantOrderByDatePublicationPostDesc(etudiant)).thenReturn(List.of(post1));
        when(republierRepository.findByEtudiantOrderByDateRepublicationDesc(etudiant)).thenReturn(List.of(republication));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/profil").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("etudiant", etudiant))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("postDates"))
                .andExpect(view().name("profil"));
    }


    /**
     * Teste l'accès à la page de modification du profil lorsque l'étudiant n'est pas connecté.
     * On attend une vue vide avec des listes vides pour posts et postDates.
     */
    @Test
    void testShowEditForm_EtudiantNonConnecte() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/profil/modifier").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("etudiant"))
                .andExpect(model().attribute("posts", empty()))
                .andExpect(model().attribute("postDates", anEmptyMap()))
                .andExpect(view().name("profil_modifier"));
    }

    /**
     * Teste l'accès à la page de modification du profil avec publications et republications.
     */
    @Test
    void testShowEditForm_AvecPostsEtRepublications() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);

        Post post1 = new Post();
        post1.setIdPost(1L);
        post1.setEtudiant(etudiant);
        post1.setDatePublicationPost(LocalDateTime.now().minusDays(1));

        Post post2 = new Post();
        post2.setIdPost(2L);
        post2.setEtudiant(etudiant);
        post2.setDatePublicationPost(LocalDateTime.now().minusDays(2));

        Republier republication = new Republier();
        republication.setEtudiant(etudiant);
        republication.setPost(post2);
        republication.setDateRepublication(LocalDateTime.now());

        Mockito.when(postRepository.findByEtudiantOrderByDatePublicationPostDesc(etudiant)).thenReturn(List.of(post1));
        Mockito.when(republierRepository.findByEtudiantOrderByDateRepublicationDesc(etudiant)).thenReturn(List.of(republication));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/profil/modifier").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("etudiant", etudiant))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("postDates"))
                .andExpect(view().name("profil_modifier"));
    }

    /**
     * Teste la mise à jour complète du profil avec une nouvelle photo.
     */
    @Test
    void testSaveProfile_MiseAJourAvecPhoto() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();

        when(etudiantRepository.findByEmailEtudiant(etudiant.getEmailEtudiant()))
                .thenReturn(Optional.of(etudiant));

        MockMultipartFile photoFile = new MockMultipartFile(
                "photo", "photo.jpg", "image/jpeg", "fake-image".getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/profil/modifier")
                        .file(photoFile)
                        .param("emailEtudiant", etudiant.getEmailEtudiant())
                        .param("nomEtudiant", "Durand")
                        .param("prenomEtudiant", "Marc")
                        .param("dateNaissanceEtudiant", "1999-12-31")
                        .param("sexeEtudiant", "Homme")
                        .param("descriptionEtudiant", "Nouveau profil")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"));

        // Vérifie que l'étudiant a été sauvegardé
        verify(etudiantRepository, times(1)).save(any(Etudiant.class));
    }


/**
 * Teste le cas où l'étudiant n'est pas trouvé dans la base (aucune mise à jour).
 */
@Test
void testSaveProfile_EtudiantInexistant() throws Exception {
    // Given
    String emailInexistant = "inconnu@example.com";
    when(etudiantRepository.findByEmailEtudiant(emailInexistant))
            .thenReturn(Optional.empty());

    // Simule un fichier vide (obligatoire même为空)
    MockMultipartFile photoFile = new MockMultipartFile(
            "photo", "", "image/jpeg", new byte[0]
    );

    // When & Then
    mockMvc.perform(multipart("/profil/modifier")
                    .file(photoFile)
                    .param("emailEtudiant", emailInexistant)
                    .param("nomEtudiant", "Durand")
                    .param("prenomEtudiant", "Marc")
                    .param("dateNaissanceEtudiant", "1999-12-31")
                    .param("sexeEtudiant", "Homme")
                    .param("descriptionEtudiant", "Profil inexistant")
                    .session(new MockHttpSession()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/profil"));

    // Vérifie qu'aucune sauvegarde n'a été effectuée
    verify(etudiantRepository, never()).save(any(Etudiant.class));
}

    /**
     * Teste la publication d'un post avec contenu et image par un étudiant connecté.
     */
    @Test
    void testPublierPost_Connecte_AvecContenuEtImage() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);

        MockMultipartFile image = new MockMultipartFile(
                "images", "image.jpg", "image/jpeg", "fake-image-content".getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/profil/publier")
                        .file(image)
                        .param("contenu", "Bonjour à tous !")
                        .param("estPublic", "true")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attributeExists("success"));

        // Vérifie que le post a été sauvegardé
        verify(postRepository, times(1)).save(any(Post.class));
    }


    /**
     * Teste la tentative de publication sans être connecté (doit échouer).
     */
    @Test
    void testPublierPost_NonConnecte() throws Exception {
        MockMultipartFile emptyImage = new MockMultipartFile("images", "", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/profil/publier")
                        .file(emptyImage)
                        .param("contenu", "Bonjour à tous !")
                        .param("estPublic", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Veuillez vous connecter pour publier."));

        // ✅ 强调断言行为，而不是 save()
        verifyNoInteractions(postRepository);
    }



    @Test
    void testPublierPost_ContenuVideEtSansImage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);

        mockMvc.perform(multipart("/profil/publier")
                        .param("contenu", "   ")
                        .param("estPublic", "false")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Le contenu ne peut pas être vide."));

        verifyNoInteractions(postRepository);  // ✅ 更安全，不会误报
    }




    @Test
    void testPublierPost_TropDImages() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);

        List<MockMultipartFile> images = List.of(
                new MockMultipartFile("images", "1.jpg", "image/jpeg", "1".getBytes()),
                new MockMultipartFile("images", "2.jpg", "image/jpeg", "2".getBytes()),
                new MockMultipartFile("images", "3.jpg", "image/jpeg", "3".getBytes()),
                new MockMultipartFile("images", "4.jpg", "image/jpeg", "4".getBytes())
        );

        // ✅ 正确顺序：先用 multipart 创建请求构建器
        MockMultipartHttpServletRequestBuilder request = multipart("/profil/publier");

        // ✅ 依次添加参数、文件、session
        request.param("contenu", "Test trop d’images");
        request.param("estPublic", "true");
        images.forEach(request::file);
        request.session(session); // ⚠️ 不要链式写在 .param() 后面！

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("error", "Maximum 3 images autorisées."));

        verifyNoInteractions(postRepository); // ✅ 避免 save 被误调用
    }
















}




