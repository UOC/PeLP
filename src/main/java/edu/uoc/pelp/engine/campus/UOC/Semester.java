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
package edu.uoc.pelp.engine.campus.UOC;

import edu.uoc.pelp.engine.campus.IPelpID;
import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.exception.PelpException;
import java.util.Date;

/**
 * Implementation for the time period in the campus of the Universitat Oberta de Catalunya.
 * As semesters are the organization períods, it represents a semster.
 * @author Xavier Baró
 */
public class Semester implements IPelpID,ITimePeriod{
    
    private Date _begin=null;
    private Date _end=null;
    private String _id;
    
    public Semester(String id) {
        _begin=null;
        _end=null;
        _id=id;
    }
    
    public Semester(String id,Date begin,Date end) {
        _begin=begin;
        _end=end;
        _id=id;
    }
    
    /**
     * Obtain the semester id
     * @return Identifier of the semester
     */
    public String getID() {
        return _id;
    }

    /**
     * Check if the object contains valid data
     * @return True if the data is valid, and False if there are incoherent values
     */
    public boolean isValid() {
        // Identifier always must be provided
        if(_id==null) {
            return false;
        }
        
        // Both dates are provided
        if(!_begin.before(_end)) {
            return false;
        }
        
        // In other cases, the object is correct
        return true;
    }

    @Override
    public boolean isActive() {
        Date now = new Date();
        
        // If begin date is provided, check if it is a past or future date
        if(_begin!=null) {
            if(_begin.after(now)) {
                return false;
            }
        }
        
        // If end date is provided, check if it is a past or future date
        if(_end!=null) {
            if(_end.before(now)) {
                return false;
            }
        }
        
        // Oterwise, the period is considered active 
        return true;
    }
    
    /**
     * This method allow fast cast from base class
     * @param timePeriod Generic object
     * @return Object casted to the Semester class or null if is not possible.
     */
    public static Semester toSemester(ITimePeriod timePeriod) {
        
        // Check input object
        if(timePeriod==null) {
            return null;
        }
        
        // Convert to specific object
        if(timePeriod instanceof Semester) {
            return (Semester)timePeriod;
        } else {
            return null;
        }
    }
    
    /**
     * Set starting date for this semester
     * @param date Starting date
     */
    public void setStartDate(Date date) {
        _begin=date;
    }
    
    /**
     * Set ending date for this semester
     * @param date Ending date
     */
    public void setEndDate(Date date) {
        _end=date;
    }

    protected void copyData(IPelpID genericID) throws PelpException {
        if (genericID instanceof Semester) {
            _begin=((Semester)genericID)._begin;
            _end=((Semester)genericID)._end;
            _id=((Semester)genericID)._id;
        } else {
            throw new PelpException("Object of type " + genericID.getClass() + " cannot be copided to an object of class " + this.getClass());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Semester other = (Semester) obj;
        if ((this._id == null) ? (other._id != null) : !this._id.equals(other._id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this._begin != null ? this._begin.hashCode() : 0);
        hash = 59 * hash + (this._end != null ? this._end.hashCode() : 0);
        hash = 59 * hash + (this._id != null ? this._id.hashCode() : 0);
        return hash;
    }

    public int compareTo(IPelpID t) {
        Semester s=(Semester)t;
        if(_begin!=null) {
            if(_begin.compareTo(s._begin)!=0) {
                return _begin.compareTo(s._begin);
            }
        } else {
            if(s._begin!=null) {
                return -1;
            }
        }
        if(_end!=null) {
            if(_end.compareTo(s._end)!=0) {
                return _end.compareTo(s._end);
            }
        } else {
            if(s._end!=null) {
                return -1;
            }
        }
        if(_id!=null) {
            return _id.compareTo(s._id);
        } else {
            if(s._id!=null) {
                return -1;
            }
        }
        
        return 0;
    }

    @Override
    public String toString() {
        return _id;
    }

    public IPelpID parse(String str) {
        return new Semester(str);
    }

    public Date getInitialDate() {
        return _begin;
    }

    public Date getFinalDate() {
        return _end;
    }
}
