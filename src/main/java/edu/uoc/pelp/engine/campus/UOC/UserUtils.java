package edu.uoc.pelp.engine.campus.UOC;

import net.opentrends.remoteinterface.auth.Auth;
import net.opentrends.remoteinterface.auth.SessionCampusData;
import net.opentrends.remoteinterface.auth.SessionContext;

import org.apache.log4j.Logger;

import edu.uoc.pelp.engine.campus.UOC.ws.WsLibBO;

public class UserUtils {

    private String AplicacioTren;
    private String AppIdTREN;
    private String appId;
    
	public static final String APPID_UOC="UOC";//Campus catalan
	public static final String APPID_UOC2000="UOC2000";//Campus castellano
	public static final String APPID_GCUOC="GCUOC";//Global campus
	
	public static final String APPTREN_EXPIB="GAT_EXPIB";//Campus castellano /Global Campus
	public static final String APPTREN_EXP="GAT_EXP";//Campus catalan
	
	public static final String CATALAN = "ca";
	public static final String CASTELLANO = "es";
	public static final String INGLES = "en";
	public static final String FRANCES = "fr";
	
	private static final Logger log = Logger.getLogger(UserUtils.class);
	
    /**
     * Funcion que retorna la SessionContext a partir de la campus Session (s)
     * @param campusSession
     * @return SessionContext
     * @throws Exception
     */
    public static SessionContext getSessionContext( final String campusSession) throws Exception {
        Auth auth = WsLibBO.getAuthServiceInstance();
        final SessionContext sessionContext = auth.getContextBySessionId(campusSession);
        if ( sessionContext == null ) {
            log.error("[UserBO] Error al obtener la SessionContext de la sesion: " + campusSession);
            throw new Exception("Error al obtener la SessionContext de la sesion: " + campusSession);
        }
        return sessionContext;
    }


    /**
     * Funcion que calcula la AppIdTREN a partir del objeto SessionContext
     * @param sessionContext
     * @return String (appIdTREN)
     * @throws Exception
     */
    public static String getAppIdTREN( final SessionContext sessionContext) throws Exception {
    	final SessionCampusData sessionCampusData = sessionContext.getSessionCampus();
    	final String appidTREN = getAppIdTren(sessionCampusData);
        return appidTREN;
    }

    /**
     * Funcion que calcula la AppIdTREN a partir del objeto SessionCampusData
     * @param sessionCampusData
     * @return String (appIdTREN)
     * @throws Exception
     */
    private static String getAppIdTren(final SessionCampusData sessionCampusData) throws Exception {
        String appidTREN = "UOC";
        if( sessionCampusData != null && sessionCampusData.getAppIdTREN() != null) {
            appidTREN = sessionCampusData.getAppIdTREN();
        }
  
        return appidTREN;
    }


    /**
     * Funcion que calcula la AppId a partir de la campusSession
     * @param campusSession
     * @return String (appId)
     * @throws Exception
     */
    public static String getAppId(final SessionContext sessionContext) throws Exception {
    	String appId = "";

    	final SessionCampusData sessionCampusData = sessionContext.getSessionCampus();

    	if( sessionCampusData != null && sessionCampusData.getAppId() != null) {
    		appId = sessionCampusData.getAppId();
    	}
    	return appId;
    }

    /**
     * Retorna aplicacionTren segun el appId de campus
     * @param appId
     * @return String (aplicacionTren)
     */
    public static String getAplicacioTren(final String appId) throws Exception {

    	String aplicacionTren;

    	if (APPID_UOC.equalsIgnoreCase(appId)){
    		aplicacionTren = APPTREN_EXP;
    	} else if (APPID_UOC2000.equalsIgnoreCase(appId)){
    		aplicacionTren = APPTREN_EXPIB;
    	} else if (APPID_GCUOC.equalsIgnoreCase(appId)){
    		aplicacionTren = APPTREN_EXPIB;
    	} else {
    		aplicacionTren = APPTREN_EXP;
    	}
    	return aplicacionTren;
    }

    /**
     * Retorna el Idioma por defecto dependiendo del appidTREN
     * @param appidTREN
     * @return String (defaultIdioma)
     * @throws Exception
     */
    public static String getCampusLanguage( final String appidTREN ) throws Exception{
        String _campusIdioma= CATALAN;
        if (APPID_UOC2000.equalsIgnoreCase(appidTREN)){
            _campusIdioma= CASTELLANO;
        }else if (APPID_UOC.equalsIgnoreCase(appidTREN)){
            _campusIdioma= CATALAN;
        }else if (APPID_GCUOC.equalsIgnoreCase(appidTREN)) {
            _campusIdioma=INGLES;
        }

        return _campusIdioma;
    }
}
