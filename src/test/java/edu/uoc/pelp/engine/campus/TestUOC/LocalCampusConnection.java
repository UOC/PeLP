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
package edu.uoc.pelp.engine.campus.TestUOC;

import edu.uoc.pelp.engine.campus.UOC.ClassroomID;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.engine.campus.UOC.UserID;
import edu.uoc.pelp.engine.campus.*;
import edu.uoc.pelp.exception.AuthPelpException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements a dummy class simulating the campus access for the 
 * Universitat Oberta de Catalunya (UOC). All the data of this class is
 * available locally, without DB or network connection.
 * @author Xavier Baró
 */
public class LocalCampusConnection implements ICampusConnection{

    /**
     * Current authenticated user (null if no authenticated user)
     */
    private IUserID _userID=null;
    
    /**
     * List of available persons
     */
    private HashMap<UserID,Person> _dummyUsers=new HashMap<UserID,Person>();
    
    /**
     * List of available subjects
     */
    private HashMap<SubjectID,Subject> _dummySubjects=new HashMap<SubjectID,Subject>();
    
    /**
     * List of available classrooms
     */
    private HashMap<ClassroomID,Classroom> _dummyClassrooms=new HashMap<ClassroomID,Classroom>();
    
    /** 
     * List of available semesters 
     **/
    private HashMap<String,Semester> _dummySemesters=new HashMap<String,Semester>();
    
    public LocalCampusConnection() {
        createDummyData();
    }
        
    public boolean isUserAuthenticated() {
        return (_userID!=null);
    }

    public Subject[] getUserSubjects() throws AuthPelpException {
        if(!isUserAuthenticated()) {
            throw new AuthPelpException();
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Classroom[] getUserClassrooms() throws AuthPelpException {
        if(!isUserAuthenticated()) {
            throw new AuthPelpException();
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IUserID getUserID() throws AuthPelpException {
         if(!isUserAuthenticated()) {
            throw new AuthPelpException();
        }
        return _userID;
    }
    
    public boolean isRole(UserRoles role, ISubjectID subject, IUserID user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRole(UserRoles role, ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRole(UserRoles role, IClassroomID classroom, IUserID user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRole(UserRoles role, IClassroomID classroom) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Person[] getRolePersons(UserRoles role, ISubjectID subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Person[] getRolePersons(UserRoles role, IClassroomID classroom) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasChildSubjects(ISubjectID subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ISubjectID[] getChildSubjects(ISubjectID subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasEquivalentSubjects(ISubjectID subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ISubjectID[] getEquivalentSubjects(ISubjectID subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCampusConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setProfile(String profileID) {
        throw new UnsupportedOperationException("Not supported yet.");
        /*if("none".equalsIgnoreCase(profileID)) {
            // User not authenticated
            _userID=null;
            _classrooms=null;
            _subjects=null;
        } else if("student1".equalsIgnoreCase(profileID)) {
            // Student with no active subjects
            _userID=null;
            _classrooms=null;
            _subjects=null;
        } else if("student2".equalsIgnoreCase(profileID)) {
            // Student with active subjects
            _userID=null;
            _classrooms=null;
            _subjects=null;
        } else if("teacher1".equalsIgnoreCase(profileID)) {
            // Teacher with no active subjects
            _userID=null;
            _classrooms=null;
            _subjects=null;
        } else if("teacher2".equalsIgnoreCase(profileID)) {
            // Teacher with active subjects
            _userID=null;
            _classrooms=null;
            _subjects=null;
        } else if("Mixed".equalsIgnoreCase(profileID)) {
            // User with some subjects as teacher and others as student.
            _userID=null;
            _classrooms=null;
            _subjects=null;
        }*/
    }

    private void createDummyData() {
        createDummyPersons();
        createDummySemesters();
        createDummySubjects();
        createDummyClassrooms();
    }

    private void createDummyPersons() {
        // Create dummy users with "punny" names (http://sandgroper14.wordpress.com/2007/04/30/fake-names-for-documentation/)
        Person p1=new Person(((IUserID)new UserID("12345")));
        p1._eMail="user1@uoc.edu";
        p1._fullName="Barb Akew";
        p1._name="Barb";
        
        Person p2=new Person(((IUserID)new UserID("67890")));
        p2._eMail="user2@uoc.edu";
        p2._fullName="Ann Chovey";
        p2._name="Ann";
        
        Person p3=new Person(((IUserID)new UserID("13579")));
        p3._eMail="user3@uoc.edu";
        p3._fullName="Hazel Nutt";
        p3._name="Hazel";
        
        Person p4=new Person(((IUserID)new UserID("01290")));
        p4._eMail="user4@uoc.edu";
        p4._fullName="Bess Twishes";
        p4._name="Bess";
        
        // Add users to the list of users
        _dummyUsers.put((UserID)p1.getUserID(), p1);
        _dummyUsers.put((UserID)p1.getUserID(), p2);
        _dummyUsers.put((UserID)p1.getUserID(), p3);
        _dummyUsers.put((UserID)p1.getUserID(), p4);
    }

    private void createDummySubjects() {
        
        // Create subjects
        Subject s1=new Subject(new SubjectID("05.554",_dummySemesters.get("20111")));
        s1.description="Introduction to Java Programming";       
                
        Subject s2=new Subject(new SubjectID("05.554",_dummySemesters.get("20112")));
        s2.description="Introduction to Java Programming";       
                
        Subject s3=new Subject(new SubjectID("05.564",_dummySemesters.get("20112")));
        s3.description="Introduction to C Programming";       
        
        // Add the subjects to the list of subjects
        _dummySubjects.put(s1.getID(), s1);
        _dummySubjects.put(s2.getID(), s2);
        _dummySubjects.put(s3.getID(), s3);
    }

    private void createDummyClassrooms() {
        /*Classroom c1=new Classroom(new ClassroomID());
        ClassroomID id1=new ClassroomID();
        
        _dummyClassrooms.put(id1, c1);*/
    }

    private void createDummySemesters() {
        
        try {
            _dummySemesters.put("20111",new Semester("20111",DateFormat.getDateInstance().parse("21/09/2011"),
                                                             DateFormat.getDateInstance().parse("15/01/2012")));
            _dummySemesters.put("20112",new Semester("20112",DateFormat.getDateInstance().parse("16/01/2011"),
                                                             DateFormat.getDateInstance().parse("01/08/2012")));
            _dummySemesters.put("20121",new Semester("20121",DateFormat.getDateInstance().parse("15/09/2012"),
                                                             DateFormat.getDateInstance().parse("17/01/2013")));
        } catch (ParseException ex) {
            Logger.getLogger(LocalCampusConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
