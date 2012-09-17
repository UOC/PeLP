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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Class representing the primary key for table PELP_ActiveSubjects
 * @author Xavier Baró
 */
@Embeddable
public class PelpActiveSubjectsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "semester", nullable = false, length = 5)
    private String semester;
    @Basic(optional = false)
    @Column(name = "subject", nullable = false, length = 6)
    private String subject;

    public PelpActiveSubjectsPK() {
    }

    public PelpActiveSubjectsPK(String semester, String subject) {
        this.semester = semester;
        this.subject = subject;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (semester != null ? semester.hashCode() : 0);
        hash += (subject != null ? subject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PelpActiveSubjectsPK)) {
            return false;
        }
        PelpActiveSubjectsPK other = (PelpActiveSubjectsPK) object;
        if ((this.semester == null && other.semester != null) || (this.semester != null && !this.semester.equals(other.semester))) {
            return false;
        }
        if ((this.subject == null && other.subject != null) || (this.subject != null && !this.subject.equals(other.subject))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.PelpActiveSubjectsPK[ semester=" + semester + ", subject=" + subject + " ]";
    }
    
}
