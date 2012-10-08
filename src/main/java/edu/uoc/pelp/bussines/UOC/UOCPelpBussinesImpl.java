package edu.uoc.pelp.bussines.UOC;

import edu.uoc.pelp.bussines.PelpBussinesImpl;
import edu.uoc.pelp.bussines.UOC.exception.InvalidSessionException;
import edu.uoc.pelp.bussines.exception.AuthorizationException;
import edu.uoc.pelp.bussines.exception.InvalidCampusConnectionException;
import edu.uoc.pelp.bussines.exception.InvalidEngineException;
import edu.uoc.pelp.bussines.vo.*;
import edu.uoc.pelp.engine.campus.UOC.CampusConnection;
import edu.uoc.pelp.exception.ExecPelpException;
import java.util.Date;

/**
 * Implementation of the UOC Bussines class
 * @author Xavier Baró
 */
public class UOCPelpBussinesImpl extends PelpBussinesImpl implements UOCPelpBussines {
    /**
     * Last used campus session
     */
    protected String _campusSession=null;
    
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
    public boolean addNewActivity(String semester, String subject, Date start, Date end, String progLangCode, int maxDelivers, MultilingualText[] descriptions, Test[] tests, Resource[] resources) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverSummary getUserDeliverSummary(String semester, String subject, int activityIndex, int deliverIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail getUserDeliverDetails(String semester, String subject, int activityIndex, int deliverIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public DeliverSummary[] getUserDeliverSummary(String semester, String subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail[] getUserDeliverDetails(String semester, String subject, int activityIndex) throws ExecPelpException, InvalidEngineException, AuthorizationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail addDeliver(String semester, String subject, int activityIndex, DeliverFile[] files) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Resource[] getActivityResources(String semester, String subject, int activityIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Subject[] getUserSubjects() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Classroom[] getUserClassrooms(String semester, String subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Activity[] getSubjectActivities(String semester, String subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Activity getActivityInformation(String semester, String subject, int activityIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverSummary[] getAllClassroomDeliverSummary(String semester, String subject, int activityIndex, int classIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail[] getAllClassroomDeliverDetails(String semester, String subject, int activityIndex, int classIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverSummary[] getLastClassroomDeliverSummary(String semester, String subject, int activityIndex, int classIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DeliverDetail[] getLastClassroomDeliverDetails(String semester, String subject, int activityIndex, int classIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
