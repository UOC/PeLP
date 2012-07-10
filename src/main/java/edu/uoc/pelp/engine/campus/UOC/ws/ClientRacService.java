package edu.uoc.pelp.engine.campus.UOC.ws;

import org.codehaus.xfire.spring.remoting.XFireClientFactoryBean;

import edu.uoc.serveis.gat.rac.service.RacService;

/**
 * @author jmangas
 *
 */
public class ClientRacService {
	
	public static RacService create(String urlEndpoint)  throws Exception {
		try {
			XFireClientFactoryBean client = new XFireClientFactoryBean();	
			client.setUrl(urlEndpoint);
			client.setServiceInterface(RacService.class);
			client.afterPropertiesSet();
			return RacService.class.cast(client.getObject());
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
