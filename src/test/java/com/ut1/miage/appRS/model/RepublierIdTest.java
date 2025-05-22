package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RepublierIdTest {

    @Test
    void testDefaultConstructor() {
        RepublierId id = new RepublierId();
        assertThat(id).isNotNull();
        assertThat(id.getPostId()).isNull();
        assertThat(id.getEtudiantId()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        RepublierId id = new RepublierId(1L, 2L);
        assertThat(id.getPostId()).isEqualTo(1L);
        assertThat(id.getEtudiantId()).isEqualTo(2L);
    }

    @Test
    void testSetters() {
        RepublierId id = new RepublierId();
        id.setPostId(3L);
        id.setEtudiantId(4L);

        assertThat(id.getPostId()).isEqualTo(3L);
        assertThat(id.getEtudiantId()).isEqualTo(4L);
    }

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

    @Test
    void testEqualsWithNullAndDifferentClass() {
        RepublierId id = new RepublierId(1L, 2L);

        assertThat(id.equals(null)).isFalse();
        assertThat(id.equals("not an id")).isFalse();
    }

    @Test
    void testEqualsWithSameInstance() {
        RepublierId id = new RepublierId(1L, 2L);
        assertThat(id.equals(id)).isTrue();
    }
}
