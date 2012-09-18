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
import java.math.BigInteger;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for table deliverTestResult
 * @author Xavier Baró
 */
@Entity
@Table(name = "delivertestresult")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeliverTestResult.findAll", query = "SELECT d FROM DeliverTestResult d"),
    @NamedQuery(name = "DeliverTestResult.findBySemester", query = "SELECT d FROM DeliverTestResult d WHERE d.deliverTestResultPK.semester = :semester"),
    @NamedQuery(name = "DeliverTestResult.findBySubject", query = "SELECT d FROM DeliverTestResult d WHERE d.deliverTestResultPK.subject = :subject"),
    @NamedQuery(name = "DeliverTestResult.findByActivityIndex", query = "SELECT d FROM DeliverTestResult d WHERE d.deliverTestResultPK.activityIndex = :activityIndex"),
    @NamedQuery(name = "DeliverTestResult.findByUserID", query = "SELECT d FROM DeliverTestResult d WHERE d.deliverTestResultPK.userID = :userID"),
    @NamedQuery(name = "DeliverTestResult.findByDeliverIndex", query = "SELECT d FROM DeliverTestResult d WHERE d.deliverTestResultPK.deliverIndex = :deliverIndex"),
    @NamedQuery(name = "DeliverTestResult.findByTestIndex", query = "SELECT d FROM DeliverTestResult d WHERE d.deliverTestResultPK.testIndex = :testIndex"),
    @NamedQuery(name = "DeliverTestResult.findByPassed", query = "SELECT d FROM DeliverTestResult d WHERE d.passed = :passed"),
    @NamedQuery(name = "DeliverTestResult.findByElapsedTime", query = "SELECT d FROM DeliverTestResult d WHERE d.elapsedTime = :elapsedTime")})
public class DeliverTestResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DeliverTestResultPK deliverTestResultPK;
    @Basic(optional = false)
    @Column(name = "passed")
    private boolean passed;
    @Lob
    @Column(name = "programOutput")
    private String programOutput;
    @Column(name = "elapsedTime")
    private BigInteger elapsedTime;

    public DeliverTestResult() {
    }

    public DeliverTestResult(DeliverTestResultPK deliverTestResultPK) {
        this.deliverTestResultPK = deliverTestResultPK;
    }

    public DeliverTestResult(DeliverTestResultPK deliverTestResultPK, boolean passed) {
        this.deliverTestResultPK = deliverTestResultPK;
        this.passed = passed;
    }

    public DeliverTestResult(String semester, String subject, int activityIndex, String user, int deliverIndex, int testIndex) {
        this.deliverTestResultPK = new DeliverTestResultPK(semester, subject, activityIndex, user, deliverIndex, testIndex);
    }

    public DeliverTestResultPK getDeliverTestResultPK() {
        return deliverTestResultPK;
    }

    public void setDeliverTestResultPK(DeliverTestResultPK deliverTestResultPK) {
        this.deliverTestResultPK = deliverTestResultPK;
    }

    public boolean getPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getProgramOutput() {
        return programOutput;
    }

    public void setProgramOutput(String programOutput) {
        this.programOutput = programOutput;
    }

    public BigInteger getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(BigInteger elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deliverTestResultPK != null ? deliverTestResultPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeliverTestResult)) {
            return false;
        }
        DeliverTestResult other = (DeliverTestResult) object;
        if ((this.deliverTestResultPK == null && other.deliverTestResultPK != null) || (this.deliverTestResultPK != null && !this.deliverTestResultPK.equals(other.deliverTestResultPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.DeliverTestResult[ deliverTestResultPK=" + deliverTestResultPK + " ]";
    }
    
}
