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
 * Entity class for table activityTest 
 * @author Xavier Baró
 */
@Entity
@Table(name = "activitytest")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivityTest.findAll", query = "SELECT a FROM ActivityTest a"),
    @NamedQuery(name = "ActivityTest.findBySemester", query = "SELECT a FROM ActivityTest a WHERE a.activityTestPK.semester = :semester"),
    @NamedQuery(name = "ActivityTest.findBySubject", query = "SELECT a FROM ActivityTest a WHERE a.activityTestPK.subject = :subject"),
    @NamedQuery(name = "ActivityTest.findByActivityIndex", query = "SELECT a FROM ActivityTest a WHERE a.activityTestPK.activityIndex = :activityIndex"),
    @NamedQuery(name = "ActivityTest.findByTestIndex", query = "SELECT a FROM ActivityTest a WHERE a.activityTestPK.testIndex = :testIndex"),
    @NamedQuery(name = "ActivityTest.findByPublicTest", query = "SELECT a FROM ActivityTest a WHERE a.publicTest = :publicTest"),
    @NamedQuery(name = "ActivityTest.findByStrInput", query = "SELECT a FROM ActivityTest a WHERE a.strInput = :strInput"),
    @NamedQuery(name = "ActivityTest.findByStrOutput", query = "SELECT a FROM ActivityTest a WHERE a.strOutput = :strOutput"),
    @NamedQuery(name = "ActivityTest.findByFileInput", query = "SELECT a FROM ActivityTest a WHERE a.fileInput = :fileInput"),
    @NamedQuery(name = "ActivityTest.findByFileOutput", query = "SELECT a FROM ActivityTest a WHERE a.fileOutput = :fileOutput")})
public class ActivityTest implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ActivityTestPK activityTestPK;
    @Column(name = "publicTest")
    private Boolean publicTest;
    @Column(name = "strInput")
    private String strInput;
    @Column(name = "strOutput")
    private String strOutput;
    @Column(name = "fileInput")
    private String fileInput;
    @Column(name = "fileOutput")
    private String fileOutput;
    @Lob
    @Column(name = "maxTime")
    private String maxTime;

    public ActivityTest() {
    }

    public ActivityTest(ActivityTestPK activityTestPK) {
        this.activityTestPK = activityTestPK;
    }

    public ActivityTest(String semester, String subject, int activityIndex, int testIndex) {
        this.activityTestPK = new ActivityTestPK(semester, subject, activityIndex, testIndex);
    }

    public ActivityTestPK getActivityTestPK() {
        return activityTestPK;
    }

    public void setActivityTestPK(ActivityTestPK activityTestPK) {
        this.activityTestPK = activityTestPK;
    }

    public Boolean getPublicTest() {
        return publicTest;
    }

    public void setPublicTest(Boolean publicTest) {
        this.publicTest = publicTest;
    }

    public String getStrInput() {
        return strInput;
    }

    public void setStrInput(String strInput) {
        this.strInput = strInput;
    }

    public String getStrOutput() {
        return strOutput;
    }

    public void setStrOutput(String strOutput) {
        this.strOutput = strOutput;
    }

    public String getFileInput() {
        return fileInput;
    }

    public void setFileInput(String fileInput) {
        this.fileInput = fileInput;
    }

    public String getFileOutput() {
        return fileOutput;
    }

    public void setFileOutput(String fileOutput) {
        this.fileOutput = fileOutput;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activityTestPK != null ? activityTestPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActivityTest)) {
            return false;
        }
        ActivityTest other = (ActivityTest) object;
        if ((this.activityTestPK == null && other.activityTestPK != null) || (this.activityTestPK != null && !this.activityTestPK.equals(other.activityTestPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.ActivityTest[ activityTestPK=" + activityTestPK + " ]";
    }
    
}
