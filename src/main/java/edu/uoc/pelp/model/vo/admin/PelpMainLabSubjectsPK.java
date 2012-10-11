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
 * Class representing the primary key for table PELP_MainLabSubjects
 * @author Xavier Baró
 */
@Embeddable
public class PelpMainLabSubjectsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "mainSubjectCode", length = 6,columnDefinition="char(6)")
    private String mainSubjectCode;
    @Basic(optional = false)
    @Column(name = "labSubjectCode", length = 6,columnDefinition="char(6)")
    private String labSubjectCode;

    public PelpMainLabSubjectsPK() {
    }

    public PelpMainLabSubjectsPK(String mainSubjectCode, String labSubjectCode) {
        this.mainSubjectCode = mainSubjectCode;
        this.labSubjectCode = labSubjectCode;
    }

    public String getMainSubjectCode() {
        return mainSubjectCode;
    }

    public void setMainSubjectCode(String mainSubjectCode) {
        this.mainSubjectCode = mainSubjectCode;
    }

    public String getLabSubjectCode() {
        return labSubjectCode;
    }

    public void setLabSubjectCode(String labSubjectCode) {
        this.labSubjectCode = labSubjectCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mainSubjectCode != null ? mainSubjectCode.hashCode() : 0);
        hash += (labSubjectCode != null ? labSubjectCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PelpMainLabSubjectsPK)) {
            return false;
        }
        PelpMainLabSubjectsPK other = (PelpMainLabSubjectsPK) object;
        if ((this.mainSubjectCode == null && other.mainSubjectCode != null) || (this.mainSubjectCode != null && !this.mainSubjectCode.equals(other.mainSubjectCode))) {
            return false;
        }
        if ((this.labSubjectCode == null && other.labSubjectCode != null) || (this.labSubjectCode != null && !this.labSubjectCode.equals(other.labSubjectCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.PelpMainLabSubjectsPK[ mainSubjectCode=" + mainSubjectCode + ", labSubjectCode=" + labSubjectCode + " ]";
    }
    
}
