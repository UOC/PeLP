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
package edu.uoc.pelp.engine.campus;

import java.util.HashMap;

/**
 * This class contains the basic information about a classroom.
 * @author Xavier Baró
 */
public class Classroom implements Comparable<Classroom> {
    /**
     * Reference to the subject containing this classroom.
     */
    private Subject _subjectRef=null;
    
    /**
     * Unique identifier for this classroom
     */
    private IClassroomID _classroomID=null;
    
    /**
     * List of teachers for this classroom
     */
    private HashMap<IUserID,Person> _teachers=null;
    
    /**
     * List of students in this classroom
     */
    private HashMap<IUserID,Person> _students=null;
    
    /** 
     * Default constuctor.
     * @param userID Unique identifier for the user
     */
    public Classroom(IClassroomID classroomID) {
        _classroomID=classroomID;
    }
    
    /**
     * Get the classroom unique identifier
     * @return Classroom identifier
     */
    public IClassroomID getClassroomID() {
        return _classroomID;
    }
    
    /**
     * Set a reference to the subject object which contains this classroom
     * @param subject Subject object
     */
    public void setSubjectRef(Subject subject) {
        _subjectRef=subject;
    }
    
    /**
     * Get the reference to the subject object which contains this classroom
     * returj Subject object
     */
    public Subject getSubjectRef() {
        return _subjectRef;
    }
    
    /**
     * Add a new teacher to the classroom.
     * @param teacher Information for the teacher.
     */
    public void addTeacher(Person teacher) {
        // If the list of main teachers is null, create a new list
        if(_teachers==null) {
            _teachers=new HashMap<IUserID,Person>();
        }
        
        // Add this person to the list if it does not exist
        if(!_teachers.containsKey(teacher.getUserID())) {
            _teachers.put(teacher.getUserID(), teacher);
        }
    }
    
    /**
     * Add a new student to the classroom.
     * @param student Information for the student.
     */
    public void addStudent(Person student) {
        // If the list of main teachers is null, create a new list
        if(_students==null) {
            _students=new HashMap<IUserID,Person>();
        }
        
        // Add this person to the list if it does not exist
        if(!_students.containsKey(student.getUserID())) {
            _students.put(student.getUserID(), student);
        }
    }
    
    /**
     * Gets the main teachers of the subject of this classroom
     * @return Map with all the main teachers
     */
    public HashMap<IUserID, Person> getMainTeachers() {
        return _subjectRef.getMainTeachers();
    }
    
    /**
     * Gets the teachers of the current classroom
     * @return Map with all the teachers
     */
    public HashMap<IUserID, Person> getTeachers() {
        return _teachers;
    }
    
    /**
     * Gets the students of the current classroom
     * @return Map with all the students
     */
    public HashMap<IUserID, Person> getStudents() {
        return _students;
    }
    
     /**
     * Remove all Students
     */
    public void clearStudents() {
        if(_students!=null) {
            _students.clear();
        }
        _students=null;
    }
    
    /**
     * Remove all teachers of the classroom
     */
    public void clearTeachers() {
        if(_teachers!=null) {
            _teachers.clear();
        }
        _teachers=null;
    }

    public int compareTo(Classroom t) {
        if(t==null) {
            return -1;
        }
        IClassroomID id=t.getClassroomID();
        if(_classroomID==null) {
            if(id==null) {
                return 0;
            } else {
                return 1;
            }
        }
        return _classroomID.compareTo(id);
    }
}
