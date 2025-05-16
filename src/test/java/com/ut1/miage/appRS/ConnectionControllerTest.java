package com.ut1.miage.appRS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour le contrôleur de connexion et d'inscription.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Annule les changements à la fin de chaque test
class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EtudiantRepository etudiantRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Nettoie la base de données avant chaque test.
     */
    @BeforeEach
    void cleanDB() {
        etudiantRepository.deleteAll();
    }

    /**
     * [Test48.1]
     * Teste l'inscription d'un nouvel étudiant avec des informations valides.
     * Vérifie que la vue de confirmation est retournée.
     */
    @Test
    void testInscriptionReussie() throws Exception {
        mockMvc.perform(post("/inscription")
                        .param("prenomEtudiant", "Jean")
                        .param("nomEtudiant", "Dupont")
                        .param("emailEtudiant", "jean@example.com")
                        .param("motDePass", "motdepasse"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmationInscription"));
    }

    /**
     * [Test48.2]
     * Teste l'inscription avec une adresse email déjà utilisée.
     * Vérifie que le formulaire est réaffiché avec un message d'erreur.
     */
    @Test
    void testEmailDejaUtilise() throws Exception {
        // Insère un étudiant existant
        Etudiant e = new Etudiant();
        e.setPrenomEtudiant("Marie");
        e.setNomEtudiant("Curie");
        e.setEmailEtudiant("marie@example.com");
        e.setMotDePass("securite");
        etudiantRepository.save(e);

        mockMvc.perform(post("/inscription")
                        .param("prenomEtudiant", "Marie")
                        .param("nomEtudiant", "Curie")
                        .param("emailEtudiant", "marie@example.com")
                        .param("motDePass", "autrepass"))
                .andExpect(status().isOk())
                .andExpect(view().name("inscription"))
                .andExpect(model().attributeExists("erreur"));
    }

    /**
     * Vérifie que l'affichage du formulaire d'inscription fonctionne correctement.
     */
    @Test
    void testAfficherFormulaireInscription() throws Exception {
        mockMvc.perform(get("/inscription"))
                .andExpect(status().isOk())
                .andExpect(view().name("inscription"))
                .andExpect(model().attributeExists("etudiant"));
    }

    /**
     * [Test49.1]
     * Teste la connexion réussie avec email et mot de passe valides.
     * Vérifie la redirection vers la page d’accueil.
     */
   @Test
   void testConnexionReussie() throws Exception {
       Etudiant etudiant = new Etudiant();
       etudiant.setPrenomEtudiant("Pierre");
       etudiant.setNomEtudiant("Martin");
       etudiant.setEmailEtudiant("pierre@example.com");
       etudiant.setMotDePass("secret123");
       etudiantRepository.save(etudiant);

       mockMvc.perform(post("/connexion")
                       .param("email", "pierre@example.com")
                       .param("motDePasse", "secret123"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/"));
   }

   /**
    * [Test49.2]
    * Teste la connexion échouée avec un mauvais mot de passe.
    * Vérifie que le formulaire est réaffiché avec une erreur.
    */
   @Test
   void testConnexionMotDePasseInvalide() throws Exception {
       Etudiant etudiant = new Etudiant();
       etudiant.setPrenomEtudiant("Alice");
       etudiant.setNomEtudiant("Durand");
       etudiant.setEmailEtudiant("alice@example.com");
       etudiant.setMotDePass("motcorrect");
       etudiantRepository.save(etudiant);

       mockMvc.perform(post("/connexion")
                       .param("email", "alice@example.com")
                       .param("motDePasse", "mauvais"))
               .andExpect(status().isOk())
               .andExpect(view().name("connexion"))
               .andExpect(model().attributeExists("erreur"));
   }

   /**
    * [Test49.3]
    * Teste la connexion avec une adresse e-mail inexistante.
    */
   @Test
   void testConnexionEmailInexistant() throws Exception {
       mockMvc.perform(post("/connexion")
                       .param("email", "inexistant@example.com")
                       .param("motDePasse", "peuimporte"))
               .andExpect(status().isOk())
               .andExpect(view().name("connexion"))
               .andExpect(model().attributeExists("erreur"));
   }

   /**
    * [Test50.1]
    * Teste la déconnexion d’un étudiant connecté.
    * Vérifie que la session est invalidée et qu’on est redirigé.
    */
   @Test
   void testDeconnexion() throws Exception {
       // Simuler une session active avec un étudiant
       Etudiant etudiant = new Etudiant();
       etudiant.setPrenomEtudiant("Luc");
       etudiant.setNomEtudiant("Petit");
       etudiant.setEmailEtudiant("luc@example.com");
       etudiant.setMotDePass("motluc");
       etudiantRepository.save(etudiant);

       mockMvc.perform(get("/deconnexion"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/"));
   }
}