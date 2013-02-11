package edu.uoc.pelp.interceptor;

import java.util.Locale;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import edu.uoc.pelp.bussines.UOC.UOCPelpBussines;
import edu.uoc.pelp.bussines.vo.UserInformation;



public class InterceptorLang extends AbstractInterceptor implements Interceptor{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	


		//called during interceptor destruction
		public void destroy() {
			System.out.println("CustomInterceptor destroy() is called...");
		}
	 
		//called during interceptor initialization
		public void init() {
			System.out.println("CustomInterceptor init() is called...");
		}
	 
		//put interceptor code here
		public String intercept(ActionInvocation invocation) throws Exception {
	 
			 WebApplicationContext context =
	        			WebApplicationContextUtils.getRequiredWebApplicationContext(
	                                            ServletActionContext.getServletContext()
	                                );
			 
			System.out.println("CustomInterceptor, before invocation.invoke()...");
			
			
			UOCPelpBussines bUOC = (UOCPelpBussines) context.getBean("bUOC");
			UserInformation userInformation = bUOC.getUserInformation();
			if(bUOC != null && userInformation != null){
				String lang = userInformation.getLanguage();
				System.out.println("IDIOMA USUARIO: "+lang);
				Map session = ActionContext.getContext().getSession();	
				
    		  	if(lang.equals("ca")){
    		  		session.put("WW_TRANS_I18N_LOCALE",new java.util.Locale("ca"));
    		  		Locale locale = new Locale("ca", "ES");
   		  		 	session.put("org.apache.tiles.LOCALE", locale);
    		  	}else if(lang.equals("es")){
    		  		session.put("WW_TRANS_I18N_LOCALE",new java.util.Locale("es"));
    		  		Locale locale = new Locale("es", "ES");
   		  		 	session.put("org.apache.tiles.LOCALE", locale);
    		  	}else if(lang.equals("en")){
    		  		session.put("WW_TRANS_I18N_LOCALE",new java.util.Locale("en"));
    		  		Locale locale = new Locale("en", "UK");
   		  		 	session.put("org.apache.tiles.LOCALE", locale);
    		  	}   	
			}
			
			String result = invocation.invoke();
	 
			System.out.println("CustomInterceptor, after invocation.invoke()...");
	 
			return result;
		}

		
}