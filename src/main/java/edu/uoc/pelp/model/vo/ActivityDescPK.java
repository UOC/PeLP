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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Annotated entity class for the primary key of table ActivityDesc
 * @author Xavier Baró
 */
@Embeddable
public class ActivityDescPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "semester",length = 5, columnDefinition="char(5)")
    private String semester;
    @Basic(optional = false)
    @Column(name = "subject",length = 6, columnDefinition="char(6)")
    private String subject;
    @Basic(optional = false)
    @Column(name = "index")
    private int index;
    @Basic(optional = false)
    @Column(name = "langCode",length = 15, columnDefinition="char(15)")
    private String langCode;

    public ActivityDescPK() {
    }

    public ActivityDescPK(String semester, String subject, int index, String langCode) {
        this.semester = semester;
        this.subject = subject;
        this.index = index;
        this.langCode = langCode;
    }
    
    public ActivityDescPK(ActivityPK activityPK, String langCode) {
        this.semester = activityPK.getSemester();
        this.subject = activityPK.getSubject();
        this.index = activityPK.getIndex();
        this.langCode = langCode;
    }
    
    public ActivityPK getActivityPK() {
        return new ActivityPK(semester, subject, index);
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (semester != null ? semester.hashCode() : 0);
        hash += (subject != null ? subject.hashCode() : 0);
        hash += (int) index;
        hash += (langCode != null ? langCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivityDescPK)) {
            return false;
        }
        ActivityDescPK other = (ActivityDescPK) object;
        if ((this.semester == null && other.semester != null) || (this.semester != null && !this.semester.equals(other.semester))) {
            return false;
        }
        if ((this.subject == null && other.subject != null) || (this.subject != null && !this.subject.equals(other.subject))) {
            return false;
        }
        if (this.index != other.index) {
            return false;
        }
        if ((this.langCode == null && other.langCode != null) || (this.langCode != null && !this.langCode.equals(other.langCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.ActivityDescPK[ semester=" + semester + ", subject=" + subject + ", index=" + index + ", langCode=" + langCode + " ]";
    }
    
}
