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
import java.util.Date;

/**
 * This interface describes the methods for activity managing.
 * @author Xavier Baró
 */
public interface IActivityManager {
    
    /**
     * Adds a new activity to a certain subject
     * @param subject Subject identifier.
     * @param start Starting date. This activity will be available from this date, or if null, from any moment.
     * @param end Ending date. This activity will be available until this date, or if null, forever.
     * @return Returns a new Activity or null if it cannot be created. Further information can be added to the activity using the editActivity method
     */
    Activity addActivity(ISubjectID subject, Date start, Date end);
    
    /**
     * Modify the information of a certain activity
     * @param activity Activity with new information. The ActivityID cannot be modifyed, the rest of the information is updated.
     * @return True if the new information has been correctly modified or False if any error occurred.
     */
    boolean editActivity(Activity activity);
    
    /**
     * Delete a certain activity
     * @param activityID Identifier of the activity to be reomoved.
     * @return True if the activity been correctly deleted or False if any error occurred.
     */
    boolean deleteActivity(ActivityID activityID);
    
    /** 
     * Add a new test to an existing activity. 
     * @param activityID Indentifier for the activity
     * @param test ActivityTest object to be added. If testID is provided, it will be overwrited.
     * @return The ActivityTest object with a new identifier or null if any error occurred.
     */
    ActivityTest addTest(ActivityID activityID,ActivityTest test);
    
    /**
     * Modifies the information of a certain test. It only can be used for tests linked to any activity, and
     * testID must be set.
     * @param test ActivityTest with new information. The testID cannot be modifyed, the rest of the information is updated.
     * @return True if the new information has been correctly modified or False if any error occurred. 
     */
    boolean editTest(ActivityTest test);
    
    /**
     * Delete a certain test
     * @param testID Identifier of the test to be reomoved.
     * @return True if the test been correctly deleted or False if any error occurred.
     */
    boolean deleteTest(TestID testID);
    
    /** 
     * Retrieve the activity information
     * @param activityID Activity Identifier
     * @return Activity object with the information or null if no activity found for this identifier.
     */    
    Activity getActivity(ActivityID activityID);
    
    /** 
     * Retrieve the test information
     * @param testID ActivityTest Identifier
     * @return ActivityTest object with the information or null if no test found for this identifier.
     */    
    ActivityTest getTest(TestID testID);
    
    /**
     * Retrieve all the activities for a certain subject, sorted by ascending index
     * @param subject Subject identifier 
     * @return Array of activities for this subject
     */
    ActivityID[] getSubjectActivities(ISubjectID subject);
    
    /**
     * Retrieve all the active activities for a certain subject, sorted by ascending index
     * @param subject Subject identifier 
     * @return Array of active activities for this subject
     */
    ActivityID[] getSubjectActiveActivities(ISubjectID subject);
    
    /**
     * Retrieve all the tests for a certain activity, sorted by ascending index
     * @param activityID Activity identifier
     * @return Array of ActivityTest objects
     */    
    TestID[] getActivityTests(ActivityID activityID);
}
