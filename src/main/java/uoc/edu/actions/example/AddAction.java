/**
 * 
 */
package uoc.edu.actions.example;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import uoc.edu.model.vo.Example;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * @author xaracil
 *
 */
public class AddAction extends ActionSupport implements Preparable, ModelDriven<Example> {
	
	@Override
	@Action(
			results={@Result(name="success", type="redirect", location="index.action")}
	)
	public String execute() throws Exception {
		
		return "success";
	}

	public Example getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void prepare() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
