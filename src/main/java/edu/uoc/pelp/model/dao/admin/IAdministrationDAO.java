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
package edu.uoc.pelp.model.dao.admin;

import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.model.vo.admin.PelpActiveSubjects;
import edu.uoc.pelp.model.vo.admin.PelpAdmins;
import edu.uoc.pelp.model.vo.admin.PelpLanguages;
import edu.uoc.pelp.model.vo.admin.PelpMainLabSubjects;
import java.util.List;

/**
 * This interface defines the basic operations of the DAO for admnistration tables
 * @author Xavier Baró
 */
public interface IAdministrationDAO {
    
    /** 
     * Adds a new administrator to the platform.
     * @param person Person object for the new administrator
     * @param active Indicates if the new administrator is created as active administrator or not
     * @param grant True for administrators with grant option and False for administrators without grant option.
     * @return True if the administrator is correctly added or False otherwise
     */
    boolean addAdmin(Person person, boolean active, boolean grant);
    
    /**
     * Get the information for the admin register related to given person
     * @param person Person object
     * @return Administration information for this person or null if it does not exist.
     */
    public PelpAdmins getAdminData(Person person);
    
    /**
     * Update the administration information for a given person
     * @param person Person object
     * @param active Active flag, that indicates that is currently an administrator or not
     * @param grant Grant flag, that indicates if the user can create new administrators or not
     * @return True if the administrator is correctly added or False otherwise
     */
    public boolean updateAdmin(Person person, boolean active, boolean grant);
    
    /**
     * Deletes the administration data for a given person
     * @param person Person object
     * @return True if the administrator is correctly added or False otherwise
     */
    public boolean delAdmin(Person person);
    
    /**
     * Get the list of supported languages in the platform
     * @return List of languages
     */
    public List<PelpLanguages> getAvailableLanguages();
    
    /**
     * Get the list of active subjects in the platform
     * @return List of active subjects
     */
    public List<PelpActiveSubjects> getActiveSubjects();
    
    /**
     * Get the list of active subjects in the platform for a given semester
     * @param semester Semester code
     * @return List of active subjects
     */
    public List<PelpActiveSubjects> getActiveSubjects(String semester);
        
    /**
     * Adds a new active subject to the platform
     * @param semester Semester code
     * @param subjectCode Subject code
     * @param active Acive flag to enable or disable the subject
     * @return True if subject is correctly added or False otherwise
     */
    public boolean addActiveSubject(String semester, String subjectCode, boolean active);
    
    /**
     * Update an existing active subject on the platform
     * @param semester Semester code
     * @param subjectCode Subject code
     * @param active Acive flag to enable or disable the subject
     * @return True if subject is correctly updated or False otherwise
     */
    public boolean updateActiveSubject(String semester, String subjectCode, boolean active);
    
    /**
     * Delete an existing active subject on the platform
     * @param semester Semester code
     * @param subjectCode Subject code
     * @return True if subject is correctly deleted or False otherwise
     */
    public boolean deleteActiveSubject(String semester, String subjectCode);
    
    /**
     * Get the list of subjects that have the given subject as a laboratory
     * @param labSubjectCode Subject code for the laboratory.
     * @return List of subject pairs
     */
    public List<PelpMainLabSubjects> getMainSubjectOfLab(String labSubjectCode);
    
    /**
     * Get the list of subjects that are a laboratory of the given main subject
     * @param mainSubjectCode Subject code for the main subject.
     * @return List of subject pairs
     */
    public List<PelpMainLabSubjects> getLabSubjectOfMain(String mainSubjectCode);
    
    /** 
     * Add a new correspondence (Main-Laboratory) between the two given subjects
     * @param mainSubject Subject code for the main subject
     * @param labSubject Subject code for the laboratory
     * @return True if the correspondence is correctly added or False otherwise
     */
    public boolean addMainLabCorrespondence(String mainSubject,String labSubject);
    
    /** 
     * Delete a correspondence (Main-Laboratory) between the two given subjects
     * @param mainSubject Subject code for the main subject
     * @param labSubject Subject code for the laboratory
     * @return True if the correspondence is correctly deleted or False otherwise
     */
    public boolean deleteMainLabCorrespondence(String mainSubject,String labSubject);
    
    /**
     * Find a language in the database
     * @param languageCode Language code
     * @return Language description object or null if does not exist
     */
    public PelpLanguages findPelpLanguage(String languageCode);
    
    /**
     * Find an active subject in the database
     * @param semester Semester code
     * @param subject  Subject code
     * @return Active subjects information or null if does not exist
     */
    public PelpActiveSubjects findActiveSubjects(String semester,String subject);
    
    /**
     * Find the relation information between two subjects
     * @param mainSubject Main subject code
     * @param labSubject  Laboratory subject code
     * @return Subjects relation information or null if does not exist
     */
    public PelpMainLabSubjects findMainLabSubjects(String mainSubject,String labSubject);

}
