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
 * which does not have access to any subject.
 * @author Xavier Baró
 */
public class LocalAuthManager_Campus {
    
    private ICampusConnection _campusConnection=(ICampusConnection) new LocalCampusConnection();
    
    public LocalAuthManager_Campus() {
        // Set an authenticated user without active subjects
        TestPeLP.getLocal(_campusConnection).setProfile("campus");
    }
    
    @Test 
    public void testNotAuthUserRightsToCampus() {
        // Check that user is not authenticated
        Assert.assertTrue("Check if user is authenticated.",_campusConnection.isUserAuthenticated());
    }
    
    @Test
    public void testNotAuthUserIDAccess() throws AuthPelpException {
        // Try to obtain the user ID
        IUserID id=_campusConnection.getUserID();
        Assert.assertNotNull(id);
        Assert.assertEquals("000000",((UserID)id).idp);
    }
    
    @Test
    public void testNotAuthAllUserSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects();
        Assert.assertNotNull(subjectList);
        Assert.assertTrue("No Subjects are retrieved", subjectList.length==0);
    }
    
    @Test
    public void testNotAuthStudentSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(UserRoles.Student);
        Assert.assertNotNull(subjectList);
        Assert.assertTrue("No Subjects are retrieved", subjectList.length==0);
    }
    
    @Test
    public void testNotAuthTeacherSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(UserRoles.Teacher);
        Assert.assertNotNull(subjectList);
        Assert.assertTrue("No Subjects are retrieved", subjectList.length==0);
    }
    
    @Test
    public void testNotAuthMainTeacherSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(UserRoles.MainTeacher);
        Assert.assertNotNull(subjectList);
        Assert.assertTrue("No Subjects are retrieved", subjectList.length==0);
    }
    
    @Test
    public void testNotAuthAllUserClassroomsAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms();
        Assert.assertNotNull(classroomsList);
        Assert.assertTrue("No Classrooms are retrieved", classroomsList.length==0);
    }
    
    @Test
    public void testNotAuthStudentClassroomAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(UserRoles.Student);
        Assert.assertNotNull(classroomsList);
        Assert.assertTrue("No Classrooms are retrieved", classroomsList.length==0);
    }
    
    @Test
    public void testNotAuthTeacherClassroomAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(UserRoles.Teacher);
        Assert.assertNotNull(classroomsList);
        Assert.assertTrue("No Classrooms are retrieved", classroomsList.length==0);
    }
    
    @Test
    public void testNotAuthMainTeacherClassroomAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(UserRoles.MainTeacher);
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
        // Obtain the information of user1
        Person person=_campusConnection.getUserData(new UserID("111111"));
    }
    
    @Test
    public void testGetSubjectClassroomsStudent() throws AuthPelpException {   
        // Obtain a test subject
        Subject s=TestPeLP.getLocal(_campusConnection).getTestSubjectByPos(0);
        // Obtain the information of user1
        IClassroomID[] classrooms=_campusConnection.getSubjectClassrooms(s.getID(), UserRoles.Student);
        
        Assert.assertNotNull(classrooms);
        Assert.assertTrue("No Classrooms are retrieved", classrooms.length==0);
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
