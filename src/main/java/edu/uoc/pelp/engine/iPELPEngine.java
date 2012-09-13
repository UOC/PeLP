package edu.uoc.pelp.engine;

import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.activity.Activity;
import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.activity.ActivityTest;
import edu.uoc.pelp.engine.activity.IActivityManager;
import edu.uoc.pelp.engine.activity.TestID;
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.engine.aem.TestData;
import edu.uoc.pelp.engine.aem.exception.AEMPelpException;
import edu.uoc.pelp.engine.campus.Classroom;
import edu.uoc.pelp.engine.campus.ICampusConnection;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.engine.campus.Subject;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.engine.deliver.IDeliverManager;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.exception.InvalidActivityPelpException;

public interface iPELPEngine {

	/**
	 * Assign a new campus object.
	 * @param campus Object allowing to access the campus information
	 */
	public abstract void setCampusConnection(ICampusConnection campus);

	/**
	 * Assign a new system configuration object
	 * @param conf Object that allows to retrieve system configuration parameters.
	 */
	public abstract void setSystemConfiguration(IPelpConfiguration conf);

	/**
	 * Assign a new activity manager
	 * @param manager Object allowing to manage the activities
	 */
	public abstract void setActivityManager(IActivityManager manager);

	/**
	 * Assign a new deliver manager
	 * @param manager Object allowing to manage the delivers
	 */
	public abstract void setDeliverManager(IDeliverManager manager);

	/**
	 * Check if the current user is authenticated or not.
	 * @return True if the user is authenticated or False otherwise.
	 */
	public abstract boolean isUserAuthenticated() throws AuthPelpException;

	/**
	 * Obtain the information of currently authenticated user
	 * @return Object with the user information for the current user
	 * @throws AuthPelpException If no user is authenticated.
	 */
	public abstract Person getUserInfo() throws AuthPelpException;

	/**
	 * Return the list of active subjects for authenticated user
	 * @return List of active subjects for current user.
	 * @throws AuthPelpException If no user is authenticated.
	 */
	public abstract Subject[] getActiveSubjects() throws AuthPelpException;

	/**
	 * Return the list of classrooms for authenticated user
	 * @param subjectID Subject identifier
	 * @return List of classrooms
	 * @throws AuthPelpException If no user is authenticated.
	 */
	public abstract Classroom[] getSubjectClassrooms(ISubjectID subjectID)
			throws AuthPelpException;

	/**
	 * Get the list of activities of a given subject
	 * @param subjectID Identifier of the subject
	 * @param filterActive If True, only active activities are returned, otherwise, all activities are returned.
	 * @return List of activities
	 * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
	 */
	public abstract Activity[] getSubjectActivity(ISubjectID subjectID,
			boolean filterActive) throws AuthPelpException;

	/**
	 * Obtain the list of delivers of a certain user for a certain activity. Only the same user or a teacher
	 * of the subject can access to this information.
	 * @param user Identifier of the user for which delivers are requested.
	 * @param activity Identifier of the activity delivers are requested from.
	 * @return Array of Delivers.
	 * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
	 */
	public abstract Deliver[] getActivityDelivers(IUserID user,
			ActivityID activity) throws AuthPelpException;

	/**
	 * Obtain the results of a certain deliver. Only the owner of the deliver and the teachers of the
	 * related subject can access this information.
	 * @param deliver Deliver identifier
	 * @return Object with the results of the deliver analysis
	 * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
	 */
	public abstract DeliverResults getDeliverResults(DeliverID deliver)
			throws AuthPelpException;

	/**
	 * Obain the test information. Only teachers can access to private tests information.
	 * @param testID Identifier for the test
	 * @return Object with the test information
	 * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
	 */
	public abstract ActivityTest getTestInformation(TestID testID)
			throws AuthPelpException;

	/**
	 * Perform a new deliver for current user to the given activity
	 * @param deliver Deliver object with all the files
	 * @param activityID Identifier for the target activity
	 * @return Results obtained from the analisis of this deliver
	 * @throws AuthPelpException If the user is not authenticated.
	 * @throws InvalidActivityPelpException If the user cannot perform delivers to this activity, because is not a student or all allowed delivers are performed.
	 * @throws ExecPelpException When files cannot be accessed or for some missconfiguration of the analyzer module.
	 */
	public abstract DeliverResults createNewDeliver(Deliver deliver,
			ActivityID activityID) throws AuthPelpException,
			InvalidActivityPelpException, ExecPelpException;

	/**
	 * Perform the analysis of a code project, both, building process and execution tests.
	 * @param project Code project to be analyzed
	 * @param tests Array of tests to be passed to the program.
	 * @return Resuls obtained from the analysis of the delivery
	 * @throws AEMPelpException If the project is incorrect, no analyzer can be instantiated for the given project or fail to read project files.
	 */
	public abstract AnalysisResults analyzeCode(CodeProject project,
			TestData[] tests) throws AEMPelpException;

	/**
	 * Checks if the current user is teacher (Teacher of MainTeacher) of the given subject. 
	 * @param subject Subject Identifier
	 * @throws AuthPelpException If user is not authenticated
	 */
	public abstract boolean isTeacher(ISubjectID subject)
			throws AuthPelpException;

	/**
	 * Checks if the current user is student of the given subject. 
	 * @param subject Subject Identifier
	 * @return True if the user is an student of this subject or False otherwise.
	 * @throws AuthPelpException If user is not authenticated
	 */
	public abstract boolean isStudent(ISubjectID subject)
			throws AuthPelpException;

}