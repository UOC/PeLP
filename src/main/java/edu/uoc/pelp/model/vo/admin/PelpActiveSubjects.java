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
package edu.uoc.pelp.model.vo.admin;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for table pelp_activeSubjects
 * @author Xavier Baró
 */
@Entity
@Table(name = "pelp_activesubjects")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PelpActiveSubjects.findAll", query = "SELECT p FROM PelpActiveSubjects p"),
    @NamedQuery(name = "PelpActiveSubjects.findBySemester", query = "SELECT p FROM PelpActiveSubjects p WHERE p.pelpActiveSubjectsPK.semester = :semester"),
    @NamedQuery(name = "PelpActiveSubjects.findBySubject", query = "SELECT p FROM PelpActiveSubjects p WHERE p.pelpActiveSubjectsPK.subject = :subject"),
    @NamedQuery(name = "PelpActiveSubjects.findByActive", query = "SELECT p FROM PelpActiveSubjects p WHERE p.active = :active")})
public class PelpActiveSubjects implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PelpActiveSubjectsPK pelpActiveSubjectsPK;
    @Column(name = "active")
    private Boolean active;

    public PelpActiveSubjects() {
    }

    public PelpActiveSubjects(PelpActiveSubjectsPK pelpActiveSubjectsPK) {
        this.pelpActiveSubjectsPK = pelpActiveSubjectsPK;
    }

    public PelpActiveSubjects(String semester, String subject) {
        this.pelpActiveSubjectsPK = new PelpActiveSubjectsPK(semester, subject);
    }

    public PelpActiveSubjectsPK getPelpActiveSubjectsPK() {
        return pelpActiveSubjectsPK;
    }

    public void setPelpActiveSubjectsPK(PelpActiveSubjectsPK pelpActiveSubjectsPK) {
        this.pelpActiveSubjectsPK = pelpActiveSubjectsPK;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pelpActiveSubjectsPK != null ? pelpActiveSubjectsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PelpActiveSubjects)) {
            return false;
        }
        PelpActiveSubjects other = (PelpActiveSubjects) object;
        if ((this.pelpActiveSubjectsPK == null && other.pelpActiveSubjectsPK != null) || (this.pelpActiveSubjectsPK != null && !this.pelpActiveSubjectsPK.equals(other.pelpActiveSubjectsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.PelpActiveSubjects[ pelpActiveSubjectsPK=" + pelpActiveSubjectsPK + " ]";
    }
    
}
