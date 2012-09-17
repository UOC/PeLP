package edu.uoc.pelp.servlets;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This servlet is loaded on startup to initiates all parameters necessary for
 * webapp functionning
 */
public class InitServlet extends HttpServlet implements Servlet {

	Log log = LogFactory.getLog(this.getClass());

	private static final long serialVersionUID = 1L;

	private static final String _STATIC_PATH_CONFIG = "StaticConfig.Path";

	private static String _init_configPath;
	public static boolean _loaded = false;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig pConfig) {

		try {
			if (!_loaded) {
				log.debug("charging init servlet configuration");

				super.init(pConfig);
				_init_configPath = getInitParameter(_STATIC_PATH_CONFIG);
				_loaded = true;
			}
		} catch (Throwable eException) {
			log.fatal("Startup fatal error!!", eException);
			System.exit(-1);
		}
	}

	public static String get_init_configPath() {
        return _init_configPath;
    }

    /**
	 *
	 */
	public void destroy() {
		super.destroy();
	} // destroy
} // InitServlet