package com.ut1.miage.appRS.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class EtuMessConversationId implements Serializable {

    private Long idEtudiant;
    private Long idConversation;

    public EtuMessConversationId() {}

    public EtuMessConversationId(Long idEtudiant, Long idConversation) {
        this.idEtudiant = idEtudiant;
        this.idConversation = idConversation;
    }

    public Long getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(Long idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public Long getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(Long idConversation) {
        this.idConversation = idConversation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EtuMessConversationId that)) return false;
        return Objects.equals(idEtudiant, that.idEtudiant) &&
               Objects.equals(idConversation, that.idConversation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEtudiant, idConversation);
    }
}
