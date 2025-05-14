package com.ut1.miage.appRS.model;

import java.io.Serializable;

public class ReagirId implements Serializable {
    private Long etudiant;
    private Long post;

    public ReagirId() {}

    public ReagirId(Long etudiant, Long post) {
        this.etudiant = etudiant;
        this.post = post;
    }

    public Long getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Long etudiant) {
        this.etudiant = etudiant;
    }

    public Long getPost() {
        return post;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((etudiant == null) ? 0 : etudiant.hashCode());
        result = prime * result + ((post == null) ? 0 : post.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReagirId other = (ReagirId) obj;
        if (etudiant == null) {
            if (other.etudiant != null)
                return false;
        } else if (!etudiant.equals(other.etudiant))
            return false;
        if (post == null) {
            if (other.post != null)
                return false;
        } else if (!post.equals(other.post))
            return false;
        return true;
    }

    public void setPost(Long post) {
        this.post = post;
    }
}
