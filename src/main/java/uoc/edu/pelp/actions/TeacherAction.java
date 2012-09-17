package uoc.edu.pelp.actions;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;

import uoc.edu.pelp.bussines.UocBussinesImpl;

import com.opensymphony.xwork2.ActionSupport;

import edu.uoc.pelp.engine.activity.Activity;
import edu.uoc.pelp.engine.campus.Classroom;
import edu.uoc.pelp.engine.campus.Subject;

/**
 * @author jsanchezramos
 */

@Namespace("/")
@ResultPath(value = "/")
@Result(name = "success", location = "jsp/teacher.jsp")
public class TeacherAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private UocBussinesImpl bUOC;

	private Subject[] listSubjects;
	private Classroom[] listClassroms;
	private Activity[] listActivity;

	private String s_assign;
	private String s_aula;
	private String s_activ;

	public String execute() throws Exception {		
		listSubjects = bUOC.getSubjects();
		listClassroms = bUOC.getClassroomSubjects(s_assign);
		listActivity = bUOC.getActivitiClassroom(s_aula);
		return SUCCESS;
	}

	public Subject[] getListSubjects() {
		return listSubjects;
	}

	public void setListSubjects(Subject[] listSubjects) {
		this.listSubjects = listSubjects;
	}

	public UocBussinesImpl getbUOC() {
		return bUOC;
	}

	public void setbUOC(UocBussinesImpl bUOC) {
		this.bUOC = bUOC;
	}

	public String getS_assign() {
		return s_assign;
	}

	public void setS_assign(String s_assign) {
		this.s_assign = s_assign;
	}

	public Classroom[] getListClassroms() {
		return listClassroms;
	}

	public void setListClassroms(Classroom[] listClassroms) {
		this.listClassroms = listClassroms;
	}

	public String getS_aula() {
		return s_aula;
	}

	public void setS_aula(String s_aula) {
		this.s_aula = s_aula;
	}

	public String getS_activ() {
		return s_activ;
	}

	public void setS_activ(String s_activ) {
		this.s_activ = s_activ;
	}

	public Activity[] getListActivity() {
		return listActivity;
	}

	public void setListActivity(Activity[] listActivity) {
		this.listActivity = listActivity;
	}

}
