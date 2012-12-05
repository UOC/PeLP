package edu.uoc.pelp.engine.campus.UOC;

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
}
