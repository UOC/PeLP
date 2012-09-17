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
package edu.uoc.pelp.test.model.dao;

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.activity.ActivityTest;
import edu.uoc.pelp.engine.activity.TestID;
import edu.uoc.pelp.model.dao.IActivityTestDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implements the DAO object for the test Activities. It uses memory structures to store de data
 * and copy objects to break the references.
 * @author Xavier Baró
 */
public class ActivityTestDAO implements IActivityTestDAO {
    
    /**
     * Table simulating the database table
     */
    private HashMap<TestID,ActivityTest> _activityTest=new HashMap<TestID,ActivityTest>();

    public TestID add(ActivityID activityID, ActivityTest object) {
        
        // Check the input object
        if(object==null || activityID==null) {
            return null;
        }
        
        // Search the last identifier
        long lastID=0;
        for(TestID id:_activityTest.keySet()) {
            if(id.activity.equals(activityID)) {
                if(id.index>lastID) {
                    lastID=id.index;
                }
            }
        }
        
        // Create a new identifier
        TestID newID=new TestID(activityID,lastID+1);
        
        // Store the object
        _activityTest.put(newID, new ActivityTest(newID,object));
        
        return newID;
    }

    public boolean delete(TestID id) {
        
        // Check the input object
        if(id==null) {
            return false;
        }
        
        // Check that the object does not exists
        if(_activityTest.remove(id)==null) {
            return false;
        }
        
        return true;
    }

    public boolean update(ActivityTest object) {
        // Check the input object
        if(object==null) {
            return false;
        }
        
        // Check that the object does not exists
        if(_activityTest.remove(object.getID())==null) {
            return false;
        }
        
        // Store the new object
        _activityTest.put(object.getID(), new ActivityTest(object.getID(),object));
        
        return _activityTest.containsKey(object.getID());
    }

    public List<ActivityTest> findAll() {
        ArrayList<ActivityTest> list=new ArrayList<ActivityTest>();
        
        // Build the output list
        for(ActivityTest test:_activityTest.values()) {
            list.add(new ActivityTest(test));
        }
        
        return list;
    }

    public ActivityTest find(TestID object) {
        // Check the input object
        if(object==null) {
            return null;
        }
        
        return new ActivityTest(_activityTest.get(object));
    }

    public List<ActivityTest> findAll(ActivityID activity) {
        ArrayList<ActivityTest> list=new ArrayList<ActivityTest>();
        
        // Build the output list
        for(ActivityTest test:_activityTest.values()) {
            if(test.getID().activity.equals(activity)) {
                list.add(new ActivityTest(test));
            }
        }
        
        return list;
    }
}

