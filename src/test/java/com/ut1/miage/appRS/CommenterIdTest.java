package com.ut1.miage.appRS;

import org.junit.jupiter.api.Test;
import com.ut1.miage.appRS.model.CommenterId;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour la classe {@link CommenterId}.
 * Vérifie le bon comportement des méthodes equals, hashCode, et des getters.
 */
class CommenterIdTest {

    /**
     * Teste le constructeur avec paramètres et les accesseurs (getters).
     */
    @Test
    void testConstructeurEtGetters() {
        CommenterId id = new CommenterId(1L, 2L);

        assertEquals(1L, id.getEtudiant());
        assertEquals(2L, id.getPost());
    }

    /**
     * Teste que deux instances avec les mêmes valeurs sont considérées comme égales.
     */
    @Test
    void testEqualsTrue() {
        CommenterId id1 = new CommenterId(1L, 2L);
        CommenterId id2 = new CommenterId(1L, 2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    /**
     * Teste que deux instances avec des valeurs différentes ne sont pas égales.
     */
    @Test
    void testEqualsFalse() {
        CommenterId id1 = new CommenterId(1L, 2L);
        CommenterId id2 = new CommenterId(2L, 3L);

        assertNotEquals(id1, id2);
    }

    /**
     * Teste l'égalité entre deux objets avec un champ null en commun.
     */
    @Test
    void testEqualsAvecNull() {
        CommenterId id1 = new CommenterId(1L, null);
        CommenterId id2 = new CommenterId(1L, null);

        assertEquals(id1, id2);
    }

    /**
     * Vérifie que equals retourne false si on compare à une instance d'une autre classe.
     */
    @Test
    void testEqualsAutreClasse() {
        CommenterId id = new CommenterId(1L, 2L);
        String autre = "pas une instance";

        assertNotEquals(id, autre);
    }

    /**
     * Vérifie que equals retourne true lorsqu'on compare une instance à elle-même.
     */
    @Test
    void testEqualsAvecLuiMeme() {
        CommenterId id = new CommenterId(5L, 10L);
        assertEquals(id, id);
    }
}