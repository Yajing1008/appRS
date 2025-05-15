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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // pour annuler les changements à la fin de chaque test
class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EtudiantRepository etudiantRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanDB() {
        etudiantRepository.deleteAll();
    }

    // [Test48.1]
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

    // [Test48.2]
    @Test
    void testEmailDejaUtilise() throws Exception {
        // Pré-insère un étudiant
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

    @Test
    void testAfficherFormulaireInscription() throws Exception {
        mockMvc.perform(get("/inscription"))
                .andExpect(status().isOk())
                .andExpect(view().name("inscription"))
                .andExpect(model().attributeExists("etudiant"));
}
}
