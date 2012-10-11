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
package edu.uoc.pelp.engine.admin;

import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.model.vo.admin.PelpActiveSubjects;
import edu.uoc.pelp.model.vo.admin.PelpLanguages;
import edu.uoc.pelp.model.vo.admin.PelpMainLabSubjects;
import java.util.Date;
import java.util.List;

/**
 * This interface describes the methods for administration managing.
 * @author Xavier Baró
 */
public interface IAdministrationManager {
    /**
     * Check if the given person has administraton rights
     * @param person Person information object
     * @return True if this person has administrative rights or False otherwise
     */
    public boolean isAdministrator(Person person);
    
    /**
     * Check if the given person has administraton rights with grant option
     * @param person Person information object
     * @return True if this person has administrative rights with grant option or False otherwise
     */
    public boolean isSuperAdministrator(Person person);
    
    /** 
     * Adds a new administrator to the platform. Only active administrators with grant option can add new administrators
     * @param person Person object for the new administrator
     * @param active Indicates if the new administrator is created as active administrator or not
     * @param grant True for administrators with grant option and False for administrators without grant option.
     * @return True if the administrator is correctly added or False otherwise
     */
    public boolean addAdministrator(Person person,boolean active,boolean grant);
    
    /**
     * Removes an administrator from the platform. Only active administrators with grant option can add new administrators.
     * @param person Person object for the new administrator
     * @return True if the administrator is correctly removed or False otherwise 
     */
    public boolean deleteAdministrator(Person person);
    
    /**
     * Updates an administrator from the platform. Only active administrators with grant option can add new administrators.
     * @param person Person object for the administrator to be updated
     * @param active Indicates if the new administrator is created as active administrator or not
     * @param grant True for administrators with grant option and False for administrators without grant option.
     * @return True if the administrator is correctly updated or False otherwise 
     */
    public boolean updateAdministrator(Person person,boolean active,boolean grant);
    
    /**
     * Check if a given language code is allowed in the platform
     * @param languageCode Language code
     * @return True if it is allowd or false otherwise
     */
    public boolean isValidLanguageCode(String languageCode);
    
    /**
     * Get the list of supported languages in the platform
     * @return List of languages
     */
    public PelpLanguages[] getAvailableLanguages();
       
    /**
     * Check if a certain subject is active
     * @param semester Semester code
     * @param subjectCode Subject code
     * @return True if this subject is active un this semester or False otherwise
     */
    public boolean isActiveSubject(String semester, String subjectCode);
    
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
     * Check if a certain semester is active or not
     * @param semester Semester code
     * @return True if the semester is active or false otherwise
     */
    public boolean isActiveSemester(String semester);
    
    /**
    * Adds a new semester to the platform
    * @param semester Semester code
    * @param start Starting date
    * @param end Ending date
    * @return True if the semester has been correctly added or false otherwise
    */
    public boolean addSemester(String semester,Date start,Date end);

    /**
    * Updates the dates of an existing semester
    * @param semester Semester code
    * @param start Starting date
    * @param end Ending date
    * @return True if the semester has been correctly updated or false otherwise
    */
    public boolean updateSemester(String semester,Date start,Date end);

    /**
    * Remove an existing semester. 
    * @param semester Semester code
    * @return True if the semester has been correctly deleted or false otherwise
    */
    public boolean removeSemester(String semester);
    
}
