package edu.uoc.pelp.bussines.UOC;

import edu.uoc.pelp.bussines.PelpBussinesImpl;
import edu.uoc.pelp.bussines.UOC.exception.InvalidSessionException;
import edu.uoc.pelp.bussines.UOC.vo.UOCClassroom;
import edu.uoc.pelp.bussines.UOC.vo.UOCSubject;
import edu.uoc.pelp.bussines.exception.AuthorizationException;
import edu.uoc.pelp.bussines.exception.InvalidCampusConnectionException;
import edu.uoc.pelp.bussines.exception.InvalidEngineException;
import edu.uoc.pelp.bussines.vo.Activity;
import edu.uoc.pelp.bussines.vo.Classroom;
import edu.uoc.pelp.bussines.vo.DeliverDetail;
import edu.uoc.pelp.bussines.vo.DeliverFile;
import edu.uoc.pelp.bussines.vo.DeliverSummary;
import edu.uoc.pelp.bussines.vo.MultilingualText;
import edu.uoc.pelp.bussines.vo.MultilingualTextArray;
import edu.uoc.pelp.bussines.vo.Resource;
import edu.uoc.pelp.bussines.vo.Subject;
import edu.uoc.pelp.bussines.vo.Test;
import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.activity.ActivityTest;
import edu.uoc.pelp.engine.campus.ICampusConnection;
import edu.uoc.pelp.engine.campus.IClassroomID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.engine.campus.UOC.CampusConnection;
import edu.uoc.pelp.engine.campus.UOC.ClassroomID;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.engine.campus.UOC.UserID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.exception.InvalidActivityPelpException;
import edu.uoc.pelp.exception.InvalidSubjectPelpException;
import edu.uoc.pelp.exception.InvalidTimePeriodPelpException;
import edu.uoc.pelp.exception.PelpException;
import java.io.File;
import java.util.Date;
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
    public DeliverSummary[] getAllClassroomDeliverSummary(String semester, String activitySubject, int activityIndex, String subject, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject activitySubjectObj=new UOCSubject(semester,activitySubject);
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getAllClassroomDeliverSummary(activitySubjectObj,activityIndex,subjectObj,classIndex);
    }

    @Override
    public DeliverDetail[] getAllClassroomDeliverDetails(String semester, String activitySubject, int activityIndex, String subject, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject activitySubjectObj=new UOCSubject(semester,activitySubject);
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getAllClassroomDeliverDetails(activitySubjectObj,activityIndex,subjectObj,classIndex);
    }

    @Override
    public DeliverSummary[] getLastClassroomDeliverSummary(String semester, String activitySubject, int activityIndex, String subject, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject activitySubjectObj=new UOCSubject(semester,activitySubject);
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getLastClassroomDeliverSummary(activitySubjectObj,activityIndex,subjectObj,classIndex);
    }

    @Override
    public DeliverDetail[] getLastClassroomDeliverDetails(String semester, String activitySubject, int activityIndex, String subject, int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject activitySubjectObj=new UOCSubject(semester,activitySubject);
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return getLastClassroomDeliverDetails(activitySubjectObj,activityIndex,subjectObj,classIndex);
    }

    @Override
    public Activity addActivity(String semester, String subject, Date start, Date end, Integer maxDelivers, String progLangCode, MultilingualTextArray activityDescriptions, Test[] activityTests, MultilingualTextArray[] testDescriptions) throws InvalidEngineException, AuthorizationException, ExecPelpException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return addActivity(subjectObj, start, end, maxDelivers, progLangCode, activityDescriptions, activityTests, testDescriptions);
    }

    @Override
    public Activity addActivity(String semester, String subject, Date start, Date end, Integer maxDelivers, String progLangCode, MultilingualTextArray activityDescriptions) throws AuthorizationException, InvalidEngineException, ExecPelpException {
        UOCSubject subjectObj=new UOCSubject(semester,subject);
        return addActivity(subjectObj, start, end, maxDelivers, progLangCode, activityDescriptions);
    }
    
    @Override
    public Activity addActivity(Subject subject, Date start, Date end, Integer maxDelivers,String progLangCode, MultilingualTextArray activityDescriptions) throws AuthorizationException, InvalidEngineException, ExecPelpException {
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
            edu.uoc.pelp.engine.campus.UOC.Semester uocSemester=(edu.uoc.pelp.engine.campus.UOC.Semester) getSemester(subject);
            subjectID=new SubjectID(sub.getSubjectCode(),uocSemester);
        }
        return subjectID;
    }
    
    @Override
    public ITimePeriod getSemester(Subject subject) {
        edu.uoc.pelp.engine.campus.UOC.Semester semester=null;
        if(subject instanceof UOCSubject) {
            UOCSubject sub=(UOCSubject)subject;
            semester=new edu.uoc.pelp.engine.campus.UOC.Semester(sub.getSemesterCode());
        }
        return semester;
    }
 
    @Override
    public UOCClassroom getClassroom(IClassroomID classroomID) {
        UOCClassroom classroom=null;
        if(classroomID instanceof ClassroomID) {
            ClassroomID classID=(ClassroomID) classroomID;
            UOCSubject subject=(UOCSubject) getSubject(classID.getSubject());          
            classroom=new UOCClassroom(subject,classID.getClassIdx());
        }
        
        return classroom;
    }
    
    @Override
    public IClassroomID getClassroomID(Classroom classroom) {
        ClassroomID classroomID=null;
        if(classroom instanceof UOCClassroom) {
            UOCClassroom classUOC=(UOCClassroom)classroom;
            SubjectID subjectID=(SubjectID) getSubjectID(classUOC.getSubject());
            classroomID=new ClassroomID(subjectID,classUOC.getIndex());
        }
        return classroomID;
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
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(subject==null) {
            return null;
        }
        
        UOCSubject subjectUOC=null;
        if(subject instanceof UOCSubject) {
            subjectUOC=(UOCSubject)subject;
        }
        if(subjectUOC==null) {
            return null;
        }
        // Creat the activity identifier
        SubjectID subjectID=new SubjectID(subjectUOC.getSubjectCode(),new Semester(subjectUOC.getSemesterCode()));
        ActivityID activityID=new ActivityID(subjectID,activityIndex);
        
        // Get the delivers
        Deliver[] delivers=null;
        try {
            delivers=_engine.getUserActivityDelivers(activityID);
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        // Build the return array
        DeliverDetail[] retArray=new DeliverDetail[delivers.length];
        for(int i=0;i<retArray.length;i++) {
            try {
                DeliverResults deliverResults=_engine.getDeliverResults(delivers[i].getID());
                retArray[i]=getDeliverDetailObject(delivers[i],deliverResults);
            } catch (InvalidActivityPelpException ex) {
                throw new ExecPelpException(ex.getMessage());
            } catch (InvalidSubjectPelpException ex) {
                throw new ExecPelpException(ex.getMessage());
            } catch (AuthPelpException ex) {
                throw new AuthorizationException(ex.getMessage());
            }
        }
        
        return retArray;
    }
    
    @Override
    public DeliverDetail addDeliver(Subject subject, int activityIndex, DeliverFile[] files) throws ExecPelpException, InvalidEngineException, AuthorizationException {

        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(subject==null || files==null) {
            return null;
        }
        UOCSubject subjectUOC=null;
        if(subject instanceof UOCSubject) {
            subjectUOC=(UOCSubject)subject;
        }
        if(subjectUOC==null) {
            return null;
        }
        // Creat the activity identifier
        SubjectID subjectID=new SubjectID(subjectUOC.getSubjectCode(),new Semester(subjectUOC.getSemesterCode()));
        ActivityID activityID=new ActivityID(subjectID,activityIndex);
        
        // Get the rootPath for files. All files must have the same rootPath
        File rootPath=null;
        for(DeliverFile file:files) {
            File fileRootPath=null;
            if(file.getRootPath()!=null) {
                fileRootPath=new File(file.getRootPath());
            }
            if(rootPath!=null) {
                if(fileRootPath!=null && !rootPath.equals(fileRootPath)) {
                    throw new ExecPelpException("Added files with different root paths");
                }
            } else {
                rootPath=fileRootPath;
            }
        }
        
        // If no root path is given, estimate it
        if(rootPath==null) {
            String commonPath=getMaxCommonPath(files);
            if(commonPath==null) {
                throw new ExecPelpException("Root path cannot be extracted from files");
            }
            rootPath=new File(commonPath);
        }
        Deliver deliver=new Deliver(rootPath);

        // Add the files to the deliver
        for(DeliverFile file:files) {
            // Check that file exists
            if(new File(file.getAbsolutePath()).exists()==false){
                throw new ExecPelpException("Input file <" + file.getRelativePath() + "> does not exist");
            }
            deliver.addFile(getDeliverFile(file));
        }
        // Perform the deliver
        DeliverResults deliverResults;
        try {
            deliverResults = _engine.createNewDeliver(deliver, activityID);
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        } catch (InvalidActivityPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        }
        
        DeliverDetail deliverDetail;
        try {
            deliverDetail = getDeliverDetailObject(deliver,deliverResults);
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        } catch (InvalidActivityPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        } catch (InvalidSubjectPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        }
        
        return deliverDetail;
    }

    @Override
    public Resource[] getActivityResources(Subject subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Classroom[] getUserClassrooms(Subject subject) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject sub;
        if(subject instanceof UOCSubject) {
            sub=(UOCSubject)subject;
        } else {
            sub=new UOCSubject(subject);
        }
        if(sub==null || sub.getSemesterCode()==null || sub.getSubjectCode()==null) {
            return null;
        }
        return getUserClassrooms(sub);
    }

    @Override
    public UOCClassroom[] getUserClassrooms(UOCSubject subject) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }

        // Check the parameters
        if(subject==null) {
            return null;
        }

        // Get the subject ID
        SubjectID subjectID=(SubjectID) getSubjectID(subject);
        if(subjectID==null) {
            return null;
        }
        
        // Get the list of classrooms
        edu.uoc.pelp.engine.campus.Classroom[] classList;
        
        try {
            classList = _engine.getSubjectClassrooms(subjectID);
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        // Create the output list
        if(classList==null) {
            return null;
        }
        UOCClassroom[] retList=new UOCClassroom[classList.length];
        for(int i=0;i<retList.length;i++) {
            retList[i]=getClassroom(classList[i].getClassroomID());
            retList[i].getSubject().setDescription(classList[i].getSubjectRef().getDescription());
            retList[i].getSubject().setShortName(classList[i].getSubjectRef().getShortName());
        }
        
        return retList;
    }

    @Override
    public Activity[] getSubjectActivities(Subject subject) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }

        // Check the parameters
        if(subject==null) {
            return null;
        }
        
        // Get the subject ID
        SubjectID subjectID=(SubjectID) getSubjectID(subject);
        
        // Get the activities
        edu.uoc.pelp.engine.activity.Activity[] activityList=null;
        try {
            
            // Search activities depending on user role
            if(_engine.isTeacher(subjectID)) {
                // In case of teachers, active and inactive activities are provided
                activityList= _engine.getSubjectActivity(subjectID, false);
            } else if(_engine.isStudent(subjectID)) {
                // If user is an student, only active activities are provided. 
                activityList=_engine.getSubjectActivity(subjectID, true);
            } else {
                // Other users cannot access to the activities of this subject
                throw new AuthorizationException("Only students and teachers can access to this information");
            }
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return getActivityList(activityList);
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
    public DeliverSummary[] getAllClassroomDeliverSummary(Subject activitySubject, int activityIndex, Subject subject,int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(activitySubject==null || activityIndex<0 || subject==null || classIndex<0) {
            return null;
        }
        
        SubjectID activitySubjectID=(SubjectID) getSubjectID(activitySubject);
        ActivityID activityID=new ActivityID(activitySubjectID,activityIndex);
        SubjectID subjectID=(SubjectID) getSubjectID(subject);
        ClassroomID classroomID=new ClassroomID(subjectID,classIndex);
        
        DeliverSummary[] retList=null;
        try {
            // Get the delivers
            Deliver[] deliverList =_engine.getClassroomDelivers(classroomID, activityID);
            if(deliverList==null) {
                return null;
            }
            // Create the output list
            retList=new DeliverSummary[deliverList.length];
            for(int i=0;i<deliverList.length;i++) {
                DeliverResults deliverResults=_engine.getDeliverResults(deliverList[i].getID());
                retList[i]=getDeliverSummaryObject(deliverList[i], deliverResults);
            }
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return retList;
    }

    @Override
    public DeliverDetail[] getAllClassroomDeliverDetails(Subject activitySubject, int activityIndex, Subject subject,int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(activitySubject==null || activityIndex<0 || subject==null || classIndex<0) {
            return null;
        }
        
        SubjectID activitySubjectID=(SubjectID) getSubjectID(activitySubject);
        ActivityID activityID=new ActivityID(activitySubjectID,activityIndex);
        SubjectID subjectID=(SubjectID) getSubjectID(subject);
        ClassroomID classroomID=new ClassroomID(subjectID,classIndex);
        
        DeliverDetail[] retList=null;
        try {
            // Get the delivers
            Deliver[] deliverList =_engine.getClassroomDelivers(classroomID, activityID);
            if(deliverList==null) {
                return null;
            }
            // Create the output list
            retList=new DeliverDetail[deliverList.length];
            for(int i=0;i<deliverList.length;i++) {
                DeliverResults deliverResults=_engine.getDeliverResults(deliverList[i].getID());
                retList[i]=getDeliverDetailObject(deliverList[i], deliverResults);
            }
        } catch (InvalidActivityPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        } catch (InvalidSubjectPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return retList;
    }

    @Override
    public DeliverSummary[] getLastClassroomDeliverSummary(Subject activitySubject, int activityIndex, Subject subject,int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(activitySubject==null || activityIndex<0 || subject==null || classIndex<0) {
            return null;
        }
        
        SubjectID activitySubjectID=(SubjectID) getSubjectID(activitySubject);
        ActivityID activityID=new ActivityID(activitySubjectID,activityIndex);
        SubjectID subjectID=(SubjectID) getSubjectID(subject);
        ClassroomID classroomID=new ClassroomID(subjectID,classIndex);
        
        DeliverSummary[] retList=null;
        try {
            // Get the delivers
            Deliver[] deliverList =_engine.getClassroomLastDelivers(classroomID, activityID);
            if(deliverList==null) {
                return null;
            }
            // Create the output list
            retList=new DeliverSummary[deliverList.length];
            for(int i=0;i<deliverList.length;i++) {
                DeliverResults deliverResults=_engine.getDeliverResults(deliverList[i].getID());
                retList[i]=getDeliverSummaryObject(deliverList[i], deliverResults);
            }
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return retList;
    }

    @Override
    public DeliverDetail[] getLastClassroomDeliverDetails(Subject activitySubject, int activityIndex, Subject subject,int classIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(activitySubject==null || activityIndex<0 || subject==null || classIndex<0) {
            return null;
        }
        
        SubjectID activitySubjectID=(SubjectID) getSubjectID(activitySubject);
        ActivityID activityID=new ActivityID(activitySubjectID,activityIndex);
        SubjectID subjectID=(SubjectID) getSubjectID(subject);
        ClassroomID classroomID=new ClassroomID(subjectID,classIndex);
        
        DeliverDetail[] retList=null;
        try {
            // Get the delivers
            Deliver[] deliverList =_engine.getClassroomLastDelivers(classroomID, activityID);
            if(deliverList==null) {
                return null;
            }
            // Create the output list
            retList=new DeliverDetail[deliverList.length];
            for(int i=0;i<deliverList.length;i++) {
                DeliverResults deliverResults=_engine.getDeliverResults(deliverList[i].getID());
                retList[i]=getDeliverDetailObject(deliverList[i], deliverResults);
            }
        } catch (InvalidActivityPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        } catch (InvalidSubjectPelpException ex) {
            throw new ExecPelpException(ex.getMessage());
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return retList;
    }

    @Override
    public Activity addActivity(Subject subject, Date start, Date end, Integer maxDelivers, String progLangCode, MultilingualTextArray activityDescriptions, Test[] activityTests, MultilingualTextArray[] testDescriptions) throws AuthorizationException, InvalidEngineException,ExecPelpException {
        ActivityID newID;
        Activity retObj;
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(subject==null || activityDescriptions==null || (activityDescriptions!=null && activityDescriptions.size()<=0)) {
            return null;
        }
        
        // Create the high level objects
        edu.uoc.pelp.engine.activity.Activity activity=new edu.uoc.pelp.engine.activity.Activity();
        activity.setStart(start);
        activity.setEnd(end);
        activity.setLanguage(progLangCode);
        activity.setMaxDelivers(maxDelivers);
        for(MultilingualText text:activityDescriptions.getArray()) {
            activity.setDescription(text.getLanguage(), text.getText());
        }
        try {
            // Create the test objects
            ActivityTest[] newTests=null;
            if(activityTests!=null && activityTests.length>0) {
                newTests=getTestDataArray(activityTests,testDescriptions);
                if(newTests==null) {
                    throw new ExecPelpException("A null activiy object has been returned by the engine");
                }
                if(newTests.length!=activityTests.length) {
                    throw new ExecPelpException("The returned number of tests diferd from added tests");
                }
            }
            
            // Create the new activity
            newID=_engine.addActivity(getSubjectID(subject), activity, newTests);
            if(newID==null) {
                throw new ExecPelpException("A null activiy ID has been returned by the engine");
            }
            
            // Get the added activity
            edu.uoc.pelp.engine.activity.Activity newActivity = _engine.getActivity(newID);
            if(newActivity==null) {
                throw new ExecPelpException("A null activiy object has been returned by the engine");
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
            retVal=_engine.activateSubject(sub.getSemesterCode(), sub.getSubjectCode());
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
            retVal=_engine.deactivateSubject(sub.getSemesterCode(), sub.getSubjectCode());
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
            retVal=_engine.removeSubjectActivationRegister(sub.getSemesterCode(), sub.getSubjectCode());
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        return retVal;
    }

    @Override
    public UOCSubject[] getUserSubjects() throws ExecPelpException, InvalidEngineException, AuthorizationException {
        UOCSubject[] retList=null;
        
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        try {
            // Get acive subjets for current user
            edu.uoc.pelp.engine.campus.Subject[] userSubjects = _engine.getActiveSubjects();
            
            // Create the output list
            if(userSubjects!=null) {
                retList=new UOCSubject[userSubjects.length];
                for(int i=0;i<userSubjects.length;i++) {
                    SubjectID subjectID=(SubjectID)userSubjects[i].getID();
                    retList[i]=new UOCSubject(subjectID.getSemester().getID(),subjectID.getCode());
                    retList[i].setShortName(userSubjects[i].getShortName());
                    retList[i].setDescription(userSubjects[i].getDescription());
                }
            }
        } catch (AuthPelpException ex) {
            //throw new AuthorizationException(ex.getMessage());
        }
        
        return retList;
    }
    @Override
    public Boolean isTeacher(UOCSubject subject) throws AuthPelpException{
    	
    	SubjectID subjectID=(SubjectID) getSubjectID(subject);
    	return _engine.isTeacher(subjectID);
    	 
    }
    @Override
    public Boolean isStudent(UOCSubject subject) throws AuthPelpException{
    	SubjectID subjectID=(SubjectID) getSubjectID(subject);
    	return _engine.isStudent(subjectID);
    }
    @Override
    public void logout() throws PelpException{
    	//this.setCampusSession("logout");
    	System.out.println("logout");
    }

    @Override
    public DeliverSummary getUserDeliverSummary(UserID userID, String semester, String subject, int activityIndex, int deliverIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail getUserDeliverDetails(UserID userID, String semester, String subject, int activityIndex, int deliverIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverSummary[] getUserDeliverSummary(UserID userID, String semester, String subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail[] getUserDeliverDetails(UserID userID, String semester, String subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        // Check the engine
        if(_engine==null) {
            throw new InvalidEngineException("Uninitialized engine.");
        }
        
        // Check the parameters
        if(semester==null || subject==null) {
            return null;
        }
                
        // Creat the activity identifier
        SubjectID subjectID=new SubjectID(subject,new Semester(semester));
        ActivityID activityID=new ActivityID(subjectID,activityIndex);
        
        // Get the delivers
        Deliver[] delivers=null;
        try {
            delivers=_engine.getActivityDelivers(userID, activityID);
        } catch (AuthPelpException ex) {
            throw new AuthorizationException(ex.getMessage());
        }
        
        // Build the return array
        DeliverDetail[] retArray=new DeliverDetail[delivers.length];
        for(int i=0;i<retArray.length;i++) {
            try {
                DeliverResults deliverResults=_engine.getDeliverResults(delivers[i].getID());
                retArray[i]=getDeliverDetailObject(delivers[i],deliverResults);
            } catch (InvalidActivityPelpException ex) {
                throw new ExecPelpException(ex.getMessage());
            } catch (InvalidSubjectPelpException ex) {
                throw new ExecPelpException(ex.getMessage());
            } catch (AuthPelpException ex) {
                throw new AuthorizationException(ex.getMessage());
            }
        }
        
        return retArray;
    }
}
