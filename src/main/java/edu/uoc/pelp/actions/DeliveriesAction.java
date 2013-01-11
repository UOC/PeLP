package edu.uoc.pelp.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.osid.OsidException;

import uoc.edu.pelp.bussines.PelpConfiguracionBO;

import com.opensymphony.xwork2.ActionSupport;

import edu.uoc.pelp.bussines.UOC.UOCPelpBussines;
import edu.uoc.pelp.bussines.UOC.vo.UOCClassroom;
import edu.uoc.pelp.bussines.UOC.vo.UOCSubject;
import edu.uoc.pelp.bussines.exception.AuthorizationException;
import edu.uoc.pelp.bussines.vo.Activity;
import edu.uoc.pelp.bussines.vo.DeliverDetail;
import edu.uoc.pelp.bussines.vo.DeliverFile;
import edu.uoc.pelp.bussines.vo.Test;
import edu.uoc.pelp.exception.PelpException;

/**
 * @author jsanchezramos
 */

@Namespace("/")
@ResultPath(value = "/")
@Results({
    @Result(name="index", type="redirectAction", params = {"actionName" , "home"}),
    @Result(name = "success", location = "jsp/home.jsp")
}) 

public class DeliveriesAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private UOCPelpBussines bUOC;

	private List<File> uploads = new ArrayList<File>();
	private List<String> uploadFileNames = new ArrayList<String>();
	private List<String> uploadContentTypes = new ArrayList<String>();

	private File testFile;
	private File testFileOut;
	private String testPlain;
	private String testPlainOut;

	private List<DeliverFile> listDeliversFile;
	private String[][] matrizFile;

	private int fileDim;

	private UOCSubject[] listSubjects;
	private UOCClassroom[] listClassroms;
	private Activity[] listActivity;

	private String s_assign;
	private String s_aula;
	private String s_activ;

	private String auxInfo;
	private String resulMessage;
	private Boolean finalDeliver;
	private String codePlain;
	
	private String username;
	private String password;
	private String imageURL;
	private String fullName;

	private static Logger log = Logger.getLogger(DeliveriesAction.class);

	public String execute() throws Exception {
		
		if(bUOC.getUserInformation()!= null){
			this.rutaFile();
			this.menuTop();
			imageURL = bUOC.getUserInformation().getUserPhoto();
			if(imageURL== null)imageURL = "img/user.png";
			fullName = bUOC.getUserInformation().getUserFullName();
		}else{
			imageURL= null;
			fullName = null;
		}
		
		this.fileupload();
		this.crearDeliverFile();
		this.listFile();

		return SUCCESS;
	}
	
	public String auth() throws Exception, OsidException{
		// FIXME
		//bUOC.setCampusSession(Utils.authUserForCampus(username, password));
		return "index";
	}
	@PreDestroy
    public String logout() throws PelpException{
    	bUOC.logout();
    	return "index";
    }

	public void delete() throws Exception {
		if (auxInfo != null) {
			String ruta = this.rutaFile();
			File directorioPracticas = new File(ruta);
			File[] ficheros = directorioPracticas.listFiles();
			if (ficheros != null && ficheros.length > 0) {
				fileDim = ficheros.length;
				for (int i = 0; i < ficheros.length; i++) {
					File file = ficheros[i];
					String nameFileHas = auxInfo.replaceAll("-", "");
					String nameFileFolder = String.valueOf(
							file.getName().hashCode()).replaceFirst("-", "");
					if (nameFileHas.equals(nameFileFolder)) {
						log.info("DELETE FILE: " + file.getName()
								+ " - FILE HASH" + nameFileHas);
						file.delete();
					}
				}
			}
		}
	}

	private void crearDeliverFile() throws Exception {
		if (matrizFile != null) {
			String ruta = this.rutaFile();
			File directorioPracticas = new File(ruta);
			File[] ficheros = directorioPracticas.listFiles();
			if (ficheros != null && ficheros.length > 0 || codePlain != null) {
				DeliverFile[] files = null;
				if (ficheros != null && ficheros.length > 0) {
					log.info("Ficheros a mostrar: " + ficheros.length);
					fileDim = ficheros.length;
					files = new DeliverFile[ficheros.length];
					Boolean isFile = false;
					for (int i = 0; i < ficheros.length; i++) {
						File file = ficheros[i];
						log.info("NOMBRE DEL FICHERO " + file.getName());
						files[i] = new DeliverFile(new File(ruta), new File(
								file.getName()));

						for (int j = 0; j < matrizFile.length; j++) {
							String nameFileHas = matrizFile[j][0].replaceAll(
									"-", "");
							String nameFileFolder = String.valueOf(
									file.getName().hashCode()).replaceAll("-",
									"");
							if (nameFileHas.equals("m" + nameFileFolder)) {
								isFile = true;
								files[i].setIsReport(true);
							}
							if (nameFileHas.equals("c" + nameFileFolder)) {
								isFile = true;
								log.warn("ENTRA CODIGO");
								files[i].setIsCode(true);
							}
							if (nameFileHas.equals("f" + nameFileFolder)) {
								isFile = true;
								log.warn("ENTRA F PRINCIPAL");
								files[i].setIsMain(true);
							}
						}
					}
					if (isFile && listActivity != null) {
						Activity objActivity = new Activity();
						for (int j = 0; j < listActivity.length; j++) {
							if (listActivity[j].getIndex() == Integer
									.parseInt(s_activ)) {
								objActivity = listActivity[j];
							}
						}

						if (finalDeliver) {

							DeliverDetail objDetail = bUOC.addDeliver(
									objActivity, files);
							resulMessage = objDetail.getCompileMessage();
							if (resulMessage.length() == 0)
								resulMessage = "OK";
						}
					}

				}
				if (( finalDeliver != null && !finalDeliver) && (matrizFile.length > 1 || (codePlain != null && codePlain.length() > 5))) {

					Test[] tests  = new Test[1];

					if (testPlain != null && testPlainOut != null) {
						tests[0] = new Test();
						if (testPlain != null && testPlain.length() > 1) {
							tests[0].setInputText(testPlain);
						}
						if (testPlainOut != null && testPlainOut.length() > 1) {
							tests[0].setExpectedOutput(testPlainOut);
						}
						if(testFile!= null){
							tests[0].setInputFilePath(testFile.getAbsolutePath());
						}
						if(testFileOut!= null){
							tests[0].setExpectedOutputFilePath(testFileOut.getAbsolutePath());
						}						
						tests[0].setPublic(true);
					}

					if (codePlain != null && codePlain.length() > 5) {
						DeliverDetail objDetail = bUOC.compileCode(codePlain,
								"JAVA", tests);
						resulMessage = objDetail.getCompileMessage();
					} else if (files != null) {
						DeliverDetail objDetail = bUOC.compileCode(files,
								"JAVA", tests, ruta);
						resulMessage = objDetail.getCompileMessage(); // FIXME Flata implementar la part de la engine diu not  support yet.
					}

				}
			}
		}
	}

	private void listFile() throws Exception {
		
				String ruta = this.rutaFile();
				File directorioPracticas = new File(ruta);
				File[] ficheros = directorioPracticas.listFiles();
				if (ficheros != null && ficheros.length > 0) {
					log.info("Ficheros a mostrar: " + ficheros.length);
					// listDeliversFile = new ArrayList<DeliverFile>();
					fileDim = ficheros.length;
					matrizFile = new String[fileDim][5];
					for (int i = 0; i < ficheros.length; i++) {
						File file = ficheros[i];
						log.info("NOMBRE DEL FICHERO " + file.getName());
						matrizFile[i][0] = file.getName();
						matrizFile[i][1] = "true";
						matrizFile[i][2] = "true";
						matrizFile[i][3] = "true";
						matrizFile[i][4] = String.valueOf(file.getName()
								.hashCode());
					}
				} else {
					matrizFile = null;
					fileDim = 0;
				}
			
		
	}

	private String rutaFile() throws Exception{
		String ruta = PelpConfiguracionBO.getSingletonConfiguration().get(
				PelpConfiguracionBO.TEMP_PATH);
		
		String fullRuta = ruta; 
		if(bUOC.getUserInformation()!=null)fullRuta +="/"+ bUOC.getUserInformation().getUserID()+"/";
		else{
			fullRuta +="/invited/";
		}
				
		if(s_assign!=null&&s_aula!=null&&s_activ!=null){
			fullRuta += s_assign+"/"+s_aula+"/"+s_activ+"/";	
		}
		
		return fullRuta;
		
		
	}
	private void fileupload() throws Exception {
		String ruta = this.rutaFile();
		
		if (uploads != null && !uploads.isEmpty()) {
			try {
				File destFile = new File(ruta, uploadFileNames.get(0));
				FileUtils.copyFile(uploads.get(0), destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		this.cleanFile();
	}

	private void menuTop() throws PelpException, AuthorizationException,
			PelpException {
		listSubjects = bUOC.getUserSubjects();

		if (s_assign != null && s_assign.length() > 0) {
			String[] infoAssing = s_assign.split("_");
			listClassroms = bUOC.getUserClassrooms(new UOCSubject(
					infoAssing[0], infoAssing[2]));
		}
		if (s_aula != null && s_aula.length() > 0 && s_assign != null
				&& s_assign.length() > 0) {
			String[] infoAssing = s_assign.split("_");
			listActivity = bUOC.getSubjectActivities(infoAssing[0],
					infoAssing[2]);
		}
	}

	private void cleanFile() {
		try {
			synchronized (this) {
				String ruta = this.rutaFile();
				File directorioPracticas = new File(ruta);
				File[] ficheros = directorioPracticas.listFiles();

				if (ficheros != null && ficheros.length > 0) {
					log.info("Borrando ficheros temporales: " + ficheros.length);
					long tiempoActual = new Date().getTime();
					long unDiaEnMilSecs = 90000000;
					int eliminados = 0;
					for (int i = 0; i < ficheros.length; i++) {
						File file = ficheros[i];
						if ((tiempoActual - file.lastModified()) > unDiaEnMilSecs) {
							if (!file.delete()) {
								file.deleteOnExit();
							}
							eliminados++;
						}
					}
					log.info("Ficheros borrados: " + eliminados);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UOCPelpBussines getbUOC() {
		return bUOC;
	}

	public void setbUOC(UOCPelpBussines bUOC) {
		this.bUOC = bUOC;
	}

	public List<File> getUpload() {
		return uploads;
	}

	public void setUpload(List<File> uploads) {
		this.uploads = uploads;
	}

	public List<String> getUploadFileName() {
		return uploadFileNames;
	}

	public void setUploadFileName(List<String> uploadFileNames) {
		this.uploadFileNames = uploadFileNames;
	}

	public List<String> getUploadContentType() {
		return uploadContentTypes;
	}

	public void setUploadContentType(List<String> uploadContentTypes) {
		this.uploadContentTypes = uploadContentTypes;
	}

	public UOCSubject[] getListSubjects() {
		return listSubjects;
	}

	public void setListSubjects(UOCSubject[] listSubjects) {
		this.listSubjects = listSubjects;
	}

	public UOCClassroom[] getListClassroms() {
		return listClassroms;
	}

	public void setListClassroms(UOCClassroom[] listClassroms) {
		this.listClassroms = listClassroms;
	}

	public Activity[] getListActivity() {
		return listActivity;
	}

	public void setListActivity(Activity[] listActivity) {
		this.listActivity = listActivity;
	}

	public String getS_assign() {
		return s_assign;
	}

	public void setS_assign(String s_assign) {
		this.s_assign = s_assign;
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

	public List<DeliverFile> getListDeliversFile() {
		return listDeliversFile;
	}

	public void setListDeliversFile(List<DeliverFile> listDeliversFile) {
		this.listDeliversFile = listDeliversFile;
	}

	public String[][] getMatrizFile() {
		return matrizFile;
	}

	public void setMatrizFile(String[][] matrizFile) {
		this.matrizFile = matrizFile;
	}

	public int getFileDim() {
		return fileDim;
	}

	public void setFileDim(int fileDim) {
		this.fileDim = fileDim;
	}

	public String getAuxInfo() {
		return auxInfo;
	}

	public void setAuxInfo(String auxInfo) {
		this.auxInfo = auxInfo;
	}

	public String getResulMessage() {
		return resulMessage;
	}

	public void setResulMessage(String resulMessage) {
		this.resulMessage = resulMessage;
	}

	public Boolean getFinalDeliver() {
		return finalDeliver;
	}

	public void setFinalDeliver(Boolean finalDeliver) {
		this.finalDeliver = finalDeliver;
	}

	public String getCodePlain() {
		return codePlain;
	}

	public void setCodePlain(String codePlain) {
		this.codePlain = codePlain;
	}

	public File getTestFile() {
		return testFile;
	}

	public void setTestFile(File testFile) {
		this.testFile = testFile;
	}

	public File getTestFileOut() {
		return testFileOut;
	}

	public void setTestFileOut(File testFileOut) {
		this.testFileOut = testFileOut;
	}

	public String getTestPlain() {
		return testPlain;
	}

	public void setTestPlain(String testPlain) {
		this.testPlain = testPlain;
	}

	public String getTestPlainOut() {
		return testPlainOut;
	}

	public void setTestPlainOut(String testPlainOut) {
		this.testPlainOut = testPlainOut;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}