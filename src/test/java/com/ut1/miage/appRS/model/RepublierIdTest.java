package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Classe de test unitaire pour la classe {@link RepublierId}.
 *
 * Vérifie le bon fonctionnement du constructeur, des accesseurs,
 * de la méthode equals et de la méthode hashCode.
 */
class RepublierIdTest {

    /**
     * Vérifie que le constructeur par défaut initialise les champs à null.
     */
    @Test
    void testDefaultConstructor() {
        RepublierId id = new RepublierId();
        assertThat(id).isNotNull();
        assertThat(id.getPostId()).isNull();
        assertThat(id.getEtudiantId()).isNull();
    }

    /**
     * Vérifie que le constructeur avec arguments initialise correctement les champs.
     */
    @Test
    void testAllArgsConstructor() {
        RepublierId id = new RepublierId(1L, 2L);
        assertThat(id.getPostId()).isEqualTo(1L);
        assertThat(id.getEtudiantId()).isEqualTo(2L);
    }

    /**
     * Vérifie le bon fonctionnement des setters.
     */
    @Test
    void testSetters() {
        RepublierId id = new RepublierId();
        id.setPostId(3L);
        id.setEtudiantId(4L);

        assertThat(id.getPostId()).isEqualTo(3L);
        assertThat(id.getEtudiantId()).isEqualTo(4L);
    }

    /**
     * Vérifie que equals et hashCode fonctionnent pour des objets égaux ou différents.
     */
    @Test
    void testEqualsAndHashCode() {
        RepublierId id1 = new RepublierId(1L, 2L);
        RepublierId id2 = new RepublierId(1L, 2L);
        RepublierId id3 = new RepublierId(2L, 1L);

        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());

        assertThat(id1).isNotEqualTo(id3);
        assertThat(id1.hashCode()).isNotEqualTo(id3.hashCode());
    }

    /**
     * Vérifie que equals retourne false avec null ou un objet d’une autre classe.
     */
    @Test
    void testEqualsWithNullAndDifferentClass() {
        RepublierId id = new RepublierId(1L, 2L);

        assertThat(id.equals(null)).isFalse();
        assertThat(id.equals("not an id")).isFalse();
    }

    /**
     * Vérifie que equals retourne true quand l’objet est comparé à lui-même.
     */
    @Test
    void testEqualsWithSameInstance() {
        RepublierId id = new RepublierId(1L, 2L);
        assertThat(id.equals(id)).isTrue();
    }
}
