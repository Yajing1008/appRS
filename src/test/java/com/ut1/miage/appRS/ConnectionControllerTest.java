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
}