/**
 * 
 */
package uoc.edu.model.vo;


/**
 * @author jsanchezramos
 *
 */

public class FileDeliver {
	private Integer id;
	private String name;
	private Boolean code;
	private Boolean memori;
	private Boolean mainFile;
	private Byte file[];
	
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setCode(Boolean code) {
		this.code = code;
	}
	public Boolean getCode() {
		return code;
	}
	public void setMemori(Boolean memori) {
		this.memori = memori;
	}
	public Boolean getMemori() {
		return memori;
	}
	public void setMainFile(Boolean mainFile) {
		this.mainFile = mainFile;
	}
	public Boolean getMainFile() {
		return mainFile;
	}
	public void setFile(Byte file[]) {
		this.file = file;
	}
	public Byte[] getFile() {
		return file;
	}
	
	
	
	
}
