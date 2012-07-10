package edu.uoc.pelp.engine.campus.UOC.ws;

import org.codehaus.xfire.spring.remoting.XFireClientFactoryBean;

import edu.uoc.serveis.gat.expedient.service.ExpedientService;

/**
 * @author jmangas
 *
 */
public class ClientExpedientsService {
	
	public static ExpedientService create(String urlEndpoint)  throws Exception {
		try {
			XFireClientFactoryBean client = new XFireClientFactoryBean();	
			client.setUrl(urlEndpoint);
			client.setServiceInterface(ExpedientService.class);
			client.afterPropertiesSet();
			return ExpedientService.class.cast(client.getObject());
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
