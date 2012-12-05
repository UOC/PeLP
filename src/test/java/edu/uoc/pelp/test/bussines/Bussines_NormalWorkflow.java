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

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import edu.uoc.pelp.bussines.UOC.UOCPelpBussines;
import edu.uoc.pelp.bussines.UOC.vo.UOCClassroom;
import edu.uoc.pelp.bussines.UOC.vo.UOCSubject;
import edu.uoc.pelp.bussines.exception.AuthorizationException;
import edu.uoc.pelp.bussines.exception.InvalidCampusConnectionException;
import edu.uoc.pelp.bussines.exception.InvalidConfigurationException;
import edu.uoc.pelp.bussines.exception.InvalidEngineException;
import edu.uoc.pelp.bussines.exception.InvalidSessionFactoryException;
import edu.uoc.pelp.bussines.vo.Activity;
import edu.uoc.pelp.bussines.vo.Classroom;
import edu.uoc.pelp.bussines.vo.DeliverDetail;
import edu.uoc.pelp.bussines.vo.DeliverFile;
import edu.uoc.pelp.bussines.vo.DeliverSummary;
import edu.uoc.pelp.bussines.vo.MultilingualText;
import edu.uoc.pelp.bussines.vo.MultilingualTextArray;
import edu.uoc.pelp.bussines.vo.Subject;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.exception.InvalidTimePeriodPelpException;
import edu.uoc.pelp.test.TestPeLP;
import edu.uoc.pelp.test.conf.PCPelpConfiguration;
import edu.uoc.pelp.test.engine.campus.TestUOC.LocalCampusConnection;

/**
 * Perform tests over the bussines class with a user wich is administrator
 * @author Xavier Baró
 */
public class Bussines_NormalWorkflow {    
    
    private UOCPelpBussines _bussines;   
    private LocalCampusConnection _campusConnection;
    private File _tempRootPath;
    
    /*
     * Define test information
     */
    private String _testSemesterCode="20121";
    private UOCSubject _testMainSubject=new UOCSubject(_testSemesterCode,"05.062");
    private UOCSubject _testLabSubject=new UOCSubject(_testSemesterCode,"05.073");
    private UOCClassroom _testMainClassroom1=new UOCClassroom(_testMainSubject,1);
    private UOCClassroom _testMainClassroom2=new UOCClassroom(_testMainSubject,2);
    private UOCClassroom _testLabClassroom1=new UOCClassroom(_testLabSubject,1);
    private UOCClassroom _testLabClassroom2=new UOCClassroom(_testLabSubject,2);
    
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
            // Create the temporal path
            _tempRootPath=TestPeLP.createTemporalFolder("BussinesNormalWorkflow");
            
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
            // Get user subjects
            UOCSubject[] userSubjects = _bussines.getUserSubjects();
            Assert.assertNotNull("Get the student subjects", userSubjects);
                     
            // Select a subject (To add activities is not necessary to select the classroom)
            UOCSubject userSubject=null;
            for(UOCSubject sub:userSubjects) {   
                if(sub.equals(_testMainSubject)) {
                    userSubject=sub;
                    break;
                }
            }
            Assert.assertEquals("Select the user Subject", this._testMainSubject,userSubject);
            
            // Create the array of descriptions for the activity
            MultilingualTextArray activityDescriptions=new MultilingualTextArray(3);
            activityDescriptions.setText(0,new MultilingualText("CAT","Activitat 1"));
            activityDescriptions.setText(1,new MultilingualText("ESP","Actividad 1"));
            activityDescriptions.setText(2,new MultilingualText("ENG","Activity 1"));
            
            // Create the tests
            edu.uoc.pelp.bussines.vo.Test[] tests= BussinesTests.getTests_Echo();
            MultilingualTextArray[] testDesc=BussinesTests.getTestDescriptions_Echo();
            
            // Add the new activity without tests
            Activity activity1=_bussines.addActivity(userSubject, null, null, 3, "JAVA", activityDescriptions);
            Assert.assertNotNull("Add activity without tests", activity1);
            
