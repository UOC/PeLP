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
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for table deliverResult
 * @author Xavier Baró
 */
@Entity
@Table(name = "deliverresult")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeliverResult.findAll", query = "SELECT d FROM DeliverResult d"),
    @NamedQuery(name = "DeliverResult.findBySemester", query = "SELECT d FROM DeliverResult d WHERE d.deliverResultPK.semester = :semester"),
    @NamedQuery(name = "DeliverResult.findBySubject", query = "SELECT d FROM DeliverResult d WHERE d.deliverResultPK.subject = :subject"),
    @NamedQuery(name = "DeliverResult.findByActivityIndex", query = "SELECT d FROM DeliverResult d WHERE d.deliverResultPK.activityIndex = :activityIndex"),
    @NamedQuery(name = "DeliverResult.findByUser", query = "SELECT d FROM DeliverResult d WHERE d.deliverResultPK.user = :user"),
    @NamedQuery(name = "DeliverResult.findByDeliverIndex", query = "SELECT d FROM DeliverResult d WHERE d.deliverResultPK.deliverIndex = :deliverIndex"),
    @NamedQuery(name = "DeliverResult.findByCompilation", query = "SELECT d FROM DeliverResult d WHERE d.compilation = :compilation"),
    @NamedQuery(name = "DeliverResult.findByExecution", query = "SELECT d FROM DeliverResult d WHERE d.execution = :execution"),
    @NamedQuery(name = "DeliverResult.findByLanguage", query = "SELECT d FROM DeliverResult d WHERE d.language = :language"),
    @NamedQuery(name = "DeliverResult.findByStartDate", query = "SELECT d FROM DeliverResult d WHERE d.startDate = :startDate"),
    @NamedQuery(name = "DeliverResult.findByEndDate", query = "SELECT d FROM DeliverResult d WHERE d.endDate = :endDate")})
public class DeliverResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DeliverPK deliverResultPK;
    @Basic(optional = false)
    @Column(name = "compilation")
    private boolean compilation;
    @Lob
    @Column(name = "compMessage")
    private String compMessage;
    @Basic(optional = false)
    @Column(name = "execution")
    private boolean execution;
    @Basic(optional = false)
    @Column(name = "progLanguage")
    private String progLanguage;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public DeliverResult() {
    }

    public DeliverResult(DeliverPK deliverResultPK) {
        this.deliverResultPK = deliverResultPK;
    }

    public DeliverResult(DeliverPK deliverResultPK, boolean compilation, boolean execution, String progLanguage) {
        this.deliverResultPK = deliverResultPK;
        this.compilation = compilation;
        this.execution = execution;
        this.progLanguage = progLanguage;
    }

    public DeliverResult(String semester, String subject, int activityIndex, String user, int deliverIndex) {
        this.deliverResultPK = new DeliverPK(semester, subject, activityIndex, user, deliverIndex);
    }

    public DeliverPK getDeliverPK() {
        return deliverResultPK;
    }

    public void setDeliverPK(DeliverPK deliverResultPK) {
        this.deliverResultPK = deliverResultPK;
    }

    public boolean getCompilation() {
        return compilation;
    }

    public void setCompilation(boolean compilation) {
        this.compilation = compilation;
    }

    public String getCompMessage() {
        return compMessage;
    }

    public void setCompMessage(String compMessage) {
        this.compMessage = compMessage;
    }

    public boolean getExecution() {
        return execution;
    }

    public void setExecution(boolean execution) {
        this.execution = execution;
    }

    public String getProgLanguage() {
        return progLanguage;
    }

    public void setProgLanguage(String progLanguage) {
        this.progLanguage = progLanguage;
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
    public int hashCode() {
        int hash = 0;
        hash += (deliverResultPK != null ? deliverResultPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeliverResult)) {
            return false;
        }
        DeliverResult other = (DeliverResult) object;
        if ((this.deliverResultPK == null && other.deliverResultPK != null) || (this.deliverResultPK != null && !this.deliverResultPK.equals(other.deliverResultPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.DeliverResult[ deliverResultPK=" + deliverResultPK + " ]";
    }
    
}
