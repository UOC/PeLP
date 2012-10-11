/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.pelp.model.vo.admin;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class implementing the language identifiers for the platform. 
 * @author Xavier Baró
 */
@Entity
@Table(name = "pelp_languages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PelpLanguages.findAll", query = "SELECT p FROM PelpLanguages p"),
    @NamedQuery(name = "PelpLanguages.findByLangCode", query = "SELECT p FROM PelpLanguages p WHERE p.langCode = :langCode"),
    @NamedQuery(name = "PelpLanguages.findByDesc", query = "SELECT p FROM PelpLanguages p WHERE p.langDesc = :langDesc")})
public class PelpLanguages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "langCode",length = 15, columnDefinition="char(15)")
    private String langCode;
    @Basic(optional = false)
    @Column(name = "langDesc")
    private String langDesc;

    public PelpLanguages() {
    }

    public PelpLanguages(String langCode) {
        this.langCode = langCode;
    }

    public PelpLanguages(String langCode, String langDesc) {
        this.langCode = langCode;
        this.langDesc = langDesc;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLangDesc() {
        return langDesc;
    }

    public void setLangDesc(String langDesc) {
        this.langDesc = langDesc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (langCode != null ? langCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PelpLanguages)) {
            return false;
        }
        PelpLanguages other = (PelpLanguages) object;
        if ((this.langCode == null && other.langCode != null) || (this.langCode != null && !this.langCode.equals(other.langCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.admin.PelpLanguages[ langCode=" + langCode + " ]";
    }
    
}
