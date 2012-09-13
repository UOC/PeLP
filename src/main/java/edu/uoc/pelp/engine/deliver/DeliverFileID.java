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

import edu.uoc.pelp.engine.activity.*;
import edu.uoc.pelp.engine.campus.IPelpID;
import edu.uoc.pelp.exception.PelpException;

/**
 * This class implements a test unique identifier, used to link tests with activities.
 * @author Xavier Baró
 */
public class DeliverFileID implements IPelpID {
    /**
     * Activiy identifier
     */
    public DeliverID deliver;
    
    /**
     * Number identifying a certain file
     */
    public long index;
    
    /**
     * Constructor with all values
     * @param deliver Deliver identifier
     * @param index Index of this file
     */
    public DeliverFileID(DeliverID deliver,long index) {
        this.deliver=deliver;
        this.index=index;
    }
    
    /**
     * Default copy constructor
     * @param testID Object to be copied
     */
    public DeliverFileID(DeliverFileID testID) {
        deliver=testID.deliver;
        index=testID.index;
    }

    protected void copyData(IPelpID genericID) throws PelpException {
        if (genericID instanceof DeliverFileID) {
            deliver.copyData(((DeliverFileID)genericID).deliver);
            index=((DeliverFileID)genericID).index;
        } else {
            throw new PelpException("Object of type " + genericID.getClass() + " cannot be copided to an object of class " + this.getClass());
        }
    }
    
    @Override
    public int compareTo(IPelpID arg0) {
        DeliverFileID arg=(DeliverFileID)arg0;
        if(deliver!=null) {
            if(deliver.compareTo(arg.deliver)!=0) {
                return deliver.compareTo(arg.deliver);
            }
        } else {
            if(arg.deliver!=null) {
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
        final DeliverFileID other = (DeliverFileID) obj;
        if (this.deliver != other.deliver && (this.deliver == null || !this.deliver.equals(other.deliver))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.deliver != null ? this.deliver.hashCode() : 0);
        hash = 11 * hash + (int) (this.index ^ (this.index >>> 32));
        return hash;
    }

    @Override
    public IPelpID parse(String str) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
