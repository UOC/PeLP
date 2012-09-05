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
package edu.uoc.pelp.engine.activity;

import edu.uoc.pelp.engine.campus.GenericID;
import edu.uoc.pelp.engine.campus.IPelpID;
import edu.uoc.pelp.exception.PelpException;

/**
 * This class implements a test unique identifier, used to link tests with activities.
 * @author Xavier Baró
 */
public class TestID extends GenericID {
    /**
     * Activiy identifier
     */
    public ActivityID activity;
    
    /**
     * Number identifying a certain test
     */
    public long index;
    
    public TestID(ActivityID activity,long index) {
        this.activity=activity;
        this.index=index;
    }

    @Override
    protected void copyData(GenericID genericID) throws PelpException {
        if (genericID instanceof TestID) {
            activity.copyData(((TestID)genericID).activity);
            index=((TestID)genericID).index;
        } else {
            throw new PelpException("Object of type " + genericID.getClass() + " cannot be copided to an object of class " + this.getClass());
        }
    }
    
    public int compareTo(IPelpID arg0) {
        TestID arg=(TestID)arg0;
        if(activity!=null) {
            if(activity.compareTo(arg.activity)!=0) {
                return activity.compareTo(arg.activity);
            }
        } else {
            if(arg.activity!=null) {
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
        final TestID other = (TestID) obj;
        if (this.activity != other.activity && (this.activity == null || !this.activity.equals(other.activity))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.activity != null ? this.activity.hashCode() : 0);
        hash = 11 * hash + (int) (this.index ^ (this.index >>> 32));
        return hash;
    }
    
    
}
