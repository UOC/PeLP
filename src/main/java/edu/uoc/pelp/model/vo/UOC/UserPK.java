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
package edu.uoc.pelp.model.vo.UOC;

import edu.uoc.pelp.model.vo.DeliverPK;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Entity class representing internal representation of users
 * @author Xavier Baró
 */
@Embeddable
public class UserPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idp")
    private String idp;
    
    public UserPK() {
    }
    
    public UserPK(String idp) {
        this.idp=idp;
    }
    
    public UserPK(DeliverPK deliver) {
        this.idp=deliver.getUserID();
    }

    public String getIdp() {
        return idp;
    }

    public void setIdp(String idp) {
        this.idp = idp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserPK other = (UserPK) obj;
        if ((this.idp == null) ? (other.idp != null) : !this.idp.equals(other.idp)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.idp != null ? this.idp.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return "edu.uoc.pelp.model.vo.UOC.UserPK[ idp=" + idp + " ]";
    }
}
