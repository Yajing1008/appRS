package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;
import com.ut1.miage.appRS.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour la classe {@link Etudiant}.
 */
public class EtudiantTest {

    /**
     * Teste les accesseurs de base (getters/setters) pour les champs simples de l'entité Etudiant.
     */
    @Test
    void testGettersAndSetters() {
        Etudiant etudiant = new Etudiant();

        etudiant.setIdEtudiant(1L);
        etudiant.setNomEtudiant("Durand");
        etudiant.setPrenomEtudiant("Alice");
        etudiant.setDateNaissanceEtudiant(LocalDate.of(2000, 5, 10));
        etudiant.setSexeEtudiant("F");
        etudiant.setEmailEtudiant("alice.durand@example.com");
        etudiant.setMotDePass("secret123");

        assertEquals(1L, etudiant.getIdEtudiant());
        assertEquals("Durand", etudiant.getNomEtudiant());
        assertEquals("Alice", etudiant.getPrenomEtudiant());
        assertEquals(LocalDate.of(2000, 5, 10), etudiant.getDateNaissanceEtudiant());
        assertEquals("F", etudiant.getSexeEtudiant());
        assertEquals("alice.durand@example.com", etudiant.getEmailEtudiant());
        assertEquals("secret123", etudiant.getMotDePass());
    }

    /**
     * Vérifie que les listes sont correctement initialisées et non nulles à la création d'un étudiant.
     */
    @Test
    void testInitialListsNotNull() {
        Etudiant etudiant = new Etudiant();

        assertNotNull(etudiant.getAmis());
        assertNotNull(etudiant.getPostsPublies());
        assertNotNull(etudiant.getPostsRepublies());
        assertNotNull(etudiant.getGroupesCrees());
        assertNotNull(etudiant.getParticipations());
        assertNotNull(etudiant.getCommentaires());
        assertNotNull(etudiant.getReactions());
        assertNotNull(etudiant.getUniversites());
        assertNotNull(etudiant.getCentresInteret());
        assertNotNull(etudiant.getEvenementsParticiper());
        assertNotNull(etudiant.getEvenementsCreer());
    }

    /**
     * Teste l'ajout manuel d'un ami dans la liste d'amis.
     */
    @Test
    void testAddAmi() {
        Etudiant e1 = new Etudiant();
        Etudiant e2 = new Etudiant();

        e1.getAmis().add(e2);

        assertEquals(1, e1.getAmis().size());
        assertSame(e2, e1.getAmis().get(0));
    }

    /**
     * Vérifie l'affectation des commentaires à un étudiant.
     */
    @Test
    void testSetCommentaires() {
        Etudiant etudiant = new Etudiant();
        List<Commenter> commentaires = new ArrayList<>();
        commentaires.add(new Commenter());
        etudiant.setCommentaires(commentaires);
        assertEquals(commentaires, etudiant.getCommentaires());
    }

    /**
     * Vérifie l'affectation des réactions à un étudiant.
     */
    @Test
    void testSetReactions() {
        Etudiant etudiant = new Etudiant();
        List<Reagir> reactions = new ArrayList<>();
        reactions.add(new Reagir());
        etudiant.setReactions(reactions);
        assertEquals(reactions, etudiant.getReactions());
    }

    /**
     * Vérifie l'affectation des publications d'un étudiant.
     */
    @Test
    void testSetPostsPublies() {
        Etudiant etudiant = new Etudiant();
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        etudiant.setPostsPublies(posts);
        assertEquals(posts, etudiant.getPostsPublies());
    }

    /**
     * Vérifie l'affectation des republications d'un étudiant.
     */
    @Test
    void testSetPostsRepublies() {
        Etudiant etudiant = new Etudiant();
        List<Post> republies = new ArrayList<>();
        republies.add(new Post());
        etudiant.setPostsRepublies(republies);
        assertEquals(republies, etudiant.getPostsRepublies());
    }

    /**
     * Vérifie l'affectation des participations de groupe d'un étudiant.
     */
    @Test
    void testSetParticipations() {
        Etudiant etudiant = new Etudiant();
        List<Participer> participations = new ArrayList<>();
        participations.add(new Participer());
        etudiant.setParticipations(participations);
        assertEquals(participations, etudiant.getParticipations());
    }

    /**
     * Vérifie l'affectation des messages envoyés dans les conversations.
     */
    @Test
    void testSetMessagesEnvoyes() {
        Etudiant etudiant = new Etudiant();
        List<EtuMessConversation> messages = new ArrayList<>();
        messages.add(new EtuMessConversation());
        etudiant.setMessagesEnvoyes(messages);
        assertEquals(messages, etudiant.getMessagesEnvoyes());
    }

    /**
     * Vérifie l'affectation d'une liste d'amis complète.
     */
    @Test
    void testSetAmis() {
        Etudiant etudiant = new Etudiant();
        List<Etudiant> amis = new ArrayList<>();
        amis.add(new Etudiant());
        etudiant.setAmis(amis);
        assertEquals(amis, etudiant.getAmis());
    }

    /**
     * Vérifie l'affectation des universités associées à un étudiant.
     */
    @Test
    void testSetUniversites() {
        Etudiant etudiant = new Etudiant();
        List<Universite> universites = new ArrayList<>();
        universites.add(new Universite());
        etudiant.setUniversites(universites);
        assertEquals(universites, etudiant.getUniversites());
    }

    /**
     * Vérifie l'affectation des centres d’intérêt d’un étudiant.
     */
    @Test
    void testSetCentresInteret() {
        Etudiant etudiant = new Etudiant();
        List<CentreInteret> centres = new ArrayList<>();
        centres.add(new CentreInteret());
        etudiant.setCentresInteret(centres);
        assertEquals(centres, etudiant.getCentresInteret());
    }

    /**
     * Vérifie l'affectation des événements auxquels l'étudiant participe.
     */
    @Test
    void testSetEvenementsParticiper() {
        Etudiant etudiant = new Etudiant();
        List<Evenement> evenements = new ArrayList<>();
        evenements.add(new Evenement());
        etudiant.setEvenementsParticiper(evenements);
        assertEquals(evenements, etudiant.getEvenementsParticiper());
    }

    /**
     * Vérifie l'affectation des événements créés par l'étudiant.
     */
    @Test
    void testSetEvenementsCreer() {
        Etudiant etudiant = new Etudiant();
        List<Evenement> evenements = new ArrayList<>();
        evenements.add(new Evenement());
        etudiant.setEvenementsCreer(evenements);
        assertEquals(evenements, etudiant.getEvenementsCreer());
    }

    /**
     * Vérifie l'affectation des groupes créés par l'étudiant.
     */
    @Test
    void testSetGroupesCrees() {
        Etudiant etudiant = new Etudiant();
        List<Groupe> groupes = new ArrayList<>();
        groupes.add(new Groupe());
        etudiant.setGroupesCrees(groupes);
        assertEquals(groupes, etudiant.getGroupesCrees());
    }
}