package com.ut1.miage.appRS.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

/**
 * Classe représentant une clé composite pour l'entité {@link Participer},
 * composée de l'identifiant d'un étudiant et d'un groupe.
 * 
 * Elle est utilisée par JPA pour identifier de manière unique une
 * participation à un groupe.
 */
@Embeddable
public class ParticiperId implements Serializable {

    /** Identifiant de l'étudiant. */
    private Long idEtudiant;

    /** Identifiant du groupe. */
    private Long idGroupe;

    /**
     * Constructeur par défaut requis pour JPA.
     */
    public ParticiperId() {}

    /**
     * Constructeur avec paramètres.
     *
     * @param idEtudiant identifiant de l'étudiant
     * @param idGroupe identifiant du groupe
     */
    public ParticiperId(Long idEtudiant, Long idGroupe) {
        this.idEtudiant = idEtudiant;
        this.idGroupe = idGroupe;
    }

    /**
     * @return l'identifiant de l'étudiant
     */
    public Long getIdEtudiant() {
        return idEtudiant;
    }

    /**
     * Définit l'identifiant de l'étudiant.
     * @param idEtudiant identifiant à définir
     */
    public void setIdEtudiant(Long idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    /**
     * @return l'identifiant du groupe
     */
    public Long getIdGroupe() {
        return idGroupe;
    }

    /**
     * Définit l'identifiant du groupe.
     * @param idGroupe identifiant à définir
     */
    public void setIdGroupe(Long idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Compare cette instance à un autre objet pour vérifier l'égalité.
     *
     * @param o l'objet à comparer
     * @return true si les deux objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticiperId that)) return false;
        return Objects.equals(idEtudiant, that.idEtudiant) &&
               Objects.equals(idGroupe, that.idGroupe);
    }

    /**
     * Calcule le hash code de cette instance.
     *
     * @return le hash code calculé
     */
    @Override
    public int hashCode() {
        return Objects.hash(idEtudiant, idGroupe);
    }
}