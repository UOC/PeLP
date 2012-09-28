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
 * Entity class for the primary key of table deliver
 * @author Xavier Baró
 */
@Embeddable
public class DeliverPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "semester")
    private String semester;
    @Basic(optional = false)
    @Column(name = "subject")
    private String subject;
    @Basic(optional = false)
    @Column(name = "activityIndex")
    private int activityIndex;
    @Basic(optional = false)
    @Column(name = "userID")
    private String userID;
    @Basic(optional = false)
    @Column(name = "deliverIndex")
    private int deliverIndex;

    public DeliverPK() {
    }

    public DeliverPK(String semester, String subject, int activityIndex, String userID, int deliverIndex) {
        this.semester = semester;
        this.subject = subject;
        this.activityIndex = activityIndex;
        this.userID = userID;
        this.deliverIndex = deliverIndex;
    }
    
    public DeliverPK(ActivityPK activity, String userID, int deliverIndex) {
        this.semester = activity.getSemester();
        this.subject = activity.getSubject();
        this.activityIndex = activity.getActivityIndex();
        this.userID = userID;
        this.deliverIndex = deliverIndex;
    }
    
    public ActivityPK getActivityPK() {
        return new ActivityPK(semester,subject, activityIndex);
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

    public int getActivityIndex() {
        return activityIndex;
    }

    public void setActivityIndex(int activityIndex) {
        this.activityIndex = activityIndex;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getDeliverIndex() {
        return deliverIndex;
    }

    public void setDeliverIndex(int deliverIndex) {
        this.deliverIndex = deliverIndex;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (semester != null ? semester.hashCode() : 0);
        hash += (subject != null ? subject.hashCode() : 0);
        hash += (int) activityIndex;
        hash += (userID != null ? userID.hashCode() : 0);
        hash += (int) deliverIndex;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeliverPK)) {
            return false;
        }
        DeliverPK other = (DeliverPK) object;
        if ((this.semester == null && other.semester != null) || (this.semester != null && !this.semester.equals(other.semester))) {
            return false;
        }
        if ((this.subject == null && other.subject != null) || (this.subject != null && !this.subject.equals(other.subject))) {
            return false;
        }
        if (this.activityIndex != other.activityIndex) {
            return false;
        }
        if ((this.userID == null && other.userID != null) || (this.userID != null && !this.userID.equals(other.userID))) {
            return false;
        }
        if (this.deliverIndex != other.deliverIndex) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.DeliverPK[ semester=" + semester + ", subject=" + subject + ", activityIndex=" + activityIndex + ", user=" + userID + ", deliverIndex=" + deliverIndex + " ]";
    }
    
}
