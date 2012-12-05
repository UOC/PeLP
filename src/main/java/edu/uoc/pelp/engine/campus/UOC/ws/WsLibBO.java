package edu.uoc.pelp.engine.campus.UOC.ws;

import net.opentrends.remoteinterface.auth.Auth;

import org.apache.log4j.Logger;

import edu.uoc.serveis.gat.dadesacademiques.service.DadesAcademiquesService;
import edu.uoc.serveis.gat.expedient.service.ExpedientService;
import edu.uoc.serveis.gat.matricula.service.MatriculaService;
import edu.uoc.serveis.gat.rac.service.RacService;
import edu.uoc.serveis.tercers.tercer.service.TercerService;

import uoc.edu.pelp.bussines.PelpConfiguracionBO;

/**
 * Classe que se encarga de hacer las llamadas a los Web Service de PRO,PRE y TEST
 *
 * @author jmangas
 *
 */
public class WsLibBO {

	private static final Logger log = Logger.getLogger(WsLibBO.class);


	private static String getUrlWS( String webService ) throws Exception {

		String url = PelpConfiguracionBO.getSingletonConfiguration().get(webService);
		if (log.isInfoEnabled()) {
			log.info("getUrlWS(String) - url webService=[" + url + "]");
		}

		return url;
	}
	
	public static Auth getAuthServiceInstance() throws Exception {
		if (log.isInfoEnabled()) {
			log.info("getAuthServiceInstance(): " + PelpConfiguracionBO.URL_WS_AUTH);
		}
		Auth authService = ClientAuthService.create( getUrlWS( PelpConfiguracionBO.URL_WS_AUTH) );
		return authService;
	}
	
	public static RacService getRacServiceInstance() throws Exception {
		if (log.isInfoEnabled()) {
			log.info("getRacServiceInstance(): " + PelpConfiguracionBO.URL_WS_RAC_SERVICE);
		}
		RacService ws = ClientRacService.create( getUrlWS( PelpConfiguracionBO.URL_WS_RAC_SERVICE ));
		return ws;
	}

	public static DadesAcademiquesService getDadesAcademiquesServiceInstance() throws Exception {
		if (log.isInfoEnabled()) {
			log.info("getDadesAcademiquesServiceInstance(): " + PelpConfiguracionBO.URL_WS_DADES_ACADEMIQUES);
		}
		DadesAcademiquesService ws = ClientDadesAcademiquesService.create( getUrlWS( PelpConfiguracionBO.URL_WS_DADES_ACADEMIQUES));
		return ws;
	}
	
	public static MatriculaService getMatriculaServiceInstance() throws Exception {
		if (log.isInfoEnabled()) {
			log.info("getMatriculaServiceInstance(): " + PelpConfiguracionBO.URL_WS_MATRICULA);
		}
		MatriculaService ws = ClientMatriculaService.create( getUrlWS( PelpConfiguracionBO.URL_WS_MATRICULA));
		return ws;
	}

	public static ExpedientService getExpedientServiceInstance() throws Exception {
		if (log.isInfoEnabled()) {
			log.info("getExpedientServiceInstance(): " + PelpConfiguracionBO.URL_WS_EXPEDIENTE);
		}
		ExpedientService ws = ClientExpedientsService.create( getUrlWS( PelpConfiguracionBO.URL_WS_EXPEDIENTE));
		return ws;
	}

	public static TercerService getTercerServiceInstance() throws Exception {
		if (log.isInfoEnabled()) {
			log.info("getTercerServiceInstance(): " + PelpConfiguracionBO.URL_WS_TERCER);
		}
		TercerService ws = ClientTercerService.create( getUrlWS( PelpConfiguracionBO.URL_WS_TERCER));
		return ws;
	}
}
