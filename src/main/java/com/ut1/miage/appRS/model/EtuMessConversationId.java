package com.ut1.miage.appRS.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

/**
 * Représente la clé composite pour l'entité {@link EtuMessConversation}.
 * 
 * Cette clé est composée de l'identifiant d'un étudiant et de l'identifiant
 * d'une conversation. Elle est utilisée pour mapper la table de liaison entre
 * étudiants et messages dans une conversation.
 */
@Embeddable
public class EtuMessConversationId implements Serializable {

    private Long idEtudiant;
    private Long idConversation;

    /**
     * Constructeur par défaut requis par JPA.
     */
    public EtuMessConversationId() {}

    /**
     * Constructeur avec paramètres.
     *
     * @param idEtudiant identifiant de l'étudiant
     * @param idConversation identifiant de la conversation
     */
    public EtuMessConversationId(Long idEtudiant, Long idConversation) {
        this.idEtudiant = idEtudiant;
        this.idConversation = idConversation;
    }

    /**
     * Retourne l'identifiant de l'étudiant.
     *
     * @return id de l'étudiant
     */
    public Long getIdEtudiant() {
        return idEtudiant;
    }

    /**
     * Définit l'identifiant de l'étudiant.
     *
     * @param idEtudiant id de l'étudiant
     */
    public void setIdEtudiant(Long idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    /**
     * Retourne l'identifiant de la conversation.
     *
     * @return id de la conversation
     */
    public Long getIdConversation() {
        return idConversation;
    }

    /**
     * Définit l'identifiant de la conversation.
     *
     * @param idConversation id de la conversation
     */
    public void setIdConversation(Long idConversation) {
        this.idConversation = idConversation;
    }

    /**
     * Vérifie l'égalité entre deux instances de {@code EtuMessConversationId}.
     *
     * @param o objet à comparer
     * @return true si les deux objets ont les mêmes identifiants
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EtuMessConversationId that)) return false;
        return Objects.equals(idEtudiant, that.idEtudiant) &&
               Objects.equals(idConversation, that.idConversation);
    }

    /**
     * Calcule le hashCode basé sur les deux identifiants.
     *
     * @return hashCode de l'objet
     */
    @Override
    public int hashCode() {
        return Objects.hash(idEtudiant, idConversation);
    }
}