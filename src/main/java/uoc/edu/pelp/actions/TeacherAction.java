package uoc.edu.pelp.actions;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;

import com.opensymphony.xwork2.ActionSupport;

import edu.uoc.pelp.engine.PELPEngine;


/**
 * @author jsanchezramos
 */

@Namespace("/")
@ResultPath(value="/")
@Result(name="success",location="jsp/teacher.jsp")
public class TeacherAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	
	private PELPEngine engine;
	
	public String execute() throws Exception {
		engine.isUserAuthenticated();
		engine.getActiveSubjects();
		return SUCCESS;
	}

	public PELPEngine getEngine() {
		return engine;
	}

	public void setEngine(PELPEngine engine) {
		this.engine = engine;
	}



}
