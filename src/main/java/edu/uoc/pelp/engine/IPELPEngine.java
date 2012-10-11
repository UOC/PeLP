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
package edu.uoc.pelp.engine;

import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.activity.*;
import edu.uoc.pelp.engine.admin.IAdministrationManager;
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.engine.aem.TestData;
import edu.uoc.pelp.engine.aem.exception.AEMPelpException;
import edu.uoc.pelp.engine.campus.*;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.engine.deliver.IDeliverManager;
import edu.uoc.pelp.engine.information.DAOInformationManager;
import edu.uoc.pelp.exception.*;
import java.util.Date;

/**
 * This class defines the interface of the engine of the PELP system. 
 * @author Xavier Baró
 */
public interface IPELPEngine {

	/**
	 * Assign a new campus object.
	 * @param campus Object allowing to access the campus information
	 */
	public void setCampusConnection(ICampusConnection campus);

	/**
	 * Assign a new system configuration object
	 * @param conf Object that allows to retrieve system configuration parameters.
	 */
	public void setSystemConfiguration(IPelpConfiguration conf);

	/**
	 * Assign a new activity manager
	 * @param manager Object allowing to manage the activities
	 */
	public void setActivityManager(IActivityManager manager);

	/**
	 * Assign a new deliver manager
	 * @param manager Object allowing to manage the delivers
	 */
	public void setDeliverManager(IDeliverManager manager);
        
        /**
        * Assign a new administration manager
        * @param manager Object allowing to manage the delivers
        */
        public void setAdministrationManager(IAdministrationManager manager);
        
        /**
        * Assign a new information manager
        * @param manager Object allowing to manage the platform information and statistics
        */
        public void setInformationManager(DAOInformationManager daoInformationManager);

	/**
	 * Check if the current user is authenticated or not.
	 * @return True if the user is authenticated or False otherwise.
	 */
	public boolean isUserAuthenticated() throws AuthPelpException;

	/**
	 * Obtain the information of currently authenticated user
	 * @return Object with the user information for the current user
	 * @throws AuthPelpException If no user is authenticated.
	 */
	public Person getUserInfo() throws AuthPelpException;

	/**
	 * Return the list of active subjects for authenticated user
	 * @return List of active subjects for current user.
	 * @throws AuthPelpException If no user is authenticated.
	 */
	public Subject[] getActiveSubjects() throws AuthPelpException;

	/**
	 * Return the list of classrooms for authenticated user
	 * @param subjectID Subject identifier
	 * @return List of classrooms
	 * @throws AuthPelpException If no user is authenticated.
	 */
	public Classroom[] getSubjectClassrooms(ISubjectID subjectID)
			throws AuthPelpException;

	/**
	 * Get the list of activities of a given subject
	 * @param subjectID Identifier of the subject
	 * @param filterActive If True, only active activities are returned, otherwise, all activities are returned.
	 * @return List of activities
	 * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
	 */
	public Activity[] getSubjectActivity(ISubjectID subjectID,
			boolean filterActive) throws AuthPelpException;

	/**
	 * Obtain the list of delivers of a certain user for a certain activity. Only the same user or a teacher
	 * of the subject can access to this information.
	 * @param user Identifier of the user for which delivers are requested.
	 * @param activity Identifier of the activity delivers are requested from.
	 * @return Array of Delivers.
	 * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
	 */
	public Deliver[] getActivityDelivers(IUserID user,
			ActivityID activity) throws AuthPelpException;
        
        /**
	 * Obtain the list of all the delivers of a certain classroom for a certain activity. Only a teacher
	 * of the classroom can access to this information. Both, laboratory and main classrooms are checked.
	 * @param classroom Identifier of the classroom for which delivers are requested.
	 * @param activity Identifier of the activity delivers are requested from.
	 * @return Array of Delivers.
	 * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
	 */
	public Deliver[] getClassroomDelivers(IClassroomID classroom,
			ActivityID activity) throws AuthPelpException;
        
