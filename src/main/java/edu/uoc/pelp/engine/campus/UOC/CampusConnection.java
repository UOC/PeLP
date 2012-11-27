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
import java.util.List;

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
import edu.uoc.serveis.gat.dadesacademiques.model.AnyAcademicVO;
import edu.uoc.serveis.gat.dadesacademiques.model.AssignaturaReduidaVO;
import edu.uoc.serveis.gat.dadesacademiques.model.AssignaturaRelacionadaVO;
import edu.uoc.serveis.gat.dadesacademiques.model.AssignaturaVO;
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

	private String aplicacioTren;
	private String appIdTREN;
	private String appId;

	private ArrayList<AssignaturaMatriculadaDocenciaVO> asignaturasMatriculadas;
	private ArrayList<AulaVO> asignaturasConsultor;
	private ArrayList<AssignaturaReduidaVO> asignaturasPRA;

	private static final Logger log = Logger.getLogger(CampusConnection.class);

	public CampusConnection(String sesion) {
		super();
		this.sesion = sesion;
	}

	@Override
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

	@Override
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

				appId = UserUtils.getAppId(sessionContext);
				aplicacioTren = UserUtils.getAplicacioTren(appId);
				appIdTREN = UserUtils.getAplicacioTren(appId);

			} catch ( Exception e){
				throw new AuthPelpException("Authentication process failed");
			}
		}
		return userID;
	}


	public ISubjectID[] getUserSubjects(ITimePeriod timePeriod) throws AuthPelpException {

		return getUserSubjects(null, timePeriod); 
	}

	@Override
	public ISubjectID[] getUserSubjects(UserRoles userRole, ITimePeriod timePeriod) throws AuthPelpException {
		ArrayList<SubjectID> subjects = new ArrayList<SubjectID>();

		if( userRole == null || userRole.compareTo(UserRoles.Student) == 0 ){
			if( asignaturasMatriculadas == null ){
				asignaturasMatriculadas = getListaAsignaturasMatriculadas( null );
			}
		} 
		if(  userRole == null || userRole.compareTo(UserRoles.Teacher) == 0 ){
			if( asignaturasConsultor == null ){
				asignaturasConsultor = getListaAsignaturasConsultor( null );
			}
		} 
		if(  userRole == null || userRole.compareTo(UserRoles.MainTeacher) == 0 ){
			if( asignaturasPRA == null ){
				asignaturasPRA = getListaAsignaturasPRA( timePeriod );
			}
		}

		ITimePeriod[] semestres;
		if( timePeriod == null ){
			semestres = getActivePeriods();
		} else {
			semestres = new ITimePeriod[1];
			semestres[0] = timePeriod;
		}

		for (ITimePeriod iTimePeriod : semestres) {
			Semester semester = (Semester) iTimePeriod;
			// asignaturas matriculadas estudiante
			for (AssignaturaMatriculadaDocenciaVO assignaturaMatriculadaDocencia : asignaturasMatriculadas) {
				AssignaturaReduidaVO asignatura;
				asignatura = (AssignaturaReduidaVO) assignaturaMatriculadaDocencia.getAssignatura();
				if( assignaturaMatriculadaDocencia.getAnyAcademic().equalsIgnoreCase(semester.getID()) ){
					SubjectID subID = new SubjectID(asignatura.getCodAssignatura(), semester);
					subjects.add(subID);
				}
			}
			// asignaturas consultores
			for (AulaVO aula : asignaturasConsultor) {
				if( aula.getAnyAcademic().equalsIgnoreCase(semester.getID()) ){
					SubjectID subID = new SubjectID(aula.getAssignatura().getCodAssignatura(), semester);
					subjects.add(subID);
				}
			}

			// asignaturas PRA
			for (AssignaturaReduidaVO asignatura : asignaturasPRA) {
				SubjectID subID = new SubjectID(asignatura.getCodAssignatura(), semester);
				subjects.add(subID);
			}

		}

		SubjectID[] subs = new SubjectID[subjects.size()];
		return subjects.toArray(subs); 
	}



	public IClassroomID[] getUserClassrooms(ISubjectID subjectID) throws AuthPelpException {

		return getUserClassrooms(null, subjectID);
	}

	@Override
	public IClassroomID[] getUserClassrooms(UserRoles userRole, ISubjectID subjectID) throws AuthPelpException {
		ArrayList<ClassroomID> classrooms = new ArrayList<ClassroomID>();


		if( userRole == null || userRole.compareTo(UserRoles.Student) == 0 ){
			if( asignaturasMatriculadas == null ){
				asignaturasMatriculadas = getListaAsignaturasMatriculadas( null );
			}
		} 
		if(  userRole == null || userRole.compareTo(UserRoles.Teacher) == 0 ){
			if( asignaturasConsultor == null ){
				asignaturasConsultor = getListaAsignaturasConsultor( null );
			}
		} 
		if(  userRole == null || userRole.compareTo(UserRoles.MainTeacher) == 0 ){
			if( asignaturasPRA == null ){
				asignaturasPRA = getListaAsignaturasPRA( null );
			}
		}
		try {

			ITimePeriod[] semestres = getActivePeriods();
			boolean todasLasAsignaturas = (subjectID == null);
			SubjectID subject = new SubjectID("XXX", new Semester("XXX"));
			if( !todasLasAsignaturas ) {
				subject = (SubjectID) subjectID;
			} 
			for (ITimePeriod iTimePeriod : semestres) {
				Semester semester = (Semester) iTimePeriod;
				ClassroomID classroom;

				// asignaturas matriculadas estudiante
				for (AssignaturaMatriculadaDocenciaVO assignaturaMatriculadaDocencia : asignaturasMatriculadas) {
					if( todasLasAsignaturas || assignaturaMatriculadaDocencia.getAssignatura().getCodAssignatura().equalsIgnoreCase( subject.getCode() ) ){
						classroom = new ClassroomID(subject, assignaturaMatriculadaDocencia.getNumAula() );
						classrooms.add(classroom);
					}
				}

				// asignaturas consultores
				for (AulaVO aula : asignaturasConsultor) {					
					if( todasLasAsignaturas || aula.getAssignatura().getCodAssignatura().equalsIgnoreCase( subject.getCode()  ) ){
						SubjectID subTmp = new SubjectID(aula.getAssignatura().getCodAssignatura(),  semester);
						classroom = new ClassroomID(subTmp, aula.getNumAula() );
						classrooms.add(classroom);
					}
				}


				// asignaturas PRA
				for (AssignaturaReduidaVO asignatura : asignaturasPRA) {
					if( todasLasAsignaturas || asignatura.getCodAssignatura().equalsIgnoreCase( subject.getCode() )){

						RacService racService = WsLibBO.getRacServiceInstance();
						AulaVO[] aulas = racService.getAulesByAssignaturaAny(asignatura.getCodAssignatura(), semester.getID());
						SubjectID subTmp = new SubjectID(asignatura.getCodAssignatura(), semester);
						for (AulaVO aula : aulas) {
							classroom = new ClassroomID(subTmp, aula.getNumAula() );
							classrooms.add(classroom);
						}
					}
				}
			}
			
		} catch (Exception e) {
			log.error("Error al obtener el listado de aulas");
			e.printStackTrace();
			throw new AuthPelpException("Error al obtener el listado de aulas.");   
		}
		ClassroomID[] classroomsArray = new ClassroomID[classrooms.size()];
		return classrooms.toArray(classroomsArray); 
	}

	@Override
	public IClassroomID[] getSubjectClassrooms(ISubjectID subject, UserRoles userRole) throws AuthPelpException {
		return getUserClassrooms(userRole, subject);
	}

	@Override
	public boolean isRole(UserRoles role, ISubjectID subject, IUserID user) {
		
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isRole(UserRoles role, ISubjectID subject) throws AuthPelpException {
		return isRole(role,subject,getUserID());
	}

	@Override
	public boolean isRole(UserRoles role, IClassroomID classroom, IUserID user) throws AuthPelpException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isRole(UserRoles role, IClassroomID classroom) throws AuthPelpException {
		boolean encontrado = false;
		IClassroomID[] classrooms = getUserClassrooms(role, null);
		for (IClassroomID iclassroomID : classrooms) {
			ClassroomID classroomID = (ClassroomID) iclassroomID;
			if( classroomID.compareTo(classroom) == 0){
				encontrado = true;
				break;
			}
		}
		return encontrado;
	}

	@Override
	public IUserID[] getRolePersons(UserRoles role, ISubjectID subject) throws AuthPelpException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public IUserID[] getRolePersons(UserRoles role, IClassroomID classroom) throws AuthPelpException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean hasLabSubjects(ISubjectID subject) throws AuthPelpException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ISubjectID[] getLabSubjects(ISubjectID subject) throws AuthPelpException {
		//TODO: Una solucio es utilitzar la taula PELP_MainLabSubjects, amb les correspondencies
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean hasEquivalentSubjects(ISubjectID subject) throws AuthPelpException {
		return getEquivalentSubjects(subject).length > 0;
	}

	@Override
	public ISubjectID[] getEquivalentSubjects(ISubjectID subject) throws AuthPelpException {
		List<SubjectID> lista = new ArrayList<SubjectID>();
		String TIPO_EQUIVALENTE = "E";
		String TIPO_COMPARTIDA = "C";
		try {
			DadesAcademiquesService dades = WsLibBO.getDadesAcademiquesServiceInstance();
			SubjectID subjectID = (SubjectID) subject;
			AssignaturaRelacionadaVO[] asignaturasRelacionadas = dades.getAssignaturesRelacionades(0, subjectID.getCode(), subjectID.getSemester().getID(), UserUtils.getCampusLanguage(appId) );
			for(int i = 0; i < asignaturasRelacionadas.length; i++){
				AssignaturaRelacionadaVO asignatura = asignaturasRelacionadas[i];
				String tipo = asignatura.getTipusRelacio();
				if(tipo == null) throw new AuthPelpException("AsignaturaRelacionada con codigo de tipo de relacion nulo; datos: " + subjectID.getCode() + " " + subjectID.getSemester().getID() + " " + UserUtils.getCampusLanguage(appId));
				if( tipo.equals(TIPO_EQUIVALENTE) || tipo.equals( TIPO_COMPARTIDA	) ){
					SubjectID subjectAux = new SubjectID(asignatura.getCodi(), subjectID.getSemester());
					lista.add(subjectAux);
				}
			}
		} catch (Exception e) {			
			e.printStackTrace();
			throw new AuthPelpException("Error to recover equivalent subjects");
		}
		
		SubjectID[] SubjectArray = new SubjectID[lista.size()];
		return lista.toArray(SubjectArray);

	}

	@Override
	public boolean isCampusConnection() {
		return true;
	}

	@Override
	public Subject getSubjectData(ISubjectID isubjectID) throws AuthPelpException {
		Subject subject;
		try {
			DadesAcademiquesService dades = WsLibBO.getDadesAcademiquesServiceInstance();
			SubjectID subjectID = (SubjectID) isubjectID;
			AssignaturaVO asignatura = dades.getAssignaturaByCodi( subjectID.getCode() );
			subject = new Subject( subjectID );
			subject.setDescription(asignatura.getDescLlarga()[0].getValor());
			subject.setShortName(asignatura.getDescripcio()[0].getValor());
		} catch (Exception e) {			
			e.printStackTrace();
			throw new AuthPelpException("Error to recover subject data");
		}
		return subject;
	}

	@Override
	public Classroom getClassroomData(IClassroomID classroomID) throws AuthPelpException {
		Classroom classroom = new Classroom(classroomID);
		
		Subject subject = new Subject(subjectID);
		classroom.setSubjectRef(subject)SubjectRef(subject);
		
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Person getUserData(IUserID userID) throws AuthPelpException {
		if( userID == null ) {
			userID = getUserID();
		}
		
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Person getUserData() throws AuthPelpException {
		return getUserData( null ); 
	}



	@Override
	public ITimePeriod[] getPeriods() {
		String MODUL = "NOTESAVAL0";
		AnyAcademicVO[] anysAcademics;
		ArrayList<Semester> semestres =  new ArrayList<Semester>();
		try {
			DadesAcademiquesService dades = WsLibBO.getDadesAcademiquesServiceInstance();
			anysAcademics = dades.getAnysAcademicsCalendari(appIdTREN, aplicacioTren, MODUL);
			for (AnyAcademicVO anyAcademic : anysAcademics) {
				semestres.add( new Semester(anyAcademic.getAnyAcademic(), anyAcademic.getDataInici(), anyAcademic.getDataFinal()) );			
			}
		}  catch (Exception e) {
			log.error("Error al obtener la lista de calendarios abiertos");
			e.printStackTrace();			
		}

		Semester[] sems = new Semester[ semestres.size() ];
		semestres.toArray(sems);
		return sems;
	}

	@Override
	public ITimePeriod[] getActivePeriods() {
		return getPeriods();
	}
	
	private ArrayList<AssignaturaMatriculadaDocenciaVO> getListaAsignaturasMatriculadas(ITimePeriod timePeriod) throws AuthPelpException {

		asignaturasMatriculadas = new ArrayList<AssignaturaMatriculadaDocenciaVO>();

		try {
			if( userID == null ) {
				userID = (UserID) getUserID();
			}
			int idp = Integer.valueOf( userID.idp );

			ExpedientService expedientService = WsLibBO.getExpedientServiceInstance();
			ExpedientVO[] expedientes = expedientService.getExpedientsByEstudiant( idp );
			MatriculaService matriculaService = WsLibBO.getMatriculaServiceInstance();

			ITimePeriod[] semestres;
			if( timePeriod == null ){
				semestres = getActivePeriods();
			} else {
				semestres = new ITimePeriod[1];
				semestres[0] = timePeriod;
			}

			for (ITimePeriod iTimePeriod : semestres) {
				Semester semester = (Semester) iTimePeriod;
				String semesterId = semester.getID();
				for (ExpedientVO expedient : expedientes) {
					AssignaturaMatriculadaDocenciaVO[] asignaturas = matriculaService.getAssignaturesDocenciaMatriculadesEstudiant(expedient.getNumExpedient(), semesterId);
					for (AssignaturaMatriculadaDocenciaVO assignaturaMatriculadaDocencia : asignaturas) {

						asignaturasMatriculadas.add( assignaturaMatriculadaDocencia );

					}
				}
			}

		} catch (Exception e) {
			log.error("Error al obtener el listado de asignaturas del usuario.");
			e.printStackTrace();
			throw new AuthPelpException("Error al obtener el listado de asignaturas del usuario.");            
		}
		return asignaturasMatriculadas;
	}

	private ArrayList<AulaVO> getListaAsignaturasConsultor(ITimePeriod timePeriod) throws AuthPelpException {

		asignaturasConsultor = new ArrayList<AulaVO>();
		try {
			if( userID == null ) {
				userID = (UserID) getUserID();
			}
			int idp = Integer.valueOf( userID.idp );
			RacService rac = WsLibBO.getRacServiceInstance();

			ITimePeriod[] semestres;
			if( timePeriod == null ){
				semestres = getActivePeriods();
			} else {
				semestres = new ITimePeriod[1];
				semestres[0] = timePeriod;
			}

			for (ITimePeriod iTimePeriod : semestres) {
				Semester semester = (Semester) iTimePeriod;
				String semesterId = semester.getID();
				AulaVO[] asignaturas = rac.getAulesByConsultorAny(idp, semesterId);
				for (AulaVO aula : asignaturas) {
					asignaturasConsultor.add( aula );
				}
			}
		} catch (Exception e) {
			log.error("Error al obtener el listado de asignaturas del consultor.");
			e.printStackTrace();
			throw new AuthPelpException("Error al obtener el listado de asignaturas del consultor.");   
		}

		return asignaturasConsultor;
	}

	private ArrayList<AssignaturaReduidaVO> getListaAsignaturasPRA(ITimePeriod timePeriod) throws AuthPelpException {

		asignaturasPRA = new ArrayList<AssignaturaReduidaVO>();
		try {
			if( userID == null ) {
				userID = (UserID) getUserID();
			}
			int idp = Integer.valueOf( userID.idp );
			DadesAcademiquesService dades = WsLibBO.getDadesAcademiquesServiceInstance();

			ITimePeriod[] semestres;
			if( timePeriod == null ){
				semestres = getActivePeriods();
			} else {
				semestres = new ITimePeriod[1];
				semestres[0] = timePeriod;
			}

			for (ITimePeriod iTimePeriod : semestres) {
				Semester semester = (Semester) iTimePeriod;
				String semesterId = semester.getID();

				AssignaturaReduidaVO[] asignaturas = dades.getAssignaturesByResponsableAny(idp, semesterId);
				for (AssignaturaReduidaVO aula : asignaturas) {
					asignaturasPRA.add( aula );
				}
			}
		} catch (Exception e) {
			log.error("Error al obtener el listado de asignaturas del PRA.");
			e.printStackTrace();
			throw new AuthPelpException("Error al obtener el listado de asignaturas del PRA.");   
		}

		return asignaturasPRA;
	}
}
