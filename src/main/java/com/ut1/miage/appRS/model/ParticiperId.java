package com.ut1.miage.appRS.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ParticiperId implements Serializable {

    private Long idEtudiant;
    private Long idGroupe;

    public ParticiperId() {}

    public ParticiperId(Long idEtudiant, Long idGroupe) {
        this.idEtudiant = idEtudiant;
        this.idGroupe = idGroupe;
    }

    public Long getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(Long idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public Long getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(Long idGroupe) {
        this.idGroupe = idGroupe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticiperId that)) return false;
        return Objects.equals(idEtudiant, that.idEtudiant) &&
               Objects.equals(idGroupe, that.idGroupe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEtudiant, idGroupe);
    }
}
