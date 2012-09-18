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
 * Entity class for table PELP_MainLabSubjects
 * @author Xavier Baró
 */
@Entity
@Table(name = "pelp_mainlabsubjects")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PelpMainLabSubjects.findAll", query = "SELECT p FROM PelpMainLabSubjects p"),
    @NamedQuery(name = "PelpMainLabSubjects.findByMainSubjectCode", query = "SELECT p FROM PelpMainLabSubjects p WHERE p.pelpMainLabSubjectsPK.mainSubjectCode = :mainSubjectCode"),
    @NamedQuery(name = "PelpMainLabSubjects.findByLabSubjectCode", query = "SELECT p FROM PelpMainLabSubjects p WHERE p.pelpMainLabSubjectsPK.labSubjectCode = :labSubjectCode")})
public class PelpMainLabSubjects implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PelpMainLabSubjectsPK pelpMainLabSubjectsPK;

    public PelpMainLabSubjects() {
    }

    public PelpMainLabSubjects(PelpMainLabSubjectsPK pelpMainLabSubjectsPK) {
        this.pelpMainLabSubjectsPK = pelpMainLabSubjectsPK;
    }

    public PelpMainLabSubjects(String mainSubjectCode, String labSubjectCode) {
        this.pelpMainLabSubjectsPK = new PelpMainLabSubjectsPK(mainSubjectCode, labSubjectCode);
    }

    public PelpMainLabSubjectsPK getPelpMainLabSubjectsPK() {
        return pelpMainLabSubjectsPK;
    }

    public void setPelpMainLabSubjectsPK(PelpMainLabSubjectsPK pelpMainLabSubjectsPK) {
        this.pelpMainLabSubjectsPK = pelpMainLabSubjectsPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pelpMainLabSubjectsPK != null ? pelpMainLabSubjectsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PelpMainLabSubjects)) {
            return false;
        }
        PelpMainLabSubjects other = (PelpMainLabSubjects) object;
        if ((this.pelpMainLabSubjectsPK == null && other.pelpMainLabSubjectsPK != null) || (this.pelpMainLabSubjectsPK != null && !this.pelpMainLabSubjectsPK.equals(other.pelpMainLabSubjectsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.PelpMainLabSubjects[ pelpMainLabSubjectsPK=" + pelpMainLabSubjectsPK + " ]";
    }
    
}
