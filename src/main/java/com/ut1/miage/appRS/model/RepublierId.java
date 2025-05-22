package com.ut1.miage.appRS.model;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RepublierId implements Serializable {

    @Column(name = "id_post")
    private Long postId;

    @Column(name = "id_etudiant")
    private Long etudiantId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RepublierId)) return false;
        RepublierId that = (RepublierId) o;
        return Objects.equals(postId, that.postId) && Objects.equals(etudiantId, that.etudiantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, etudiantId);
    }

    public RepublierId() {
    }

    public RepublierId(Long postId, Long etudiantId) {
        this.postId = postId;
        this.etudiantId = etudiantId;
    }


    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

}

