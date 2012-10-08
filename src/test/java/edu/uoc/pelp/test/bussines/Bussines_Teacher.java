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
package edu.uoc.pelp.test.bussines;

import edu.uoc.pelp.bussines.UOC.UOCPelpBussines;
import edu.uoc.pelp.bussines.exception.*;
import edu.uoc.pelp.bussines.vo.*;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.test.conf.PCPelpConfiguration;
import edu.uoc.pelp.test.engine.campus.TestUOC.LocalCampusConnection;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Perform tests over the bussines class without authentication.
 * @author Xavier Baró
 */
public class Bussines_Teacher {    
    
    private UOCPelpBussines _bussines;   
        
    public Bussines_Teacher() {                
        try {
            // Create the bussines object using local resource
            _bussines=new LocalUOCPelpBussinesImpl("hibernate_test.cfg.xml");
            
            // Set logging as student
            LocalCampusConnection campusConnection=new LocalCampusConnection();
            campusConnection.setProfile("teacher1");
            
            // Assign the campus connection
            _bussines.setCampusConnection(campusConnection);
            
            // Assign the test local configuration object
            PCPelpConfiguration conf=new PCPelpConfiguration();
            _bussines.setConfiguration(conf);
           
            // Initialize the engine
            _bussines.initializeEngine();
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidSessionFactoryException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidCampusConnectionException ex) {
            Assert.fail(ex.getMessage());
        }
    } 
    
    public void clearData() {
        // Remove old data
        
        // Insert data for test
    }
    
    @Test 
    public void testCorrectTeacherWorkflow() throws ExecPelpException, InvalidEngineException, AuthorizationException {
        // Clear data
        clearData();
        
        // Get the user information
        UserInformation userInfo = _bussines.getUserInformation();
        Assert.assertNotNull("User information is retrieved", userInfo);
        
        // Get the subjects for this user
        Subject[] userSubjects = _bussines.getUserSubjects();
        Assert.assertEquals("Check the list of subjects",1,userSubjects.length);
        
        // GUI: Select a subject
        Subject selectedSubject=userSubjects[0];
        
        // Check that only one classroom is available (normal situation for students)
        Classroom[] userClassrooms = _bussines.getUserClassrooms(selectedSubject.getSemester(), selectedSubject.getSubjectCode());
        Assert.assertEquals("Check that only one classroom is available",1,userClassrooms.length);
               
        // [GUI]: Select the classroom
        Classroom selectedClassroom=userClassrooms[0];
        
        // Get the activities for this subject (does not depend on the classroom)
        Activity[] activities = _bussines.getSubjectActivities(selectedSubject.getSemester(), selectedSubject.getSubjectCode());
        Assert.assertEquals("Check that there exist some activities for this subject",1, activities.length); 
        
        // GUI: Select the activity
        Activity selectedActivity=activities[0];
        
        // Get the summary information of all the delivers of this classroom
        DeliverSummary[] deliversLastS = _bussines.getLastClassroomDeliverSummary(selectedSubject.getSemester(), selectedSubject.getSubjectCode(), selectedActivity.getIndex(), selectedClassroom.getIndex());
        // Get the detailed information of all the delivers of this user for this activity
        DeliverDetail[] deliversLastD = _bussines.getLastClassroomDeliverDetails(selectedSubject.getSemester(), selectedSubject.getSubjectCode(), selectedActivity.getIndex(), selectedClassroom.getIndex());
        
        // Get the summary information of all the delivers of this classroom
        DeliverSummary[] deliversAllS = _bussines.getAllClassroomDeliverSummary(selectedSubject.getSemester(), selectedSubject.getSubjectCode(), selectedActivity.getIndex(), selectedActivity.getIndex());
        // Get the detailed information of all the delivers of this user for this activity
        DeliverDetail[] deliversAllD = _bussines.getAllClassroomDeliverDetails(selectedSubject.getSemester(), selectedSubject.getSubjectCode(), selectedActivity.getIndex(), selectedActivity.getIndex());  
    }
}
