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
package edu.uoc.pelp.test.engine.activity;

import edu.uoc.pelp.engine.activity.*;
import edu.uoc.pelp.engine.campus.ISubjectID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * Implements a dummy class to manage activities. It uses dynamic memory structures to
 * store the objects.
 * @author Xavier Baró
 */
public class LocalActivityManager implements IActivityManager {
    
    /**
     * Map storing all activities
     */
    private HashMap<ActivityID,Activity> _activities=new HashMap<ActivityID,Activity>();
    
    /**
     * Map storing all tests
     */
    private HashMap<TestID,ActivityTest> _tests=new HashMap<TestID,ActivityTest>();
    
    @Override
    public ActivityID addActivity(ISubjectID subject, Date start, Date end) {
        // Get all activities of a certain subject
        ActivityID[] activities=getSubjectActivities(subject);
        
        // Search the last identifier
        long lastID=0;
        for(ActivityID id:activities) {
            if(id.index>lastID) {
                lastID=id.index;
            }
        }
        
        // Create a new identifier
        ActivityID newID=new ActivityID(subject,lastID+1);
        
        // Create the new Activity
        _activities.put(newID,new Activity(newID,start,end));
        
        return newID;
    }

    @Override
    public ActivityID addActivity(ISubjectID subject, Activity activity) {
        // Get all activities of a certain subject
        ActivityID[] activities=getSubjectActivities(subject);
        
        // Search the last identifier
        long lastID=0;
        for(ActivityID id:activities) {
            if(id.index>lastID) {
                lastID=id.index;
            }
        }
        
        // Create a new identifier
        ActivityID newID=new ActivityID(subject,lastID+1);
        
        // Create the new Activity
        activity.setActivityID(newID);
        _activities.put(newID,new Activity(newID,activity.clone()));
        
        return newID;
    }
    
    @Override
    public boolean editActivity(Activity activity) {
        // Check if the activity exists
        if(_activities.containsKey(activity.getActivity())) {
            // Check if this activity is correct
            if(activity.getStart()!=null && activity.getEnd()!=null) {
                if(activity.getStart().after(activity.getEnd())) {
                    // Incorrect dates, do nothing and return false.
                    return false;
                }
            }
            // Remove old entry and add the new one
            _activities.remove(activity.getActivity());
            _activities.put(activity.getActivity(),activity.clone());
            return true;
        }
        
        return false;
    }

    @Override
    public boolean deleteActivity(ActivityID activityID) {
        // Check if the activity exists
        if(_activities.containsKey(activityID)) {
            // Remove the activity
            _activities.remove(activityID);
            return true;
        }
        
        return true;
    }

    @Override
    public TestID addTest(ActivityID activityID, ActivityTest test) {
        // Check the activity
        if(!_activities.containsKey(activityID)) {
            return null;
        }
        
        // Get all tests of a certain activity
        TestID[] tests=getActivityTests(activityID);
        
        // Search the last identifier
        long lastID=0;
        for(TestID testObj:tests) {
            if(testObj.index>lastID) {
                lastID=testObj.index;
            }
        }
        
        // Create a new identifier
        TestID newID=new TestID(activityID,lastID+1);
        ActivityTest newTest=test.clone();
        newTest.setTestID(newID);
        
        // Add the new test
        _tests.put(newID, newTest);
                
        // Return the modified object
        return newID;
    }

    @Override
    public boolean editTest(ActivityTest test) {
        // Check if the test exists
        if(_tests.containsKey(test.getID())) {
            // Remove old entry and add the new one
            _tests.remove(test.getID());
            _tests.put(test.getID(),test.clone());
            return true;
        }
        
        return false;
    }

    @Override
    public boolean deleteTest(TestID testID) {
        // Check if the test exists
        if(_tests.containsKey(testID)) {
            // Remove the test
            _tests.remove(testID);
            return true;
        }
        
        return false;
    }

    @Override
    public Activity getActivity(ActivityID activityID) {
        Activity newActivity=null;
        
        // Get the object
        Activity activity=_activities.get(activityID);
        
        // Create a new object, to avoid returning references
        if(activity!=null) {
            newActivity=activity.clone();
        }
        
        return newActivity;
    }

    @Override
    public ActivityTest getTest(TestID testID) {
        ActivityTest newTest=null;
        
        // Get the object
        ActivityTest test=_tests.get(testID);
        
        // Create a new object, to avoid returning references
        if(test!=null) {
            newTest=test.clone();
        }
        
        return newTest;
    }

    @Override
    public ActivityID[] getSubjectActivities(ISubjectID subject) {
        ArrayList<ActivityID> listIDs=new ArrayList<ActivityID>();
        
        // Create the list of identifiers
        for(Activity activity:_activities.values()) {
            if(subject.equals(activity.getActivity().subjectID)) {
                listIDs.add(activity.getActivity());
            }
        }
        
        // Sort the list of activities
        Collections.sort(listIDs);
        
        // Create the output array
        ActivityID[] retList=new ActivityID[listIDs.size()];
        listIDs.toArray(retList);
        
        return retList;
    }

    @Override
    public ActivityID[] getSubjectActiveActivities(ISubjectID subject) {
        ArrayList<ActivityID> listIDs=new ArrayList<ActivityID>();
        
        // Create the list of identifiers
        for(Activity activity:_activities.values()) {
            if(subject.equals(activity.getActivity().subjectID)) {
                if(activity.isActive()) {
                    listIDs.add(activity.getActivity());
                }
            }
        }
        
        // Create the output array
        ActivityID[] retList=new ActivityID[listIDs.size()];
        listIDs.toArray(retList);
        
        return retList;
    }

    @Override
    public TestID[] getActivityTests(ActivityID activityID) {
        ArrayList<TestID> testList=new ArrayList<TestID>();
        
        // Create the list of identifiers
        for(ActivityTest test:_tests.values()) {
            if(test.getID().activity.equals(activityID)) {
                testList.add(test.getID());
            }
        }
        
        // Create the output array
        TestID[] retList=new TestID[testList.size()];
        testList.toArray(retList);
        
        return retList;
    }
}
