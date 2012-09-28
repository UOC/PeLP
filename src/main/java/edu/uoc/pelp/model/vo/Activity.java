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
 * Entity class for activity table
 * @author Xavier Baró
 */
@Entity
@Table(name = "activity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activity.findAll", query = "SELECT a FROM Activity a"),
    @NamedQuery(name = "Activity.findBySemester", query = "SELECT a FROM Activity a WHERE a.activityPK.semester = :semester"),
    @NamedQuery(name = "Activity.findBySubject", query = "SELECT a FROM Activity a WHERE a.activityPK.subject = :subject"),
    @NamedQuery(name = "Activity.findByActivityIndex", query = "SELECT a FROM Activity a WHERE a.activityPK.activityIndex = :activityIndex"),
    @NamedQuery(name = "Activity.findByStartDate", query = "SELECT a FROM Activity a WHERE a.startDate = :startDate"),
    @NamedQuery(name = "Activity.findByEndDate", query = "SELECT a FROM Activity a WHERE a.endDate = :endDate"),
    @NamedQuery(name = "Activity.findByMaxDelivers", query = "SELECT a FROM Activity a WHERE a.maxDelivers = :maxDelivers"),
    @NamedQuery(name = "Activity.findByProgLanguage", query = "SELECT a FROM Activity a WHERE a.progLanguage = :progLanguage"),
    @NamedQuery(name = "Activity.findLast", query = "SELECT a FROM Activity a WHERE a.activityPK.semester = :semester AND a.activityPK.subject = :subject ORDER BY a.activityPK.activityIndex desc limit 1"),
    @NamedQuery(name = "Activity.findActive", query = "SELECT a FROM Activity a WHERE (a.startDate is null or a.startDate<=now()) and (a.endDate is null or a.endDate>=now()) order by a.activityPK.semester,a.activityPK.subject,a.activityPK.activityIndex asc"),
    @NamedQuery(name = "Activity.findActivity", query = "SELECT a FROM Activity a WHERE a.activityPK.semester = :semester AND a.activityPK.subject = :subject AND a.activityPK.activityIndex = :activityIndex"),
    @NamedQuery(name = "Activity.findAllBySubject", query = "SELECT a FROM Activity a WHERE a.activityPK.semester = :semester AND a.activityPK.subject = :subject ORDER BY a.activityPK.semester,a.activityPK.subject,a.activityPK.activityIndex asc"),
    @NamedQuery(name = "Activity.findActiveBySubject", query = "SELECT a FROM Activity a WHERE a.activityPK.semester = :semester AND a.activityPK.subject = :subject AND (a.startDate is null or a.startDate<=now()) and (a.endDate is null or a.endDate>=now()) order by a.activityPK.semester,a.activityPK.subject,a.activityPK.activityIndex asc")
})
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ActivityPK activityPK;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "maxDelivers")
    private Integer maxDelivers;
    @Column(name = "progLanguage")
    private String progLanguage;

    public Activity() {
    }

    public Activity(ActivityPK activityPK) {
        this.activityPK = activityPK;
    }

    public Activity(String semester, String subject, int index) {
        this.activityPK = new ActivityPK(semester, subject, index);
    }

    public ActivityPK getActivityPK() {
        return activityPK;
    }

    public void setActivityPK(ActivityPK activityPK) {
        this.activityPK = activityPK;
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

    public Integer getMaxDelivers() {
        return maxDelivers;
    }

    public void setMaxDelivers(Integer maxDelivers) {
        this.maxDelivers = maxDelivers;
    }

    public String getLanguage() {
        return progLanguage;
    }

    public void setProgLanguage(String progLanguage) {
        this.progLanguage = progLanguage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activityPK != null ? activityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Activity)) {
            return false;
        }
        Activity other = (Activity) object;
        if ((this.activityPK == null && other.activityPK != null) || (this.activityPK != null && !this.activityPK.equals(other.activityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.Activity[ activityPK=" + activityPK + " ]";
    }
    
}
