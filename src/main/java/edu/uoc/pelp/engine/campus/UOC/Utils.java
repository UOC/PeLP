package edu.uoc.pelp.engine.campus.UOC;

import org.springframework.web.client.RestTemplate;

import edu.uoc.serveis.gat.dadesacademiques.model.DescripcioVO;

public class Utils {


	public static String getLanguageTitle(DescripcioVO[] descripciones, String idioma) throws Exception{
		String textTraduit = null;
		if (descripciones!= null){
			if (descripciones.length>0){

				for(int z = 0; z < descripciones.length; z++){
					String valor = descripciones[z].getValor();
					String codIndicador = descripciones[z].getLang();
					if(codIndicador!=null){
						if(codIndicador.equals(idioma)){

							if(valor!=null){
								textTraduit = valor;
							}
						}
					}

				}
			}
		}
		return textTraduit;
	}

	public static String authUserForCampus(String username,String password){
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://cv-pre.uoc.edu/webapps/CampusAuth/CampusAuthServlet?login={username}&password={password}", String.class, username, password);
		
		int init = result.indexOf("<session>");
		int fin = result.indexOf("</session>");
		
		String contentSession = "";
		if(init!=-1&&fin!=-1)contentSession = result.substring(init+9,fin);
		return contentSession;
	}
}
