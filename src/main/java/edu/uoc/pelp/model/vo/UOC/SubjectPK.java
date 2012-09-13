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
package edu.uoc.pelp.model.vo.UOC;

import edu.uoc.pelp.model.vo.ActivityPK;
import edu.uoc.pelp.model.vo.DeliverPK;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Entity class for primary key of table testDesc
 * @author Xavier Baró
 */
@Embeddable
public class SubjectPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "semester")
    private String semester;
    @Basic(optional = false)
    @Column(name = "subject")
    private String subject;

    public SubjectPK() {
    }
    
    public SubjectPK(String semester,String subject) {
        this.semester=semester;
        this.subject=subject;
    }
    
    public SubjectPK(ActivityPK activity) {
        this.semester=activity.getSemester();
        this.subject=activity.getSubject();
    }
    
    public SubjectPK(DeliverPK deliver) {
        this.semester=deliver.getSemester();
        this.subject=deliver.getSubject();
    }

    public String getSemester() {
        return semester;
    }

    public String getSubject() {
        return subject;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubjectPK other = (SubjectPK) obj;
        if ((this.semester == null) ? (other.semester != null) : !this.semester.equals(other.semester)) {
            return false;
        }
        if ((this.subject == null) ? (other.subject != null) : !this.subject.equals(other.subject)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.semester != null ? this.semester.hashCode() : 0);
        hash = 17 * hash + (this.subject != null ? this.subject.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.UOC.SubjectPK[ semester=" + semester + ", subject=" + subject + " ]";
    }
}
