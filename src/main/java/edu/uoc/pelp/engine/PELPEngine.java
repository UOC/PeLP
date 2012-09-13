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
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.aem.BasicCodeAnalyzer;
import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.engine.aem.TestData;
import edu.uoc.pelp.engine.aem.exception.AEMPelpException;
import edu.uoc.pelp.engine.campus.*;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.engine.deliver.IDeliverManager;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.exception.InvalidActivityPelpException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class implements the engine of the PELP system. 
 * @author Xavier Baró
 */
public class PELPEngine {
    
    /**
     * Object used to access the campus information
     */
    protected ICampusConnection _campusConnection=null;
    
    /**
     * Object to access the activities manager
     */
    protected IActivityManager _activityManager=null;
    
    /**
     * Object to access the delivers manager
     */
    protected IDeliverManager _deliverManager=null;
    
    /**
     * Object to access the system configuration
     */
    protected IPelpConfiguration _configuration=null;
    
    /**
     * Assign a new campus object.
     * @param campus Object allowing to access the campus information
     */
    public void setCampusConnection(ICampusConnection campus) {
        _campusConnection=campus;
    }
    
    /**
     * Assign a new system configuration object
     * @param conf Object that allows to retrieve system configuration parameters.
     */
    public void setSystemConfiguration(IPelpConfiguration conf) {
        _configuration=conf;
    }
    
    /**
     * Assign a new activity manager
     * @param manager Object allowing to manage the activities
     */
    public void setActivityManager(IActivityManager manager) {
        _activityManager=manager;
    }
    
    /**
     * Assign a new deliver manager
     * @param manager Object allowing to manage the delivers
     */
    public void setDeliverManager(IDeliverManager manager) {
        _deliverManager=manager;
    }           

    /**
     * Check if the current user is authenticated or not.
     * @return True if the user is authenticated or False otherwise.
     */
    public boolean isUserAuthenticated() {
        return _campusConnection.isUserAuthenticated();
    }
    
