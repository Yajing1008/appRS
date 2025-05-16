package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;
import com.ut1.miage.appRS.model.ReagirId;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour la classe {@link ReagirId}, qui représente une clé composite
 * pour l'entité {@link com.ut1.miage.appRS.model.Reagir}.
 */
class ReagirIdTest {

    /**
     * Vérifie que les valeurs passées au constructeur sont correctement accessibles via les getters.
     */
    @Test
    void testConstructeurEtGetters() {
        ReagirId id = new ReagirId(1L, 2L);
        assertEquals(1L, id.getEtudiant());
        assertEquals(2L, id.getPost());
    }

    /**
     * Vérifie que les setters permettent de modifier les champs {@code etudiant} et {@code post}.
     */
    @Test
    void testSetters() {
        ReagirId id = new ReagirId();
        id.setEtudiant(3L);
        id.setPost(4L);

        assertEquals(3L, id.getEtudiant());
        assertEquals(4L, id.getPost());
    }

    /**
     * Vérifie le bon fonctionnement de {@code equals()} et {@code hashCode()} pour deux identifiants équivalents.
     */
    @Test
    void testEqualsEtHashCode() {
        ReagirId id1 = new ReagirId(5L, 6L);
        ReagirId id2 = new ReagirId(5L, 6L);
        ReagirId id3 = new ReagirId(7L, 8L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
    }

    /**
     * Vérifie que la méthode {@code equals()} retourne {@code false} face à un objet nul ou d’un autre type.
     */
    @Test
    void testEqualsAvecNullEtAutreClasse() {
        ReagirId id = new ReagirId(1L, 1L);
        assertNotEquals(null, id);
        assertNotEquals("une chaîne", id);
    }

    /**
     * Vérifie que le constructeur par défaut initialise les champs à {@code null}.
     */
    @Test
    void testValeursParDefaut() {
        ReagirId id = new ReagirId();
        assertNull(id.getEtudiant());
        assertNull(id.getPost());
    }
}