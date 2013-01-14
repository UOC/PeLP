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
package edu.uoc.pelp.model.vo.admin;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for table pelp_admins
 * @author Xavier Baró
 */
@Entity
@Table(name = "PELP_Admins")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PelpAdmins.findAll", query = "SELECT p FROM PelpAdmins p"),
    @NamedQuery(name = "PelpAdmins.findByUser", query = "SELECT p FROM PelpAdmins p WHERE p.userName = :userName"),
    @NamedQuery(name = "PelpAdmins.findByActive", query = "SELECT p FROM PelpAdmins p WHERE p.active = :active")})
public class PelpAdmins implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "userName", length = 25,columnDefinition="char(25)")
    private String userName;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "grantAllowed")
    private Boolean grantAllowed;

    public PelpAdmins() {
    }

    public PelpAdmins(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getGrantAllowed() {
        return grantAllowed;
    }

    public void setGrantAllowed(Boolean grantAllowed) {
        this.grantAllowed = grantAllowed;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PelpAdmins)) {
            return false;
        }
        PelpAdmins other = (PelpAdmins) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.PelpAdmins[ userName=" + userName + " ]";
    }
    
}
