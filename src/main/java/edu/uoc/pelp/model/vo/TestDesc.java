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
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for table testDesc
 * @author Xavier Baró
 */
@Entity
@Table(name = "testdesc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestDesc.findAll", query = "SELECT t FROM TestDesc t"),
    @NamedQuery(name = "TestDesc.findBySemester", query = "SELECT t FROM TestDesc t WHERE t.testDescPK.semester = :semester"),
    @NamedQuery(name = "TestDesc.findBySubject", query = "SELECT t FROM TestDesc t WHERE t.testDescPK.subject = :subject"),
    @NamedQuery(name = "TestDesc.findByActivityIndex", query = "SELECT t FROM TestDesc t WHERE t.testDescPK.activityIndex = :activityIndex"),
    @NamedQuery(name = "TestDesc.findByTestIndex", query = "SELECT t FROM TestDesc t WHERE t.testDescPK.testIndex = :testIndex"),
    @NamedQuery(name = "TestDesc.findByLangCode", query = "SELECT t FROM TestDesc t WHERE t.testDescPK.langCode = :langCode"),
    @NamedQuery(name = "TestDesc.findByActivityDesc", query = "SELECT t FROM TestDesc t WHERE t.activityDesc = :activityDesc")})
public class TestDesc implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TestDescPK testDescPK;
    @Basic(optional = false)
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "activityDesc")
    private String activityDesc;

    public TestDesc() {
    }

    public TestDesc(TestDescPK testDescPK) {
        this.testDescPK = testDescPK;
    }

    public TestDesc(TestDescPK testDescPK, String description) {
        this.testDescPK = testDescPK;
        this.description = description;
    }

    public TestDesc(String semester, String subject, int activityIndex, int testIndex, String langCode) {
        this.testDescPK = new TestDescPK(semester, subject, activityIndex, testIndex, langCode);
    }

    public TestDescPK getTestDescPK() {
        return testDescPK;
    }

    public void setTestDescPK(TestDescPK testDescPK) {
        this.testDescPK = testDescPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (testDescPK != null ? testDescPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestDesc)) {
            return false;
        }
        TestDesc other = (TestDesc) object;
        if ((this.testDescPK == null && other.testDescPK != null) || (this.testDescPK != null && !this.testDescPK.equals(other.testDescPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.TestDesc[ testDescPK=" + testDescPK + " ]";
    }
    
}
