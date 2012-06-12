/**
 * 
 */
package uoc.edu.model.vo;


/**
 * @author jsanchezramos
 *
 */

public class DeliverData {
	private Integer idDeliver;	
	private Integer idActivity;
	private Integer userId;
	private String container;
	private Integer codEvent;
	private FileDeliver file[];
	
	
	public void setIdDeliver(Integer idDeliver) {
		this.idDeliver = idDeliver;
	}
	public Integer getIdDeliver() {
		return idDeliver;
	}
	public void setIdActivity(Integer idActivity) {
		this.idActivity = idActivity;
	}
	public Integer getIdActivity() {
		return idActivity;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setContainer(String container) {
		this.container = container;
	}
	public String getContainer() {
		return container;
	}
	public void setCodEvent(Integer codEvent) {
		this.codEvent = codEvent;
	}
	public Integer getCodEvent() {
		return codEvent;
	}
	public void setFile(FileDeliver file[]) {
		this.file = file;
	}
	public FileDeliver[] getFile() {
		return file;
	}
	
}
