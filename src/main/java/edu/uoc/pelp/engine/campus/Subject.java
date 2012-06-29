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

import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import java.util.HashMap;

/**
 * This class contains the basic information about a subject. 
 * @author Xavier Baró
 */
public class Subject {
    /**
     * Unique Identifier for this subject
     */
    private SubjectID _id=null;
    
    /**
     * Description of the subject
     */
    public String description=null;
    
    /**
     * Short name of the subject
     */
    public String shortName=null;
    
   /**
    * Subject coordinators.
    */   
    private HashMap<IUserID,Person> _mainTeachers=null;
    
    /**
    * Classrooms for this subject.
    */   
    private HashMap<IClassroomID,Classroom> _classrooms=null;
    
    /**
     * Default constructor. 
     * @param subjectID Identifier for this subject object.
     */
    public Subject(SubjectID subjectID) {
        _id=subjectID;
    }
    
    /**
     * Add a new main teacher to the subject.
     * @param teacher Information for the teacher.
     */
    public void addTeacher(Person teacher) {
        // If the list of main teachers is null, create a new list
        if(_mainTeachers==null) {
            _mainTeachers=new HashMap<IUserID,Person>();
        }
        
        // Add this person to the list if it does not exist
        if(!_mainTeachers.containsKey(teacher.getUserID())) {
            _mainTeachers.put(teacher.getUserID(), teacher);
        }
    }
    
    /**
     * Add a new classroom to the subject.
     * @param classroom Information for the classroom.
     */
    public void addClassroom(Classroom classroom) {
        // If the list of classrooms is null, create a new list
        if(_classrooms==null) {
            _classrooms=new HashMap<IClassroomID,Classroom>();
        }
        
        // Add this classroom to the list if it does not exist
        if(!_classrooms.containsKey(classroom.getClassroomID())) {
            // Set the reference to this object
            classroom.setSubjectRef(this);
            // Add to list
            _classrooms.put(classroom.getClassroomID(), classroom);
        }
    }
    
    /**
     * Gets the subject identifier
     * @return Subject identifier
     */
    public SubjectID getID() {
        return _id;
    }
}
