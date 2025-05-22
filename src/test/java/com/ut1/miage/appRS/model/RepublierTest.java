package com.ut1.miage.appRS.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Classe de test unitaire pour l'entité {@link Republier}.
 *
 * Vérifie la construction, les accesseurs, les mutateurs
 * et le bon enregistrement des données liées à la republication.
 */
class RepublierTest {

    /** Identifiant composite de republication. */
    private RepublierId republierId;

    /** Publication d’origine liée à la republication. */
    private Post post;

    /** Étudiant ayant republicé la publication. */
    private Etudiant etudiant;

    /** Date et heure de la republication. */
    private LocalDateTime date;

    /** Commentaire ajouté lors de la republication. */
    private String commentaire;

    /** Statut de visibilité de la republication. */
    private boolean estPublic;

    /**
     * Initialise les objets de test communs avant chaque test.
     */
    @BeforeEach
    void setUp() {
        republierId = new RepublierId(1L, 2L);
        post = new Post();
        etudiant = new Etudiant();
        date = LocalDateTime.of(2024, 5, 1, 12, 0);
        commentaire = "Test commentaire";
        estPublic = true;
    }

    /**
     * Vérifie que le constructeur par défaut initialise les champs correctement.
     */
    @Test
    void testDefaultConstructor() {
        Republier republier = new Republier();
        assertThat(republier.getId()).isNotNull();
        assertThat(republier.getDateRepublication()).isNotNull();
    }

    /**
     * Vérifie le constructeur complet et les getters associés.
     */
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

    /**
     * Vérifie les accesseurs et mutateurs (getters et setters).
     */
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
