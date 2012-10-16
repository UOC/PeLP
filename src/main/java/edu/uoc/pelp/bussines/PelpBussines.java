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
*/package edu.uoc.pelp.bussines;

import edu.uoc.pelp.bussines.exception.*;
import edu.uoc.pelp.bussines.vo.*;
import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.campus.ICampusConnection;
import edu.uoc.pelp.engine.campus.IClassroomID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.exception.ExecPelpException;
import java.util.Date;
import org.hibernate.SessionFactory;

/**
 * PeLP bussines interface, that defines the interaction between services and the platform
 * @author Xavier Baró
 */
public interface PelpBussines {
    
    /**
     * Assigns a new campus connection
     * @param campusConnection Campus connection object
     */
    public void setCampusConnection(ICampusConnection campusConnection) throws InvalidCampusConnectionException;
    
    /**
     * Assigns a new Session Factory for DAO support
     * @param sessionFactory Session factory object
     * @throws InvalidSessionFactoryException If an invalid Session Factory is detected
     */
    public void setSessionFactory(SessionFactory sessionFactory) throws InvalidSessionFactoryException;
    
    /**
     * Assigns a new Configuration object for the PeLP platform
     * @param config Configuration Object
     * @throws InvalidSessionFactoryException If an invalid Configuration is detected
     */
    public void setConfiguration(IPelpConfiguration config) throws InvalidConfigurationException;
    
    /**
     * Create and configure the PeLP engine
     * @throws InvalidEngineException if an error occurs creating the engine
     */
    public void initializeEngine() throws InvalidEngineException;
    
        /**
     * Compile a code and test it with given tests
     * @param code String with the code
     * @param programmingLanguage Programming language code for given code
     * @param tests Set of tests to be passed to the created application
     * @return Object with the detail of given code or null if an error occurred.
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if the user has no enough privilegies
     */
    public DeliverDetail compileCode(String code,String programmingLanguage, Test[] tests) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
     * Compile a code files and test it with given tests
     * @param codeFiles Code files to be analyzed
     * @param programmingLanguage Programming language code for given code
     * @param tests Set of tests to be passed to the created application
     * @param rootPath Set the source rootpath. All files are taked relative to this root path. If null, this path is estimated from the files.
     * @return Object with the detail of given code or null if an error occurred.
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if the user has no enough privilegies
     */
    public DeliverDetail compileCode(DeliverFile[] codeFiles,String programmingLanguage, Test[] tests, String rootPath) throws ExecPelpException,InvalidEngineException;
    
    /**
     * Get the current user information or null if it is not logged in
     * @return User information or null if no user is authenticated
     * @throws AuthorizationException if an error occurs accessing authentication information
     * @throws InvalidEngineException if the engine is not properly initialized
     */
    public UserInformation getUserInformation() throws ExecPelpException,InvalidEngineException;
    
    /**
     * Get the list of subjects for current user, that are currently active in PeLP
     * @return List of subjects objects
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not authenticated
     */
    public Subject[] getUserSubjects() throws ExecPelpException,InvalidEngineException,AuthorizationException;
 
    /**
     * Get a resource the platform
     * @param code Code for the resource
     * @return Resource information or null if it is not accessible or does not exist
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if the user has no access to this resource
     */
    public Resource getResource(String code) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
    * Add a new activity to the given subject without tests
    * @param subject Subject object
    * @param start Start date for this activity
    * @param end Ending date for this activity
    * @param maxDelivers Maximum allowed delivers per user to this activity
    * @param progLangCode Programming language
    * @param activityDescriptions Activity descriptions in multiple languages
    * @return New created activity of null if it is not created correctly
    * @throws InvalidEngineException if the engine is not properly initialized
    * @throws AuthorizationException if user is not a teacher of this subject
    * @throws ExecPelpException if there is a problem during the process.
    */
    public Activity addActivity(Subject subject, Date start, Date end, Integer maxDelivers,String progLangCode, MultilingualTextArray activityDescriptions) throws AuthorizationException,InvalidEngineException,ExecPelpException;

