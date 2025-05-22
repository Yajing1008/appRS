package com.ut1.miage.appRS.controller;

import com.ut1.miage.appRS.model.*;
import com.ut1.miage.appRS.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private ParticiperRepository participerRepository;

    @Autowired
    private DemandeRejoindreGroupeRepository demandeRepository;

    private Etudiant etudiant;
    private MockHttpSession session;

    @BeforeEach
    void setup() {
        participerRepository.deleteAll();
        demandeRepository.deleteAll();
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

    /** [Test8.1] Afficher formulaire création groupe (connecté) */
    @Test
    void testAfficherFormulaireGroupeConnecte() throws Exception {
        mockMvc.perform(get("/groupe/nouveau").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("formulaireGroupe"))
                .andExpect(model().attributeExists("groupe"));
    }

    /** [Test8.2] Afficher formulaire création groupe (non connecté) */
    @Test
    void testAfficherFormulaireGroupeSansConnexion() throws Exception {
        mockMvc.perform(get("/groupe/nouveau"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"));
    }

    /** [Test8.3] Création groupe public */
    @Test
    void testCreationGroupePublic() throws Exception {
        MockMultipartFile photo = new MockMultipartFile(
                "photo", "photo.jpg", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/groupe/nouveau")
                        .file(photo)
                        .param("nomGroupe", "Groupe Java")
                        .param("descriptionGroupe", "Discussions sur Java")
                        .param("estPublicGroupe", "true")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/groupes"));

        Groupe groupe = groupeRepository.findAll().get(0);
        assertEquals("Groupe Java", groupe.getNomGroupe());
        assertTrue(Boolean.TRUE.equals(groupe.getEstPublicGroupe()));
    }

    /** [Test8.4] Création groupe privé */
    @Test
    void testCreationGroupePrive() throws Exception {
        MockMultipartFile photo = new MockMultipartFile(
                "photo", "photo.jpg", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/groupe/nouveau")
                        .file(photo)
                        .param("nomGroupe", "Groupe Securite")
                        .param("descriptionGroupe", "Cybersecurite")
                        .param("estPublicGroupe", "false")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/groupes"));

        Groupe groupe = groupeRepository.findAll().get(0);
        assertEquals("Groupe Securite", groupe.getNomGroupe());
        assertFalse(Boolean.TRUE.equals(groupe.getEstPublicGroupe()));
    }

    /** [Test10.1] Rejoindre un groupe public */
    @Test
    void testRejoindreGroupePublic() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("Dev");
        groupe.setEstPublicGroupe(true);
        groupe.setCreateur(null);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        mockMvc.perform(post("/groupe/" + groupe.getIdGroupe() + "/rejoindre").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/" + groupe.getIdGroupe() + "/details"));
    }

    /** [Test11.1] Demande pour rejoindre un groupe privé */
    @Test
    void testDemanderRejoindreGroupePrive() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("Private");
        groupe.setEstPublicGroupe(false);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        mockMvc.perform(post("/groupe/" + groupe.getIdGroupe() + "/demande-rejoindre").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/groupes"));
    }

    /** [Test13.1] Quitter un groupe */
    @Test
    void testQuitterGroupe() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("QuitMe");
        groupe.setEstPublicGroupe(true);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupe.setCreateur(etudiant);
        groupeRepository.save(groupe);

        ParticiperId pid = new ParticiperId(etudiant.getIdEtudiant(), groupe.getIdGroupe());
        Participer participation = new Participer();
        participation.setId(pid);
        participation.setEtudiant(etudiant);
        participation.setGroupe(groupe);
        participation.setRole("membre");
        participerRepository.save(participation);

        mockMvc.perform(post("/groupe/" + groupe.getIdGroupe() + "/quitter").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/groupes"));
    }

    /** [Test53.1] Voir mes groupes (créés / rejoints) */
    @Test
    void testVoirMesGroupes() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("MesGroupes");
        groupe.setEstPublicGroupe(true);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupe.setCreateur(etudiant);
        groupeRepository.save(groupe);

        mockMvc.perform(get("/groupe/groupes").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("groupes"))
                .andExpect(model().attributeExists("groupesCrees"));
    }

    /** [Test18.1] Modifier groupe */
    @Test
    void testModifierGroupe() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("Ancien");
        groupe.setDescriptionGroupe("Old");
        groupe.setEstPublicGroupe(true);
        groupe.setCreateur(etudiant);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/groupe/" + groupe.getIdGroupe() + "/modifier")
                        .file(photo)
                        .param("nomGroupe", "Nouveau")
                        .param("descriptionGroupe", "New")
                        .param("estPublicGroupe", "false")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/" + groupe.getIdGroupe() + "/details"));
    }

    /** [Test54.1] Supprimer groupe */
    @Test
    void testSupprimerGroupe() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("DeleteMe");
        groupe.setCreateur(etudiant);
        groupe.setEstPublicGroupe(true);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        mockMvc.perform(post("/groupe/" + groupe.getIdGroupe() + "/supprimer").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/groupes"));
    }

    /** [Test16.1] : Voir les détails d’un groupe en tant que membre */
    @Test
    void testVoirDetailsGroupeAvecEtudiantMembre() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("DetailGroup");
        groupe.setEstPublicGroupe(true);
        groupe.setCreateur(etudiant);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        Participer participer = new Participer();
        participer.setGroupe(groupe);
        participer.setEtudiant(etudiant);
        participer.setRole("membre");
        participer.setId(new ParticiperId(etudiant.getIdEtudiant(), groupe.getIdGroupe()));
        participerRepository.save(participer);

        mockMvc.perform(get("/groupe/" + groupe.getIdGroupe() + "/details").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("groupeDetail"))
                .andExpect(model().attributeExists("groupe"))
                .andExpect(model().attributeExists("estMembre"))
                .andExpect(model().attributeExists("etudiantConnecte"));
    }

    /** [Test16.2] : Voir les détails d’un groupe sans être connecté */
    @Test
    void testVoirDetailsGroupeSansConnexion() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("TestGroupe");
        groupe.setEstPublicGroupe(true);
        groupe.setCreateur(etudiant);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        mockMvc.perform(get("/groupe/" + groupe.getIdGroupe() + "/details"))
                .andExpect(status().isOk())
                .andExpect(view().name("groupeDetail"));
    }

    /** [Test16.3] : Voir les demandes d’adhésion à mes groupes */
    @Test
    void testAfficherDemandesEnAttente() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("Demandes");
        groupe.setEstPublicGroupe(false);
        groupe.setCreateur(etudiant);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        DemandeRejoindreGroupe demande = new DemandeRejoindreGroupe();
        demande.setGroupe(groupe);
        demande.setEtudiant(etudiant);
        demandeRepository.save(demande);

        mockMvc.perform(get("/groupe/mes-demandes").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("mesDemandes"))
                .andExpect(model().attributeExists("demandes"));
    }

    /** [Test15.1] : Accepter une demande d’adhésion */
    @Test
    void testAccepterDemande() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("GroupeAcc");
        groupe.setCreateur(etudiant);
        groupe.setEstPublicGroupe(false);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        DemandeRejoindreGroupe demande = new DemandeRejoindreGroupe();
        demande.setEtudiant(etudiant);
        demande.setGroupe(groupe);
        demandeRepository.save(demande);

        mockMvc.perform(post("/groupe/demande/" + demande.getIdDemande() + "/accepter"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/mes-demandes"));
    }

    /** [Test15.2] : Refuser une demande d’adhésion */
    @Test
    void testRefuserDemande() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("GroupeRef");
        groupe.setCreateur(etudiant);
        groupe.setEstPublicGroupe(false);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        DemandeRejoindreGroupe demande = new DemandeRejoindreGroupe();
        demande.setEtudiant(etudiant);
        demande.setGroupe(groupe);
        demandeRepository.save(demande);

        mockMvc.perform(post("/groupe/demande/" + demande.getIdDemande() + "/refuser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/mes-demandes"));
    }

    // [Test8.5] : Envoi de photo lors de la création de groupe
    @Test
    void testCreationGroupeAvecPhoto() throws Exception {
        MockMultipartFile photo = new MockMultipartFile(
                "photo", "photo.jpg", "image/jpeg", "test-image".getBytes());

        mockMvc.perform(multipart("/groupe/nouveau")
                        .file(photo)
                        .param("nomGroupe", "PhotoGroup")
                        .param("descriptionGroupe", "Avec photo")
                        .param("estPublicGroupe", "true")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/groupes"));

        Groupe groupe = groupeRepository.findAll().get(0);
        assertTrue(groupe.getPhotoGroupe().startsWith("data:image/jpeg;base64,"));
    }

    //[Test18.2] : Envoi de photo lors de la modification de groupe
    @Test
    void testModifierGroupeAvecPhoto() throws Exception {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("Avant");
        groupe.setDescriptionGroupe("Sans photo");
        groupe.setEstPublicGroupe(true);
        groupe.setCreateur(etudiant);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        MockMultipartFile nouvellePhoto = new MockMultipartFile("photo", "nouvelle.jpg", "image/png", "image-data".getBytes());

        mockMvc.perform(multipart("/groupe/" + groupe.getIdGroupe() + "/modifier")
                        .file(nouvellePhoto)
                        .param("nomGroupe", "Apres")
                        .param("descriptionGroupe", "Avec photo")
                        .param("estPublicGroupe", "false")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/" + groupe.getIdGroupe() + "/details"));

        Groupe modifie = groupeRepository.findById(groupe.getIdGroupe()).get();
        assertTrue(modifie.getPhotoGroupe().startsWith("data:image/png;base64,"));
    }

    // [Test53.2] : Regrouper correctement mes groupes créés et rejoints
    @Test
    void testAfficherGroupesAvecGroupesCreesEtRejoints() throws Exception {
        Groupe groupeCree = new Groupe();
        groupeCree.setNomGroupe("Cree");
        groupeCree.setEstPublicGroupe(true);
        groupeCree.setCreateur(etudiant);
        groupeCree.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupeCree);

        Groupe groupeRejoint = new Groupe();
        groupeRejoint.setNomGroupe("Rejoint");
        groupeRejoint.setEstPublicGroupe(true);
        groupeRejoint.setDateCreerGroupe(LocalDate.now());
        groupeRejoint.setCreateur(etudiant);
        groupeRepository.save(groupeRejoint);

        Participer participer = new Participer();
        participer.setGroupe(groupeRejoint);
        participer.setEtudiant(etudiant);
        participer.setRole("membre");
        participer.setId(new ParticiperId(etudiant.getIdEtudiant(), groupeRejoint.getIdGroupe()));
        participerRepository.save(participer);

        mockMvc.perform(get("/groupe/groupes").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groupesCrees"))
                .andExpect(model().attributeExists("groupesMembre"))
                .andExpect(model().attributeExists("groupesRejoints"));
    }

    /** [Test17.1] : Retirer un membre d’un groupe */
    @Test
    void testRetirerMembreDuGroupe() throws Exception {
        // Création du groupe par l'étudiant connecté
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("TestKick");
        groupe.setEstPublicGroupe(true);
        groupe.setCreateur(etudiant);
        groupe.setDateCreerGroupe(LocalDate.now());
        groupeRepository.save(groupe);

        // Création d’un autre étudiant à retirer
        Etudiant autreEtudiant = new Etudiant();
        autreEtudiant.setPrenomEtudiant("Paul");
        autreEtudiant.setNomEtudiant("Dupont");
        autreEtudiant.setEmailEtudiant("paul@example.com");
        autreEtudiant.setMotDePass("mdp123");
        etudiantRepository.save(autreEtudiant);

        // Ajout de l’autre étudiant dans le groupe
        Participer participer = new Participer();
        participer.setGroupe(groupe);
        participer.setEtudiant(autreEtudiant);
        participer.setRole("membre");
        participer.setId(new ParticiperId(autreEtudiant.getIdEtudiant(), groupe.getIdGroupe()));
        participerRepository.save(participer);

        // Vérification que la participation existe bien avant suppression
        assertEquals(1, participerRepository.findAll().size());

        // Appel au contrôleur pour retirer l’étudiant
        mockMvc.perform(post("/groupe/" + groupe.getIdGroupe() + "/retirer-membre/" + autreEtudiant.getIdEtudiant())
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groupe/" + groupe.getIdGroupe() + "/details"));

        // Vérification que la participation a été supprimée
        assertEquals(0, participerRepository.findAll().size());
    }
}