package com.ut1.miage.appRS.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.transaction.annotation.Transactional;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.EtudiantRepository;

/**
 * Tests d’intégration pour le contrôleur {@link ConnectionController}.
 * Couvre l’inscription, la connexion et la déconnexion des étudiants.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EtudiantRepository etudiantRepository;

    private Etudiant etudiant;

    @BeforeEach
    public void setUp() {
        etudiant = new Etudiant();
        etudiant.setNomEtudiant("Test");
        etudiant.setPrenomEtudiant("Utilisateur");
        etudiant.setEmailEtudiant("test@example.com");
        etudiant.setMotDePass("123456");
        etudiant.setDateNaissanceEtudiant(LocalDate.of(2000, 1, 1));
        etudiantRepository.save(etudiant);
    }

    /**
     * Vérifie que le formulaire d'inscription s'affiche correctement.
     */
    @Test
    public void testAfficherFormulaireInscription() throws Exception {
        mockMvc.perform(get("/inscription"))
                .andExpect(status().isOk())
                .andExpect(view().name("inscription"))
                .andExpect(model().attributeExists("etudiant"));
    }

    /**
     * Vérifie l'inscription d'un nouvel utilisateur.
     */
    @Test
    public void testInscriptionEtudiant() throws Exception {
        mockMvc.perform(post("/inscription")
                        .param("emailEtudiant", "nouveau@example.com")
                        .param("motDePass", "abcdef")
                        .param("nomEtudiant", "Nouveau")
                        .param("prenomEtudiant", "Étudiant"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmationInscription"));
    }

    /**
     * Vérifie qu'on ne peut pas s'inscrire avec un email déjà utilisé.
     */
    @Test
    public void testInscriptionEmailExistant() throws Exception {
        mockMvc.perform(post("/inscription")
                        .param("emailEtudiant", "test@example.com")  // déjà utilisé
                        .param("motDePass", "abcdef")
                        .param("nomEtudiant", "Dupont")
                        .param("prenomEtudiant", "Jean"))
                .andExpect(status().isOk())
                .andExpect(view().name("inscription"))
                .andExpect(model().attributeExists("erreur"));
    }

    /**
     * Vérifie que le formulaire de connexion est accessible.
     */
    @Test
    public void testAfficherFormulaireConnexion() throws Exception {
        mockMvc.perform(get("/connexion"))
                .andExpect(status().isOk())
                .andExpect(view().name("connexion"));
    }

    /**
     * Vérifie qu’un utilisateur peut se connecter avec des identifiants valides.
     */
    @Test
    public void testConnexionValide() throws Exception {
        mockMvc.perform(post("/connexion")
                        .param("email", "test@example.com")
                        .param("motDePasse", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    /**
     * Vérifie que la connexion échoue avec un mauvais mot de passe.
     */
    @Test
    public void testConnexionMotDePasseInvalide() throws Exception {
        mockMvc.perform(post("/connexion")
                        .param("email", "test@example.com")
                        .param("motDePasse", "mauvais"))
                .andExpect(status().isOk())
                .andExpect(view().name("connexion"))
                .andExpect(model().attributeExists("erreur"));
    }

    /**
     * Vérifie que la connexion échoue si l'email n'existe pas.
     */
    @Test
    public void testConnexionEmailInexistant() throws Exception {
        mockMvc.perform(post("/connexion")
                        .param("email", "inexistant@example.com")
                        .param("motDePasse", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("connexion"))
                .andExpect(model().attributeExists("erreur"));
    }

    /**
     * Vérifie que la déconnexion supprime la session et redirige vers l’accueil.
     */
    @Test
    public void testDeconnexion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);

        mockMvc.perform(get("/deconnexion").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
