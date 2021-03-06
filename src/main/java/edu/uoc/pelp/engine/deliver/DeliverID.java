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
package edu.uoc.pelp.engine.deliver;

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.campus.IPelpID;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.exception.PelpException;

/**
 * This class implements a deliver identifier.
 * @author Xavier Baró
 */
public class DeliverID implements IPelpID{
    /**
     * User who makes the deliver
     */
    public IUserID user;
    
    /**
     * Activity related to the deliver
     */
    public ActivityID activity;

    /**
    * Deliver index
    */ 
    public long index;

    public DeliverID(IUserID user,ActivityID activity,long index) {
        this.user=user;
        this.activity=activity;
        this.index=index;
    }

    public DeliverID(DeliverID deliverID) {
        this.user=deliverID.user;
        this.activity=deliverID.activity;
        this.index=deliverID.index;
    }

    protected void copyData(IPelpID genericID) throws PelpException {
        if (genericID instanceof DeliverID) {
            user=((DeliverID)genericID).user;
            activity=((DeliverID)genericID).activity;
            index=((DeliverID)genericID).index;
        } else {
            throw new PelpException("Object of type " + genericID.getClass() + " cannot be copided to an object of class " + this.getClass());
        }
    }

    public int compareTo(IPelpID id) {
        DeliverID arg=(DeliverID)id;
        if(activity!=null) {
            if(activity.compareTo(arg.activity)!=0) {
                return activity.compareTo(arg.activity);
            }
        } else {
            if(arg.activity!=null) {
                return -1;
            }
        }
        if(user!=null) {
            if(user.compareTo(arg.user)!=0) {
                return user.compareTo(arg.user);
            }
        } else {
            if(arg.user!=null) {
                return -1;
            }
        }
        return (new Long(index)).compareTo(arg.index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DeliverID other = (DeliverID) obj;
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
        if (this.activity != other.activity && (this.activity == null || !this.activity.equals(other.activity))) {
            return false;
        }
        if (this.index != other.index) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 19 * hash + (this.activity != null ? this.activity.hashCode() : 0);
        hash = 19 * hash + (int) (this.index ^ (this.index >>> 32));
        return hash;
    }
    
    @Override
    public DeliverID clone() {
        return new DeliverID(this);
    }

    public IPelpID parse(String str) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
