/**
 * 
 */
package uoc.edu.model.vo;


/**
 * @author jsanchezramos
 *
 */

public class ActivityData {
	private Integer idActivity;
	private String codAssigment;	
	private String description;
	
	

	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setIdActivity(Integer idActivity) {
		this.idActivity = idActivity;
	}
	public Integer getIdActivity() {
		return idActivity;
	}
	public void setCodAssigment(String codAssigment) {
		this.codAssigment = codAssigment;
	}
	public String getCodAssigment() {
		return codAssigment;
	}	
	
	
}
