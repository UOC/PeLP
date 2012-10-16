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
import edu.uoc.pelp.engine.aem.BasicCodeAnalyzer;
import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.engine.aem.TestData;
import edu.uoc.pelp.engine.aem.exception.AEMPelpException;
import edu.uoc.pelp.engine.campus.*;
import edu.uoc.pelp.engine.campus.UOC.ClassroomID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.engine.deliver.IDeliverManager;
import edu.uoc.pelp.engine.information.DAOInformationManager;
import edu.uoc.pelp.engine.information.IInformationManager;
import edu.uoc.pelp.exception.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * This class implements the engine of the PELP system. 
 * @author Xavier Baró
 */
public class PELPEngine implements IPELPEngine {
    
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
     * Object to access the administration manager
     */
    protected IAdministrationManager _administrationManager=null;
    
    /**
     * Object to access the information manager
     */
    protected IInformationManager _informationManager=null;
    
    /**
     * Object to access the system configuration
     */
    protected IPelpConfiguration _configuration=null;
    
    /**
     * Assign a new campus object.
     * @param campus Object allowing to access the campus information
     */
    @Override
    public void setCampusConnection(ICampusConnection campus) {
        _campusConnection=campus;
    }
    
    /**
     * Assign a new system configuration object
     * @param conf Object that allows to retrieve system configuration parameters.
     */
    @Override
    public void setSystemConfiguration(IPelpConfiguration conf) {
        _configuration=conf;
    }
    
    /**
     * Assign a new activity manager
     * @param manager Object allowing to manage the activities
     */
    @Override
    public void setActivityManager(IActivityManager manager) {
        _activityManager=manager;
    }
    
    /**
     * Assign a new deliver manager
     * @param manager Object allowing to manage the delivers
     */
    @Override
    public void setDeliverManager(IDeliverManager manager) {
        _deliverManager=manager;
    }           
    
    /**
     * Assign a new administration manager
     * @param manager Object allowing to manage the delivers
     */
    @Override
    public void setAdministrationManager(IAdministrationManager manager) {
        _administrationManager=manager;
    } 
    
    /**
    * Assign a new information manager
    * @param manager Object allowing to manage the platform information and statistics
    */
    @Override
    public void setInformationManager(DAOInformationManager manager) {
        _informationManager=manager;
    }

    /**
     * Check if the current user is authenticated or not.
     * @return True if the user is authenticated or False otherwise.
     * @throws AuthPelpException 
     */
    @Override
    public boolean isUserAuthenticated() throws AuthPelpException {
        return _campusConnection.isUserAuthenticated();
    }
    
