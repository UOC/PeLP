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
package edu.uoc.pelp.test.model.dao.UOC;

import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.model.dao.ITimePeriodDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implements the DAO object for the Semester class
 * @author Xavier Baró
 */
public class SemesterDAO implements ITimePeriodDAO {
    
    /**
     * Table simulating the database table
     */
    private HashMap<String,Semester> _semester=new HashMap<String,Semester>();

    public boolean save(ITimePeriod object) {
        
        // Check the input object
        if(Semester.toSemester(object)==null) {
            return false;
        }
        
        // Check that the object does not exists
        if(_semester.containsKey(Semester.toSemester(object).getID())) {
            return false;
        }
        
        // Store the object
        _semester.put(Semester.toSemester(object).getID(), Semester.toSemester(object));
        
        return _semester.containsKey(Semester.toSemester(object).getID());
    }

    public boolean delete(ITimePeriod object) {
        
        // Check the input object
        if(Semester.toSemester(object)==null) {
            return false;
        }
        
        // Check that the object does not exists
        if(_semester.remove(Semester.toSemester(object).getID())==null) {
            return false;
        }
        
        return true;
    }

    public boolean update(ITimePeriod object) {
        // Check the input object
        if(Semester.toSemester(object)==null) {
            return false;
        }
        
        // Check that the object does not exists
        if(_semester.remove(Semester.toSemester(object).getID())==null) {
            return false;
        }
        
        // Store the new object
        _semester.put(Semester.toSemester(object).getID(), Semester.toSemester(object));
        
        return _semester.containsKey(Semester.toSemester(object).getID());
    }

    public List<ITimePeriod> findAll() {
        ArrayList<ITimePeriod> list=new ArrayList<ITimePeriod>();
        
        // Build the output list
        for(Semester semester:_semester.values()) {
            list.add(semester);
        }
        
        return list;
    }

    public List<ITimePeriod> findActive() {
        ArrayList<ITimePeriod> list=new ArrayList<ITimePeriod>();
        
        // Build the output list
        for(Semester semester:_semester.values()) {
            if(semester.isActive()) {
                list.add(semester);
            }
        }
        
        return list;
    }

    public ITimePeriod find(ITimePeriod object) {
        // Check the input object
        if(Semester.toSemester(object)==null) {
            return null;
        }
        
        return _semester.get(Semester.toSemester(object).getID());
    }
    
}
