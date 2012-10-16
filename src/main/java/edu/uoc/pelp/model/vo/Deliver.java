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

import edu.uoc.pelp.model.vo.UOC.ClassroomPK;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for delivers
 * @author Xavier Baró
 */
@Entity
@Table(name = "deliver")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Deliver.findAll", query = "SELECT d FROM Deliver d"),
    @NamedQuery(name = "Deliver.findBySemester", query = "SELECT d FROM Deliver d WHERE d.deliverPK.semester = :semester"),
    @NamedQuery(name = "Deliver.findBySubject", query = "SELECT d FROM Deliver d WHERE d.deliverPK.subject = :subject"),
    @NamedQuery(name = "Deliver.findByActivityIndex", query = "SELECT d FROM Deliver d WHERE d.deliverPK.activityIndex = :activityIndex"),
    @NamedQuery(name = "Deliver.findByUserID", query = "SELECT d FROM Deliver d WHERE d.deliverPK.userID = :userID"),
    @NamedQuery(name = "Deliver.findByDeliverIndex", query = "SELECT d FROM Deliver d WHERE d.deliverPK.deliverIndex = :deliverIndex"),
    @NamedQuery(name = "Deliver.findByRootPath", query = "SELECT d FROM Deliver d WHERE d.rootPath = :rootPath"),
    @NamedQuery(name = "Deliver.findBySubmissionDate", query = "SELECT d FROM Deliver d WHERE d.submissionDate = :submissionDate"),
    @NamedQuery(name = "Deliver.findByClassroom", query = "SELECT d FROM Deliver d WHERE d.classroom = :classroom"),
    @NamedQuery(name = "Deliver.findByLaboratory", query = "SELECT d FROM Deliver d WHERE d.laboratory = :laboratory"),
    @NamedQuery(name = "Deliver.findById", query = "SELECT d FROM Deliver d WHERE d.deliverPK.semester = :semester AND d.deliverPK.subject = :subject AND d.deliverPK.activityIndex = :activityIndex AND d.deliverPK.userID = :userID AND d.deliverPK.deliverIndex = :deliverIndex"),
    @NamedQuery(name = "Deliver.findBySubjectUserID", query = "SELECT d FROM Deliver d WHERE d.deliverPK.semester = :semester AND d.deliverPK.subject = :subject AND d.deliverPK.userID = :userID"),
    @NamedQuery(name = "Deliver.findByActivityUserID", query = "SELECT d FROM Deliver d WHERE d.deliverPK.semester = :semester AND d.deliverPK.subject = :subject AND d.deliverPK.activityIndex = :activityIndex AND d.deliverPK.userID = :userID"),
    @NamedQuery(name = "Deliver.findByActivity", query = "SELECT d FROM Deliver d WHERE d.deliverPK.semester = :semester AND d.deliverPK.subject = :subject AND d.deliverPK.activityIndex = :activityIndex"),
    @NamedQuery(name = "Deliver.findLast", query = "SELECT d FROM Deliver d WHERE d.deliverPK.semester = :semester AND d.deliverPK.subject = :subject AND d.deliverPK.activityIndex = :activityIndex AND d.deliverPK.userID = :userID ORDER BY d.deliverPK.deliverIndex desc limit 1"),
    @NamedQuery(name = "Deliver.findActivityDeliversByClass", query = "SELECT d FROM Deliver d WHERE d.deliverPK.semester=:semester and d.deliverPK.subject=:subject and d.deliverPK.activityIndex=:activityIndex and (d.classroom = :classroom or d.laboratory = :classroom) ORDER BY d.deliverPK.semester,d.deliverPK.subject,d.deliverPK.activityIndex,d.deliverPK.userID,d.deliverPK.deliverIndex"),
    @NamedQuery(name = "Deliver.findAtivityLastDeliverByClass", query = "SELECT d FROM Deliver d LEFT JOIN d.deliverPK d2 WITH d.deliverPK.deliverIndex<d2.deliverIndex WHERE d2.deliverIndex is null and d.deliverPK.semester=:semester and d.deliverPK.subject=:subject and d.deliverPK.activityIndex=:activityIndex and (d.classroom = :classroom or d.laboratory = :classroom) ORDER BY d.deliverPK.semester,d.deliverPK.subject,d.deliverPK.activityIndex,d.deliverPK.userID")})
    //@NamedQuery(name = "Deliver.findAtivityLastDeliverByClass", query = "SELECT d1 FROM Deliver d1 LEFT JOIN Deliver d2 ON d1.deliverPK.semester=d2.deliverPK.semester and d1.deliverPK.subject=d2.deliverPK.subject and d1.deliverPK.activityIndex=d2.deliverPK.activityIndex and d1.deliverPK.userID=d2.deliverPK.userID and d1.deliverPK.deliverIndex<d2.deliverPK.deliverIndex WHERE d2.deliverPK.deliverIndex is null and d1.deliverPK.semester=:semester and d1.deliverPK.subject=:subject and d1.deliverPK.activityIndex=:activityIndex and (d1.classroom = :classroom or d1.laboratory = :classroom) ORDER BY d1.deliverPK.semester,d1.deliverPK.subject,d1.deliverPK.activityIndex,d1.deliverPK.userID")})
public class Deliver implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DeliverPK deliverPK;
    @Basic(optional = false)
    @Column(name = "rootPath")
    private String rootPath;
    @Basic(optional = false)
    @Column(name = "submissionDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionDate;
    @Column(name = "classroom")
    private String classroom;
    @Column(name = "laboratory")
    private String laboratory;

    public Deliver() {
    }

    public Deliver(DeliverPK deliverPK) {
        this.deliverPK = deliverPK;
    }

    public Deliver(DeliverPK deliverPK, String rootPath, Date submissionDate) {
        this.deliverPK = deliverPK;
        this.rootPath = rootPath;
        this.submissionDate = submissionDate;
    }

    public Deliver(String semester, String subject, int activityIndex, String user, int deliverIndex) {
        this.deliverPK = new DeliverPK(semester, subject, activityIndex, user, deliverIndex);
    }

    public DeliverPK getDeliverPK() {
        return deliverPK;
    }

    public void setDeliverPK(DeliverPK deliverPK) {
        this.deliverPK = deliverPK;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
    
    public void setClassroom(ClassroomPK classroom) {
        this.classroom=null;
        if(classroom!=null) {
            this.classroom = classroom.toString();
        }
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }
    
    public void setLaboratory(ClassroomPK laboratory) {
        this.laboratory=null;
        if(laboratory!=null) {
            this.laboratory = laboratory.toString();
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deliverPK != null ? deliverPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Deliver)) {
            return false;
        }
        Deliver other = (Deliver) object;
        if ((this.deliverPK == null && other.deliverPK != null) || (this.deliverPK != null && !this.deliverPK.equals(other.deliverPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.Deliver[ deliverPK=" + deliverPK + " ]";
    }
    
}
