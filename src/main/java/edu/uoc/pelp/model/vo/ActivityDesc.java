/*
	Copyright 2011-2012 Fundació per a la Universitat Oberta de Catalunya

	This file is part of PeLP (Programming eLearning Plaform).

    PeLP is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    PeLP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uoc.pelp.model.vo;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for activityDesc table
 * @author Xavier Baró
 */
@Entity
@Table(name = "activityDesc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivityDesc.findAll", query = "SELECT a FROM ActivityDesc a"),
    @NamedQuery(name = "ActivityDesc.findBySemester", query = "SELECT a FROM ActivityDesc a WHERE a.activityDescPK.semester = :semester"),
    @NamedQuery(name = "ActivityDesc.findBySubject", query = "SELECT a FROM ActivityDesc a WHERE a.activityDescPK.subject = :subject"),
    @NamedQuery(name = "ActivityDesc.findByActivityIndex", query = "SELECT a FROM ActivityDesc a WHERE a.activityDescPK.activityIndex = :activityIndex"),
    @NamedQuery(name = "ActivityDesc.findByLangCode", query = "SELECT a FROM ActivityDesc a WHERE a.activityDescPK.langCode = :langCode"),
    @NamedQuery(name = "ActivityDesc.findByActivity", query = "SELECT a FROM ActivityDesc a WHERE a.activityDescPK.semester = :semester AND a.activityDescPK.subject = :subject AND a.activityDescPK.activityIndex = :activityIndex"),
    @NamedQuery(name = "ActivityDesc.findByPK", query = "SELECT a FROM ActivityDesc a WHERE a.activityDescPK.semester = :semester AND a.activityDescPK.subject = :subject AND a.activityDescPK.activityIndex = :activityIndex AND a.activityDescPK.langCode = :langCode ORDER BY a.activityDescPK.semester,a.activityDescPK.subject,a.activityDescPK.activityIndex,a.activityDescPK.langCode ASC")})
public class ActivityDesc implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ActivityDescPK activityDescPK;
    @Basic(optional = false)
    @Lob
    @Column(name = "description")
    private String description;
    
    public ActivityDesc() {
    }

    public ActivityDesc(ActivityDescPK activityDescPK) {
        this.activityDescPK = activityDescPK;
    }

    public ActivityDesc(ActivityDescPK activityDescPK, String description) {
        this.activityDescPK = activityDescPK;
        this.description = description;
    }

    public ActivityDesc(String semester, String subject, int index, String langCode) {
        this.activityDescPK = new ActivityDescPK(semester, subject, index, langCode);
    }

    public ActivityDescPK getActivityDescPK() {
        return activityDescPK;
    }

    public void setActivityDescPK(ActivityDescPK activityDescPK) {
        this.activityDescPK = activityDescPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activityDescPK != null ? activityDescPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivityDesc)) {
            return false;
        }
        ActivityDesc other = (ActivityDesc) object;
        if ((this.activityDescPK == null && other.activityDescPK != null) || (this.activityDescPK != null && !this.activityDescPK.equals(other.activityDescPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.ActivityDesc[ activityDescPK=" + activityDescPK + " ]";
    }
    
}
