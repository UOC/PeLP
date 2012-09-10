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
public class PELPEngine implements iPELPEngine {
    
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
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#setCampusConnection(edu.uoc.pelp.engine.campus.ICampusConnection)
	 */
    public void setCampusConnection(ICampusConnection campus) {
        _campusConnection=campus;
    }
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#setSystemConfiguration(edu.uoc.pelp.conf.IPelpConfiguration)
	 */
    public void setSystemConfiguration(IPelpConfiguration conf) {
        _configuration=conf;
    }
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#setActivityManager(edu.uoc.pelp.engine.activity.IActivityManager)
	 */
    public void setActivityManager(IActivityManager manager) {
        _activityManager=manager;
    }
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#setDeliverManager(edu.uoc.pelp.engine.deliver.IDeliverManager)
	 */
    public void setDeliverManager(IDeliverManager manager) {
        _deliverManager=manager;
    }           

    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#isUserAuthenticated()
	 */
    public boolean isUserAuthenticated() throws AuthPelpException {
    	return _campusConnection.isUserAuthenticated();
    }
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#getUserInfo()
	 */
    public Person getUserInfo() throws AuthPelpException {
        // Check user authentication
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("User must be authenticated");
        }
        return _campusConnection.getUserData();
    }
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#getActiveSubjects()
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
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#getSubjectClassrooms(edu.uoc.pelp.engine.campus.ISubjectID)
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
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#getSubjectActivity(edu.uoc.pelp.engine.campus.ISubjectID, boolean)
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
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#getActivityDelivers(edu.uoc.pelp.engine.campus.IUserID, edu.uoc.pelp.engine.activity.ActivityID)
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
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#getDeliverResults(edu.uoc.pelp.engine.deliver.DeliverID)
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
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#getTestInformation(edu.uoc.pelp.engine.activity.TestID)
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
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#createNewDeliver(edu.uoc.pelp.engine.deliver.Deliver, edu.uoc.pelp.engine.activity.ActivityID)
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
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#analyzeCode(edu.uoc.pelp.engine.aem.CodeProject, edu.uoc.pelp.engine.aem.TestData[])
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
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#isTeacher(edu.uoc.pelp.engine.campus.ISubjectID)
	 */
    public boolean isTeacher(ISubjectID subject) throws AuthPelpException {
        if(_campusConnection.isRole(UserRoles.Teacher, subject) ||
           _campusConnection.isRole(UserRoles.MainTeacher, subject)) {
            return true;
        }
        
        return false;
    }
    
    /* (non-Javadoc)
	 * @see edu.uoc.pelp.engine.iPELPEngine#isStudent(edu.uoc.pelp.engine.campus.ISubjectID)
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
}
