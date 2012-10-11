package edu.uoc.pelp.bussines.UOC;

import edu.uoc.pelp.bussines.PelpBussinesImpl;
import edu.uoc.pelp.bussines.UOC.exception.InvalidSessionException;
import edu.uoc.pelp.bussines.UOC.vo.UOCSubject;
import edu.uoc.pelp.bussines.exception.AuthorizationException;
import edu.uoc.pelp.bussines.exception.InvalidCampusConnectionException;
import edu.uoc.pelp.bussines.exception.InvalidEngineException;
import edu.uoc.pelp.bussines.vo.*;
import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.activity.ActivityTest;
import edu.uoc.pelp.engine.campus.ICampusConnection;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.engine.campus.UOC.CampusConnection;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.exception.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;

/**
 * Implementation of the UOC Bussines class
 * @author Xavier Baró
 */
public class UOCPelpBussinesImpl extends PelpBussinesImpl implements UOCPelpBussines {
    /**
     * Last used campus session
     */
    protected String _campusSession=null;
    
    /**
     * Default constructor. If all components are instantiated the engine is started.
     */
    public UOCPelpBussinesImpl() throws InvalidEngineException {
        super();
    }    
    
    /**
     * Constructor with all the components. If all components are instantiated the engine is started.
     * @param campusConnection Connection to the campus
     * @param sessionFactory Hibernate session factory
     * @param configObject Configuration object
     * @throws InvalidEngineException if there is some problem starting the engine
     */
    public UOCPelpBussinesImpl(ICampusConnection campusConnection,SessionFactory sessionFactory,IPelpConfiguration configObject) throws InvalidEngineException {
        super(campusConnection, sessionFactory, configObject);
    }
    
    /**
     * Constructor with all the components. If all components are instantiated the engine is started.
     * @param campusSession Connection to the campus
     * @param sessionFactory Hibernate session factory
     * @param configObject Configuration object
     * @throws InvalidEngineException if there is some problem starting the engine
     */
    public UOCPelpBussinesImpl(String campusSession,SessionFactory sessionFactory,IPelpConfiguration configObject) throws InvalidEngineException {
        super(new CampusConnection(campusSession), sessionFactory, configObject);
    }
    
    @Override
    public void setCampusSession(String session) throws InvalidSessionException {
        // With invalid campus session the campus connection become innaccessible
        if(session==null) {
            _campusSession=null;
            _engine=null;
            throw new InvalidSessionException("Null campus session"); 
        }
        if(_campusSession==null || _campusConnection==null || !_campusSession.equals(_campusSession)) {
            // Store new campus session
            _campusSession=session;
            try {
                setCampusConnection(new CampusConnection(session));
            } catch (InvalidCampusConnectionException ex) {
                throw new InvalidSessionException("Invalid campus connection exception when used given session connection."); 
            }
        }
    }

    @Override
    public DeliverSummary getUserDeliverSummary(String semester, String subject, int activityIndex, int deliverIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getUserDeliverSummary(subjectObj,activityIndex,deliverIndex);
    }

    @Override
    public DeliverDetail getUserDeliverDetails(String semester, String subject, int activityIndex, int deliverIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getUserDeliverDetails(subjectObj,activityIndex,deliverIndex);
    }
    
    @Override
    public DeliverSummary[] getUserDeliverSummary(String semester, String subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getUserDeliverSummary(subjectObj, activityIndex);
    }

    @Override
    public DeliverDetail[] getUserDeliverDetails(String semester, String subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getUserDeliverDetails(subjectObj, activityIndex);
    }

