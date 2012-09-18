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
@Table(name = "pelp_admins")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PelpAdmins.findAll", query = "SELECT p FROM PelpAdmins p"),
    @NamedQuery(name = "PelpAdmins.findByUser", query = "SELECT p FROM PelpAdmins p WHERE p.user = :user"),
    @NamedQuery(name = "PelpAdmins.findByActive", query = "SELECT p FROM PelpAdmins p WHERE p.active = :active")})
public class PelpAdmins implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user", nullable = false, length = 25)
    private String user;
    @Column(name = "active")
    private Boolean active;

    public PelpAdmins() {
    }

    public PelpAdmins(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (user != null ? user.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PelpAdmins)) {
            return false;
        }
        PelpAdmins other = (PelpAdmins) object;
        if ((this.user == null && other.user != null) || (this.user != null && !this.user.equals(other.user))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.PelpAdmins[ user=" + user + " ]";
    }
    
}
