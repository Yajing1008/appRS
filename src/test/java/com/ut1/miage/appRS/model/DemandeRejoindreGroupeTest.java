package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour {@link DemandeRejoindreGroupe}.
 */
class DemandeRejoindreGroupeTest {

    /**
     * Vérifie que les getters et setters fonctionnent correctement.
     */
    @Test
    void testGettersAndSetters() {
        DemandeRejoindreGroupe demande = new DemandeRejoindreGroupe();

        Long id = 1L;
        Etudiant etudiant = new Etudiant();
        Groupe groupe = new Groupe();
        LocalDate date = LocalDate.now();
        String statut = "EN_ATTENTE";
        Boolean approuvee = true;
        boolean refusee = true;

        demande.setIdDemande(id);
        demande.setEtudiant(etudiant);
        demande.setGroupe(groupe);
        demande.setDateDemande(date);
        demande.setStatut(statut);
        demande.setApprouvee(approuvee);
        demande.setRefusee(refusee);

        assertEquals(id, demande.getIdDemande());
        assertSame(etudiant, demande.getEtudiant());
        assertSame(groupe, demande.getGroupe());
        assertEquals(date, demande.getDateDemande());
        assertEquals(statut, demande.getStatut());
        assertEquals(approuvee, demande.getApprouvee());
        assertTrue(demande.isRefusee());
    }

    /**
     * Vérifie les valeurs par défaut après instanciation.
     */
    @Test
    void testValeursParDefaut() {
        DemandeRejoindreGroupe demande = new DemandeRejoindreGroupe();

        assertNull(demande.getIdDemande());
        assertNull(demande.getEtudiant());
        assertNull(demande.getGroupe());
        assertNull(demande.getDateDemande());
        assertNull(demande.getStatut());
        assertNull(demande.getApprouvee());
        assertFalse(demande.isRefusee());
    }

    /**
     * Vérifie le comportement des méthodes equals() et hashCode().
     */
    @Test
    void testEqualsAndHashCode() {
        DemandeRejoindreGroupe d1 = new DemandeRejoindreGroupe();
        d1.setIdDemande(1L);

        DemandeRejoindreGroupe d2 = new DemandeRejoindreGroupe();
        d2.setIdDemande(1L);

        DemandeRejoindreGroupe d3 = new DemandeRejoindreGroupe();
        d3.setIdDemande(2L);

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());

        assertNotEquals(d1, d3);
        assertNotEquals(d1.hashCode(), d3.hashCode());
    }
}