    @Override
    public DeliverDetail addDeliver(String semester, String subject, int activityIndex, DeliverFile[] files) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return addDeliver(subjectObj, activityIndex,files);
    }

    @Override
    public Resource[] getActivityResources(String semester, String subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getActivityResources(subjectObj, activityIndex);
    }

    @Override
    public Classroom[] getUserClassrooms(String semester, String subject) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getUserClassrooms(subjectObj);
    }

    @Override
    public Activity[] getSubjectActivities(String semester, String subject) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getSubjectActivities(subjectObj);
    }

    @Override
    public Activity getActivityInformation(String semester, String subject, int activityIndex) throws ExecPelpException, InvalidEngineException,AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getActivityInformation(subjectObj,activityIndex);
    }

    @Override
    public DeliverSummary[] getAllClassroomDeliverSummary(String semester, String subject, int activityIndex, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getAllClassroomDeliverSummary(subjectObj,activityIndex,classIndex);
    }

    @Override
    public DeliverDetail[] getAllClassroomDeliverDetails(String semester, String subject, int activityIndex, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getAllClassroomDeliverDetails(subjectObj,activityIndex,classIndex);
    }

    @Override
    public DeliverSummary[] getLastClassroomDeliverSummary(String semester, String subject, int activityIndex, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getLastClassroomDeliverSummary(subjectObj,activityIndex,classIndex);
    }

    @Override
    public DeliverDetail[] getLastClassroomDeliverDetails(String semester, String subject, int activityIndex, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getLastClassroomDeliverDetails(subjectObj,activityIndex,classIndex);
    }

    @Override
    public Activity addActivity(String semester, String subject, Date start, Date end, Integer maxDelivers, String progLangCode, MultilingualText[] activityDescriptions, Test[] activityTests, MultilingualText[] testDescriptions) throws InvalidEngineException, AuthorizationException, ExecPelpException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return addActivity(subjectObj, start, end, maxDelivers, progLangCode, activityDescriptions, activityTests, testDescriptions);
    }

    @Override
    public Activity addActivity(String semester, String subject, Date start, Date end, Integer maxDelivers, String progLangCode, MultilingualText[] activityDescriptions) throws AuthorizationException, InvalidEngineException, ExecPelpException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return addActivity(subjectObj, start, end, maxDelivers, progLangCode, activityDescriptions);
    }
    
    @Override
    public Activity addActivity(Subject subject, Date start, Date end, Integer maxDelivers,String progLangCode, MultilingualText[] activityDescriptions) throws AuthorizationException, InvalidEngineException, ExecPelpException {
        return addActivity(subject, start, end, maxDelivers, progLangCode, activityDescriptions,null,null);
    }

    @Override
    public boolean activateSubject(String semester, String subject) throws AuthorizationException, InvalidEngineException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return activateSubject(subjectObj);
    }

    @Override
    public boolean deactivateSubject(String semester, String subject) throws AuthorizationException, InvalidEngineException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return deactivateSubject(subjectObj);
    }

    @Override
    public boolean removeSubjectActivationRegister(String semester, String subject) throws AuthorizationException, InvalidEngineException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return removeSubjectActivationRegister(subjectObj);
    }

    @Override
    public boolean addSemester(String semester, Date start, Date end) throws InvalidTimePeriodPelpException, AuthorizationException, InvalidEngineException {
        boolean retValue=false;
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        try {
            retValue=_engine.addSemester(semester, start, end);
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        } catch (InvalidTimePeriodPelpException ex) {
            throw new InvalidTimePeriodPelpException(ex.getMessage());
        }
        
        return retValue;
    }

    @Override
    public boolean updateSemester(String semester, Date start, Date end) throws AuthorizationException, InvalidEngineException, InvalidTimePeriodPelpException {
        boolean retValue=false;
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        try {
            retValue=_engine.updateSemester(semester, start, end);
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        } catch (InvalidTimePeriodPelpException ex) {
            throw new InvalidTimePeriodPelpException(ex.getMessage());
        }
        
        return retValue;
    }

    @Override
    public boolean removeSemester(String semester) throws AuthorizationException, InvalidEngineException {
        boolean retValue=false;
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        try {
            retValue=_engine.removeSemester(semester);
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        } 
        
        return retValue;
    }
    
    @Override
    public Subject getSubject(ISubjectID subjectID) {
        Subject subject=null;
        if(subjectID instanceof SubjectID) {
            SubjectID subID=(SubjectID) subjectID;
            subject=new UOCSubject(subID.getSemester().getID(), subID.getCode());
        }
        return subject;
    }
    
    @Override
    public ISubjectID getSubjectID(Subject subject) {
        SubjectID subjectID=null;
        if(subject instanceof UOCSubject) {
            UOCSubject sub=(UOCSubject)subject;
            edu.uoc.pelp.engine.campus.UOC.Semester UOCSemester=(edu.uoc.pelp.engine.campus.UOC.Semester) getSemester(subject);
            subjectID=new SubjectID(sub.getSubjectCode(),UOCSemester);
        }
        return subjectID;
    }
    
    @Override
    public ITimePeriod getSemester(Subject subject) {
        edu.uoc.pelp.engine.campus.UOC.Semester semester=null;
        if(subject instanceof UOCSubject) {
            UOCSubject sub=(UOCSubject)subject;
            semester=new edu.uoc.pelp.engine.campus.UOC.Semester(sub.getSemester());
        }
        return semester;
    }
 

    
    
    
    
    
    @Override
    public DeliverSummary getUserDeliverSummary(Subject subject, int activityIndex, int deliverIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail getUserDeliverDetails(Subject subject, int activityIndex, int deliverIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverSummary[] getUserDeliverSummary(Subject subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail[] getUserDeliverDetails(Subject subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public DeliverDetail addDeliver(Subject subject, int activityIndex, DeliverFile[] files) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Resource[] getActivityResources(Subject subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Classroom[] getUserClassrooms(Subject subject) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Activity[] getSubjectActivities(Subject subject) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Activity getActivityInformation(Subject subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(subject==null) {
            return null;
        }
        
        SubjectID subjectID=(SubjectID) getSubjectID(subject);
        ActivityID activityID=new ActivityID(subjectID,activityIndex);
        Activity activity=null;
        try {
            activity=getActivity(_engine.getActivity(activityID));
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        } catch (InvalidActivityPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        } catch (InvalidSubjectPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        }
        
        return activity;
    }

    @Override
    public DeliverSummary[] getAllClassroomDeliverSummary(Subject subject, int activityIndex, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail[] getAllClassroomDeliverDetails(Subject subject, int activityIndex, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverSummary[] getLastClassroomDeliverSummary(Subject subject, int activityIndex, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail[] getLastClassroomDeliverDetails(Subject subject, int activityIndex, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Activity addActivity(Subject subject, Date start, Date end, Integer maxDelivers, String progLangCode, MultilingualText[] activityDescriptions, Test[] activityTests, MultilingualText[] testDescriptions) throws AuthorizationException, InvalidEngineException,ExecPelpException {
        ActivityID newID;
        Activity retObj;
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(subject==null || activityDescriptions==null || (activityDescriptions!=null && activityDescriptions.length<=0)) {
            return null;
        }

        // Create the high level objects
        edu.uoc.pelp.engine.activity.Activity activity=new edu.uoc.pelp.engine.activity.Activity();
        activity.setStart(start);
        activity.setEnd(end);
        activity.setLanguage(progLangCode);
        activity.setMaxDelivers(maxDelivers);
        for(MultilingualText text:activityDescriptions) {
            activity.setDescription(text.getLanguage(), text.getText());
        }
        try {
            // Create the new activity
            newID=_engine.addActivity(getSubjectID(subject), activity, getTestDataArray(activityTests));
            if(newID==null) {
                throw new ExecPelpException("A null activiy ID has been returned by the engine");
            }
            
            // Get the added activity
            edu.uoc.pelp.engine.activity.Activity newActivity = _engine.getActivity(newID);
            if(newActivity==null) {
                throw new ExecPelpException("A null activiy object has been returned by the engine");
            }
            
            // Get the tests
            if(activityTests!=null && activityTests.length>0) {
                ActivityTest[] newTests=_engine.getActivityTests(newID);
                if(newTests==null) {
                    throw new ExecPelpException("A null activiy object has been returned by the engine");
                }
                if(newTests.length!=activityTests.length) {
                    throw new ExecPelpException("The returned number of tests diferd from added tests");
                }
            }
            
            // Create the output object
            retObj=getActivity(newActivity);
            
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        } catch (InvalidActivityPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        } catch (InvalidSubjectPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        } 
        
        return retObj;
    }

    @Override
    public boolean activateSubject(Subject subject) throws AuthorizationException, InvalidEngineException {
        boolean retVal=false;
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(subject==null || !(subject instanceof UOCSubject)) {
            return false;
        }
        UOCSubject sub=(UOCSubject) subject;
        try {
            retVal=_engine.activateSubject(sub.getSemester(), sub.getSubjectCode());
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return retVal;
    }

    @Override
    public boolean deactivateSubject(Subject subject) throws AuthorizationException, InvalidEngineException {
         boolean retVal=false;
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(subject==null || !(subject instanceof UOCSubject)) {
            return false;
        }
        UOCSubject sub=(UOCSubject) subject;
        try {
            retVal=_engine.deactivateSubject(sub.getSemester(), sub.getSubjectCode());
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return retVal;
    }

    @Override
    public boolean removeSubjectActivationRegister(Subject subject) throws AuthorizationException, InvalidEngineException {
         boolean retVal=false;
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(subject==null || !(subject instanceof UOCSubject)) {
            return false;
        }
        UOCSubject sub=(UOCSubject) subject;
        try {
            retVal=_engine.removeSubjectActivationRegister(sub.getSemester(), sub.getSubjectCode());
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return retVal;
    }

    @Override
    public Subject[] getUserSubjects() throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
