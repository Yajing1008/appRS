package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour la classe {@link Groupe}.
 */
class GroupeTest {

    /**
     * Vérifie que tous les accesseurs et mutateurs (getters/setters) fonctionnent correctement.
     */
    @Test
    void testGettersAndSetters() {
        Groupe groupe = new Groupe();
        Long id = 10L;
        String nom = "Groupe Java";
        LocalDate date = LocalDate.of(2025, 5, 15);
        String description = "Groupe d'entraide en Java";
        Boolean estPublic = true;
        Conversation conversation = new Conversation();
        Etudiant createur = new Etudiant();
        List<Participer> membres = new ArrayList<>();
        membres.add(new Participer());

        groupe.setIdGroupe(id);
        groupe.setNomGroupe(nom);
        groupe.setDateCreerGroupe(date);
        groupe.setDescriptionGroupe(description);
        groupe.setEstPublicGroupe(estPublic);
        groupe.setConversation(conversation);
        groupe.setCreateur(createur);
        groupe.setMembres(membres);

        assertEquals(id, groupe.getIdGroupe());
        assertEquals(nom, groupe.getNomGroupe());
        assertEquals(date, groupe.getDateCreerGroupe());
        assertEquals(description, groupe.getDescriptionGroupe());
        assertTrue(groupe.getEstPublicGroupe());
        assertSame(conversation, groupe.getConversation());
        assertSame(createur, groupe.getCreateur());
        assertEquals(1, groupe.getMembres().size());
    }

    /**
     * Vérifie que les valeurs par défaut du constructeur sont correctes.
     */
    @Test
    void testValeursParDefaut() {
        Groupe groupe = new Groupe();

        assertNull(groupe.getIdGroupe());
        assertNull(groupe.getNomGroupe());
        assertNull(groupe.getDateCreerGroupe());
        assertNull(groupe.getDescriptionGroupe());
        assertNull(groupe.getEstPublicGroupe());
        assertNull(groupe.getConversation());
        assertNull(groupe.getCreateur());
        assertNotNull(groupe.getMembres());
        assertTrue(groupe.getMembres().isEmpty());
    }

    /**
     * Vérifie que l'ajout de membres dans la liste fonctionne correctement.
     */
    @Test
    void testAjoutDeMembres() {
        Groupe groupe = new Groupe();
        Participer p1 = new Participer();
        Participer p2 = new Participer();

        List<Participer> membres = new ArrayList<>();
        membres.add(p1);
        membres.add(p2);

        groupe.setMembres(membres);

        assertEquals(2, groupe.getMembres().size());
    }

    /**
    * Vérifie le bon fonctionnement du champ photoGroupe.
    */
    @Test
    void testPhotoGroupe() {
        Groupe groupe = new Groupe();
        String photo = "photo.png";
        groupe.setPhotoGroupe(photo);
        assertEquals(photo, groupe.getPhotoGroupe());
    }

    /**
    * Vérifie l'ajout et la récupération des demandes de rejoindre un groupe.
    */
    @Test
    void testDemandesGroupe() {
        Groupe groupe = new Groupe();
        DemandeRejoindreGroupe demande1 = new DemandeRejoindreGroupe();
        DemandeRejoindreGroupe demande2 = new DemandeRejoindreGroupe();
        List<DemandeRejoindreGroupe> demandes = new ArrayList<>();
        demandes.add(demande1);
        demandes.add(demande2);

        groupe.setDemandes(demandes);

        assertEquals(2, groupe.getDemandes().size());
        assertSame(demande1, groupe.getDemandes().get(0));
    }

    /**
    * Vérifie que les méthodes equals et hashCode fonctionnent correctement.
    */
    @Test
    void testEqualsAndHashCode() {
        Groupe g1 = new Groupe();
        g1.setIdGroupe(1L);

        Groupe g2 = new Groupe();
        g2.setIdGroupe(1L);

        Groupe g3 = new Groupe();
        g3.setIdGroupe(2L);

        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
        assertNotEquals(g1, g3);
        assertNotEquals(g1.hashCode(), g3.hashCode());
    }
}