package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Classe de test unitaire pour {@link CommenterId}.
 *
 * Vérifie le comportement du constructeur, des accesseurs, de l’égalité et du hashCode.
 */
class CommenterIdTest {

    /**
     * Vérifie que le constructeur par défaut initialise les champs à null.
     */
    @Test
    void testDefaultConstructor() {
        CommenterId id = new CommenterId();
        assertThat(id).isNotNull();
        assertThat(id.getEtudiant()).isNull();
        assertThat(id.getPost()).isNull();
    }

    /**
     * Vérifie que le constructeur avec arguments initialise correctement les champs.
     */
    @Test
    void testAllArgsConstructor() {
        CommenterId id = new CommenterId(1L, 10L);
        assertThat(id.getEtudiant()).isEqualTo(1L);
        assertThat(id.getPost()).isEqualTo(10L);
    }

    /**
     * Vérifie le bon fonctionnement des getters et setters.
     */
    @Test
    void testSettersAndGetters() {
        CommenterId id = new CommenterId();
        id.setEtudiant(2L);
        id.setPost(20L);

        assertThat(id.getEtudiant()).isEqualTo(2L);
        assertThat(id.getPost()).isEqualTo(20L);
    }

    /**
     * Vérifie que equals et hashCode fonctionnent correctement avec deux instances égales ou différentes.
     */
    @Test
    void testEqualsAndHashCode() {
        CommenterId id1 = new CommenterId(3L, 30L);
        CommenterId id2 = new CommenterId(3L, 30L);
        CommenterId id3 = new CommenterId(4L, 40L);

        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());

        assertThat(id1).isNotEqualTo(id3);
        assertThat(id1.hashCode()).isNotEqualTo(id3.hashCode());
    }

    /**
     * Vérifie que equals retourne false si l'objet est null ou d'une autre classe.
     */
    @Test
    void testEqualsWithNullAndOtherClass() {
        CommenterId id = new CommenterId(5L, 50L);

        assertThat(id.equals(null)).isFalse();
        assertThat(id.equals("not an id")).isFalse();
    }

    /**
     * Vérifie que equals retourne true si on compare une instance avec elle-même.
     */
    @Test
    void testEqualsWithSameInstance() {
        CommenterId id = new CommenterId(6L, 60L);
        assertThat(id.equals(id)).isTrue();
    }
}
