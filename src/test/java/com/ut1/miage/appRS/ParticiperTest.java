package com.ut1.miage.appRS;

import com.ut1.miage.appRS.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticiperTest {

    @Test
    void testGettersAndSetters() {
        Etudiant etudiant = new Etudiant();
        Groupe groupe = new Groupe();
        ParticiperId id = new ParticiperId(1L, 2L);
        String role = "Administrateur";

        Participer participer = new Participer();
        participer.setId(id);
        participer.setEtudiant(etudiant);
        participer.setGroupe(groupe);
        participer.setRole(role);

        assertEquals(id, participer.getId());
        assertSame(etudiant, participer.getEtudiant());
        assertSame(groupe, participer.getGroupe());
        assertEquals("Administrateur", participer.getRole());
    }

    @Test
    void testDefaultConstructorValues() {
        Participer participer = new Participer();

        assertNotNull(participer.getId()); // id est initialisé par défaut
        assertNull(participer.getEtudiant());
        assertNull(participer.getGroupe());
        assertNull(participer.getRole());
    }
}