    /**
     * Obtain the information of currently authenticated user
     * @return Object with the user information for the current user
     * @throws AuthPelpException If no user is authenticated.
     */
    @Override
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
    @Override
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
    @Override
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
    @Override
    public Activity[] getSubjectActivity(ISubjectID subjectID,boolean filterActive) throws AuthPelpException {
        ActivityID[] activityIDs;
        
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check that current user is student or teacher of this subject
        boolean userIsTeacher=true;
        if(!isTeacher(subjectID)) {
            userIsTeacher=false;
            if(!isStudent(subjectID)) {
                throw new AuthPelpException("User is not incribed to this subject");
            }
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
        
        if(userIsTeacher) {
            ISubjectID[] mainSubjects=_administrationManager.getMainSubjectOfLab(subjectID);
            if(mainSubjects!=null) {
                // Add activities in main subjects
                for(ISubjectID mainSub:mainSubjects) {
                    ActivityID[] mainActivityIDs;
                    if(filterActive) {
                        mainActivityIDs = _activityManager.getSubjectActiveActivities(mainSub);
                    } else {
                        mainActivityIDs=_activityManager.getSubjectActivities(mainSub);
                    }
                    // Add to previous list
                    if(mainActivityIDs!=null) {
                        for(ActivityID actID:mainActivityIDs) {
                            activityList.add(_activityManager.getActivity(actID));
                        }
                    }
                }
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
    @Override
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
     * Obtain the list of delivers of the current user for a certain activity. 
     * @param activity Identifier of the activity delivers are requested from.
     * @return Array of Delivers.
     * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
     */
    @Override
    public Deliver[] getUserActivityDelivers(ActivityID activity) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Return the delivers
        return getActivityDelivers(_campusConnection.getUserID(),activity);
    }
    
    /**
     * Obtain the results of a certain deliver. Only the owner of the deliver and the teachers of the
     * related subject can access this information.
     * @param deliver Deliver identifier
     * @return Object with the results of the deliver analysis
     * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
     */
    @Override
    public DeliverResults getDeliverResults(DeliverID deliver) throws AuthPelpException {
       
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions (Owner or teacher)
        boolean userIsTeacher=false;
        if(!getUserInfo().getUserID().equals(deliver.user)) {
            userIsTeacher=true;
            if(!isTeacher(deliver.activity.subjectID)) {
                // Get the deliver information
                Deliver del=_deliverManager.getDeliver(deliver);
                ClassroomID labSubject=(ClassroomID) del.getUserLabClassroom();
                if(labSubject==null || (labSubject!=null && !isTeacher(labSubject.getSubject()))) {
                    throw new AuthPelpException("Not enough rights to access this information");
                }
            }
        }
        
        // Obtain the results
        DeliverResults results=_deliverManager.getResults(deliver);
        
        // If the user is not teacher, delete information from private tests
        if(!userIsTeacher) {
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
    @Override
    public ActivityTest getTestInformation(TestID testID) throws AuthPelpException {
        
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isStudent(testID.activity.subjectID) && !isTeacher(testID.activity.subjectID) && !isLabTeacher(testID.activity.subjectID)) {
            
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
    @Override
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
               
        // Obtain the main classroom for this student and add it to the deliver information
        for(IClassroomID classroom:_campusConnection.getUserClassrooms(UserRoles.Student, activityID.subjectID)) {
            deliver.addMainClassroom(classroom);
        }
        
        // Obtain the laboratory classroom for this student
        ISubjectID[] labSubjects=_administrationManager.getLabSubjectOfMain(activityID.subjectID);
        if(labSubjects!=null && labSubjects.length>0) {
            // Add only the first lab
            boolean hasLab=false;
            for(ISubjectID labSubjectID:labSubjects) {
                if(hasLab) {
                    break;
                }
                for(IClassroomID classroom:_campusConnection.getUserClassrooms(UserRoles.Student, labSubjectID)) {
                    deliver.addLabClassroom(classroom);
                    hasLab=true;
                    break;
                }
            }
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
        if(deliverID==null) {
            throw new ExecPelpException("Unknown error adding the new deliver");
        }
        
        // Recover deliver data
        Deliver newDeliver=_deliverManager.getDeliver(deliverID);
        if(newDeliver==null) {
            throw new ExecPelpException("Null object recovering new added deliver");
        }
        
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
        CodeProject project=newDeliver.getCodeProject();
        project.setLanguage(activity.getLanguage());
        
        // Analyze the code project
        AnalysisResults analysisResults=analyzeCode(project,tests);
                        
        // Store the results
        if(!_deliverManager.addResults(deliverID, analysisResults)) {
            throw new ExecPelpException("Error adding deliver results.");
        }
        
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
    @Override
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
    @Override
    public boolean isTeacher(ISubjectID subject) throws AuthPelpException {
        if(_campusConnection.isRole(UserRoles.Teacher, subject) ||
           _campusConnection.isRole(UserRoles.MainTeacher, subject)) {
            return true;
        }
        
        return false;
    }
    
    /**
    * Checks if the current user is teacher (Teacher of MainTeacher) of any laboratory of the given subject. 
    * @param subject Subject Identifier
    * @throws AuthPelpException If user is not authenticated
    */
    @Override
    public boolean isLabTeacher(ISubjectID subject) throws AuthPelpException {
        
        ISubjectID[] labSubjects=_administrationManager.getLabSubjectOfMain(subject);
        if(labSubjects==null) {
            return false;
        }
        for(ISubjectID sub:labSubjects) {
            if(isTeacher(sub)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the current user is student of the given subject. 
     * @param subject Subject Identifier
     * @return True if the user is an student of this subject or False otherwise.
     * @throws AuthPelpException If user is not authenticated
     */
    @Override
    public boolean isStudent(ISubjectID subject) throws AuthPelpException {
        if(_campusConnection.isRole(UserRoles.Student, subject)) {
            return true;
        }
        
        return false;
    }
    
    /**
    * Obtain the list of all the delivers of a certain classroom for a certain activity. Only a teacher
    * of the classroom can access to this information. Both, laboratory and main classrooms are checked.
    * @param classroom Identifier of the classroom for which delivers are requested.
    * @param activity Identifier of the activity delivers are requested from.
    * @return Array of Delivers.
    * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
    */
    @Override
    public Deliver[] getClassroomDelivers(IClassroomID classroom, ActivityID activity) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!_campusConnection.isRole(UserRoles.Teacher, classroom) && !_campusConnection.isRole(UserRoles.MainTeacher, classroom))  {
            throw new AuthPelpException("Not enough rights to access this information");
        }
        
        // Return the delivers
        return _deliverManager.getClassroomDelivers(classroom, activity);
    }

    /**
    * Obtain the last submitted deliver for each user of a certain classroom for a certain activity. Only a teacher
    * of the classroom can access to this information. Both, laboratory and main classrooms are checked.
    * @param classroom Identifier of the classroom for which delivers are requested.
    * @param activity Identifier of the activity delivers are requested from.
    * @return Array of Delivers.
    * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
    */
    @Override
    public Deliver[] getClassroomLastDelivers(IClassroomID classroom, ActivityID activity) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!_campusConnection.isRole(UserRoles.Teacher, classroom) && !_campusConnection.isRole(UserRoles.MainTeacher, classroom))  {
            throw new AuthPelpException("Not enough rights to access this information");
        }
                
        // Return the delivers
        return _deliverManager.getClassroomLastDelivers(classroom, activity);
    }
    
    /**
    * Checks if the current user is administrator of the platform. 
    * @param subject Subject Identifier
    * @return True if the user is an administrator or False otherwise.
    * @throws AuthPelpException If user is not authenticated
    */
    @Override
    public boolean isAdministrator() throws AuthPelpException {
        // Check if this user is an administrator
        return _administrationManager.isAdministrator(getUserInfo());
    }
    
    @Override
    public ActivityID addActivity(ISubjectID subject,Activity activity, TestData[] tests) throws AuthPelpException,InvalidActivityPelpException,InvalidSubjectPelpException,ExecPelpException {
        
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!_campusConnection.isRole(UserRoles.Teacher, subject) && !_campusConnection.isRole(UserRoles.MainTeacher, subject))  {
            throw new InvalidSubjectPelpException("Only teachers can add activities to a subject");
        }
        
        // Check activity data
        if(activity==null) {
            throw new InvalidActivityPelpException("Null activity is detected"); 
        }
        if(activity.getStart()!=null && activity.getEnd()!=null) {
            if(activity.getStart().after(activity.getEnd())) {
                throw new InvalidActivityPelpException("Starting date cannot be after ending date.");
            }
        }
        if(activity.getEnd()!=null && activity.getEnd().before(new Date())) {
            throw new InvalidActivityPelpException("Ending date cannot be in the past.");
        }
        
        // Add the new activity
        ActivityID newID=_activityManager.addActivity(subject, activity);
        if(newID==null) {
            throw new ExecPelpException("Cannot add the new activity");
        }
        
        // Add the tests
        if(tests!=null) {
            for(TestData test:tests) {
                ActivityTest newTest;
                if(test instanceof ActivityTest) {
                    newTest=(ActivityTest) test;
                } else {
                    newTest=new ActivityTest(test);
                }
                if(_activityManager.addTest(newID, newTest)==null) {
                    _activityManager.deleteActivity(newID);
                    throw new ExecPelpException("Cannot add tests. Activity removed.");
                }
            }
        }
        
        return newID;
    }
    
    @Override
    public Activity getActivity(ActivityID activityID) throws AuthPelpException, InvalidActivityPelpException, InvalidSubjectPelpException, ExecPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isLabTeacher(activityID.subjectID) && !_campusConnection.isRole(UserRoles.Teacher, activityID.subjectID) && !_campusConnection.isRole(UserRoles.MainTeacher, activityID.subjectID))  {
            throw new InvalidSubjectPelpException("Only teachers can get activities from the subject");
        }
        
        // Check activity identifer
        if(activityID==null) {
            throw new InvalidActivityPelpException("Null activity identifier is detected"); 
        }
        
        // Get the activity
        return _activityManager.getActivity(activityID);
    }
    
    @Override
    public ActivityTest[] getActivityTests(ActivityID activityID) throws AuthPelpException, InvalidActivityPelpException, InvalidSubjectPelpException, ExecPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!_campusConnection.isRole(UserRoles.Teacher, activityID.subjectID) && !_campusConnection.isRole(UserRoles.MainTeacher, activityID.subjectID))  {
            throw new InvalidSubjectPelpException("Only teachers can get activities from the subject");
        }
        
        // Check activity identifer
        if(activityID==null) {
            throw new InvalidActivityPelpException("Null activity identifier is detected"); 
        }
        
        // Get the activity test IDs
        TestID[] testIDs=_activityManager.getActivityTests(activityID);
        if(testIDs==null) {
            return null;
        }
        
        // Create the output array
        ActivityTest[] retList=new ActivityTest[testIDs.length];
        for(int i=0;i<testIDs.length;i++) {
            retList[i]=_activityManager.getTest(testIDs[i]);
        }
        
        return retList;
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
    
    @Override
    public String getUserLanguageCode() throws AuthPelpException {
        Person userData=_campusConnection.getUserData();
                
        // Convert internal language codes to PeLP language code
        String languageCode="";
        //return languageCode;
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addSemester(String semester, Date start, Date end) throws AuthPelpException, InvalidTimePeriodPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isAdministrator())  {
            throw new AuthPelpException("Only administrators can add new semesters");
        }
        
        return _administrationManager.addSemester(semester,start,end);
    }

    @Override
    public boolean updateSemester(String semester, Date start, Date end) throws AuthPelpException, InvalidTimePeriodPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isAdministrator())  {
            throw new AuthPelpException("Only administrators can modify semesters");
        }
        
