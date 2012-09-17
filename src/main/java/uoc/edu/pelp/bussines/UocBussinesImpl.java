package uoc.edu.pelp.bussines;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.uoc.pelp.engine.PELPEngine;
import edu.uoc.pelp.engine.activity.Activity;
import edu.uoc.pelp.engine.campus.Classroom;
import edu.uoc.pelp.engine.campus.Subject;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.exception.PelpException;



/**
 * @author jsanchezramos
 */
public class UocBussinesImpl implements UocBussines  {
	
	
	private PELPEngine engine;
	
	/* (non-Javadoc)
	 * @see uoc.edu.pelp.bussines.UocBussines#getSubjects()
	 */
	@Override
	public Subject[] getSubjects() throws Exception{
		return engine.getActiveSubjects();
	}
	
	public Classroom[] getClassroomSubjects(String idSubject) throws Exception{
		DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		Classroom[] arrayClass = null;
		
		if(idSubject != null){
			String[] subjectArray = idSubject.split("-");
			System.out.println("ASSIGNATURA : " + idSubject);
			
			Semester objSemester = new Semester(subjectArray[1],df.parse("21/09/2011"),df.parse("15/01/2012"));
			
			SubjectID objSubject = new SubjectID(subjectArray[0],objSemester);
			
			arrayClass =  engine.getSubjectClassrooms(objSubject);
		}
		
		return arrayClass;	
	}
	
	public Activity[] getActivitiClassroom(String idClassroom) throws Exception, PelpException{
		DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		Activity[] arrayActiviy = null;
		
		if(idClassroom != null && idClassroom.length()!= 0){
			String[] classroomArray = idClassroom.split("-");
			Semester objSemester = new Semester(classroomArray[1],df.parse("21/09/2011"),df.parse("15/01/2012"));
			
			SubjectID objSubject = new SubjectID(classroomArray[0],objSemester);
			
			arrayActiviy = engine.getSubjectActivity(objSubject, false);
			
		}
		return arrayActiviy;
	}

	
	public PELPEngine getEngine() {
		return engine;
	}

	public void setEngine(PELPEngine engine) {
		this.engine = engine;
	}
	
}
