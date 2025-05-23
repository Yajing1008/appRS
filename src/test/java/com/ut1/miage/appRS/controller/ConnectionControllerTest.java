package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d’intégration pour le contrôleur {@link ConnectionController}.
 * Couvre l’inscription, la connexion et la déconnexion des étudiants.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ConnectionControllerTest {

    /** Objet permettant de simuler des requêtes HTTP dans les tests. */
    @Autowired
    private MockMvc mockMvc;

    /** Référentiel pour accéder aux données des étudiants pendant les tests. */
    @Autowired
    private EtudiantRepository etudiantRepository;

    /** Étudiant utilisé comme jeu de données pour les tests. */
    private Etudiant etudiant;

    /**
     * Initialise un étudiant de test avant chaque méthode de test.
     *
     * L’étudiant est enregistré dans la base de données en mémoire avec des informations fictives.
     */
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
    /**
     * Vérifie que la page de réinitialisation du mot de passe s'affiche correctement.
     */
    @Test
    void testAfficherFormulaireReinitialisationMotDePasse() throws Exception {
        mockMvc.perform(get("/motdepasse/reinitialiser"))
                .andExpect(status().isOk())
                .andExpect(view().name("motdepasse_modifier"));
    }
    /**
     * Vérifie qu'un message d'erreur s'affiche si l'e-mail est inconnu.
     */
    @Test
    void testReinitialiserMotDePasse_emailInconnu() throws Exception {
        mockMvc.perform(post("/motdepasse/reinitialiser")
                        .param("email", "unknown@example.com")
                        .param("nouveauMotDePasse", "newpass123")
                        .param("confirmation", "newpass123"))
                .andExpect(status().isOk())
                .andExpect(view().name("motdepasse_modifier"))
                .andExpect(model().attributeExists("erreur"))
                .andExpect(model().attribute("erreur", "Aucun compte trouvé avec cette adresse e-mail."));
    }
    /**
     * Vérifie qu'un message d'erreur s'affiche si les mots de passe ne correspondent pas.
     */
    @Test
    void testReinitialiserMotDePasse_motDePasseNonConfirme() throws Exception {
        mockMvc.perform(post("/motdepasse/reinitialiser")
                        .param("email", "test@example.com")
                        .param("nouveauMotDePasse", "newpass123")
                        .param("confirmation", "differentpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("motdepasse_modifier"))
                .andExpect(model().attributeExists("erreur"))
                .andExpect(model().attribute("erreur", "La confirmation ne correspond pas au nouveau mot de passe."));
    }
    /**
     * Vérifie que le mot de passe est réinitialisé avec succès quand toutes les conditions sont remplies.
     */
    @Test
    void testReinitialiserMotDePasse_succes() throws Exception {
        mockMvc.perform(post("/motdepasse/reinitialiser")
                        .param("email", "test@example.com")
                        .param("nouveauMotDePasse", "newpassword")
                        .param("confirmation", "newpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("motdepasse_modifier"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Mot de passe réinitialisé avec succès."));


        Etudiant updated = etudiantRepository.findByEmailEtudiant("test@example.com").get();
        assert(updated.getMotDePass().equals("newpassword"));
    }


}