        return _administrationManager.updateSemester(semester,start,end);
    }

    @Override
    public boolean removeSemester(String semester) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isAdministrator())  {
            throw new AuthPelpException("Only administrators can delete a semester");
        }
        
        return _administrationManager.removeSemester(semester);
    }
    
    @Override
    public boolean addLaboratory(String mainSubject, String laboratory) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isAdministrator())  {
            throw new AuthPelpException("Only administrators can delete a semester");
        }
        
        return _administrationManager.addMainLabCorrespondence(mainSubject, laboratory);
    }

    @Override
    public boolean removeLaboratory(String mainSubject, String laboratory) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isAdministrator())  {
            throw new AuthPelpException("Only administrators can delete a semester");
        }
        
        return _administrationManager.deleteMainLabCorrespondence(mainSubject, laboratory);
    }

    @Override
    public boolean activateSubject(String semester, String subject) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isAdministrator())  {
            throw new AuthPelpException("Only administrators can delete a semester");
        }
        
        return _administrationManager.addActiveSubject(semester, subject, true);
    }

    @Override
    public boolean deactivateSubject(String semester, String subject) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isAdministrator())  {
            throw new AuthPelpException("Only administrators can delete a semester");
        }
        
        // Change active value to false
        return _administrationManager.updateActiveSubject(semester, subject, false);
    }

    @Override
    public boolean removeSubjectActivationRegister(String semester, String subject) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isAdministrator())  {
            throw new AuthPelpException("Only administrators can delete a semester");
        }
        
        // Remove the regioster
        return _administrationManager.deleteActiveSubject(semester, subject);
    }
    
    @Override
    public Deliver getDeliver(DeliverID deliver) throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions (Owner or teacher)
        Deliver deliverObj=_deliverManager.getDeliver(deliver);
        if(!getUserInfo().getUserID().equals(deliver.user)) {
            if(!isTeacher(deliver.activity.subjectID)) {
                // Check the deliver information
                ClassroomID labSubject=(ClassroomID) deliverObj.getUserLabClassroom();
                if(labSubject==null || (labSubject!=null && !isTeacher(labSubject.getSubject()))) {
                    throw new AuthPelpException("Not enough rights to access this information");
                }
            }
        }
        
        // Obtain the deliver
        return deliverObj;
    }
    
    @Override
    public ActivityTest[] getTestInfo(ActivityID activityID) throws AuthPelpException {
        // Chech the parameters
        if(activityID==null) {
            return null;
        }
        
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        boolean isTeacher=isTeacher(activityID.subjectID);
        if(!isStudent(activityID.subjectID) && !isTeacher) {
            throw new AuthPelpException("Not enough rights to access this information");
        }
        
        // Get the activity
        TestID[] testList=_activityManager.getActivityTests(activityID);
        if(testList==null) {
            return null;
        }
        
        // Create the output list
        ActivityTest[] retObj=new ActivityTest[testList.length];
        for(int i=0;i<testList.length;i++) {
            ActivityTest test=_activityManager.getTest(testList[i]);
            if(!test.isPublic() && !isTeacher) {
                // Create an empty test object
                test=new ActivityTest();
                test.setPublic(false);
                test.setTestID(testList[i]);
            }
            retObj[i]=test;
        }
        
        return retObj;
    }

    @Override
    public int getActivityMaxDelivers(ActivityID activityID) throws AuthPelpException {
        // Chech the parameters
        if(activityID==null) {
            return -1;
        }
        
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        
        // Check user restrictions
        if(!isStudent(activityID.subjectID) && !isTeacher(activityID.subjectID) && !isLabTeacher(activityID.subjectID)) {
            throw new AuthPelpException("Not enough rights to access this information");
        }
        
        // Get the activity
        Activity activity=_activityManager.getActivity(activityID);
        if(activity==null) {
            return -1;
        }
        
        return activity.getMaxDelivers();
    }

    
    //TODO: Direct methods for service demands
    
    //TODO: Administration methods
    // Create/Update Activities(+Tests) (Check all information)
    
    //TODO: Resources access    

}
