package edu.uoc.pelp.engine.campus.UOC.ws;

import org.codehaus.xfire.spring.remoting.XFireClientFactoryBean;

import net.opentrends.remoteinterface.auth.Auth;

/**
 * Client del WS d'autoritzacio.
 * @author jmangas
 *
 */
public class ClientAuthService {
	
	public static Auth create(String urlEndpoint)  throws Exception {
		try {
			XFireClientFactoryBean client = new XFireClientFactoryBean();	
			client.setUrl(urlEndpoint);
			client.setServiceInterface(Auth.class);
			client.afterPropertiesSet();
			return Auth.class.cast(client.getObject());
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
