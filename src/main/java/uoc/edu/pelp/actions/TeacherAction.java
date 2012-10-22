package uoc.edu.pelp.actions;

import java.util.Map;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;

import uoc.edu.pelp.bussines.UocBussinesImpl;

import com.opensymphony.xwork2.ActionSupport;

import edu.uoc.pelp.bussines.UOC.vo.UOCSubject;
import edu.uoc.pelp.engine.campus.Classroom;
import edu.uoc.pelp.engine.deliver.Deliver;

/**
 * @author jsanchezramos
 */

@Namespace("/viejo")
@ResultPath(value = "/")
@Result(name = "success", location = "jsp/teacher.jsp")
public class TeacherAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private UocBussinesImpl bUOC;

	private UOCSubject[] listSubjects;
	private Classroom[] listClassroms;
	private Map<String, String> listActivity;
	private Deliver[] listDelivers;

	private String s_assign;
	private String s_aula;
	private String s_activ;

	public String execute() throws Exception {		
		listSubjects = bUOC.getSubjects();
		listClassroms = bUOC.getClassroomSubjects(s_assign);
		listActivity = bUOC.getActivitiClassroom(s_aula);
		listDelivers = bUOC.getDelivers(s_activ);
		
		return SUCCESS;
	}

	public UOCSubject[] getListSubjects() {
		return listSubjects;
	}

	public void setListSubjects(UOCSubject[] listSubjects) {
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

	public Map<String, String> getListActivity() {
		return listActivity;
	}

	public void setListActivity(Map<String, String> listActivity) {
		this.listActivity = listActivity;
	}

	public Deliver[] getListDelivers() {
		return listDelivers;
	}

	public void setListDelivers(Deliver[] listDelivers) {
		this.listDelivers = listDelivers;
	}

}
