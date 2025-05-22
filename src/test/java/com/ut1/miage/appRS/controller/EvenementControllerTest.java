package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.Etudiant;
import com.ut1.miage.appRS.model.Evenement;
import com.ut1.miage.appRS.repository.EtudiantRepository;
import com.ut1.miage.appRS.repository.EvenementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EvenementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private EvenementRepository evenementRepository;
    @Autowired private EvenementController evenementController;

    private Etudiant etudiant;
    private Etudiant cible;
    private Evenement createdEvent;
    private Evenement joinedEvent;

    @BeforeEach
    void setUp() {

        etudiant = new Etudiant();
        etudiant.setNomEtudiant("Dupont");
        etudiant.setPrenomEtudiant("Jean");
        etudiant.setEmailEtudiant("jean@example.com");
        etudiant.setDateNaissanceEtudiant(LocalDate.of(2000, 1, 1));
        etudiant = etudiantRepository.save(etudiant);

        cible = new Etudiant();
        cible.setNomEtudiant("Cible");
        cible.setPrenomEtudiant("Etudiant");
        cible.setEmailEtudiant("cible@example.com");
        cible.setDateNaissanceEtudiant(LocalDate.of(2000, 2, 2));
        cible = etudiantRepository.save(cible);


        createdEvent = new Evenement();
        createdEvent.setNomEvenement("Mon évènement");
        createdEvent.setCreateur(etudiant);
        createdEvent.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        createdEvent.setDateHeureFinEvenement(LocalDateTime.now().plusDays(1).plusHours(2));
        createdEvent.setMembreGroupe(new ArrayList<>(List.of(etudiant)));
        createdEvent = evenementRepository.save(createdEvent);


        joinedEvent = new Evenement();
        joinedEvent.setNomEvenement("Activité externe");
        joinedEvent.setCreateur(cible);
        joinedEvent.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(2));
        joinedEvent.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2).plusHours(2));
        joinedEvent.setMembreGroupe(new ArrayList<>(List.of(etudiant)));
        joinedEvent = evenementRepository.save(joinedEvent);

        Evenement e1 = new Evenement();
        e1.setNomEvenement("Salon Étudiant");
        e1.setCreateur(etudiant);
        e1.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        e1.setDateHeureFinEvenement(LocalDateTime.now().plusDays(1).plusHours(2));
        evenementRepository.save(e1);

        Evenement e2 = new Evenement();
        e2.setNomEvenement("Hackathon Java");
        e2.setCreateur(etudiant);
        e2.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(2));
        e2.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2).plusHours(2));
        evenementRepository.save(e2);
    }

    @Test
    void testAfficherEvenementsEtCalendrier_Connecte() throws Exception {
        mockMvc.perform(get("/evenement")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("event"))
                .andExpect(model().attribute("utilisateurConnecte", hasProperty("idEtudiant", is(etudiant.getIdEtudiant()))))
                .andExpect(model().attribute("mesCreations", hasItem(hasProperty("nomEvenement", is("Mon évènement")))))
                .andExpect(model().attribute("mesParticipations", hasItem(hasProperty("nomEvenement", is("Activité externe")))))
                .andExpect(model().attributeExists("eventJson"));
    }

    @Test
    void testAfficherEvenementsEtCalendrier_NonConnecte() throws Exception {
        mockMvc.perform(get("/evenement"))
                .andExpect(status().isOk())
                .andExpect(view().name("event"))
                .andExpect(model().attribute("eventJson", "[]"))
                .andExpect(model().attribute("mesCreations", empty()))
                .andExpect(model().attribute("mesParticipations", empty()))
                .andExpect(model().attributeDoesNotExist("utilisateurConnecte"));
    }

    @Test
    void testAfficherEvenementsEtCalendrier_ConnecteSansEvenements() throws Exception {

        Etudiant nouveau = new Etudiant();
        nouveau.setNomEtudiant("Sans activité");
        nouveau.setPrenomEtudiant("Test");
        nouveau.setEmailEtudiant("vide@example.com");
        nouveau.setDateNaissanceEtudiant(LocalDate.of(2001, 1, 1));
        nouveau = etudiantRepository.save(nouveau);

        mockMvc.perform(get("/evenement")
                        .sessionAttr("etudiantConnecte", nouveau))
                .andExpect(status().isOk())
                .andExpect(view().name("event"))
                .andExpect(model().attribute("mesCreations", empty()))
                .andExpect(model().attribute("mesParticipations", empty()))
                .andExpect(model().attribute("eventJson", is("[]")));

    }

    @Test
    void testAfficherFormulaireCreation() throws Exception {
        mockMvc.perform(get("/evenement/creer"))
                .andExpect(status().isOk())
                .andExpect(view().name("event_creer"))
                .andExpect(model().attributeExists("evenement"));
    }

    @Test
    void testSauvegarderEvenement_Succes() throws Exception {
        MockMultipartFile photo = new MockMultipartFile(
                "photo", "photo.jpg", "image/jpeg", "fake-image".getBytes());

        mockMvc.perform(multipart("/evenement/save")
                        .file(photo)
                        .param("nomEvenement", "Test Event")
                        .param("dateHeureDebutEvenement", LocalDateTime.now().plusDays(1).toString())
                        .param("dateHeureFinEvenement", LocalDateTime.now().plusDays(1).plusHours(2).toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("success", "Événement créé avec succès."));
    }

    @Test
    void testSauvegarderEvenement_NonConnecte() throws Exception {
        mockMvc.perform(multipart("/evenement/save")
                        .param("nomEvenement", "Test")
                        .param("dateHeureDebutEvenement", LocalDateTime.now().plusDays(1).toString())
                        .param("dateHeureFinEvenement", LocalDateTime.now().plusDays(2).toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"))
                .andExpect(flash().attribute("error", "Vous devez être connecté pour créer un événement."));
    }

    @Test
    void testSauvegarderEvenement_DebutDansLePasse() throws Exception {
        mockMvc.perform(multipart("/evenement/save")
                        .param("nomEvenement", "Test")
                        .param("dateHeureDebutEvenement", LocalDateTime.now().minusDays(1).toString())
                        .param("dateHeureFinEvenement", LocalDateTime.now().plusDays(1).toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "La date de début doit être postérieure à la date actuelle."));
    }


    @Test
    void testSauvegarderEvenement_FinAvantDebut() throws Exception {
        LocalDateTime debut = LocalDateTime.now().plusDays(1);
        LocalDateTime fin = debut.minusHours(1);

        mockMvc.perform(multipart("/evenement/save")
                        .param("nomEvenement", "Test")
                        .param("dateHeureDebutEvenement", debut.toString())
                        .param("dateHeureFinEvenement", fin.toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "La date de fin doit être postérieure à la date de début."));
    }

    @Test
    void testControllerIsNotNull() {
        assertNotNull(evenementController, "Le contrôleur EvenementController doit être injecté par Spring.");
    }

    @Test
    void testAnnulerEvenement_Succes() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Annulable");
        event.setCreateur(etudiant);
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(1).plusHours(2));
        event = evenementRepository.save(event);

        mockMvc.perform(post("/evenement/annuler")
                        .param("idEvenement", event.getIdEvenement().toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("success", "Événement annulé avec succès."));

        assertFalse(evenementRepository.findById(event.getIdEvenement()).isPresent());
    }

    @Test
    void testAnnulerEvenement_NonConnecte() throws Exception {
        mockMvc.perform(post("/evenement/annuler")
                        .param("idEvenement", "999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"))
                .andExpect(flash().attribute("error", "Vous devez être connecté pour annuler un événement."));
    }


    @Test
    void testAnnulerEvenement_PasCreateur() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Interdit");
        event.setCreateur(cible); // autre personne
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(1).plusHours(1));
        event = evenementRepository.save(event);

        mockMvc.perform(post("/evenement/annuler")
                        .param("idEvenement", event.getIdEvenement().toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "Vous n'êtes pas le créateur de cet événement."));
    }

    @Test
    void testAnnulerEvenement_EnCours() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Déjà commencé");
        event.setCreateur(etudiant);
        event.setDateHeureDebutEvenement(LocalDateTime.now().minusHours(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusHours(1));
        event = evenementRepository.save(event);

        mockMvc.perform(post("/evenement/annuler")
                        .param("idEvenement", event.getIdEvenement().toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "L'événement a déjà commencé, il ne peut plus être annulé."));
    }

    @Test
    void testListerTousLesEvenements_Connecte() throws Exception {
        Evenement e1 = new Evenement();
        e1.setNomEvenement("Futur 1");
        e1.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        e1.setDateHeureFinEvenement(LocalDateTime.now().plusDays(1).plusHours(2));
        e1.setCreateur(etudiant);
        evenementRepository.save(e1);

        mockMvc.perform(get("/evenement/liste")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("event_rejoindre"))
                .andExpect(model().attribute("utilisateurConnecte", hasProperty("idEtudiant", is(etudiant.getIdEtudiant()))))
                .andExpect(model().attribute("evenementsFiltres", hasItem(hasProperty("nomEvenement", is("Futur 1")))));
    }

    @Test
    void testListerTousLesEvenements_NonConnecteAucunEvenement() throws Exception {
        evenementRepository.deleteAll(); // 确保没有事件

        mockMvc.perform(get("/evenement/liste"))
                .andExpect(status().isOk())
                .andExpect(view().name("event_rejoindre"))
                .andExpect(model().attributeDoesNotExist("utilisateurConnecte"))
                .andExpect(model().attribute("evenementsFiltres", empty()));
    }

    @Test
    void testRechercherEvenements_AvecMotCle() throws Exception {
        mockMvc.perform(get("/evenement/recherche")
                        .param("motCle", "java")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("event_rejoindre"))
                .andExpect(model().attribute("evenementsFiltres", hasItem(hasProperty("nomEvenement", containsString("Java")))))
                .andExpect(model().attribute("utilisateurConnecte", hasProperty("idEtudiant", is(etudiant.getIdEtudiant()))));
    }

    @Test
    void testRechercherEvenements_SansMotCle() throws Exception {
        mockMvc.perform(get("/evenement/recherche")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("event_rejoindre"))
                .andExpect(model().attribute("evenementsFiltres", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(model().attributeExists("utilisateurConnecte"));
    }

    @Test
    void testRechercherEvenements_NonConnecteSansMotCle() throws Exception {
        mockMvc.perform(get("/evenement/recherche"))
                .andExpect(status().isOk())
                .andExpect(view().name("event_rejoindre"))
                .andExpect(model().attribute("evenementsFiltres", not(empty())))
                .andExpect(model().attributeDoesNotExist("utilisateurConnecte"));
    }

    @Test
    void testRejoindreEvenement_Succes() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Rejoignable");
        event.setCreateur(cible); // 他人创建
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2));
        event = evenementRepository.save(event);

        mockMvc.perform(get("/evenement/rejoindre/" + event.getIdEvenement())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("success", "Vous avez rejoint l'événement avec succès !"));

        Evenement reloaded = evenementRepository.findById(event.getIdEvenement()).get();
        assertTrue(reloaded.getMembreGroupe().contains(etudiant));
    }

    @Test
    void testRejoindreEvenement_NonConnecte() throws Exception {
        mockMvc.perform(get("/evenement/rejoindre/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"))
                .andExpect(flash().attribute("error", "Vous devez être connecté pour rejoindre un événement."));
    }

    @Test
    void testRejoindreEvenement_Inexistant() throws Exception {
        mockMvc.perform(get("/evenement/rejoindre/999999")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "Événement introuvable."));
    }

    @Test
    void testRejoindreEvenement_Termine() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Ancien");
        event.setCreateur(cible);
        event.setDateHeureDebutEvenement(LocalDateTime.now().minusDays(2));
        event.setDateHeureFinEvenement(LocalDateTime.now().minusHours(1));
        event = evenementRepository.save(event);

        mockMvc.perform(get("/evenement/rejoindre/" + event.getIdEvenement())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "L'événement est déjà terminé."));
    }

    @Test
    void testRejoindreEvenement_EstCreateur() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Mon événement");
        event.setCreateur(etudiant); // 自己创建的
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2));
        event = evenementRepository.save(event);

        mockMvc.perform(get("/evenement/rejoindre/" + event.getIdEvenement())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "Vous êtes le créateur de cet événement."));
    }

    @Test
    void testRejoindreEvenement_DejaParticipant() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Déjà là");
        event.setCreateur(cible);
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2));
        event.getMembreGroupe().add(etudiant);
        event = evenementRepository.save(event);

        mockMvc.perform(get("/evenement/rejoindre/" + event.getIdEvenement())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "Vous participez déjà à cet événement."));
    }

    @Test
    void testQuitterEvenement_Succes() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Atelier UX");
        event.setCreateur(cible);
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2));
        event.getMembreGroupe().add(etudiant);
        event = evenementRepository.save(event);

        mockMvc.perform(get("/evenement/quitter/" + event.getIdEvenement())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("success", "Vous avez quitté l'événement avec succès."));

        Evenement updated = evenementRepository.findById(event.getIdEvenement()).get();
        assertFalse(updated.getMembreGroupe().contains(etudiant));
    }

    @Test
    void testQuitterEvenement_NonConnecte() throws Exception {
        mockMvc.perform(get("/evenement/quitter/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"))
                .andExpect(flash().attribute("error", "Vous devez être connecté pour quitter un événement."));
    }

    @Test
    void testQuitterEvenement_Inexistant() throws Exception {
        mockMvc.perform(get("/evenement/quitter/999999")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "Événement introuvable."));
    }

    @Test
    void testQuitterEvenement_Termine() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Ancien événement");
        event.setCreateur(cible);
        event.setDateHeureDebutEvenement(LocalDateTime.now().minusDays(2));
        event.setDateHeureFinEvenement(LocalDateTime.now().minusHours(1));
        event.getMembreGroupe().add(etudiant);
        event = evenementRepository.save(event);

        mockMvc.perform(get("/evenement/quitter/" + event.getIdEvenement())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "L'événement est déjà terminé, vous ne pouvez plus le quitter."));
    }

    @Test
    void testQuitterEvenement_PasParticipant() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Pas inscrit");
        event.setCreateur(cible);
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2));
        event = evenementRepository.save(event);

        mockMvc.perform(get("/evenement/quitter/" + event.getIdEvenement())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "Vous ne participez pas à cet événement."));
    }

    @Test
    void testAfficherFormulaireModification_Succes() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("À modifier");
        event.setCreateur(etudiant);
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(1).plusHours(2));
        event = evenementRepository.save(event);

        mockMvc.perform(get("/evenement/modifier/" + event.getIdEvenement())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().isOk())
                .andExpect(view().name("event_modifier"))
                .andExpect(model().attribute("evenement", hasProperty("nomEvenement", is("À modifier"))));
    }

    @Test
    void testAfficherFormulaireModification_NonConnecte() throws Exception {
        mockMvc.perform(get("/evenement/modifier/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"))
                .andExpect(flash().attribute("error", "Vous devez être connecté pour modifier un événement."));
    }

    @Test
    void testAfficherFormulaireModification_Inexistant() throws Exception {
        mockMvc.perform(get("/evenement/modifier/999999")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "Événement introuvable."));
    }

    @Test
    void testAfficherFormulaireModification_PasCreateur() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Autre événement");
        event.setCreateur(cible);
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2));
        event = evenementRepository.save(event);

        mockMvc.perform(get("/evenement/modifier/" + event.getIdEvenement())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "Vous n'êtes pas le créateur de cet événement."));
    }

    @Test
    void testModifierEvenement_SuccesAvecPhoto() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Original");
        event.setCreateur(etudiant);
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2));
        event = evenementRepository.save(event);

        MockMultipartFile file = new MockMultipartFile("photo", "image.jpg", "image/jpeg", "fake-image-data".getBytes());

        mockMvc.perform(multipart("/evenement/update")
                        .file(file)
                        .param("idEvenement", event.getIdEvenement().toString())
                        .param("nomEvenement", "Modifié")
                        .param("lieuEvenement", "Nouveau lieu")
                        .param("dateHeureDebutEvenement", LocalDateTime.now().plusDays(3).toString())
                        .param("dateHeureFinEvenement", LocalDateTime.now().plusDays(4).toString())
                        .param("descriptionEvenement", "Nouvelle description")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("success", "Événement modifié avec succès."));
    }

    @Test
    void testModifierEvenement_NonConnecte() throws Exception {
        mockMvc.perform(post("/evenement/update")
                        .param("idEvenement", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"))
                .andExpect(flash().attribute("error", "Vous devez être connecté pour modifier un événement."));
    }

    @Test
    void testModifierEvenement_Inexistant() throws Exception {
        mockMvc.perform(post("/evenement/update")
                        .param("idEvenement", "99999")
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "L'événement que vous essayez de modifier n'existe pas."));
    }

    @Test
    void testModifierEvenement_NonCreateur() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Pas le tien");
        event.setCreateur(cible);
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2));
        event = evenementRepository.save(event);

        mockMvc.perform(post("/evenement/update")
                        .param("idEvenement", event.getIdEvenement().toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("erreur", "Vous n'êtes pas le créateur de cet événement."));
    }

    @Test
    void testModifierEvenement_DateDebutPasse() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("À corriger");
        event.setCreateur(etudiant);
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(1));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(2));
        event = evenementRepository.save(event);

        mockMvc.perform(post("/evenement/update")
                        .param("idEvenement", event.getIdEvenement().toString())
                        .param("dateHeureDebutEvenement", LocalDateTime.now().minusDays(1).toString())
                        .param("dateHeureFinEvenement", LocalDateTime.now().plusDays(2).toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "La date de début doit être postérieure à la date actuelle."));
    }

    @Test
    void testModifierEvenement_DateFinAvantDebut() throws Exception {
        Evenement event = new Evenement();
        event.setNomEvenement("Erreur date");
        event.setCreateur(etudiant);
        event.setDateHeureDebutEvenement(LocalDateTime.now().plusDays(2));
        event.setDateHeureFinEvenement(LocalDateTime.now().plusDays(3));
        event = evenementRepository.save(event);

        mockMvc.perform(post("/evenement/update")
                        .param("idEvenement", event.getIdEvenement().toString())
                        .param("dateHeureDebutEvenement", LocalDateTime.now().plusDays(5).toString())
                        .param("dateHeureFinEvenement", LocalDateTime.now().plusDays(4).toString())
                        .sessionAttr("etudiantConnecte", etudiant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/evenement"))
                .andExpect(flash().attribute("error", "La date de fin doit être postérieure à la date de début."));
    }









}