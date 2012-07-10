package edu.uoc.pelp.engine.campus.UOC.ws;

import org.codehaus.xfire.spring.remoting.XFireClientFactoryBean;

import edu.uoc.serveis.gat.dadesacademiques.service.DadesAcademiquesService;

/**
 * @author jmangas
 *
 */
public class ClientDadesAcademiquesService {
	
	public static DadesAcademiquesService create(String urlEndpoint)  throws Exception {
		try {
			XFireClientFactoryBean client = new XFireClientFactoryBean();	
			client.setUrl(urlEndpoint);
			client.setServiceInterface(DadesAcademiquesService.class);
			client.afterPropertiesSet();
			return DadesAcademiquesService.class.cast(client.getObject());
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
