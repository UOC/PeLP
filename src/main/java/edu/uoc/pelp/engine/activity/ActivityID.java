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
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.exception.PelpException;

/**
 * This class implements the identifier for an activity
 * @author Xavier Baró
 */
public class ActivityID extends GenericID{
    /**
     * String representation for the subject identifier
     */
    public ISubjectID subjectID;
    
    /**
     * Number identifying this activity
     */
    public long index;
    
    /**
     * Default constructor
     * @param subjectID Subject identifier
     * @param index Index of this activity in the given subject
     */
    public ActivityID(ISubjectID subjectID,long index) {
        this.subjectID=subjectID;
        this.index=index;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActivityID other = (ActivityID) obj;
        if (this.subjectID != other.subjectID && (this.subjectID == null || !this.subjectID.equals(other.subjectID))) {
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
        hash = 89 * hash + (this.subjectID != null ? this.subjectID.hashCode() : 0);
        hash = 89 * hash + (int) (this.index ^ (this.index >>> 32));
        return hash;
    }

    @Override
    protected void copyData(GenericID genericID) throws PelpException {
        if (genericID instanceof ActivityID) {
            index=((ActivityID)genericID).index;
            subjectID=((ActivityID)genericID).subjectID;
        } else {
            throw new PelpException("Object of type " + genericID.getClass() + " cannot be copided to an object of class " + this.getClass());
        }
    }

    public int compareTo(IPelpID id) {
        ActivityID arg=(ActivityID)id;
        if(subjectID!=null) {
            if(subjectID.compareTo(arg.subjectID)!=0) {
                return subjectID.compareTo(arg.subjectID);
            }
        } else {
            if(arg.subjectID!=null) {
                return -1;
            }
        }
        return (new Long(index)).compareTo(arg.index);
    }
    
}
