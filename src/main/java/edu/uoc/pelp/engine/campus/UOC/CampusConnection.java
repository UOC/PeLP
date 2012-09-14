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

import java.util.ArrayList;

import net.opentrends.remoteinterface.auth.Auth;
import net.opentrends.remoteinterface.auth.SessionContext;

import org.apache.log4j.Logger;

import edu.uoc.pelp.engine.campus.Classroom;
import edu.uoc.pelp.engine.campus.ICampusConnection;
import edu.uoc.pelp.engine.campus.IClassroomID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.engine.campus.Subject;
import edu.uoc.pelp.engine.campus.UserRoles;
import edu.uoc.pelp.engine.campus.UOC.ws.WsLibBO;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.serveis.exceptions.WSException;
import edu.uoc.serveis.gat.dadesacademiques.model.AnyAcademicVO;
import edu.uoc.serveis.gat.dadesacademiques.model.AssignaturaReduidaVO;
import edu.uoc.serveis.gat.dadesacademiques.service.DadesAcademiquesService;
import edu.uoc.serveis.gat.expedient.model.ExpedientVO;
import edu.uoc.serveis.gat.expedient.service.ExpedientService;
import edu.uoc.serveis.gat.matricula.model.AssignaturaMatriculadaDocenciaVO;
import edu.uoc.serveis.gat.matricula.service.MatriculaService;
import edu.uoc.serveis.gat.rac.model.AulaVO;
import edu.uoc.serveis.gat.rac.service.RacService;

/**
 * Implements the campus access for the Universitat Oberta de Catalunya (UOC).
 * @author Xavier Baró
 */
public class CampusConnection implements ICampusConnection{
    
    private String sesion;
    private UserID userID;

    private ArrayList<AssignaturaMatriculadaDocenciaVO> asignaturasMatriculadas;
    
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

    	if( userID == null ) {

    		try {
    			Auth authService = WsLibBO.getAuthServiceInstance();
    			final SessionContext sessionContext = authService.getContextBySessionId(sesion);
    			if ( sessionContext == null ) {
    				log.error("Error al obtener la SessionContext de la sesion: " + sesion);
    				throw new Exception("Error al obtener la SessionContext de la sesion: " + sesion);
    			}
    			userID = new UserID( String.valueOf(sessionContext.getIdp()) );

    		} catch ( Exception e){
    			throw new AuthPelpException("Authentication process failed");
    		}
    	}
    	return userID;
    }

    
    public ISubjectID[] getUserSubjects(ITimePeriod timePeriod) throws AuthPelpException {
    	ArrayList<SubjectID> subjects = new ArrayList<SubjectID>();

    	if( asignaturasMatriculadas == null ){
    		asignaturasMatriculadas = getUserSubjectsObjects(timePeriod);
    	}
		
    	Semester semester = (Semester) timePeriod;
		for (AssignaturaMatriculadaDocenciaVO assignaturaMatriculadaDocencia : asignaturasMatriculadas) {
			AssignaturaReduidaVO  asignatura;
			asignatura = (AssignaturaReduidaVO) assignaturaMatriculadaDocencia.getAssignatura();

			SubjectID subID = new SubjectID(asignatura.getCodAssignatura(), semester);
			subjects.add(subID);
		}
		
    	SubjectID[] subs=new SubjectID[subjects.size()];
    	return subjects.toArray(subs); 
    }

	private ArrayList<AssignaturaMatriculadaDocenciaVO> getUserSubjectsObjects( ITimePeriod timePeriod ) throws AuthPelpException {
		
		asignaturasMatriculadas = new ArrayList<AssignaturaMatriculadaDocenciaVO>();
		
		try {
	    	if( userID == null ) {
	    		userID = (UserID) getUserID();
	    	}
    		int idp = Integer.valueOf( userID.idp );

    		ExpedientService expedientService = WsLibBO.getExpedientServiceInstance();
    		ExpedientVO[] expedientes = expedientService.getExpedientsByEstudiant( idp );
    		MatriculaService matriculaService = WsLibBO.getMatriculaServiceInstance();
    		Semester sem = (Semester) timePeriod;
    		String semester = sem.get_id();
    		for (ExpedientVO expedient : expedientes) {
    			AssignaturaMatriculadaDocenciaVO[] asignaturas = matriculaService.getAssignaturesDocenciaMatriculadesEstudiant(expedient.getNumExpedient(), semester);
    			for (AssignaturaMatriculadaDocenciaVO assignaturaMatriculadaDocencia : asignaturas) {
    				
    				asignaturasMatriculadas.add( assignaturaMatriculadaDocencia );

    			}
    		}
    	} catch (Exception e) {
    		log.error("Error al obtener el listado de asignaturas del usuario.");
    		e.printStackTrace();
    		throw new AuthPelpException("Error al obtener el listado de asignaturas del usuario.");            
    	}
		return asignaturasMatriculadas;
	}

    
    public IClassroomID[] getUserClassrooms(ISubjectID subjectID) throws AuthPelpException {
    	
    	if( asignaturasMatriculadas == null ){
    		asignaturasMatriculadas = getUserSubjectsObjects(timePeriod);
    	}
    	
    	IClassroomID[] classrooms = new ClassroomID[2];
    	
    	Semester semester = (Semester) timePeriod;
    	SubjectID subject = (SubjectID) subjectID;
		for (AssignaturaMatriculadaDocenciaVO assignaturaMatriculadaDocencia : asignaturasMatriculadas) {
			AssignaturaReduidaVO  asignatura;
			asignatura = (AssignaturaReduidaVO) assignaturaMatriculadaDocencia.getAssignatura();
			SubjectID subID = new SubjectID(asignatura.getCodAssignatura(), semester);
			
			if( subject.compareTo( subID ) == 0 ) {
				ClassroomID classroom = new ClassroomID(subject, asignatura.);
			}
				
			
			subjects.add(subID);
		}
		
    	
    	
    	ClassroomID classroom = new ClassroomID(subject, classIdx);
    	
    	
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
    	
        try {
			RacService rac = WsLibBO.getRacServiceInstance();
			AulaVO[] aulas = rac.getAulesByConsultorAny(1,"");
			for (int i=0;i<aulas.length;i++)
			{/*
			    if ( (codiTercers.equals(aulas[i].getAssignatura().getCodAssignatura())) && (numAula == aulas[i].getNumAula()))
			    {
			        return true;
			    }*/
			}
			//return false;
		} catch (WSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
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
    	String MODUL = "NOTESAVAL0";
    	AnyAcademicVO[] anysAcademics;
		DadesAcademiquesService dades = WsLibBO.getDadesAcademiquesServiceInstance();
		anysAcademics = dades.getAnysAcademicsCalendari(usuario.getAppIdTREN(), usuario.getAplicacioTren(), MODUL);
		if( anysAcademics != null && anysAcademics.length > 0 ) {
			anyAcademic = anysAcademics[0].getAnyAcademic();
		} else {
			anyAcademic = RacConfiguracion.getSingletonConfiguration().get(RacConfiguracion.ACADEMIC_DATE);
		}
        // Accedir a una taula de la Base de Dades
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ITimePeriod[] getActivePeriods() {
        // Accedir a una taula de la Base de Dades
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
