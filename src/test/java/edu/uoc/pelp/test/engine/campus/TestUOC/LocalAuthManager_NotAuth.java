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
 * This class uses the local campus connection with a non authenticated user.
 * @author Xavier Baró
 */
public class LocalAuthManager_NotAuth {
    
    private ICampusConnection _campusConnection=(ICampusConnection) new LocalCampusConnection();
     
    public LocalAuthManager_NotAuth() {
        // Set a not authenticated user
        TestPeLP.getLocal(_campusConnection).setProfile("none");
    }

    @Test 
    public void testNotAuthUserRightsToCampus()  throws AuthPelpException  {
        // Check that user is not authenticated
        Assert.assertFalse("Check if user is authenticated.",_campusConnection.isUserAuthenticated());
    }
    
    @Test(expected=AuthPelpException.class)
    public void testNotAuthUserIDAccess() throws AuthPelpException {
        // Try to obtain the user ID
        IUserID id=_campusConnection.getUserID();
    }
    
    @Test(expected=AuthPelpException.class)
    public void testNotAuthAllUserSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(null);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testNotAuthStudentSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(UserRoles.Student,null);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testNotAuthTeacherSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(UserRoles.Teacher,null);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testNotAuthMainTeacherSubjectsAccess() throws AuthPelpException {
        // Try to obtain the user subjects
        ISubjectID[] subjectList=_campusConnection.getUserSubjects(UserRoles.MainTeacher,null);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testNotAuthAllUserClassroomsAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(null);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testNotAuthStudentClassroomAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(UserRoles.Student,null);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testNotAuthTeacherClassroomAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(UserRoles.Teacher,null);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testNotAuthMainTeacherClassroomAccess() throws AuthPelpException {
        // Try to obtain the user classrooms
        IClassroomID[] classroomsList=_campusConnection.getUserClassrooms(UserRoles.MainTeacher,null);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testGetUserInfo() throws AuthPelpException {
        // Obtain the user information
        Person person=_campusConnection.getUserData();       
    }
    
    @Test(expected=AuthPelpException.class)
    public void testGetOtherUserInfo() throws AuthPelpException {       
        // Obtain the information of user1
        Person person=_campusConnection.getUserData(new UserID("123456"));
    }
    
    @Test(expected=AuthPelpException.class)
    public void testGetSubjectClassroomsStudent() throws AuthPelpException {   
        // Obtain a test subject
        Subject s=TestPeLP.getLocal(_campusConnection).getTestSubjectByPos(0);
        // Obtain the information of user1
        IClassroomID[] subjects=_campusConnection.getSubjectClassrooms(s.getID(), UserRoles.Student);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testGetSubjectClassroomsTeacher() throws AuthPelpException {   
        // Obtain a test subject
        Subject s=TestPeLP.getLocal(_campusConnection).getTestSubjectByPos(0);
        // Obtain the information of user1
        IClassroomID[] subjects=_campusConnection.getSubjectClassrooms(s.getID(), UserRoles.Teacher);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testGetSubjectClassroomsMainTeacher() throws AuthPelpException {   
        // Obtain a test subject
        Subject s=TestPeLP.getLocal(_campusConnection).getTestSubjectByPos(0);
        // Obtain the information of user1
        IClassroomID[] subjects=_campusConnection.getSubjectClassrooms(s.getID(), UserRoles.MainTeacher);
    }
    
    @Test(expected=AuthPelpException.class)
    public void testIsRoleStudent() throws AuthPelpException {   
        // Obtain a test subject
        Subject s=TestPeLP.getLocal(_campusConnection).getTestSubjectByPos(0);
        // Obtain the information of user1
        IClassroomID[] subjects=_campusConnection.getSubjectClassrooms(s.getID(), UserRoles.Teacher);
    }   
}
