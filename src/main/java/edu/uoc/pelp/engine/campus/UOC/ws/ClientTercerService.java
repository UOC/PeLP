package edu.uoc.pelp.engine.campus.UOC.ws;

import org.codehaus.xfire.spring.remoting.XFireClientFactoryBean;

import edu.uoc.serveis.tercers.tercer.service.TercerService;

/**
 * @author jmangas
 *
 */
public class ClientTercerService {
	
	public static TercerService create(String urlEndpoint)  throws Exception {
		try {
			XFireClientFactoryBean client = new XFireClientFactoryBean();	
			client.setUrl(urlEndpoint);
			client.setServiceInterface(TercerService.class);
			client.afterPropertiesSet();
			return TercerService.class.cast(client.getObject());
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
