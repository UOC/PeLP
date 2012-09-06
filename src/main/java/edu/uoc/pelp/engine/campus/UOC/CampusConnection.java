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
package edu.uoc.pelp.engine.campus.UOC;

import edu.uoc.pelp.engine.campus.UOC.ws.WsLibBO;
import edu.uoc.pelp.engine.campus.*;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.serveis.gat.expedient.model.ExpedientVO;
import edu.uoc.serveis.gat.expedient.service.ExpedientService;
import edu.uoc.serveis.gat.matricula.service.MatriculaService;
import java.util.ArrayList;
import net.opentrends.remoteinterface.auth.Auth;
import net.opentrends.remoteinterface.auth.SessionContext;
import org.apache.log4j.Logger;

/**
 * Implements the campus access for the Universitat Oberta de Catalunya (UOC).
 * @author Xavier Baró
 */
public class CampusConnection implements ICampusConnection{
    
    private String sesion;
    private UserID userID;

    private static final Logger log = Logger.getLogger(CampusConnection.class);

    public CampusConnection(String sesion) {
            super();
            this.sesion = sesion;
    }

    public boolean isUserAuthenticated() throws AuthPelpException {
    	boolean authenticated = false;
    	try {
    		Auth authService = WsLibBO.getAuthServiceInstance();
    		authenticated = authService.isUserAuthenticated( sesion );
    	} catch ( Exception e){
    		throw new AuthPelpException("Authentication process failed");
    	}        
    	return authenticated;
    }

    public IUserID getUserID() throws AuthPelpException {
        IUserID userId;  
    	try {
    		Auth authService = WsLibBO.getAuthServiceInstance();
            final SessionContext sessionContext = authService.getContextBySessionId(sesion);
            if ( sessionContext == null ) {
                log.error("Error al obtener la SessionContext de la sesion: " + sesion);
                throw new Exception("Error al obtener la SessionContext de la sesion: " + sesion);
            }
            userId = new UserID( String.valueOf(sessionContext.getIdp()) );
           
    	} catch ( Exception e){
    		throw new AuthPelpException("Authentication process failed");
    	} 
    	 return userId;
    }

    public ISubjectID[] getUserSubjects(ITimePeriod timePeriod) throws AuthPelpException {
    	ArrayList<SubjectID> subjects=null;
    	if( userID == null ) {
    		userID = (UserID) getUserID();
    	}
        
    	try {
            int idp = Integer.valueOf( userID.idp );
            
            ExpedientService expedientService = WsLibBO.getExpedientServiceInstance();
            ExpedientVO[] expedientes = expedientService.getExpedientsByEstudiant( idp );
            MatriculaService matriculaService = WsLibBO.getMatriculaServiceInstance();
            // TODO semester?
            String semester = "";
            for (ExpedientVO expedient : expedientes) {
                int numExpedient = expedient.getNumExpedient();
/*                AssignaturaMatriculadaDocenciaVO[] asignaturas = matriculaService.getAssignaturesDocenciaMatriculadesEstudiant(idp, semester);

                for (AssignaturaMatriculadaDocenciaVO assignaturaMatriculadaDocencia : asignaturas) {
                        AssignaturaReduidaVO asignatura;
                        asignatura = assignaturaMatriculadaDocencia.getAssignatura();
                        Semester sem = new Semester(semester);
                        SubjectID subID = new SubjectID(asignatura.getCodAssignatura(), sem);
                        subjects.add(subID);
                }
*/            }	
        // TODO: There is an error of incompatible data types...
        throw new UnsupportedOperationException("Not supported yet."); 
    	} catch (Exception e) {
            e.printStackTrace();
        }
        
    	SubjectID[] subs=new SubjectID[subjects.size()];
        return subjects.toArray(subs); 
    }

    public IClassroomID[] getUserClassrooms(ISubjectID subject) throws AuthPelpException {
        return getUserClassrooms(null);
    }
    
    public IClassroomID[] getUserClassrooms(UserRoles userRole,ISubjectID subject) throws AuthPelpException {
        // Check if the user is authenticated
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("Authentication is required");
        }
        
        // TODO: Retorna la llista d'aules de l'usuari actual, filtrades per rol
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public IClassroomID[] getSubjectClassrooms(ISubjectID subject, UserRoles userRole) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRole(UserRoles role, ISubjectID subject, IUserID user) {
        /* 
         * TODO: Comprova si l'usuari donat té el rol indicat per aquest assignatura.
         * La equivalencia de rols son:
         *         
         */
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRole(UserRoles role, ISubjectID subject) throws AuthPelpException {
        return isRole(role,subject,getUserID());
    }

    public boolean isRole(UserRoles role, IClassroomID classroom, IUserID user) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRole(UserRoles role, IClassroomID classroom) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IUserID[] getRolePersons(UserRoles role, ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IUserID[] getRolePersons(UserRoles role, IClassroomID classroom) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasLabSubjects(ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ISubjectID[] getLabSubjects(ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasEquivalentSubjects(ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ISubjectID[] getEquivalentSubjects(ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCampusConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Subject getSubjectData(ISubjectID subjectID) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Classroom getClassroomData(IClassroomID classroomID) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Person getUserData(IUserID userID) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Person getUserData() throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ISubjectID[] getUserSubjects(UserRoles userRole,ITimePeriod timePeriod) throws AuthPelpException {
        // TODO Auto-generated method stub
        return null;
    }

    public ITimePeriod[] getPeriods() {
        // Accedir a una taula de la Base de Dades
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ITimePeriod[] getActivePeriods() {
        // Accedir a una taula de la Base de Dades
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
