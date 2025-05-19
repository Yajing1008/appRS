package com.ut1.miage.appRS;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Groupe;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import com.ut1.miage.appRS.repository.GroupeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GroupeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private GroupeRepository groupeRepository;

    private Etudiant etudiant;

    private MockHttpSession session;

    @BeforeEach
    void setup() {
        groupeRepository.deleteAll();
        etudiantRepository.deleteAll();

        etudiant = new Etudiant();
        etudiant.setPrenomEtudiant("Lucas");
        etudiant.setNomEtudiant("Bertrand");
        etudiant.setEmailEtudiant("lucas@example.com");
        etudiant.setMotDePass("motdepasse");
        etudiantRepository.save(etudiant);

        session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", etudiant);
    }

    /**
     * [Test8.1]
     * Teste l'accès au formulaire de création de groupe lorsqu'un étudiant est connecté.
     */
    @Test
    void testAfficherFormulaireGroupeConnecte() throws Exception {
        mockMvc.perform(get("/groupe/nouveau").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("formulaireGroupe"))
                .andExpect(model().attributeExists("groupe"));
    }

    /**
     * [Test8.2]
     * Vérifie que l'accès au formulaire redirige si l'étudiant n'est pas connecté.
     */
    @Test
    void testAfficherFormulaireGroupeSansConnexion() throws Exception {
        mockMvc.perform(get("/groupe/nouveau"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"));
    }

    /**
     * [Test8.3] Teste la création d'un groupe public avec un étudiant connecté.
     */
    @Test
    void testCreationGroupePublic() throws Exception {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEtudiant("Nom");
        etudiant.setPrenomEtudiant("Prenom");
        etudiant.setEmailEtudiant("public@example.com");
        etudiant.setMotDePass("password");
        etudiantRepository.save(etudiant);

        mockMvc.perform(post("/groupe/nouveau")
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("nomGroupe", "Groupe Java")
                        .param("descriptionGroupe", "Discussions sur Java")
                        .param("estPublicGroupe", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/groupes"));

        List<Groupe> groupes = groupeRepository.findAll();
        assertFalse(groupes.isEmpty());

        Groupe groupe = groupes.get(0);
        assertEquals("Groupe Java", groupe.getNomGroupe());
        assertTrue(Boolean.TRUE.equals(groupe.getEstPublicGroupe())); // ✅ pas de isEstPublicGroupe
    }

    /**
     * [Test8.4] Teste la création d'un groupe privé avec un étudiant connecté.
     */
    @Test
    void testCreationGroupePrive() throws Exception {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEtudiant("Nom");
        etudiant.setPrenomEtudiant("Prenom");
        etudiant.setEmailEtudiant("prive@example.com");
        etudiant.setMotDePass("password");
        etudiantRepository.save(etudiant);

        mockMvc.perform(post("/groupe/nouveau")
                        .sessionAttr("etudiantConnecte", etudiant)
                        .param("nomGroupe", "Groupe Sécurité")
                        .param("descriptionGroupe", "Cyber sécurité et vie privée")
                        .param("estPublicGroupe", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/groupes"));

        List<Groupe> groupes = groupeRepository.findAll();
        assertFalse(groupes.isEmpty());

        Groupe groupe = groupes.get(0);
        assertEquals("Groupe Sécurité", groupe.getNomGroupe());
        assertFalse(Boolean.TRUE.equals(groupe.getEstPublicGroupe()));
    }
}