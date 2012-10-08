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

import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.model.dao.IActivityDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Implements a class to manage activities.
 * @author Xavier Baró
 */
public class DAOActivityManager implements IActivityManager {
    
    /**
     * Activities DAO
     */
    private IActivityDAO _activities;
    
    /**
     * Default constructor for the DAOActivityManager
     * @param activityDAO Object to access all the activities information
     */
    public DAOActivityManager(IActivityDAO activityDAO) {
        _activities=activityDAO;
    }
    
    @Override
    public ActivityID addActivity(ISubjectID subjectID, Date start, Date end) {
        
        // Check the dates
        if(start!=null && end!=null) {
            if(start.after(end)) {
                return null;
            }
        } 
        
        Activity activity=new Activity();
        activity.setStart(start);
        activity.setEnd(end);
        return _activities.add(subjectID,activity);
    }

    @Override
    public boolean editActivity(Activity activity) {     
        // Check the dates
        if(activity.getStart()!=null && activity.getEnd()!=null) {
            if(activity.getStart().after(activity.getEnd())) {
                return false;
            }
        } 
        return _activities.update(activity);
    }

    @Override
    public boolean deleteActivity(ActivityID activityID) {
        return _activities.delete(activityID);
    }

    @Override
    public TestID addTest(ActivityID activityID, ActivityTest test) {
        // Check the activity
        if(_activities.find(activityID)==null) {
            return null;
        }
                
        // Return the new identifier
        return _activities.add(activityID, test);
    }

    @Override
    public boolean editTest(ActivityTest test) {      
        return _activities.update(test);
    }

    @Override
    public boolean deleteTest(TestID testID) {
        return _activities.delete(testID);
    }

    @Override
    public Activity getActivity(ActivityID activityID) {
        // Get the object
        return _activities.find(activityID);
    }

    @Override
    public ActivityTest getTest(TestID testID) {  
        return _activities.find(testID);
    }

    @Override
    public ActivityID[] getSubjectActivities(ISubjectID subject) {
        ArrayList<ActivityID> listIDs=new ArrayList<ActivityID>();
        
        // Create the list of identifiers
        for(Activity activity:_activities.findAll(subject)) {
            listIDs.add(activity.getActivity());
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
        for(Activity activity:_activities.findActive(subject)) {
            listIDs.add(activity.getActivity());
        }
        
        // Sort the list of activities
        Collections.sort(listIDs);
        
        // Create the output array
        ActivityID[] retList=new ActivityID[listIDs.size()];
        listIDs.toArray(retList);
        
        return retList;
    }

    @Override
    public TestID[] getActivityTests(ActivityID activityID) {
        ArrayList<TestID> testList=new ArrayList<TestID>();
        
        // Create the list of identifiers
        for(ActivityTest test:_activities.findAll(activityID)) {
            testList.add(test.getID());
        }
        
        // Create the output array
        TestID[] retList=new TestID[testList.size()];
        testList.toArray(retList);
        
        return retList;
    }
}