    /**
    * Add a new activity to the given subject
    * @param subject Subject object
    * @param start Start date for this activity
    * @param end Ending date for this activity
    * @param maxDelivers Maximum allowed delivers per user to this activity
    * @param activityDescriptions Activity descriptions in multiple languages
    * @param activityTests Tests to be passed to the delivers
    * @param testDescriptions Test descriptions in multiple languages. An array for each test
    * @return New created activity of null if it is not created correctly
    * @throws InvalidEngineException if the engine is not properly initialized
    * @throws AuthorizationException if user is not a teacher of this subject
    * @throws ExecPelpException if there is a problem during the process.
    */
    public Activity addActivity(Subject subject, Date start, Date end, Integer maxDelivers, String progLangCode, MultilingualTextArray activityDescriptions, Test[] activityTests, MultilingualTextArray[] testDescriptions) throws AuthorizationException,InvalidEngineException,ExecPelpException;
    
    /**
     * Add a new deliver for a certain activity
     * @param subject Subject object
     * @param activityIndex Activity index
     * @param files Files attached to the deliver
     * @return Object with the detail of added deliver or null if an error occurred adding the new deliver
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this activity
     */
    public DeliverDetail addDeliver(Subject subject,int activityIndex, DeliverFile[] files) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Add a new deliver for a certain activity
     * @param activity Activity object
     * @param files Files attached to the deliver
     * @return Object with the detail of added deliver or null if an error occurred adding the new deliver
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this activity
     */
    public DeliverDetail addDeliver(Activity activity, DeliverFile[] files) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get a detailed information for last deliver of each user in a given classroom
     * @param subject Subject object for the activity
     * @param activityIndex Activity Index
     * @param subject Subject object for classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getLastClassroomDeliverDetails(Subject activitySubject, int activityIndex, Subject subject,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException; 
  
    /**
     * Get a detailed information for last deliver of each user in a given classroom
     * @param activity Activity object
     * @param classroom Classroom object
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getLastClassroomDeliverDetails(Activity activity,Classroom classroom) throws ExecPelpException,InvalidEngineException,AuthorizationException; 

    /**
     * Get a detailed information for last deliver of each user in a given classroom
     * @param activity Activity object
     * @param subject Subject object for classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getLastClassroomDeliverDetails(Activity activity,Subject subject, int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException; 

    /**
     * Get a summarized information for last deliver of each user in a given classroom
     * @param activitySubject Subject object for the activity
     * @param activityIndex Activity Index
     * @param subject Subject object for the classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getLastClassroomDeliverSummary(Subject activitySubject, int activityIndex, Subject subject,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get a summarized information for last deliver of each user in a given classroom
     * @param activity Activity object
     * @param classroom Classroom object
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getLastClassroomDeliverSummary(Activity activity,Classroom classroom) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get a summarized information for last deliver of each user in a given classroom
     * @param activity Activity object
     * @param subject Subject object for the classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getLastClassroomDeliverSummary(Activity activity,Subject subject,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

        /**
     * Get a detailed information for all delivers in a given classroom
     * @param activitySubject Subject object for the activity
     * @param activityIndex Activity Index
     * @param subject Subject object for the classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getAllClassroomDeliverDetails(Subject activitySubject, int activityIndex, Subject subject,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException; 

    /**
     * Get a detailed information for all delivers in a given classroom
     * @param activity Activity object
     * @param subject Subject object for the classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getAllClassroomDeliverDetails(Activity activity,Subject subject,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException; 

    /**
     * Get a detailed information for all delivers in a given classroom
     * @param activity Activity object
     * @param classroom Classroom object
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getAllClassroomDeliverDetails(Activity activity,Classroom classroom) throws ExecPelpException,InvalidEngineException,AuthorizationException; 

    /**
     * Get a summarized information for all delivers in a given classroom
     * @param activitySubject Subject object for the activity
     * @param activityIndex Activity Index
     * @param subject Subject object for the classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getAllClassroomDeliverSummary(Subject activitySubject, int activityIndex, Subject subject,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get a summarized information for all delivers in a given classroom
     * @param activity Activity object
     * @param classroom Classroom object
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getAllClassroomDeliverSummary(Activity activity,Classroom classroom) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get a summarized information for all delivers in a given classroom
     * @param activity Activity object
     * @param subject Subject object for the classroom
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getAllClassroomDeliverSummary(Activity activity,Subject subject,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

        /**
     * Get the information for a given activity
     * @param subject Subject object
     * @param activityIndex Activity index
     * @return Activity information object
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this activity
     */
    public Activity getActivityInformation(Subject subject,int activityIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get the information for a given activity
     * @param activity Activity object
     * @return Activity information object
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this activity
     */
    public Activity getActivityInformation(Activity activity) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get the list of available activities for a given subject
     * @param subject Subject object
     * @return List of activities
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this subject
     */
    public Activity[] getSubjectActivities(Subject subject) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
     * Get the list of available classrooms for a given subject
     * @param subject Subject object
     * @return List of classrooms objects
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this subject
     */
    public Classroom[] getUserClassrooms(Subject subject) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get all the resources for an activity
     * @param subject Subject object
     * @param activityIndex Activity index
     * @return Array of resources for the activity
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this activity
     */
    public Resource[] getActivityResources(Subject subject,int activityIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get all the resources for an activity
     * @param activity Activity object
     * @return Array of resources for the activity
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user has no access to this activity
     */
    public Resource[] getActivityResources(Activity activity) throws ExecPelpException,InvalidEngineException,AuthorizationException;

     /**
     * Get a detailed information for all delivers from this user to given activity
     * @param subject Subject object
     * @param activityIndex Activity Index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getUserDeliverDetails(Subject subject,int activityIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException; 

    /**
     * Get a detailed information for all delivers from this user to given activity
     * @param activity Activity object
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getUserDeliverDetails(Activity activity) throws ExecPelpException,InvalidEngineException,AuthorizationException; 

    /**
     * Get a summarized information for all delivers in a given classroom
     * @param subject Subject object
     * @param activityIndex Activity Index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getUserDeliverSummary(Subject subject,int activityIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get a detailed information for a certain deliver
     * @param subject Subject object
     * @param activityIndex Activity index
     * @param deliverIndex Deliver index
     * @return Object with summary information of the deliver
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not the owner or a teacher of the classroom where owner belongs to
     */
    public DeliverDetail getUserDeliverDetails(Subject subject,int activityIndex,int deliverIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;

    /**
     * Get a summary information for a certain deliver
     * @param subject Subject object
     * @param activityIndex Activity index
     * @param deliverIndex Deliver index
     * @return Object with summary information of the deliver
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not the owner or a teacher of the classroom where owner belongs to
     */
    public DeliverSummary getUserDeliverSummary(Subject subject,int activityIndex,int deliverIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
     * Activate a subject in the platform. If it exists an activation register for this subject, it sets it as active, if not, it creates a new one
     * @param subject Subject object
     * @return True if the subject has been correctly activated or false otherwise
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean activateSubject(Subject subject) throws AuthorizationException,InvalidEngineException;
    
    /**
     * Disable a subject in the platform. If it exists an activation register for this subject, it sets it as inactive, if not, it does nothing
     * @param subject Subject object
     * @return True if the subject has been correctly disabled or false otherwise
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean deactivateSubject(Subject subject) throws AuthorizationException,InvalidEngineException;

    /**
     * Removes the activation register for this subject. The subject will become inactive.
     * @param subject Subject object
     * @return True if the subject activation register has been correctly removed or false otherwise
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean removeSubjectActivationRegister(Subject subject) throws AuthorizationException,InvalidEngineException;
    
    /**
     * Adds a laboratory subject to a certain main subject. This relation does not depends on the semester. If it exists, does nothing.
     * @param mainSubject Main subject code
     * @param labSubject Laboratory subject code
     * @return True if the laboratory has been correctly added or false otherwise
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean addLaboratory(String mainSubject,String laboratory) throws AuthorizationException,InvalidEngineException;
    
    /**
     * Remove a laboratory subject from a certain main subject. This relation does not depends on the semester.
     * @param mainSubject Main subject code
     * @param labSubject Laboratory subject code
     * @return True if the laboratory has been correctly removed or false otherwise
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not an administrator
     */
    public boolean removeLaboratory(String mainSubject,String laboratory) throws AuthorizationException,InvalidEngineException;
    
    /**
     * Get a subject object from with identifier information from a generic identifier. 
     * @param subjectID Identifier object
     * @return Subject object
     */
    public Subject getSubject(ISubjectID subjectID);
    
    /**
     * Get a generic subject identifier from a subject object. 
     * @param subject Subject object
     * @return Subject object identifier
     */
    public ISubjectID getSubjectID(Subject subject);
    
    /**
     * Get the generic time period object from a subject
     * @param subject Subject object
     * @return Time period object
     */
    public ITimePeriod getSemester(Subject subject);
    
    /**
     * Get a classroom object from with identifier information from a generic identifier. 
     * @param classroomID Identifier object
     * @return Classroom object
     */
    public Classroom getClassroom(IClassroomID classroomID);
    
    /**
     * Get a generic classroom identifier from a classroom object. 
     * @param classroom Classroom object
     * @return Classroom object identifier
     */
    public IClassroomID getClassroomID(Classroom classroom);
    
}

