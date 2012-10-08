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
 * Entity class for table deliverFile
 * @author Xavier Baró
 */
@Entity
@Table(name = "deliverfile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeliverFile.findAll", query = "SELECT d FROM DeliverFile d"),
    @NamedQuery(name = "DeliverFile.findBySemester", query = "SELECT d FROM DeliverFile d WHERE d.deliverFilePK.semester = :semester"),
    @NamedQuery(name = "DeliverFile.findBySubject", query = "SELECT d FROM DeliverFile d WHERE d.deliverFilePK.subject = :subject"),
    @NamedQuery(name = "DeliverFile.findByActivityIndex", query = "SELECT d FROM DeliverFile d WHERE d.deliverFilePK.activityIndex = :activityIndex"),
    @NamedQuery(name = "DeliverFile.findByUserID", query = "SELECT d FROM DeliverFile d WHERE d.deliverFilePK.userID = :userID"),
    @NamedQuery(name = "DeliverFile.findByDeliverIndex", query = "SELECT d FROM DeliverFile d WHERE d.deliverFilePK.deliverIndex = :deliverIndex"),
    @NamedQuery(name = "DeliverFile.findByFileIndex", query = "SELECT d FROM DeliverFile d WHERE d.deliverFilePK.fileIndex = :fileIndex"),
    @NamedQuery(name = "DeliverFile.findByRelativePath", query = "SELECT d FROM DeliverFile d WHERE d.relativePath = :relativePath"),
    @NamedQuery(name = "DeliverFile.findByType", query = "SELECT d FROM DeliverFile d WHERE d.type = :type"),
    @NamedQuery(name = "DeliverFile.findByMain", query = "SELECT d FROM DeliverFile d WHERE d.main = :main"),
    @NamedQuery(name = "DeliverFile.findByDeliverID", query = "SELECT d FROM DeliverFile d WHERE d.deliverFilePK.semester = :semester AND d.deliverFilePK.subject = :subject AND d.deliverFilePK.activityIndex = :activityIndex AND d.deliverFilePK.userID = :userID AND d.deliverFilePK.deliverIndex = :deliverIndex ORDER BY d.deliverFilePK.fileIndex ASC"),
    @NamedQuery(name = "DeliverFile.findById", query = "SELECT d FROM DeliverFile d WHERE d.deliverFilePK.semester = :semester AND d.deliverFilePK.subject = :subject AND d.deliverFilePK.activityIndex = :activityIndex AND d.deliverFilePK.userID = :userID AND d.deliverFilePK.deliverIndex = :deliverIndex AND d.deliverFilePK.fileIndex = :fileIndex"),
    @NamedQuery(name = "DeliverFile.findLast", query = "SELECT d FROM DeliverFile d WHERE d.deliverFilePK.semester = :semester AND d.deliverFilePK.subject = :subject AND d.deliverFilePK.activityIndex = :activityIndex AND d.deliverFilePK.userID = :userID AND d.deliverFilePK.deliverIndex = :deliverIndex ORDER BY d.deliverFilePK.fileIndex desc limit 1")})
public class DeliverFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DeliverFilePK deliverFilePK;
    @Basic(optional = false)
    @Column(name = "relativePath")
    private String relativePath;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "main")
    private boolean main;

    public DeliverFile() {
    }

    public DeliverFile(DeliverFilePK deliverFilePK) {
        this.deliverFilePK = deliverFilePK;
    }

    public DeliverFile(DeliverFilePK deliverFilePK, String relativePath, String type, boolean main) {
        this.deliverFilePK = deliverFilePK;
        this.relativePath = relativePath;
        this.type = type;
        this.main = main;
    }

    public DeliverFile(String semester, String subject, int activityIndex, String user, int deliverIndex, int fileIndex) {
        this.deliverFilePK = new DeliverFilePK(semester, subject, activityIndex, user, deliverIndex, fileIndex);
    }

    public DeliverFilePK getDeliverFilePK() {
        return deliverFilePK;
    }

    public void setDeliverFilePK(DeliverFilePK deliverFilePK) {
        this.deliverFilePK = deliverFilePK;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deliverFilePK != null ? deliverFilePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeliverFile)) {
            return false;
        }
        DeliverFile other = (DeliverFile) object;
        if ((this.deliverFilePK == null && other.deliverFilePK != null) || (this.deliverFilePK != null && !this.deliverFilePK.equals(other.deliverFilePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.DeliverFile[ deliverFilePK=" + deliverFilePK + " ]";
    }
    
}
