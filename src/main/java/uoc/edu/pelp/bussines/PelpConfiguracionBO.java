package uoc.edu.pelp.bussines;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.servlets.InitServlet;

/**
 * Clase que se carga al iniciar la aplicacion donde se guardan todas las variables de configuracion,
 * es un Singleton donde cada vez que se consulta se mira si la variable a sido inicializada con los datos o no.
 *
 * @author jmangas
 *
 */
public class PelpConfiguracionBO extends HashMap<String, String> implements IPelpConfiguration {


	private static final long serialVersionUID = -4027985297795478169L;
	private static PelpConfiguracionBO config;

	private static String dirFile = null;
	
	public static final String ENTORNO = 					"enviroment";
	public static final String URL_WS_AUTH	 = 				"urlSoapAuth";
	public static final String URL_WS_RAC_SERVICE = 		"urlSoapRacService";
	public static final String URL_WS_DADES_ACADEMIQUES = 	"urlSoapDadesAcademiquesService";
	public static final String URL_WS_MATRICULA = 			"urlSoapMatriculaService";
	public static final String URL_WS_EXPEDIENTE = 			"urlSoapExpedientService";
	public static final String URL_WS_TERCER = 				"urlSoapTercerService";
	
	public static final String TEMP_PATH = 					"tempPath";
	public static final String DELIVERY_PATH = 				"deliveryPath";
	public static final String COMPILER_PATH = 				"compilerPath";
	
	public static final String ACTIVE_SUBJECTS = 			"activeSubjects";
	
	


	private PelpConfiguracionBO() throws IOException {
		super();
		dirFile = InitServlet.get_init_configPath();
	}


	public static PelpConfiguracionBO getSingletonConfiguration()throws Exception {

		if(config == null){
			synchronized (PelpConfiguracionBO.class) {
				if(config == null){
					config=new PelpConfiguracionBO();
					config.inicializarConfiguracion();
				}
			}
		}

		return config;
	}

	private synchronized void inicializarConfiguracion() throws Exception {
		// Aqui cargamos un archivo externo donde se alojara toda la configuracion.
		Properties prop = new Properties();
		InputStream is = null;
		try {
			is=new FileInputStream(dirFile);
			prop.load(is);
			for (Enumeration<Object> e = prop.keys(); e.hasMoreElements() ; ) {
				// Obtenemos el objeto
				Object obj = e.nextElement();
				config.put((String) obj, prop.getProperty(obj.toString()));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if( is != null ) is.close();
			} catch (IOException e) {
				throw e;
			}
		}

	}


	public String getEnvironmentID() {
		return get(ENTORNO);
	}


	public String getEnvironmentDesc() {
		return get(ENTORNO);
	}


	public File getTempPath() {
		return new File( get(TEMP_PATH) );
	}


	public File getDeliveryPath() {
		return new File( get(DELIVERY_PATH) );
	}


	public File getCompiler(String languageID) {
		return new File( get(COMPILER_PATH) );
	}


	public ISubjectID[] getActiveSubjects() {
		String[] ids = get(ACTIVE_SUBJECTS).split(";");
		SubjectID[] subjectIds = new SubjectID[ids.length];
		for (int i = 0; i < ids.length; i++) {
			// TODO falta definir el semestre ?
			Semester semester = new Semester("");
			SubjectID subjectId = new SubjectID(ids[i], semester);
			subjectIds[i] = subjectId;
 		}
		return subjectIds;
	}


}
