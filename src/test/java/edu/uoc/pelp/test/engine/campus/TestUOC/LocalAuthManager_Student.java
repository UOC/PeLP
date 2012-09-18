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
package edu.uoc.pelp.test.engine.campus.TestUOC;

import edu.uoc.pelp.engine.campus.UOC.UserID;
import edu.uoc.pelp.engine.campus.*;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.test.TestPeLP;
import org.junit.Assert;
import org.junit.Test;

/**
 * Perform all tests over the Authorization module of the PeLP platform.
 * This class uses the local campus connection with an authenticated user,
 * which has access to one subject.
 * @author Xavier Baró
 */
public class LocalAuthManager_Student {
    
    private ICampusConnection _campusConnection=(ICampusConnection) new LocalCampusConnection();
    
    public LocalAuthManager_Student() {
        // Set an authenticated user with active subjects
        TestPeLP.getLocal(_campusConnection).setProfile("student1");
    }
    
    @Test 
    public void testAuthUserRightsToCampus() {
        // Check that user is authenticated
        Assert.assertTrue("Check if user is authenticated.",_campusConnection.isUserAuthenticated());
    }
    
    @Test
    public void testAuthUserIDAccess() throws AuthPelpException {
        // Try to obtain the user ID
        IUserID id=_campusConnection.getUserID();
        Assert.assertNotNull(id);
        Assert.assertEquals(((UserID)id),TestPeLP.getUser(_campusConnection, 3).getUserID());
    }
    
    @Test
    public void testAuthAllUserSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(null);
        Assert.assertNotNull(subjectList);
        Assert.assertTrue("One subject is retrived", subjectList.length==1);
        Assert.assertEquals(TestPeLP.getLocal(_campusConnection).getTestSubjectByPos(0).getID(),subjectList[0]);
    }
    
    @Test
    public void testAuthStudentSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(UserRoles.Student,null);
        Assert.assertNotNull(subjectList);
        Assert.assertTrue("One subject is retrived", subjectList.length==1);
        Assert.assertEquals(TestPeLP.getLocal(_campusConnection).getTestSubjectByPos(0).getID(),subjectList[0]);
    }
    
    @Test
    public void testAuthTeacherSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(UserRoles.Teacher,null);
        Assert.assertNotNull(subjectList);
        Assert.assertTrue("No Subjects are retrieved", subjectList.length==0);
    }
    
    @Test
    public void testAuthMainTeacherSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(UserRoles.MainTeacher,null);
        Assert.assertNotNull(subjectList);
        Assert.assertTrue("No Subjects are retrieved", subjectList.length==0);
    }
    
    @Test
    public void testAuthAllUserClassroomsAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(null);
        Assert.assertNotNull(classroomsList);
        Assert.assertTrue("One classroom is retrieved", classroomsList.length==1);
        Assert.assertEquals(TestPeLP.getClassroom(_campusConnection, 0,1).getClassroomID(), classroomsList[0]);
    }
    
    @Test
    public void testAuthStudentClassroomAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(UserRoles.Student,null);
        Assert.assertNotNull(classroomsList);
        Assert.assertTrue("One classroom is retrieved", classroomsList.length==1);
        Assert.assertEquals(TestPeLP.getClassroom(_campusConnection, 0,1).getClassroomID(), classroomsList[0]);
    }
    
    @Test
    public void testAuthTeacherClassroomAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(UserRoles.Teacher,null);
        Assert.assertNotNull(classroomsList);
        Assert.assertTrue("No Classrooms are retrieved", classroomsList.length==0);
    }
    
    @Test
    public void testAuthMainTeacherClassroomAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(UserRoles.MainTeacher,null);
        Assert.assertNotNull(classroomsList);
        Assert.assertTrue("No Classrooms are retrieved", classroomsList.length==0);
    }
    
    @Test
    public void testGetUserInfo() throws AuthPelpException {
        // Obtain the user information
        Person person=_campusConnection.getUserData();   
        Assert.assertNotNull(person);
        Assert.assertEquals(person, TestPeLP.getLocal(_campusConnection).getTestUser((UserID)person.getUserID()));
    }
    
    @Test(expected=AuthPelpException.class)
    public void testGetOtherUserInfo() throws AuthPelpException {       
        // Obtain the information of user0 (Person with no relationshop)
        Person person=_campusConnection.getUserData(TestPeLP.getUser(_campusConnection,0).getUserID());
    }
    
    @Test(expected=AuthPelpException.class)
    public void testGetOtherStudentInfo() throws AuthPelpException {       
        // Obtain the information of user4 (Student of the same class)
        Person person=_campusConnection.getUserData(TestPeLP.getUser(_campusConnection,4).getUserID());
    }
    
    @Test(expected=AuthPelpException.class)
    public void testGetOtherTeacherInfo() throws AuthPelpException {       
        // Obtain the information of user5 (Teacher of other classroom)
        Person person=_campusConnection.getUserData(TestPeLP.getUser(_campusConnection,5).getUserID());
    }   
    
    @Test
    public void testGetTeacherInfo() throws AuthPelpException {       
        // Obtain the information of user2 (Teacher of the classroom)
        Person person=_campusConnection.getUserData(TestPeLP.getUser(_campusConnection,2).getUserID());
        Assert.assertNotNull(person);
        Assert.assertEquals(TestPeLP.getUser(_campusConnection,2), person);
    }
    
    @Test
    public void testGetMainTeacherInfo() throws AuthPelpException {       
        // Obtain the information of user1 (Main teacher of the subject)
        Person person=_campusConnection.getUserData(TestPeLP.getUser(_campusConnection,1).getUserID());
        Assert.assertNotNull(person);
        Assert.assertEquals(TestPeLP.getUser(_campusConnection,1), person);
    }
    
    @Test
    public void testGetSubjectClassroomsStudent() throws AuthPelpException {   
        // Obtain a test subject
        Subject s=TestPeLP.getLocal(_campusConnection).getTestSubjectByPos(0);
        // Obtain the information of user1
        IClassroomID[] classrooms=_campusConnection.getSubjectClassrooms(s.getID(), UserRoles.Student);
        
        Assert.assertNotNull(classrooms);
        Assert.assertTrue("One Classrooms is retrieved", classrooms.length==1);
        Assert.assertEquals(TestPeLP.getClassroom(s,1).getClassroomID(),classrooms[0]);
    }
    
    @Test
    public void testGetSubjectClassroomsTeacher() throws AuthPelpException {   
        // Obtain a test subject
        Subject s=TestPeLP.getLocal(_campusConnection).getTestSubjectByPos(0);
        // Obtain the information of user1
        IClassroomID[] classrooms=_campusConnection.getSubjectClassrooms(s.getID(), UserRoles.Teacher);
        
        Assert.assertNotNull(classrooms);
        Assert.assertTrue("No Classrooms are retrieved", classrooms.length==0);
    }
    
    @Test
    public void testGetSubjectClassroomsMainTeacher() throws AuthPelpException {   
        // Obtain a test subject
        Subject s=TestPeLP.getLocal(_campusConnection).getTestSubjectByPos(0);
        // Obtain the information of user1
        IClassroomID[] classrooms=_campusConnection.getSubjectClassrooms(s.getID(), UserRoles.MainTeacher);
        
        Assert.assertNotNull(classrooms);
        Assert.assertTrue("No Classrooms are retrieved", classrooms.length==0);
    }
}