        /**
	 * Obtain the last submitted deliver for each user of a certain classroom for a certain activity. Only a teacher
	 * of the classroom can access to this information. Both, laboratory and main classrooms are checked.
	 * @param classroom Identifier of the classroom for which delivers are requested.
	 * @param activity Identifier of the activity delivers are requested from.
	 * @return Array of Delivers.
	 * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
	 */
	public Deliver[] getClassroomLastDelivers(IClassroomID classroom,
			ActivityID activity) throws AuthPelpException;
        
	/**
	 * Obtain the results of a certain deliver. Only the owner of the deliver and the teachers of the
	 * related subject can access this information.
	 * @param deliver Deliver identifier
	 * @return Object with the results of the deliver analysis
	 * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
	 */
	public DeliverResults getDeliverResults(DeliverID deliver) throws AuthPelpException;

	/**
	 * Obain the test information. Only teachers can access to private tests information.
	 * @param testID Identifier for the test
	 * @return Object with the test information
	 * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
	 */
	public ActivityTest getTestInformation(TestID testID) throws AuthPelpException;

	/**
	 * Perform a new deliver for current user to the given activity
	 * @param deliver Deliver object with all the files
	 * @param activityID Identifier for the target activity
	 * @return Results obtained from the analisis of this deliver
	 * @throws AuthPelpException If the user is not authenticated.
	 * @throws InvalidActivityPelpException If the user cannot perform delivers to this activity, because is not a student or all allowed delivers are performed.
	 * @throws ExecPelpException When files cannot be accessed or for some missconfiguration of the analyzer module.
	 */
	public DeliverResults createNewDeliver(Deliver deliver,
			ActivityID activityID) throws AuthPelpException,
			InvalidActivityPelpException, ExecPelpException;

	/**
	 * Perform the analysis of a code project, both, building process and execution tests.
	 * @param project Code project to be analyzed
	 * @param tests Array of tests to be passed to the program.
	 * @return Resuls obtained from the analysis of the delivery
	 * @throws AEMPelpException If the project is incorrect, no analyzer can be instantiated for the given project or fail to read project files.
	 */
	public AnalysisResults analyzeCode(CodeProject project,	TestData[] tests) throws AEMPelpException;

	/**
	 * Checks if the current user is teacher (Teacher of MainTeacher) of the given subject. 
	 * @param subject Subject Identifier
	 * @throws AuthPelpException If user is not authenticated
	 */
	public boolean isTeacher(ISubjectID subject) throws AuthPelpException;

	/**
	 * Checks if the current user is student of the given subject. 
	 * @param subject Subject Identifier
	 * @return True if the user is an student of this subject or False otherwise.
	 * @throws AuthPelpException If user is not authenticated
	 */
	public boolean isStudent(ISubjectID subject) throws AuthPelpException;
        
        /**
	 * Checks if the current user is administrator of the platform. 
	 * @param subject Subject Identifier
	 * @return True if the user is an administrator or False otherwise.
	 * @throws AuthPelpException If user is not authenticated
	 */
	public boolean isAdministrator() throws AuthPelpException;
        
        /**
        * Add a new activity to the given subject
        * @param subject Subject where activity will be added
        * @param activity Activity object to be added. Activity identifier must be null
        * @param tests Tests that delivers to this acivity should pass. It can be null.
        * @return Identifier for the new created activity
        * @throws AuthPelpException If the user is not authenticated.
	* @throws InvalidActivityPelpException If the information for this activity is incorrect.
        * @throws InvalidSubjectPelpException If the user cannot add activities to this subject, because is not a teacher.
	* @throws ExecPelpException When files in tests cannot be accessed or there is any problem adding the activity.
        */
        public ActivityID addActivity(ISubjectID subject,Activity activity, TestData[] tests) throws AuthPelpException,InvalidActivityPelpException,InvalidSubjectPelpException,ExecPelpException;     
        
        /**
        * Get the information of an activity
        * @param activityID Activity identifier
        * @return Activity object
        * @throws AuthPelpException If the user is not authenticated.
	* @throws InvalidActivityPelpException If the information for this activity is incorrect.
        * @throws InvalidSubjectPelpException If the user cannot add activities to this subject, because is not a teacher.
	* @throws ExecPelpException When files in tests cannot be accessed or there is any problem adding the activity.
        */
        public Activity getActivity(ActivityID activityID) throws AuthPelpException,InvalidActivityPelpException,InvalidSubjectPelpException,ExecPelpException;
        
