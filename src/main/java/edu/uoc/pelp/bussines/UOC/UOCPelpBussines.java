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
package edu.uoc.pelp.bussines.UOC;

import edu.uoc.pelp.bussines.PelpBussines;
import edu.uoc.pelp.bussines.UOC.exception.InvalidSessionException;
import edu.uoc.pelp.bussines.UOC.vo.UOCClassroom;
import edu.uoc.pelp.bussines.UOC.vo.UOCSubject;
import edu.uoc.pelp.bussines.exception.AuthorizationException;
import edu.uoc.pelp.bussines.exception.InvalidEngineException;
import edu.uoc.pelp.bussines.vo.*;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.exception.InvalidTimePeriodPelpException;
import java.util.Date;

/**
 * PeLP bussines interface for Universitat Oberta de Catalunya, that extends basic functionalities
 * @author Xavier Baró
 */
public interface UOCPelpBussines extends PelpBussines {
    /**
     * Set a new campus session.
     * @param session Campus session string
     * @throws InvalidSessionException If an invalid Campus session is detected
     */
    public void setCampusSession(String session) throws InvalidSessionException;

    /**
     * Get a summary information for a certain deliver
     * @param semester Semester code
     * @param subject Subject code
     * @param activityIndex Activity index
     * @param deliverIndex Deliver index
     * @return Object with summary information of the deliver
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not the owner or a teacher of the classroom where owner belongs to
     */
    public DeliverSummary getUserDeliverSummary(String semester,String subject,int activityIndex,int deliverIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
     * Get a detailed information for a certain deliver
     * @param semester Semester code
     * @param subject Subject code
     * @param activityIndex Activity index
     * @param deliverIndex Deliver index
     * @return Object with summary information of the deliver
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not the owner or a teacher of the classroom where owner belongs to
     */
    public DeliverDetail getUserDeliverDetails(String semester,String subject,int activityIndex,int deliverIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get a summarized information for all delivers in a given classroom
     * @param semester Semester code
     * @param subject Subject code
     * @param activityIndex Activity Index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getUserDeliverSummary(String semester,String subject,int activityIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get a detailed information for all delivers from this user to given activity
     * @param semester Semester code
     * @param subject Subject code
     * @param activityIndex Activity Index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getUserDeliverDetails(String semester,String subject,int activityIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException; 
    
    /**
     * Add a new deliver for a certain activity
     * @param semester Semester Code
     * @param subject Subject Code
     * @param activityIndex Activity index
     * @param files Files attached to the deliver
     * @return Object with the detail of added deliver or null if an error occurred adding the new deliver
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this activity
     */
    public DeliverDetail addDeliver(String semester,String subject,int activityIndex, DeliverFile[] files) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
     * Get all the resources for an activity
     * @param semester Semester Code
     * @param subject Subject Code
     * @param activityIndex Activity index
     * @return Array of resources for the activity
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this activity
     */
    public Resource[] getActivityResources(String semester,String subject,int activityIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;
         
    /**
     * Get the list of available classrooms for a given subject
     * @param semester Semester Code
     * @param subject Subject Code
     * @return List of classrooms objects
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this subject
     */
    public Classroom[] getUserClassrooms(String semester,String subject) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get the list of available activities for a given subject
     * @param semester Semester Code
     * @param subject Subject Code
     * @return List of activities
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this subject
     */
    public Activity[] getSubjectActivities(String semester,String subject) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get the information for a given activity
     * @param semester Semester Code
     * @param subject Subject Code
     * @param activityIndex Activity index
     * @return Activity information object
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this activity
     */
    public Activity getActivityInformation(String semester,String subject,int activityIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
     * Get a summarized information for all delivers in a given classroom
     * @param semester Semester code
     * @param activitySubject Subject code for activity
     * @param activityIndex Activity Index
     * @param subject Subject code for classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getAllClassroomDeliverSummary(String semester, String activitySubject, int activityIndex, String subject, int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get a detailed information for all delivers in a given classroom
     * @param semester Semester code
     * @param activitySubject Subject code for activity
     * @param activityIndex Activity Index
     * @param subject Subject code for classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getAllClassroomDeliverDetails(String semester, String activitySubject, int activityIndex, String subject, int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException; 
    
    /**
     * Get a summarized information for last deliver of each user in a given classroom
     * @param semester Semester code
     * @param activitySubject Subject code for activity
     * @param activityIndex Activity Index
     * @param subject Subject code for classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getLastClassroomDeliverSummary(String semester, String activitySubject, int activityIndex, String subject, int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
     * Get a detailed information for last deliver of each user in a given classroom
     * @param semester Semester code
     * @param activitySubject Subject code for activity
     * @param activityIndex Activity Index
     * @param subject Subject code for classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getLastClassroomDeliverDetails(String semester, String activitySubject, int activityIndex, String subject, int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException; 
          
    /**
    * Add a new activity to the given subject
    * @param semester Semester code
    * @param subject Subject code
    * @param start Start date for this activity
    * @param end Ending date for this activity
    * @param maxDelivers Maximum allowed delivers per user to this activity
    * @param activityDescriptions Activity descriptions in multiple languages
    * @param activityTests Tests to be passed to the delivers
    * @param testDescriptions Test descriptions in multiple languages
    * @return New created activity of null if it is not created correctly
    * @throws InvalidEngineException if the engine is not properly initialized
    * @throws AuthorizationException if user is not a teacher of this subject
    * @throws ExecPelpException if there is a problem during the process.
    */
    public Activity addActivity(String semester, String subject, Date start, Date end, Integer maxDelivers, String progLangCode,  MultilingualTextArray activityDescriptions, Test[] activityTests, MultilingualTextArray[] testDescriptions)  throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
    * Add a new activity to the given subject without tests
    * @param semester Semester code
    * @param subject Subject code
    * @param start Start date for this activity
    * @param end Ending date for this activity
    * @param maxDelivers Maximum allowed delivers per user to this activity
    * @param activityDescriptions Activity descriptions in multiple languages
    * @return New created activity of null if it is not created correctly
    * @throws InvalidEngineException if the engine is not properly initialized
    * @throws AuthorizationException if user is not a teacher of this subject
    * @throws ExecPelpException if there is a problem during the process.
    */
    public Activity addActivity(String semester, String subject, Date start, Date end, Integer maxDelivers, String progLangCode, MultilingualTextArray activityDescriptions) throws AuthorizationException,InvalidEngineException,ExecPelpException;
    
    /**
     * Activate a subject in the platform. If it exists an activation register for this subject, it sets it as active, if not, it creates a new one
     * @param semester Semester code
     * @param subject Subject code
     * @return True if the subject has been correctly activated or false otherwise
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean activateSubject(String semester,String subject) throws AuthorizationException,InvalidEngineException;

    /**
     * Disable a subject in the platform. If it exists an activation register for this subject, it sets it as inactive, if not, it does nothing
     * @param semester Semester code
     * @param subject Subject code
     * @return True if the subject has been correctly disabled or false otherwise
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean deactivateSubject(String semester,String subject) throws AuthorizationException,InvalidEngineException;

    /**
     * Removes the activation register for this subject. The subject will become inactive.
     * @param semester Semester code
     * @param subject Subject code
     * @return True if the subject activation register has been correctly removed or false otherwise
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean removeSubjectActivationRegister(String semester,String subject) throws AuthorizationException,InvalidEngineException;

    /**
     * Adds a new semester to the platform
     * @param semester Semester code
     * @param start Starting date
     * @param end Ending date
     * @return True if the semester has been correctly added or false otherwise
     * @throws InvalidTimePeriodPelpException if information for the semester is invalid
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean addSemester(String semester,Date start,Date end) throws AuthorizationException,InvalidEngineException,InvalidTimePeriodPelpException;
    
    /**
     * Updates the dates of an existing semester
     * @param semester Semester code
     * @param start Starting date
     * @param end Ending date
     * @return True if the semester has been correctly updated or false otherwise
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws InvalidTimePeriodPelpException if information for the semester is invalid
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean updateSemester(String semester,Date start,Date end) throws AuthorizationException,InvalidEngineException,InvalidTimePeriodPelpException;
    
    /**
     * Remove an existing semester. Any operation with this semester will be done if it does not exist in the platform
     * @param semester Semester code
     * @return True if the semester has been correctly deleted or false otherwise
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean removeSemester(String semester) throws AuthorizationException,InvalidEngineException;
    
    /**
     * Get the list of subjects for current user, that are currently active in PeLP
     * @return List of subjects objects
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not authenticated
     */
    @Override
    public UOCSubject[] getUserSubjects() throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get the list of available classrooms for a given subject
     * @param subject Subject object
     * @return List of classrooms objects
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this subject
     */
    public UOCClassroom[] getUserClassrooms(UOCSubject subject) throws ExecPelpException,InvalidEngineException,AuthorizationException;

}
