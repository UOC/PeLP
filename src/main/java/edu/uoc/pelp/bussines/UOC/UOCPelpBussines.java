package edu.uoc.pelp.bussines.UOC;

import edu.uoc.pelp.bussines.PelpBussines;
import edu.uoc.pelp.bussines.UOC.exception.InvalidSessionException;
import edu.uoc.pelp.bussines.exception.AuthorizationException;
import edu.uoc.pelp.bussines.exception.InvalidEngineException;
import edu.uoc.pelp.bussines.vo.*;
import edu.uoc.pelp.exception.ExecPelpException;
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
     * Add a new activity
     * @param semester Semester code
     * @param subject Subject code
     * @param start Starting date
     * @param end Ending date
     * @param progLangCode Programming language for this activity. Null if there is no restriction.
     * @param maxDelivers Maximum number of delivers allowed for this activity. A negative values means that there is no restriction.
     * @param descriptions Descriptions for this activity in multiple languages
     * @param tests Tests for this activity
     * @param resources Resources assigned to this activity
     * @return True if the activity is corretly created of False otherwise.
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this subject
     */
    public boolean addNewActivity(String semester,String subject,Date start,Date end, String progLangCode,int maxDelivers, MultilingualText[] descriptions, Test[] tests, Resource[] resources) throws ExecPelpException,InvalidEngineException,AuthorizationException;

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
     * Get the list of subjects for current objects, that are currently active in PeLP
     * @return List of subjects objects
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not authenticated
     */
    public Subject[] getUserSubjects() throws ExecPelpException,InvalidEngineException,AuthorizationException;
      
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
     * @param subject Subject code
     * @param activityIndex Activity Index
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getAllClassroomDeliverSummary(String semester,String subject,int activityIndex,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
     * Get a detailed information for all delivers in a given classroom
     * @param semester Semester code
     * @param subject Subject code
     * @param activityIndex Activity Index
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getAllClassroomDeliverDetails(String semester,String subject,int activityIndex,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException; 
    
    /**
     * Get a summarized information for last deliver of each user in a given classroom
     * @param semester Semester code
     * @param subject Subject code
     * @param activityIndex Activity Index
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverSummary[] getLastClassroomDeliverSummary(String semester,String subject,int activityIndex,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException;
    
    /**
     * Get a detailed information for last deliver of each user in a given classroom
     * @param semester Semester code
     * @param subject Subject code
     * @param activityIndex Activity Index
     * @param classIndex Classroom index
     * @return Array of Object with summary information of the delivers
     * @throws ExecPelpException if some error accurs during process execution
     * @throws InvalidEngineException if the engine is not properly initialized
     * @throws AuthorizationException if user is not a teacher of this classroom
     */
    public DeliverDetail[] getLastClassroomDeliverDetails(String semester,String subject,int activityIndex,int classIndex) throws ExecPelpException,InvalidEngineException,AuthorizationException; 
}
