package com.ut1.miage.appRS.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Classe de test unitaire pour la classe {@link DemandeAmi}.
 * Elle vérifie le bon fonctionnement des getters, setters et l'initialisation par défaut.
 */
public class DemandeAmiTest {

    /**
     * Teste tous les getters et setters de la classe DemandeAmi.
     * Vérifie que les valeurs affectées sont bien récupérées.
     */
    @Test
    void testGettersAndSetters() {
        DemandeAmi demande = new DemandeAmi();

        Long id = 1L;
        Etudiant demandeur = new Etudiant();
        Etudiant receveur = new Etudiant();
        String statut = "EN_ATTENTE";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        demande.setIdDemande(id);
        demande.setDemandeur(demandeur);
        demande.setReceveur(receveur);
        demande.setStatut(statut);
        demande.setDateDemande(now);

        assertEquals(id, demande.getIdDemande());
        assertEquals(demandeur, demande.getDemandeur());
        assertEquals(receveur, demande.getReceveur());
        assertEquals(statut, demande.getStatut());
        assertEquals(now, demande.getDateDemande());
    }

    /**
     * Vérifie que la date de la demande est automatiquement initialisée
     * à la date/heure actuelle lors de la création d'une instance.
     */
    @Test
    void testDefaultDateDemandeInitialization() {
        DemandeAmi demande = new DemandeAmi();
        assertNotNull(demande.getDateDemande());

        // Vérifie que la date est très proche de "maintenant"
        assertTrue(java.time.Duration.between(demande.getDateDemande(), LocalDateTime.now()).getSeconds() < 2);
    }

    /**
     * Vérifie que les différentes valeurs de statut peuvent être correctement définies et récupérées.
     */
    @Test
    void testStatutValues() {
        DemandeAmi demande = new DemandeAmi();

        demande.setStatut("EN_ATTENTE");
        assertEquals("EN_ATTENTE", demande.getStatut());

        demande.setStatut("ACCEPTEE");
        assertEquals("ACCEPTEE", demande.getStatut());

        demande.setStatut("REFUSEE");
        assertEquals("REFUSEE", demande.getStatut());
    }
}
