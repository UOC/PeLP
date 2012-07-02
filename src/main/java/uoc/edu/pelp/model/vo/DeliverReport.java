/**
 * 
 */
package uoc.edu.pelp.model.vo;


/**
 * @author jsanchezramos
 *
 */

public class DeliverReport {
	
	private DeliverData objData;
	private String state;
	
	
	public void setObjData(DeliverData objData) {
		this.objData = objData;
	}
	public DeliverData getObjData() {
		return objData;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getState() {
		return state;
	}
	
}
