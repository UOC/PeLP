package uoc.edu.pelp.bussines;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Classe que se carga al iniciar la aplicacion donde se guardan todas las variables de configuracion,
 * es un Singleton donde cada vez que se consulta se mira si la variable a sido inicializada con los datos o no.
 *
 * @author dmartinj
 *
 */
public class PelpConfiguracionBO extends HashMap<String, String> {


	private static final long serialVersionUID = -4027985297795478169L;
	private static PelpConfiguracionBO config;

	public static final String ENTORNO_WS = 				"WSEnviroment";
	public static final String URL_WS_AUTH	 = 				"urlSoapAuth";
	public static final String URL_WS_RAC_SERVICE = 		"urlSoapRacService";
	public static final String URL_WS_DADES_ACADEMIQUES = 	"urlSoapDadesAcademiquesService";
	public static final String URL_WS_MATRICULA = 			"urlSoapMatriculaService";
	public static final String URL_WS_EXPEDIENTE = 			"urlSoapExpedientService";
	public static final String URL_WS_CUA_MAIL = 			"urlSoapCUAMail";


	private PelpConfiguracionBO() throws IOException {
		super();
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
		// Aqui cargamos un archivo externo donde se alojara toda la configuración.
		ResourceBundle resource = ResourceBundle.getBundle("global.properties");
		InputStream is = null;
		try {

			for (Enumeration<String> e = resource.getKeys(); e.hasMoreElements() ; ) {
				// Obtenemos el objeto
				Object obj = e.nextElement();
				config.put((String) obj, resource.getString(obj.toString()));
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


}
