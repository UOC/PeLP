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
package edu.uoc.pelp.model.vo;

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.activity.ActivityTestResult;
import edu.uoc.pelp.engine.activity.TestID;
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.aem.BuildResult;
import edu.uoc.pelp.engine.aem.TestResult;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.engine.campus.UOC.UserID;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.model.vo.UOC.SubjectPK;
import java.math.BigInteger;
import java.util.Date;

/**
 * This class provides conversions between internal objects and DAO representations
 * @author Xavier Baró
 */
public class ObjectFactory {
    
    /**
     * Get the primary key from of the entity class from the internal Id class
     * @param objectID Object identifier to be processed 
     * @return Instance of the primary key or null if information is incorrect.
     */
    public static ActivityPK getActivityPK(ActivityID objectID) {
        SubjectID subjectID=null;
        UserID userID=null;
        
        // Check input parameters
        if(objectID==null) {
            return null;
        }
        
        // Get proper objects
        if(objectID.subjectID instanceof SubjectID) {
            subjectID=(SubjectID)objectID.subjectID;
        }
        
        // Check the types
        if(subjectID==null) {
            return null;
        }
        
        // Extract the parameters from previous objects
        String semester=subjectID.getSemester().getID();
        String subject=subjectID.getCode();
        int activityIndex=(int)objectID.index;                

        // Return the new object
        return new ActivityPK(semester,subject,activityIndex);
    }
    
    /**
     * Get the primary key from of the entity class from the internal Id class
     * @param objectID Object identifier to be processed 
     * @return Instance of the primary key or null if information is incorrect.
     */
    public static SubjectPK getSubjectPK(ISubjectID objectID) {
        SubjectID subjectID;
        
        // Check input parameters
        if(objectID==null) {
            return null;
        }
        
        // Get proper objects
        if(objectID instanceof SubjectID) {
            subjectID=(SubjectID)objectID;
        } else {
            return null;
        }
        
        // Extract the parameters from previous objects
        String semester=subjectID.getSemester().getID();
        String subject=subjectID.getCode();
        
        // Return the new object
        return new SubjectPK(semester,subject);
    }
    
    /**
     * Get the primary key from of the entity class from the internal Id class
     * @param objectID Object identifier to be processed 
     * @return Instance of the primary key or null if information is incorrect.
     */
    public static DeliverPK getDeliverPK(DeliverID objectID) {
        SubjectID subjectID=null;
        UserID userID=null;
        
        // Check input parameters
        if(objectID==null) {
            return null;
        }
        if(objectID.activity==null || objectID.user==null) {
            return null;
        }
        
        // Get proper objects
        if(objectID.activity.subjectID instanceof SubjectID) {
            subjectID=(SubjectID)objectID.activity.subjectID;
        }
        if(objectID.user instanceof UserID) {
            userID=(UserID)objectID.user;
        }
        
        // Check the types
        if(subjectID==null || userID==null) {
            return null;
        }
        
        // Extract the parameters from previous objects
        String semester=subjectID.getSemester().getID();
        String subject=subjectID.getCode();
        int activityIndex=(int)objectID.activity.index;                
        String user=userID.idp;
        int deliverIndex=(int)objectID.index;
        
        // Return the new object
        return new DeliverPK(semester,subject,activityIndex,user,deliverIndex);
    }
    
    /**
     * Get the primary key from of the entity class from the internal Id class
     * @param objectID Object identifier to be processed 
     * @return Instance of the primary key or null if information is incorrect.
     */
    public static DeliverTestResultPK getDeliverTestResultPK(DeliverID deliverID,TestID testID) {
        
        // Check input objects
         if(deliverID==null || testID==null) {
            return null;
        }
         
        // Check that objects are compatible
        if(deliverID.activity==null || testID.activity==null) {
            return null;
        }
        if(!deliverID.activity.equals(testID.activity)) {
            return null;
        }
        
        // Get the deliver primary key
        DeliverPK deliverPK=getDeliverPK(deliverID); 
        if(deliverPK==null) {
            return null;
        }
        
        // Return the new object
        int testIndex=(int)testID.index;
        return new DeliverTestResultPK(deliverPK,testIndex);
    }
    
