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

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for table semester
 * @author Xavier Baró
 */
@Entity
@Table(name = "semester")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Semester.findAll", query = "SELECT s FROM Semester s order by s.semester asc"),
    @NamedQuery(name = "Semester.findBySemester", query = "SELECT s FROM Semester s WHERE s.semester = :semester"),
    @NamedQuery(name = "Semester.findByStartDate", query = "SELECT s FROM Semester s WHERE s.startDate = :startDate order by s.semester asc"),
    @NamedQuery(name = "Semester.findByEndDate", query = "SELECT s FROM Semester s WHERE s.endDate = :endDate order by s.semester asc"),
    @NamedQuery(name = "Semester.findActive", query = "SELECT s FROM Semester s WHERE (s.startDate is null or s.startDate<=now()) and (s.endDate is null or s.endDate>=now()) order by s.semester asc")})
public class Semester implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "semester", nullable = false, length = 5, columnDefinition="char(5)")
    private String semester;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public Semester() {
    }

    public Semester(String semester) {
        this.semester = semester;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Semester other = (Semester) obj;
        if ((this.semester == null) ? (other.semester != null) : !this.semester.equals(other.semester)) {
            return false;
        }
        if (this.startDate != other.startDate && (this.startDate == null || !this.startDate.equals(other.startDate))) {
            return false;
        }
        if (this.endDate != other.endDate && (this.endDate == null || !this.endDate.equals(other.endDate))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.semester != null ? this.semester.hashCode() : 0);
        hash = 79 * hash + (this.startDate != null ? this.startDate.hashCode() : 0);
        hash = 79 * hash + (this.endDate != null ? this.endDate.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.Semester[ semester=" + semester + " ]";
    }
    
}
