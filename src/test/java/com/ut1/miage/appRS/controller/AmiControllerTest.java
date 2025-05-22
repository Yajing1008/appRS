package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.DemandeAmi;
import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.repository.DemandeAmiRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour le contrôleur {@link AmiController}.
 * Ce test utilise MockMvc pour simuler les appels HTTP.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AmiControllerTest {

    /** Objet MockMvc utilisé pour simuler des requêtes HTTP dans les tests. */
    @Autowired
    private MockMvc mockMvc;

    /** Référentiel d'accès aux données des étudiants pour les tests. */
    @Autowired
    private EtudiantRepository etudiantRepository;

    /** Référentiel d'accès aux demandes d'ami pour les tests. */
    @Autowired
    private DemandeAmiRepository demandeAmiRepository;

    /** Étudiant jouant le rôle de demandeur dans les tests. */
    private Etudiant demandeur;

    /** Étudiant jouant le rôle de receveur dans les tests. */
    private Etudiant receveur;

    /** Session simulée pour représenter un étudiant connecté. */
    private MockHttpSession session;


    /**
     * Initialise les étudiants et la session pour les tests.
     */
    @BeforeEach
    public void setUp() {
        demandeur = new Etudiant();
        demandeur.setNomEtudiant("Dupont");
        demandeur.setPrenomEtudiant("Jean");
        demandeur.setEmailEtudiant("jean@example.com");
        demandeur.setDateNaissanceEtudiant(LocalDate.of(2000, 1, 1));
        demandeur = etudiantRepository.save(demandeur);

        receveur = new Etudiant();
        receveur.setNomEtudiant("Martin");
        receveur.setPrenomEtudiant("Paul");
        receveur.setEmailEtudiant("paul@example.com");
        receveur.setDateNaissanceEtudiant(LocalDate.of(2000, 2, 2));
        receveur = etudiantRepository.save(receveur);

        session = new MockHttpSession();
        session.setAttribute("etudiantConnecte", demandeur);
    }

    /**
     * Vérifie que la page /ami est accessible et retourne le bon template.
     */
    @Test
    public void testAfficherPageAmi() throws Exception {
        mockMvc.perform(get("/ami").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("ami"));
    }

    /**
     * Vérifie qu'une demande d'ami peut être envoyée.
     */
    @Test
    public void testEnvoyerDemande() throws Exception {
        mockMvc.perform(get("/envoyerDemande")
                        .param("idReceveur", receveur.getIdEtudiant().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ami"));
    }

    /**
     * Vérifie qu'une demande d'ami peut être acceptée.
     */
    @Test
    public void testAccepterDemande() throws Exception {
        DemandeAmi demande = new DemandeAmi();
        demande.setDemandeur(demandeur);
        demande.setReceveur(receveur);
        demande.setStatut("EN_ATTENTE");
        demande = demandeAmiRepository.save(demande);

        session.setAttribute("etudiantConnecte", receveur);

        mockMvc.perform(get("/accepterDemande")
                        .param("idDemande", demande.getIdDemande().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ami"));
    }

    /**
     * Vérifie qu'une demande d'ami peut être refusée.
     */
    @Test
    public void testRefuserDemande() throws Exception {
        DemandeAmi demande = new DemandeAmi();
        demande.setDemandeur(demandeur);
        demande.setReceveur(receveur);
        demande.setStatut("EN_ATTENTE");
        demande = demandeAmiRepository.save(demande);

        session.setAttribute("etudiantConnecte", receveur);

        mockMvc.perform(get("/refuserDemande")
                        .param("idDemande", demande.getIdDemande().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ami"));
    }

    /**
     * Vérifie qu'un ami peut être supprimé.
     */
    @Test
    public void testSupprimerAmi() throws Exception {
        // Ajouter les deux comme amis
        demandeur.getAmis().add(receveur);
        receveur.getAmis().add(demandeur);
        etudiantRepository.save(demandeur);
        etudiantRepository.save(receveur);

        mockMvc.perform(get("/supprimerAmi")
                        .param("id", receveur.getIdEtudiant().toString())
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
