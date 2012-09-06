package uoc.edu.pelp.actions;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author jsanchezramos
 */

@Namespace("/")
@ResultPath(value="/")
@Result(name="success",location="jsp/teacher.jsp")
public class TeacherAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	public String execute() throws Exception {

		return SUCCESS;
	}
}
