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
 * This class contains the basic information about a subject. 
 * @author Xavier Baró
 */
public class Subject implements Comparable<Subject> {
    /**
     * Unique Identifier for this subject
     */
    private ISubjectID _id=null;
    
    /**
     * Description of the subject
     */
    private String _description=null;
    
    /**
     * Short name of the subject
     */
    private String _shortName=null;
    
   /**
    * Subject coordinators.
    */   
    private HashMap<IUserID,Person> _mainTeachers=null;
    
    /**
    * Classrooms for this subject.
    */   
    private HashMap<IClassroomID,Classroom> _classrooms=null;
    
    /**
     * Laboratories for this subject
     */
    private HashMap<ISubjectID,ISubjectID> _labSubjects=null;
    
    /**
     * Equivalent subjects, in other degrees or languages
     */
    private HashMap<ISubjectID,ISubjectID> _equivalentSubjects=null;
    
    /**
     * Flag that indicates if the current subject is a laboratory of another subject,
     * which must be stored in the _parentSubject. 
     * @see getParent
     * @see setParent
     * @see setLabFlag
     * @see isLaboratory
     */
    private boolean _isLaboratory=false;
    
    /**
     * In Laboratory subjects, it stores the identifier of the parent subject.
     */
    private ISubjectID _parentSubject=null;
        
    /**
     * Default constructor. 
     * @param subjectID Identifier for this subject object.
     */
    public Subject(ISubjectID subjectID) {
        _id=subjectID;
    }

    /**
     * Get the subject description
     * @return Subject description
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Assign a description to the subject
     * @param description Description of the subject.
     */
    public void setDescription(String description) {
        this._description = description;
    }

    /**
     * Get short name for this subject
     * @return Short name of the subject
     */
    public String getShortName() {
        return _shortName;
    }

    /**
     * Assign a new short name to the sabject
     * @param shortName Short name to refer this subject
     */
    public void setShortName(String shortName) {
        this._shortName = shortName;
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
     * Add a new main teacher to the subject
     * @param teacher Information for the new teacher
     */
    public void addMainTeacher(Person teacher) {
        // If the list of classrooms is null, create a new list
        if(_mainTeachers==null) {
            _mainTeachers=new HashMap<IUserID,Person>();
        }
        
        // Add this classroom to the list if it does not exist
        if(!_mainTeachers.containsKey(teacher.getUserID())) {
            // Add to list
            _mainTeachers.put(teacher.getUserID(), teacher);
        }
    }
    
    /**
     * Add a new laboratory to this subject
     * @param subjectID Identifier of the child subject
     */
    public void addLaboratory(ISubjectID subjectID) {
        // If the list of laboratories is null, create a new list
        if(_labSubjects==null) {
            _labSubjects=new HashMap<ISubjectID,ISubjectID>();
        }
        
        // Add this subject to the list if it does not exist
        if(!_labSubjects.containsKey(subjectID)) {
            // Add to list
            _labSubjects.put(subjectID, subjectID);
        }
    }
    
    /**
     * Add a new equivalent subject to this subject. Equivalent subjects are relations 
     * between different subjects that share the same contents, such as subjects in different
     * degrees or different languages.
     */
    public void addEquivalentSubject(ISubjectID subjectID) {
        // If the list of equivalences is null, create a new list
        if(_equivalentSubjects==null) {
            _equivalentSubjects=new HashMap<ISubjectID,ISubjectID>();
        }
        
        // Add this subject to the list if it does not exist
        if(!_equivalentSubjects.containsKey(subjectID)) {
            // Add to list
            _equivalentSubjects.put(subjectID, subjectID);
        }
    }
    
    /**
     * Gets the subject identifier
     * @return Subject identifier
     */
    public ISubjectID getID() {
        return _id;
    }

    /**
     * Gets the classrooms of the current subject
     * @return Map with all the classrooms
     */
    public HashMap<IClassroomID, Classroom> getClassrooms() {
        return _classrooms;
    }

    /**
     * Gets the main teachers of the current subject
     * @return Map with all the main teachers
     */
    public HashMap<IUserID, Person> getMainTeachers() {
        return _mainTeachers;
    }   
    
    /**
     * Gets the child subjects for this subject
     * @return Map with all the subject ids or null if no laboratories exist
     */
    public HashMap<ISubjectID, ISubjectID> getChildSubjects() {
        return _labSubjects;
    } 
    
    /**
     * Gets the equivalent subjects for this subject. Equivalent subjects are relations 
     * between different subjects that share the same contents, such as subjects in different
     * degrees or different languages.
     * @return Map with all the subject ids or null if no equivalent subjects exist
     */
    public HashMap<ISubjectID, ISubjectID> getEquivalentSubjects() {
        return _equivalentSubjects;
    } 
    
    /**
     * Returns the parent subject in laboraories.
     * @return Subject id for which this subject is a laboratory
     */    
    public ISubjectID getParent() {
        return _parentSubject;
    }
    
    /*
     * Assgins a parent to a laboratory subject. It also activate the flag _isLaboratory
     */
    public void setParent(ISubjectID parent) {
        _isLaboratory=true;
        _parentSubject=parent;
    }
    
    /**
     * Check whether a certain subject corresponds to a laboratory
     * @return True if it is a laboratory or false otherwise
     */
    public boolean isLaboratory() {
        return _isLaboratory;
    }
    
    /**
     * Change the _isLaboratory flag, which indicates if the subject corresponds to a laboratory or not.
     * @param value New value for the flag. 
     */
    public void setLabFlag(boolean value) {
        _isLaboratory=value;
    }
    
    /**
     * Remove all equivalent subjects
     */
    public void clearEquivalentSubjects() {
        if(_equivalentSubjects!=null) {
            _equivalentSubjects.clear();
        }
        _equivalentSubjects=null;
    }
    
    /**
     * Remove all laboratories
     */
    public void clearLabSubjects() {
        if(_labSubjects!=null) {
            _labSubjects.clear();
        }
        _labSubjects=null;
    }
    
    /**
     * Remove all classrooms
     */
    public void clearClassrooms() {
        if(_classrooms!=null) {
            _classrooms.clear();
        }
        _classrooms=null;
    }
    
    /**
     * Remove all main teachers of the subject
     */
    public void clearTeachers() {
        if(_mainTeachers!=null) {
            _mainTeachers.clear();
        }
        _mainTeachers=null;
    }

    @Override
    public int compareTo(Subject t) {
        if(t==null) {
            return -1;
        }
        ISubjectID id=t.getID();
        if(_id==null) {
            if(id==null) {
                return 0;
            } else {
                return 1;
            }
        }
        return _id.compareTo(id);
    }

	@Override
	public String toString() {
		return "Subject [_id=" + _id + "]";
	}
}