    /**
     * Obtain the information of currently authenticated user
     * @return Object with the user information for the current user
     * @throws AuthPelpException If no user is authenticated.
     */
    public Person getUserInfo() throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        return _campusConnection.getUserData();
    }
    
    /**
     * Return the list of active subjects for authenticated user
     * @return List of active subjects for current user.
     * @throws AuthPelpException If no user is authenticated.
     */
    public Subject[] getActiveSubjects() throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        ArrayList<Subject> subjectsList=new ArrayList<Subject>();
        
        // Add subjects
        for(ITimePeriod period:_campusConnection.getActivePeriods()) {            
            for(ISubjectID subjectID:_campusConnection.getUserSubjects(period)) {
                subjectsList.add(_campusConnection.getSubjectData(subjectID));
            }
        }
        
        // Sort the results
        Collections.sort(subjectsList);
        
        // Create the return list
        Subject[] retList=new Subject[subjectsList.size()];
        subjectsList.toArray(retList);
        
        return retList;
    }
    
    /**
     * Return the list of classrooms for authenticated user
     * @param subjectID Subject identifier
     * @return List of classrooms
     * @throws AuthPelpException If no user is authenticated.
     */
    public Classroom[] getSubjectClassrooms(ISubjectID subjectID) throws AuthPelpException {  
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        ArrayList<Classroom> classroomList=new ArrayList<Classroom>();
        
        // Add classrooms
        for(IClassroomID classID:_campusConnection.getUserClassrooms(subjectID)) {            
            classroomList.add(_campusConnection.getClassroomData(classID));
        }
        
        // Sort the results
        Collections.sort(classroomList);
        
        // Create the return list
        Classroom[] retList=new Classroom[classroomList.size()];
        classroomList.toArray(retList);
        
        return retList;
    } 
    
    /**
     * Get the list of activities of a given subject
     * @param subjectID Identifier of the subject
     * @param filterActive If True, only active activities are returned, otherwise, all activities are returned.
     * @return List of activities
     * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
     */
    public Activity[] getSubjectActivity(ISubjectID subjectID,boolean filterActive) throws AuthPelpException {
        ActivityID[] activityIDs;
        
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check that current user is student or teacher of this subject
        if(!isStudent(subjectID) && !isTeacher(subjectID)) {
            throw new AuthPelpException("User is not incribed to this subject");
        }
           
        // Obtain the list of activities
        if(filterActive) {
            activityIDs=_activityManager.getSubjectActiveActivities(subjectID);
        } else {
            activityIDs=_activityManager.getSubjectActivities(subjectID);
        }
        
        // Build the list of activities        
        ArrayList<Activity> activityList=new ArrayList<Activity>();
        
        // Add activities
        if(activityIDs!=null) {
            for(ActivityID actID:activityIDs) {
                activityList.add(_activityManager.getActivity(actID));
            }
        }
        
        // Sort the results
        Collections.sort(activityList);
        
        // Create the return list
        Activity[] retList=new Activity[activityList.size()];
        activityList.toArray(retList);
        
        return retList;
    }
    
    /**
     * Obtain the list of delivers of a certain user for a certain activity. Only the same user or a teacher
     * of the subject can access to this information.
     * @param user Identifier of the user for which delivers are requested.
     * @param activity Identifier of the activity delivers are requested from.
     * @return Array of Delivers.
     * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
     */
    public Deliver[] getActivityDelivers(IUserID user,ActivityID activity) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!(_campusConnection.getUserID().equals(user) && isStudent(activity.subjectID)) &&
           !isTeacher(activity.subjectID)) {
            throw new AuthPelpException("Not enough rights to access this information");
        }
        
        // Create the list of delivers        
        ArrayList<Deliver> deliverList=new ArrayList<Deliver>();
        
        // Add activities
        DeliverID[] deliverIDs=_deliverManager.getUserDelivers(user, activity);
        if(deliverIDs!=null) {
            for(DeliverID delID:deliverIDs) {
                deliverList.add(_deliverManager.getDeliver(delID));
            }
        }
        
        // Sort the results
        Collections.sort(deliverList);
        
        // Create the return list
        Deliver[] retList=new Deliver[deliverList.size()];
        deliverList.toArray(retList);
        
        return retList;
    }
    
    /**
     * Obtain the results of a certain deliver. Only the owner of the deliver and the teachers of the
     * related subject can access this information.
     * @param deliver Deliver identifier
     * @return Object with the results of the deliver analysis
     * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
     */
    public DeliverResults getDeliverResults(DeliverID deliver) throws AuthPelpException {
       
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isStudent(deliver.activity.subjectID) && !isTeacher(deliver.activity.subjectID)) {
            throw new AuthPelpException("Not enough rights to access this information");
        }
        
        // Obtain the results
        DeliverResults results=_deliverManager.getResults(deliver);
        
        // If the user is not teacher, delete information from private tests
        if(!isTeacher(deliver.activity.subjectID)) {
            removePrivateResultInformation(results);
        }
        
        return results;
    }
    
    /**
     * Obain the test information. Only teachers can access to private tests information.
     * @param testID Identifier for the test
     * @return Object with the test information
     * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
     */
    public ActivityTest getTestInformation(TestID testID) throws AuthPelpException {
        
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isStudent(testID.activity.subjectID) && !isTeacher(testID.activity.subjectID)) {
            throw new AuthPelpException("Not enough rights to access this information");
        }
        
        // Get the test information
        ActivityTest test=_activityManager.getTest(testID);
        
        // If the user is not a teacher, replace private tests with tests
        if(!test.isPublic() && !isTeacher(testID.activity.subjectID)) {
            test=new ActivityTest(testID);
            test.setPublic(false);
            test.setTestID(testID);
        } 
        
        return test;
    }
    
    /**
     * Perform a new deliver for current user to the given activity
     * @param deliver Deliver object with all the files
     * @param activityID Identifier for the target activity
     * @return Results obtained from the analisis of this deliver
     * @throws AuthPelpException If the user is not authenticated.
     * @throws InvalidActivityPelpException If the user cannot perform delivers to this activity, because is not a student or all allowed delivers are performed.
     * @throws ExecPelpException When files cannot be accessed or for some missconfiguration of the analyzer module.
     */
    public DeliverResults createNewDeliver(Deliver deliver, ActivityID activityID) throws AuthPelpException, InvalidActivityPelpException, ExecPelpException {
        
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check that the activity exists
        Activity activity=_activityManager.getActivity(activityID);
        if(activity==null) {
            throw new InvalidActivityPelpException("Given activity does not exist");
        }
        
        // Check user information
        if(!isStudent(activityID.subjectID)) {
            throw new AuthPelpException("Only students can create new delivers");
        }
        
        // Obtain the main classroom for this student and add them to the deliver information
        for(IClassroomID classroom:_campusConnection.getUserClassrooms(UserRoles.Student, activityID.subjectID)) {
            deliver.addMainClassroom(classroom);
        }
                
        // Check the number of delivers limit
        int numDelivers=_deliverManager.getNumUserDelivers(_campusConnection.getUserID(), activityID);   
        if(activity.getMaxDelivers()!=null) {
            if(numDelivers>=activity.getMaxDelivers()) {
                throw new InvalidActivityPelpException("The number of delivers for this activity has been exceeded");
            }
        }
                
        // Store the new deliver information
        DeliverID deliverID=_deliverManager.addDeliver(_campusConnection.getUserID(), activityID, deliver);
        
        // Create the list of tests
        TestID[] testID=_activityManager.getActivityTests(activityID);
        ActivityTest[] tests=null;
        if(testID.length>0) {
            tests=new ActivityTest[testID.length];
            for(int i=0;i<testID.length;i++) {
                tests[i]=_activityManager.getTest(testID[i]);
            }
        }
        
        // Obtain the code project
        CodeProject project=deliver.getCodeProject();
        project.setLanguage(activity.getLanguage());
        
        // Analyze the code project
        AnalysisResults analysisiResults=analyzeCode(project,tests);
                        
        // Store the results
        _deliverManager.addResults(deliverID, analysisiResults);
        
        // Return the results
        return _deliverManager.getResults(deliverID);
    }
    
    /**
     * Perform the analysis of a code project, both, building process and execution tests.
     * @param project Code project to be analyzed
     * @param tests Array of tests to be passed to the program.
     * @return Resuls obtained from the analysis of the delivery
     * @throws AEMPelpException If the project is incorrect, no analyzer can be instantiated for the given project or fail to read project files.
     */
    public AnalysisResults analyzeCode(CodeProject project,TestData[] tests) throws AEMPelpException {
              
        // Check the project
        if(!project.checkProject()) {
            throw new AEMPelpException("Invalid Code Project");
        }
        
        // Create the analyzer
        BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
        // Check the compiler
        if(codeAnalyzer==null) {
            if(project.getLanguage()!=null) {
                throw new AEMPelpException("Cannot instatiate a code analyzer for this project [" + project.getLanguage() + "]");
            } else {
                throw new AEMPelpException("Cannot instatiate a code analyzer for this project");
            }
        }
        
        // Set the configuration object
        codeAnalyzer.setConfiguration(_configuration);
                
        // Create the output object
        AnalysisResults result=codeAnalyzer.analyzeProject(project, tests);
        
        return result;
    }
    
    /**
     * Checks if the current user is teacher (Teacher of MainTeacher) of the given subject. 
     * @param subject Subject Identifier
     * @throws AuthPelpException If user is not authenticated
     */
    public boolean isTeacher(ISubjectID subject) throws AuthPelpException {
        if(_campusConnection.isRole(UserRoles.Teacher, subject) ||
           _campusConnection.isRole(UserRoles.MainTeacher, subject)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Checks if the current user is student of the given subject. 
     * @param subject Subject Identifier
     * @return True if the user is an student of this subject or False otherwise.
     * @throws AuthPelpException If user is not authenticated
     */
    public boolean isStudent(ISubjectID subject) throws AuthPelpException {
        if(_campusConnection.isRole(UserRoles.Student, subject)) {
            return true;
        }
        
        return false;
    }

    /**
    * Remove all the information of inputs and outputs for private tests
    * @param results Deliver results to be modified
    */
    private void removePrivateResultInformation(DeliverResults results) {
        
        // Analyze each test and remove the information from private ones.
        for(TestID testID:_activityManager.getActivityTests(results.getDeliverID().activity)){
            ActivityTest test=_activityManager.getTest(testID);
            if(!test.isPublic()) {
                results.removePrivateInformation(testID);
            }
        }
    }   
    
    //TODO: Direct methods for service demands
    
    //TODO: Administration methods
    // Create/Update Activities(+Tests) (Check all information)
    // Create/Update Semesters
    
    //TODO: Resources access
}
