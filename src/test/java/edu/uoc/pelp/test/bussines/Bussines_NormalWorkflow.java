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
import edu.uoc.pelp.bussines.UOC.vo.UOCSubject;
import edu.uoc.pelp.bussines.exception.*;
import edu.uoc.pelp.bussines.vo.Activity;
import edu.uoc.pelp.bussines.vo.Classroom;
import edu.uoc.pelp.bussines.vo.MultilingualText;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.exception.InvalidTimePeriodPelpException;
import edu.uoc.pelp.test.conf.PCPelpConfiguration;
import edu.uoc.pelp.test.engine.campus.TestUOC.LocalCampusConnection;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Perform tests over the bussines class with a user wich is administrator
 * @author Xavier Baró
 */
public class Bussines_NormalWorkflow {    
    
    private UOCPelpBussines _bussines;   
    private LocalCampusConnection _campusConnection;
    
    /*
     * Define test information
     */
    private String _testSemesterCode="20121";
    private UOCSubject _testMainSubject=new UOCSubject(_testSemesterCode,"05.062");
    private UOCSubject _testLabSubject=new UOCSubject(_testSemesterCode,"05.073");
    private Classroom _testMainClassroom1=new Classroom(_testMainSubject,1);
    private Classroom _testMainClassroom2=new Classroom(_testMainSubject,2);
    private Classroom _testLabClassroom1=new Classroom(_testLabSubject,1);
    private Classroom _testLabClassroom2=new Classroom(_testLabSubject,2);
    
    /* LOCAL CAMPUS CONNECTION SCENARIO:
     *      
     * admin: 
     *      Administrator for the PeLP platform
     * pra1: 
     *      Main teacher for all classrooms for subjects 05.062 and 05.073 for semester 20121
     * teacher1: 
     *      Teacher of the classroom 1 of subject 05.062 semester 20121
     * teacher2:
     *      Teacher of the classroom 2 of subject 05.073 semester 20121
     * student1:
     *      Student in the classroom 1 of subject 05.062 and classroom 1 of subject 05.073 semester 20121
     * student2:
     *      Student in the classroom 1 of subject 05.062 and classroom 2 of subject 05.073 semester 20121
     * student3:
     *      Student in the classroom 1 of subject 05.062 and classroom 2 of subject 05.073 semester 20121
     */
        
    public Bussines_NormalWorkflow() {                
        try {
            // Create the bussines object using local resource
            LocalUOCPelpBussinesImpl localBussines=new LocalUOCPelpBussinesImpl("hibernate_test.cfg.xml");
            _bussines=localBussines;
            
            // Remove all the data
            localBussines.clearDatabaseData();
            
            // Create a new local campus connetion
            _campusConnection=new LocalCampusConnection();
                        
            // Add the register to the admin database to give administration rights
            _campusConnection.setProfile("admin");
            localBussines.getAdminDAO().addAdmin(_campusConnection.getUserData(), true, false);
            _campusConnection.setProfile("none");
                        
            // Assign the campus connection
            _bussines.setCampusConnection(_campusConnection);
            
            // Assign the test local configuration object
            PCPelpConfiguration conf=new PCPelpConfiguration();
            _bussines.setConfiguration(conf);
           
            // Initialize the engine
            if(_bussines instanceof LocalUOCPelpBussinesImpl) {
                ((LocalUOCPelpBussinesImpl)_bussines).initializeTestEngine();
            } else {
                _bussines.initializeEngine();
            }
            
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            Assert.fail(ex.getMessage());
        } catch (AuthPelpException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidSessionFactoryException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidCampusConnectionException ex) {
            Assert.fail(ex.getMessage());
        }
    } 
    
    @Test
    public void testCorrectWorkflow() {
        // An administrator log in and prepare the subject to be used by the platform
        _campusConnection.setProfile("admin");
        prepareSubject();
        
        // Once the subject is active, a teacher log in and add the activities for the subject
        _campusConnection.setProfile("teacher1");
        addActivities();
        
        // Now is all prepared for students. 
        _campusConnection.setProfile("student1");
        addDeliversStudent1();
        _campusConnection.setProfile("student2");
        addDeliversStudent2();
        _campusConnection.setProfile("student3");
        addDeliversStudent3();
        
        // The teacher from the main classroom can see all the delivers
        _campusConnection.setProfile("teacher1");
        checkDelivers1();
                
        // The teacher from the laboratory only the delivers from students 2 and 3
        _campusConnection.setProfile("teacher2");
        checkDelivers2();
        
    }

    private void prepareSubject() {
        try {
            // Only admins can perform those actions. If current user is not an admin will fail
            
            // Add the semester to the platform
            Assert.assertTrue("Add the semester",_bussines.addSemester(_testSemesterCode, null, null));
            
            // Activate the subject
            Assert.assertTrue("Activate the subject",_bussines.activateSubject(_testMainSubject));
            
            // Set links between main subject and laboratory
            Assert.assertTrue("Add the laboratory to the main subject",_bussines.addLaboratory(_testMainSubject.getSubjectCode(), _testLabSubject.getSubjectCode()));
            
        } catch (InvalidTimePeriodPelpException ex) {
            Assert.fail(ex.getMessage());
        } catch (AuthorizationException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    private void addActivities() {
        try {
            // Create an activity
            MultilingualText[] activityDescriptions=new MultilingualText[3];
            activityDescriptions[0]=new MultilingualText("CAT","Activitat 1");
            activityDescriptions[1]=new MultilingualText("ESP","Actividad 1");
            activityDescriptions[2]=new MultilingualText("ENG","Activity 1");
            
            MultilingualText[] test1Descriptions=new MultilingualText[3];
            activityDescriptions[0]=new MultilingualText("CAT","Activitat 1 (Test1)");
            activityDescriptions[1]=new MultilingualText("ESP","Actividad 1 (Test1)");
            activityDescriptions[2]=new MultilingualText("ENG","Activity 1 (Test1)");
            
            MultilingualText[] test2Descriptions=new MultilingualText[3];
            activityDescriptions[0]=new MultilingualText("CAT","Activitat 1 (Test2)");
            activityDescriptions[1]=new MultilingualText("ESP","Actividad 1 (Test2)");
            activityDescriptions[2]=new MultilingualText("ENG","Activity 1 (Test2)");
            
            MultilingualText[] test3Descriptions=new MultilingualText[3];
            activityDescriptions[0]=new MultilingualText("CAT","Activitat 1 (Test3)");
            activityDescriptions[1]=new MultilingualText("ESP","Actividad 1 (Test3)");
            activityDescriptions[2]=new MultilingualText("ENG","Activity 1 (Test3)");
            
            MultilingualText[] test4Descriptions=new MultilingualText[3];
            activityDescriptions[0]=new MultilingualText("CAT","Activitat 1 (Test4)");
            activityDescriptions[1]=new MultilingualText("ESP","Actividad 1 (Test4)");
            activityDescriptions[2]=new MultilingualText("ENG","Activity 1 (Test4)");
            
            Activity activiy1=_bussines.addActivity(_testMainSubject, null, null, 3, "JAVA", activityDescriptions);
            
        } catch (AuthorizationException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (ExecPelpException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    private void addDeliversStudent1() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void addDeliversStudent2() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void addDeliversStudent3() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void checkDelivers1() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void checkDelivers2() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
