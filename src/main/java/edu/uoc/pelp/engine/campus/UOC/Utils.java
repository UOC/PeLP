package edu.uoc.pelp.engine.campus.UOC;

import net.opentrends.remoteinterface.auth.Auth;
import net.opentrends.remoteinterface.auth.SessionContext;
import edu.uoc.pelp.engine.campus.UOC.ws.WsLibBO;

public class Utils {

	
    /**
     * Funcion que retorna la SessionContext a partir de la campus Session (s)
     * @param campusSession
     * @return SessionContext
     * @throws Exception
     */
    public static SessionContext getSessionContext( final String campusSession) throws Exception {
        Auth authService = WsLibBO.getAuthServiceInstance();
        
        final SessionContext sessionContext = authService.getContextBySessionId(campusSession);
        if ( sessionContext == null ) {
            throw new Exception("Error al obtener la SessionContext de la sesion: " + campusSession);
        }
        return sessionContext;
    }


}
