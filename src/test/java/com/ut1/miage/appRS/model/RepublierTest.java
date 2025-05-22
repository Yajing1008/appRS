package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RepublierTest {

    private RepublierId republierId;
    private Post post;
    private Etudiant etudiant;
    private LocalDateTime date;
    private String commentaire;
    private boolean estPublic;

    @BeforeEach
    void setUp() {
        republierId = new RepublierId(1L, 2L);
        post = new Post();
        etudiant = new Etudiant();
        date = LocalDateTime.of(2024, 5, 1, 12, 0);
        commentaire = "Test commentaire";
        estPublic = true;
    }

    @Test
    void testDefaultConstructor() {
        Republier republier = new Republier();
        assertThat(republier.getId()).isNotNull();
        assertThat(republier.getDateRepublication()).isNotNull();
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        Republier republier = new Republier(republierId, post, etudiant, date, estPublic, commentaire);

        assertThat(republier.getId()).isEqualTo(republierId);
        assertThat(republier.getPost()).isEqualTo(post);
        assertThat(republier.getEtudiant()).isEqualTo(etudiant);
        assertThat(republier.getDateRepublication()).isEqualTo(date);
        assertThat(republier.getCommentaireRepublication()).isEqualTo(commentaire);
        assertThat(republier.isEstPublic()).isEqualTo(estPublic);
    }

    @Test
    void testSettersAndGetters() {
        Republier republier = new Republier();

        republier.setId(republierId);
        republier.setPost(post);
        republier.setEtudiant(etudiant);
        republier.setDateRepublication(date);
        republier.setCommentaireRepublication(commentaire);
        republier.setEstPublic(estPublic);

        assertThat(republier.getId()).isEqualTo(republierId);
        assertThat(republier.getPost()).isEqualTo(post);
        assertThat(republier.getEtudiant()).isEqualTo(etudiant);
        assertThat(republier.getDateRepublication()).isEqualTo(date);
        assertThat(republier.getCommentaireRepublication()).isEqualTo(commentaire);
        assertThat(republier.isEstPublic()).isEqualTo(estPublic);
    }
}