    /**
     * Get an object of the entity class from the internal class
     * @param object Object to be processed 
     * @return Instance of the entity or null if information is incorrect.
     */
    public static DeliverResult getDeliverResultReg(DeliverResults object) {
        
        // Get the primary key
        DeliverPK key=getDeliverPK(object.getDeliverID());
        if(key==null) {
            return null;
        }
        
        // Check the tests
        boolean compilation=object.getBuildResults().isCorrect();
        boolean execution=false;
        if(compilation) {
            execution=true;
            for(TestResult test:object.getResults(object.getDeliverID())) {
                if(!test.isPassed()) {
                    execution=false;
                    break;
                }
            }
        }
        
        // Create the new object
        DeliverResult result=new DeliverResult(key, compilation, execution, object.getBuildResults().getLanguage());
        
        // Set additional information
        result.setCompMessage(object.getBuildResults().getMessge());
        result.setStartDate(object.getBuildResults().getStartTime());
        result.setEndDate(object.getBuildResults().getEndTime());
        
        // Return the new object
        return result;
    }
    
    /**
     * Get the internal object ID from the primary key
     * @param key Primary key to be processed 
     * @return Instance of the object ID or null if information is incorrect.
     */
    public static ActivityID getActivityID(ActivityPK key) {
        // Check input parameters
        if(key==null) {
            return null;
        }
        
        // Get parameters
        ISubjectID subjectID=new SubjectID(key.getSubject(),new Semester(key.getSemester()));
        
        // Return the new object
        return new ActivityID(subjectID,key.getIndex());
    }
    
    /**
     * Get the internal object ID from the primary key
     * @param key Primary key to be processed 
     * @return Instance of the object ID or null if information is incorrect.
     */
    public static DeliverID getDeliverID(DeliverPK key) {        
        // Check input parameters
        if(key==null) {
            return null;
        }
        
        // Get parameters
        IUserID user=new UserID(key.getUserID());
        Semester semester=new Semester(key.getSemester());
        ISubjectID subjectID=new SubjectID(key.getSubject(),semester);
        ActivityID activity=new ActivityID(subjectID,key.getActivityIndex());
                
        // Return the new object
        return new DeliverID(user,activity,key.getDeliverIndex());
    }
    
    /**
     * Get an object of the internal class from the entity class object
     * @param object Entity object to be processed
     * @return Instance of the internal class or null if information is incorrect.
     */
    public static TestResult getTestResultObj(DeliverTestResult object) {
        
        // Build output object
        return new TestResult(object.getPassed(),object.getProgramOutput(),object.getElapsedTime().longValue());
    }
    
    /**
     * Get an entity object from the internal class object
     * @param object Object to be processed
     * @return Instance of the Entity class or null if information is incorrect.
     */
    public static DeliverTestResult getTestResultReg(DeliverID deliverID,ActivityTestResult object) {
        // Check the input object
        if(deliverID==null || object==null) {
            return null;
        }
        
        // Get elements information
        DeliverPK deliverPK=getDeliverPK(deliverID);
        TestID testID=object.getTestID();
        if(deliverPK==null || testID==null) {
            return null;
        }
        
        // Create the new objects   
        int testIndex=(int)testID.index;
        DeliverTestResultPK deliverTestPK=new DeliverTestResultPK(deliverPK,testIndex);
        
        // Build output object
        DeliverTestResult newObject=new DeliverTestResult(deliverTestPK,object.isPassed());
        newObject.setElapsedTime(BigInteger.valueOf(object.getElapsedTime()));
        newObject.setProgramOutput(object.getOutput());
        return newObject;
    }
    
    /**
     * Get an object of the internal class from the entity class object
     * @param object Entity object to be processed
     * @param tests List of test results
     * @return Instance of the internal class or null if information is incorrect.
     */
    public static DeliverResults getDeliverResulsObj(DeliverResult object,DeliverTestResult[] tests) {
        
        // Get the identifier
        DeliverID deliverID=getDeliverID(object.getDeliverPK());
        if(deliverID==null) {
            return null;
        }
                
        // Get the results
        AnalysisResults analysisResult=new AnalysisResults(getBuildResultObj(object));
        
        // Add the tests
        if(tests!=null) {
            for(DeliverTestResult test:tests) {
                if(!analysisResult.addTestResult(getTestResultObj(test))) {
                    return null;
                }
            }
        }
        
        // Return the new object
        return new DeliverResults(deliverID,analysisResult);
    }
    