        /**
        * Get the activity tests
        * @param activityID Activity identifier
        * @return Activity tests
        * @throws AuthPelpException If the user is not authenticated.
	* @throws InvalidActivityPelpException If the information for this activity is incorrect.
        * @throws InvalidSubjectPelpException If the user cannot add activities to this subject, because is not a teacher.
	* @throws ExecPelpException When files in tests cannot be accessed or there is any problem adding the activity.
        */
        public ActivityTest[] getActivityTests(ActivityID activityID) throws AuthPelpException,InvalidActivityPelpException,InvalidSubjectPelpException,ExecPelpException;
        
        /** 
         * Get the current user language
         * @return Language code for current user
         * @throws AuthPelpException If the user is not authenticated.
         */
        public String getUserLanguageCode() throws AuthPelpException;
        
        /**
        * Adds a new semester to the platform
        * @param semester Semester code
        * @param start Starting date
        * @param end Ending date
        * @return True if the semester has been correctly added or false otherwise
        * @throws InvalidTimePeriodPelpException if the provided information for time register is incorrect
        * @throws AuthPelpException if user is not an administrator
        */
        public boolean addSemester(String semester,Date start,Date end) throws AuthPelpException,InvalidTimePeriodPelpException;

        /**
        * Updates the dates of an existing semester
        * @param semester Semester code
        * @param start Starting date
        * @param end Ending date
        * @return True if the semester has been correctly updated or false otherwise
        * @throws InvalidTimePeriodPelpException if the provided information for time register is incorrect
        * @throws AuthPelpException if user is not an administrator
        */
        public boolean updateSemester(String semester,Date start,Date end) throws AuthPelpException,InvalidTimePeriodPelpException;

        /**
        * Remove an existing semester. Any operation with this semester will be done if it does not exist in the platform
        * @param semester Semester code
        * @return True if the semester has been correctly deleted or false otherwise
        * @throws AuthPelpException if user is not an administrator
        */
        public boolean removeSemester(String semester) throws AuthPelpException;
        
            /**
     * Adds a laboratory subject to a certain main subject. This relation does not depends on the semester. If it exists, does nothing.
     * @param mainSubject Main subject code
     * @param labSubject Laboratory subject code
     * @return True if the laboratory has been correctly added or false otherwise
     * @throws AuthPelpException if user is not an administrator
     */
    public boolean addLaboratory(String mainSubject,String laboratory) throws AuthPelpException;
    
    /**
     * Remove a laboratory subject from a certain main subject. This relation does not depends on the semester.
     * @param mainSubject Main subject code
     * @param labSubject Laboratory subject code
     * @return True if the laboratory has been correctly removed or false otherwise
     * @throws AuthPelpException if user is not an administrator
     */
    public boolean removeLaboratory(String mainSubject,String laboratory) throws AuthPelpException;

    /**
     * Activate a subject in the platform. If it exists an activation register for this subject, it sets it as active, if not, it creates a new one
     * @param semester Semester code
     * @param subject Subject code
     * @return True if the subject has been correctly activated or false otherwise
     * @throws AuthPelpException if user is not an administrator
     */
    public boolean activateSubject(String semester,String subject) throws AuthPelpException;

    /**
     * Disable a subject in the platform. If it exists an activation register for this subject, it sets it as inactive, if not, it does nothing
     * @param semester Semester code
     * @param subject Subject code
     * @return True if the subject has been correctly disabled or false otherwise
     * @throws AuthPelpException if user is not an administrator
     */
    public boolean deactivateSubject(String semester,String subject) throws AuthPelpException;

    /**
     * Removes the activation register for this subject. The subject will become inactive.
     * @param semester Semester code
     * @param subject Subject code
     * @return True if the subject activation register has been correctly removed or false otherwise
     * @throws AuthPelpException if user is not an administrator
     */
    public boolean removeSubjectActivationRegister(String semester,String subject) throws AuthPelpException;


}