            // Add the new activity with tests
            Activity activity2=_bussines.addActivity(userSubject, null, null, 3, "JAVA", activityDescriptions,tests,testDesc);
            Assert.assertNotNull("Add activity without tests", activity2);
                  
        } catch (AuthorizationException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (ExecPelpException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    private void addDeliversStudent1() {
        try {
            
            // Get activities for test subject
            Activity[] userActivities = _bussines.getSubjectActivities(_testMainSubject);
            Assert.assertNotNull("Get the student activities", userActivities);
            Assert.assertEquals("Get the student activities (check length)", 2 ,userActivities.length);
            
            // Add one deliver to activity 1
            perfomDeliverAddition(_testMainSubject, userActivities[0], 1);
                  
            // Get the list of delivers for activities
            DeliverDetail[] delAct1=_bussines.getUserDeliverDetails(userActivities[0]);
            Assert.assertEquals("Get user delivers to activity 1",1,delAct1.length);
            DeliverDetail[] delAct2=_bussines.getUserDeliverDetails(userActivities[1]);
            Assert.assertEquals("Get user delivers to activity 2",0,delAct2.length);
            
            
            // Add two delivers to activity 2
            perfomDeliverAddition(_testMainSubject, userActivities[1], 2);
                        
            // Get the list of delivers for activities
            delAct1=_bussines.getUserDeliverDetails(userActivities[0]);
            Assert.assertEquals("Get user delivers to activity 1",1,delAct1.length);
            delAct2=_bussines.getUserDeliverDetails(userActivities[1]);
            Assert.assertEquals("Get user delivers to activity 2",2,delAct2.length);
            
        } catch (ExecPelpException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (AuthorizationException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    private void addDeliversStudent2() {
        try {
            
            // Get activities for test subject
            Activity[] userActivities = _bussines.getSubjectActivities(_testMainSubject);
            Assert.assertNotNull("Get the student activities", userActivities);
            Assert.assertEquals("Get the student activities (check length)", 2 ,userActivities.length);
            
            // Add three delivers to activity 1
            perfomDeliverAddition(_testMainSubject, userActivities[0], 3);
                  
            // Get the list of delivers for activities
            DeliverDetail[] delAct1=_bussines.getUserDeliverDetails(userActivities[0]);
            Assert.assertEquals("Get user delivers to activity 1",3,delAct1.length);
            DeliverDetail[] delAct2=_bussines.getUserDeliverDetails(userActivities[1]);
            Assert.assertEquals("Get user delivers to activity 2",0,delAct2.length);
            
            
            // Add one deliver to activity 2
            perfomDeliverAddition(_testMainSubject, userActivities[1], 1);
                        
            // Get the list of delivers for activities
            delAct1=_bussines.getUserDeliverDetails(userActivities[0]);
            Assert.assertEquals("Get user delivers to activity 1",3,delAct1.length);
            delAct2=_bussines.getUserDeliverDetails(userActivities[1]);
            Assert.assertEquals("Get user delivers to activity 2",1,delAct2.length);
            
        } catch (ExecPelpException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (AuthorizationException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    private void addDeliversStudent3() {
        try {
            
            // Get activities for test subject
            Activity[] userActivities = _bussines.getSubjectActivities(_testMainSubject);
            Assert.assertNotNull("Get the student activities", userActivities);
            Assert.assertEquals("Get the student activities (check length)", 2 ,userActivities.length);
            
            // Add two delivers to activity 1
            perfomDeliverAddition(_testMainSubject, userActivities[0], 2);
                  
            // Get the list of delivers for activities
            DeliverDetail[] delAct1=_bussines.getUserDeliverDetails(userActivities[0]);
            Assert.assertEquals("Get user delivers to activity 1",2,delAct1.length);
            DeliverDetail[] delAct2=_bussines.getUserDeliverDetails(userActivities[1]);
            Assert.assertEquals("Get user delivers to activity 2",0,delAct2.length);
            
            
            // Add two delivers to activity 2
            perfomDeliverAddition(_testMainSubject, userActivities[1], 2);
                        
            // Get the list of delivers for activities
            delAct1=_bussines.getUserDeliverDetails(userActivities[0]);
            Assert.assertEquals("Get user delivers to activity 1",2,delAct1.length);
            delAct2=_bussines.getUserDeliverDetails(userActivities[1]);
            Assert.assertEquals("Get user delivers to activity 2",2,delAct2.length);
            
        } catch (ExecPelpException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (AuthorizationException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    private void checkDelivers1() {
        try {
            // Get activities for test subject
            Activity[] userActivities = _bussines.getSubjectActivities(_testMainSubject);
            Assert.assertNotNull("Get the student activities", userActivities);
            Assert.assertEquals("Get the student activities (check length)", 2 ,userActivities.length);

            // Teacher1 is teacher of main subject classroom 1, with student1, student2 and student3
            //    Activity1:  1 (student1) + 3 (student2) + 2 (student3) => 6 delivers from 3 students
            //    Activity2:  2 (student1) + 1 (student2) + 2 (student3) => 5 delivers from 3 students
            checkTeacherDelivers(_testMainSubject, userActivities[0], 6, 3); 
            checkTeacherDelivers(_testMainSubject, userActivities[1], 5, 3); 
            
        } catch (ExecPelpException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (AuthorizationException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    private void checkDelivers2() {
        try {
            // Get activities for test subject
            Activity[] userActivities = _bussines.getSubjectActivities(_testLabSubject);
            Assert.assertNotNull("Get the student activities", userActivities);
            Assert.assertEquals("Get the student activities (check length)", 2 ,userActivities.length);

            // Teacher2 is teacher of laboratory subject classroom 2, with student2 and student3
            //    Activity1:  3 (student2) + 2 (student3) => 5 delivers from 2 students
            //    Activity2:  1 (student2) + 2 (student3) => 3 delivers from 2 students
            checkTeacherDelivers(_testLabSubject, userActivities[0], 5, 2); 
            checkTeacherDelivers(_testLabSubject, userActivities[1], 3, 2); 
            
        } catch (ExecPelpException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (AuthorizationException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    /**
     * Create dummy delivers of the current user to a certain activity. It shows the process of
     * subject and activity selection, which may be done using the web GUI.
     * @param subject Subject to be selected from available
     * @param activity Activity to to be selected from available activities
     * @param numDelivers Number of delivers to be added to this activity
     */
    private void perfomDeliverAddition(Subject subject, Activity activity, int numDelivers) {
        try {
            // Get user subjects
            UOCSubject[] userSubjects = _bussines.getUserSubjects();
            Assert.assertNotNull("Get the student subjects", userSubjects);
                     
            // Select a subject (GUI) (Students should have only one classroom per subject, therefore, classroom selection can be avoided.)
            UOCSubject userSubject=null;
            for(UOCSubject sub:userSubjects) {   
                if(sub.equals(subject)) {
                    userSubject=sub;
                    break;
                }
            }
            Assert.assertEquals("Select the user Subject", subject,userSubject);

            
            // Get activities for selected subject
            Activity[] userActivities = _bussines.getSubjectActivities(userSubject);
            Assert.assertNotNull("Get the student activities", userActivities);
            Assert.assertEquals("Get the student activities (check length)", 2 ,userActivities.length);
            
            
            // Select the activity (GUI)
            Activity userActivity=null;
            for(Activity act:userActivities) {   
                if(act.equals(activity)) {
                    userActivity=act;
                    break;
                }
            }
            Assert.assertEquals("Select the user Activity", activity,userActivity);
            
            
            // Add delivers
            for(int i=0;i<numDelivers;i++) {
                // Create two temporal files
                File tmpFolder=TestPeLP.createTemporalFolder("BussinesStudentFiles");
                String codeFileName=BussinesTests.getCodeFilename_Echo("JAVA");
                Assert.assertTrue("Create code file", TestPeLP.createFile(tmpFolder, codeFileName, BussinesTests.getCode_Echo("JAVA")));
                Assert.assertTrue("Create report file", TestPeLP.createFile(tmpFolder, "report.txt", "This is a report file with information"));

                // Create the file objects
                DeliverFile[] files=new DeliverFile[2];
                files[0]=new DeliverFile(tmpFolder,new File(codeFileName));
                files[0].setIsMain(true);
                files[0].setIsCode(true);
                files[1]=new DeliverFile(tmpFolder,new File("report.txt"));
                files[1].setIsReport(true);

                // Add the deliver
                DeliverDetail deliver=_bussines.addDeliver(userActivity, files);
                Assert.assertNotNull("Add the deliver to activity", deliver);
            }
            
        } catch (ExecPelpException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (AuthorizationException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    /**
     * Create dummy delivers of the current user to a certain activity. It shows the process of
     * subject and activity selection, which may be done using the web GUI.
     * @param subject Subject to be selected from available
     * @param activity Activity to to be selected from available activities
     * @param numTotalDelivers Total number of delivers in this class (assuming one class per user and subject)
     * @param numTotalStudents Total number of students in this class (assuming one class per user and subject)
     */
    private void checkTeacherDelivers(Subject subject, Activity activity, int numTotalDelivers, int numTotalStudents) {
        try {
            // Get user subjects
            UOCSubject[] userSubjects = _bussines.getUserSubjects();
            Assert.assertNotNull("Get the student subjects", userSubjects);
                     
            // Select a subject (GUI)
            UOCSubject userSubject=null;
            for(UOCSubject sub:userSubjects) {   
                if(sub.equals(subject)) {
                    userSubject=sub;
                    break;
                }
            }
            Assert.assertEquals("Select the user Subject", subject,userSubject);
            
            // Select the classroom (GUI)
            UOCClassroom[] userClassrooms= _bussines.getUserClassrooms(userSubject);
            Assert.assertNotNull("Get the user classrooms", userClassrooms);
            Assert.assertEquals("Get the user classrooms (check length)", 1 ,userClassrooms.length);
            Classroom userClassroom=userClassrooms[0];
            Assert.assertNotNull("Select the user Classroom", userClassroom);
            
            
            // Get activities for selected subject
            Activity[] userActivities = _bussines.getSubjectActivities(userSubject);
            Assert.assertNotNull("Get the user activities", userActivities);
            Assert.assertEquals("Get the user activities (check length)", 2 ,userActivities.length);
            
            
            // Select the activity (GUI)
            Activity userActivity=null;
            for(Activity act:userActivities) {   
                if(act.equals(activity)) {
                    userActivity=act;
                    break;
                }
            }
            Assert.assertEquals("Select the user Activity", activity,userActivity);
            
            // Get all the delivers for teacher classroom
            DeliverSummary[] allDeliversSummary = _bussines.getAllClassroomDeliverSummary(userActivity, userClassroom);
            Assert.assertEquals("Get summarized information for all delivers in a classroom", numTotalDelivers, allDeliversSummary.length);
            DeliverDetail[] allDeliversDetail = _bussines.getAllClassroomDeliverDetails(userActivity, userClassroom);
            Assert.assertEquals("Get detailed information for all delivers in a classroom", numTotalDelivers, allDeliversDetail.length);
            
            // Get the last deliver of each student for teacher classroom
            DeliverSummary[] lastDeliversSummary = _bussines.getLastClassroomDeliverSummary(userActivity, userClassroom);
            Assert.assertEquals("Get summarized information for last delivers in a classroom", numTotalStudents, lastDeliversSummary.length);
            DeliverDetail[] lastDeliversDetail = _bussines.getLastClassroomDeliverDetails(userActivity, userClassroom);
            Assert.assertEquals("Get detailed information for last delivers in a classroom", numTotalStudents, lastDeliversDetail.length);
            
        } catch (ExecPelpException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (AuthorizationException ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
