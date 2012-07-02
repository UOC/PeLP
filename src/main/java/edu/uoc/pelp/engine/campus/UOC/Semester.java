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

import edu.uoc.pelp.engine.campus.GenericID;
import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.engine.campus.Subject;
import edu.uoc.pelp.exception.PelpException;
import java.util.Date;
import java.util.HashMap;

/**
 * Implementation for the time period in the campus of the Universitat Oberta de Catalunya.
 * As semesters are the organization períods, it represents a semster.
 * @author Xavier Baró
 */
public class Semester extends GenericID implements ITimePeriod{
    
    private Date _begin=null;
    private Date _end=null;
    private String _id;
    
    /** 
     * Subjects active on the semester. 
     */
    private HashMap<SubjectID,Subject> _subjects=null;
    
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

    @Override
    protected void copyData(GenericID genericID) throws PelpException {
        if (genericID instanceof Semester) {
            _begin=((Semester)genericID)._begin;
            _end=((Semester)genericID)._end;
            _id=((Semester)genericID)._id;
        } else {
            throw new PelpException("Object of type " + genericID.getClass() + " cannot be copided to an object of class " + this.getClass());
        }
    }
    
}
