package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommenterIdTest {

    @Test
    void testDefaultConstructor() {
        CommenterId id = new CommenterId();
        assertThat(id).isNotNull();
        assertThat(id.getEtudiant()).isNull();
        assertThat(id.getPost()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        CommenterId id = new CommenterId(1L, 10L);
        assertThat(id.getEtudiant()).isEqualTo(1L);
        assertThat(id.getPost()).isEqualTo(10L);
    }

    @Test
    void testSettersAndGetters() {
        CommenterId id = new CommenterId();
        id.setEtudiant(2L);
        id.setPost(20L);

        assertThat(id.getEtudiant()).isEqualTo(2L);
        assertThat(id.getPost()).isEqualTo(20L);
    }

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

    @Test
    void testEqualsWithNullAndOtherClass() {
        CommenterId id = new CommenterId(5L, 50L);

        assertThat(id.equals(null)).isFalse();
        assertThat(id.equals("not an id")).isFalse();
    }

    @Test
    void testEqualsWithSameInstance() {
        CommenterId id = new CommenterId(6L, 60L);
        assertThat(id.equals(id)).isTrue();
    }
}
