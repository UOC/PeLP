package edu.uoc.pelp.engine.campus.UOC.ws;

import org.codehaus.xfire.spring.remoting.XFireClientFactoryBean;

import edu.uoc.serveis.gat.matricula.service.MatriculaService;

/**
 * @author jmangas
 *
 */
public class ClientMatriculaService {
	
	public static MatriculaService create(String urlEndpoint)  throws Exception {
		try {
			XFireClientFactoryBean client = new XFireClientFactoryBean();	
			client.setUrl(urlEndpoint);
			client.setServiceInterface(MatriculaService.class);
			client.afterPropertiesSet();
			return MatriculaService.class.cast(client.getObject());
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