    /**
     * Get an object of the internal class from the entity class object
     * @param object Object to be processed 
     * @return Instance of the internal class or null if information is incorrect.
     */
    public static BuildResult getBuildResultObj(DeliverResult object) {
        
        // Get the parameters
        boolean correct=object.getCompilation();
        String message=object.getCompMessage();
        String progLanguage=object.getProgLanguage();
        Date startTime=object.getStartDate();
        Date endTime=object.getEndDate();
        
        // Get the building results
        return new BuildResult (correct,message,progLanguage,startTime,endTime);     
    }

    /**
     * Get an entity object from the internal class object
     * @param object Object to be processed
     * @return Instance of the Entity class or null if information is incorrect.
     */
    public static edu.uoc.pelp.model.vo.Activity getActivityReg(edu.uoc.pelp.engine.activity.Activity object) {
        
        // Check input parameter
        if(object==null) {
            return null;
        }
        
        // Get the primary key
        ActivityPK activityPK=ObjectFactory.getActivityPK(object.getActivity());
        
        // Create the new object
        edu.uoc.pelp.model.vo.Activity newObj=new edu.uoc.pelp.model.vo.Activity(activityPK);
        newObj.setStartDate(object.getStart());
        newObj.setEndDate(object.getEnd());
        newObj.setMaxDelivers(object.getMaxDelivers());
        newObj.setProgLanguage(object.getLanguage());
        
        return newObj;
    }

    /**
     * Get an object of the internal class from the entity class object
     * @param object Object to be processed 
     * @return Instance of the internal class or null if information is incorrect.
     */
    public static edu.uoc.pelp.engine.activity.Activity getActivityObj(edu.uoc.pelp.model.vo.Activity object, ActivityDesc[] descriptions) {
        
        // Check input parameters
        if(object==null) {
            return null;
        }
        
        // Get the parameters
        ActivityID activityID=ObjectFactory.getActivityID(object.getActivityPK());
        
        // Create the new object
        edu.uoc.pelp.engine.activity.Activity newObj=new edu.uoc.pelp.engine.activity.Activity(activityID,object.getStartDate(),object.getEndDate());  
        newObj.setLanguage(object.getLanguage());
        newObj.setMaxDelivers(object.getMaxDelivers());
        if(descriptions!=null) {
            for(ActivityDesc actDesc:descriptions) {
                newObj.setDescription(actDesc.activityDescPK.getLangCode(), actDesc.getDescription());
            }
        }
        
        return newObj;
    }
    
    /**
     * Get an entity object from the internal class object
     * @param object Object to be processed
     * @return Instance of the Entity class or null if information is incorrect.
     */
    public static edu.uoc.pelp.model.vo.UOC.Semester getSemesterReg(edu.uoc.pelp.engine.campus.UOC.Semester object) {
        
        // Check input parameter
        if(object==null) {
            return null;
        }
        
        // Create the new object
        edu.uoc.pelp.model.vo.UOC.Semester newObj=new edu.uoc.pelp.model.vo.UOC.Semester(object.getID());
        newObj.setStartDate(object.getInitialDate());
        newObj.setEndDate(object.getFinalDate());        
        
        return newObj;
    }

    /**
     * Get an object of the internal class from the entity class object
     * @param object Object to be processed 
     * @return Instance of the internal class or null if information is incorrect.
     */
    public static edu.uoc.pelp.engine.campus.UOC.Semester getSemesterObj(edu.uoc.pelp.model.vo.UOC.Semester object) {
        
        // Check input parameters
        if(object==null) {
            return null;
        }
        
        // Create the new object
        edu.uoc.pelp.engine.campus.UOC.Semester newObj=new edu.uoc.pelp.engine.campus.UOC.Semester(object.getSemester(),object.getStartDate(),object.getEndDate());  
        
        return newObj;
    }
}
