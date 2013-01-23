/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.pelp.servlets.UOC;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter for UOC Campus login process. It allows to use the UOC campus to authenticate the user
 * and access its information.
 * @author Xavier Baro
 */

public class LoginFilter extends OncePerRequestFilter  {

	private static final Logger log = Logger.getLogger(LoginFilter.class);

	/**
	 * UOC Api properties
	 */
	private Properties credentials;

	/**
	 * Enables or disables cookies
	 */
	private boolean _useCookies=true;

	final static String LOCAL_AUTH_URL = "http://localhost:8080";


	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain fc) throws ServletException, IOException {

		log.info( getAlreadyFilteredAttributeName());

		// Check if authorization is demanded
		log.info("authUOC: " + request.getSession().getAttribute("authUOC"));
		if(request.getSession().getAttribute("authUOC") != null) {
			// Get the value for current authUOC status
			String authUOC=(String)request.getSession().getAttribute("authUOC");

			// Perform actions
			if("close".compareToIgnoreCase(authUOC)==0) {
				closeAuthentication(request, response);
			} else if("request".compareToIgnoreCase(authUOC)==0) {
				requestAuthentication(request,response);
				return;
			} else if("authenticated".compareToIgnoreCase(authUOC)==0) {
				//validateAuthentication(request);
			}
		} else {
			validateAuthentication(request, response);
		}    

		// Apply other filters
		fc.doFilter(request, response);
	}


	private void requestAuthentication(HttpServletRequest request,HttpServletResponse response) throws IOException {

		// Get the application secret key
		String secretKey=credentials.getProperty("secret");

		// Create internal redirection
		String redirectURL = LOCAL_AUTH_URL + request.getRequestURI();

		// Check the current authentication status
		log.info("Obteniendo token...");

		String tokenKey = getAPIToken( request, response,redirectURL);

		log.info("TokenKey: " + tokenKey);

		if(tokenKey != null) {            
			// Get temporal user information from request
			String userIP=request.getRemoteAddr();

			// If information is incorrect, stop authentication
			if(userIP==null) {
				closeAuthentication(request, response);
				return;
			}

			// Create authentication object
			SessionTokenKey tokenObject = new SessionTokenKey(secretKey, userIP, tokenKey);
			request.getSession().setAttribute("authTokenUOC", tokenObject);   

			// Remove the authorization request
			request.getSession().removeAttribute("authUOC");
			request.getSession().setAttribute("authUOC", "authenticated"); 

			// Get original destination page
			String dstLocalURI=(String)request.getSession().getAttribute("authDst");
			if(dstLocalURI == null) dstLocalURI = request.getRequestURI();
			request.getSession().removeAttribute("authDst");

			request.getSession().setAttribute("access_token", tokenKey);


			// Redirect to initial destination URL
			String dstURI=LOCAL_AUTH_URL + dstLocalURI;
			log.info("dstURI: " + dstURI);
			response.sendRedirect(dstURI);
			return;
		} 

		// Store current destination URL
		String dest=request.getRequestURI();
		request.getSession().setAttribute("authDst", dest);

		// Ask for api code
		log.info("Solicitando code...");
		dest =  getOAuthRequest(redirectURL).getLocationUri()+"&response_type=code&scope=READ";
		log.info("dest: " + dest);
		response.sendRedirect(dest);
	}


	private OAuthClientRequest getOAuthRequest(String redirectURI) {
		OAuthClientRequest request;
		try {
			// Get the authorization uri
			String authURI=credentials.getProperty("urlUOCApi") + "webapps/uocapi/oauth/authorize";
			// Get application keys
			String clientKey=credentials.getProperty("client");

			// Create a new OAuth request with application parameters
			request = OAuthClientRequest
					.authorizationLocation(authURI)                                
					.setClientId(clientKey)
					.setRedirectURI(redirectURI).buildQueryMessage();
		} catch (OAuthSystemException e) {
			return null;
		}

		return request;        
	}

	private String getAPIToken(HttpServletRequest request, HttpServletResponse response,String redirectURL) {

		OAuthClientRequest tokenRequest;
		String token=null;
		String refreshToken=null;
		Long expiresIn=null;

		// Get token server URI
		String srvURI = credentials.getProperty("urlUOCApi") + "webapps/uocapi/oauth/token";

		// Get the application keys
		String secretKey=credentials.getProperty("secret");
		String clientKey=credentials.getProperty("client");

		// Check cookies
		if(_useCookies==true) {
			Cookie[] cookies;

			// Get an array of Cookies associated with this domain
			cookies = request.getCookies();
			if(cookies!=null && cookies.length>0) {
				for(Cookie cookie:cookies) {
					log.info(cookie.getName()+ " " + cookie.getValue());
					if(cookie.getName().equalsIgnoreCase("apiToken")) {
						token=(String)cookie.getValue();
						if(token!=null && token.equalsIgnoreCase("null")) {
							token=null;
						}
					}
					if(cookie.getName().equalsIgnoreCase("apiRefToken")) {
						refreshToken=(String)cookie.getValue();
					}
					if(cookie.getName().equalsIgnoreCase("tokenExp")) {
						expiresIn=Long.parseLong(cookie.getValue());                        
					}
				}
			}
			log.info("cookies token: " + token);

			if(token != null) {
				// Get the expiration date
				Date expDate=null;
				if(expiresIn!=null) {
					expDate=new Date(expiresIn);
				}

				// If token is expired, refresh the token
				if(expDate!=null && expDate.before(new Date())){
					// Ask for token refresh
					try {

						log.info("Solicitando refreshToken ...");

						tokenRequest = OAuthClientRequest
								.tokenLocation(srvURI)
								.setGrantType(GrantType.REFRESH_TOKEN)
								.setClientId(clientKey).setClientSecret(secretKey)
								.setRedirectURI(redirectURL).setRefreshToken(refreshToken)
								.buildBodyMessage();

						// Obtain the token
						OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
						OAuthJSONAccessTokenResponse tokenResponse;
						tokenResponse = oAuthClient.accessToken(tokenRequest);
						token = tokenResponse.getAccessToken();   
						refreshToken=tokenResponse.getRefreshToken();
						expiresIn=tokenResponse.getExpiresIn();
					} catch (OAuthSystemException ex) {
						ex.printStackTrace();
						token=null;
					} catch(OAuthProblemException ex) {
						ex.printStackTrace();
						token=null;
					}

					// If cookies are enabled, store the token
					if(_useCookies==true) {
						// Create cookies for first and last names.      
						Cookie cookieToken = new Cookie("apiToken",token);
						Cookie cookieRefToken = new Cookie("apiRefToken",refreshToken);
						Cookie cookieExpiresIn = new Cookie("tokenExp",expiresIn.toString());

						// Set expiry date after 24 Hrs for all the cookies.
						//cookie.setMaxAge(60*60*24); 

						// Add the cookie in the response header.
						response.addCookie( cookieToken );
						response.addCookie( cookieRefToken );
						response.addCookie( cookieExpiresIn );
					}
					// Return the token
					return token;
				}
			}
		}

		String code = request.getParameter("code");


		// Get local IP address
		String localAddr;
		try {
			InetAddress ip = InetAddress.getLocalHost();
			localAddr=ip.getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}

		// Get the code parameter, injected by OAuth2 authentication server


		// Check current request
		if( code == null ) {
			log.info("Parametro code nulo!!");
			return null;
		}

		// Send a new request to the authentication server asking for the key

		log.info( "code:" + code );
		try {

			log.info("Solicitando access_token ...");

			tokenRequest = OAuthClientRequest
					.tokenLocation(srvURI)
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId(clientKey).setClientSecret(secretKey)
					.setRedirectURI(redirectURL).setCode(code)
					.buildBodyMessage();

			// Obtain the token
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			OAuthJSONAccessTokenResponse tokenResponse;
			tokenResponse = oAuthClient.accessToken(tokenRequest);

			token = tokenResponse.getAccessToken();
			refreshToken=tokenResponse.getRefreshToken();
			expiresIn=tokenResponse.getExpiresIn();

			// If cookies are enabled, store the token
			if(_useCookies==true) {
				// Create cookies for first and last names.      
				Cookie cookieToken = new Cookie("apiToken",token);
				Cookie cookieRefToken = new Cookie("apiRefToken",refreshToken);
				Cookie cookieExpiresIn = new Cookie("tokenExp",expiresIn.toString());

				// Set expiry date after 24 Hrs for all the cookies.
				//cookie.setMaxAge(60*60*24); 

				// Add the cookie in the response header.
				response.addCookie( cookieToken );
				response.addCookie( cookieRefToken );
				response.addCookie( cookieExpiresIn );
			}


		} catch (OAuthProblemException e1) {
			e1.printStackTrace();
			return null;
		} catch (OAuthSystemException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  

		return token;
	}


	private void validateAuthentication(HttpServletRequest request, HttpServletResponse response) {

		log.info("validateAuthentication...");
		SessionTokenKey authTokenKey=null;

		// Get the application secret key
		String secretKey=credentials.getProperty("secret");

		// Get authentication token
		if(request.getSession().getAttribute("authTokenUOC") != null) {
			Object param=request.getSession().getAttribute("authTokenUOC");
			if(param instanceof SessionTokenKey) {
				authTokenKey=(SessionTokenKey) param;
			}
		}

		// Check authentication object and remove it if is not valid
		if(authTokenKey == null || !authTokenKey.isValid(secretKey)) {
			closeAuthentication(request, response);
		}
	}

	private void closeAuthentication(HttpServletRequest request, HttpServletResponse response) {

		log.info("closeAuthentication...");

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {

				String cookieName = cookies[i].getName();

				if( "apiToken".equalsIgnoreCase(cookieName)  || "apiRefToken".equalsIgnoreCase(cookieName) || "tokenExp".equalsIgnoreCase(cookieName) ){
					cookies[i].setValue("");
					cookies[i].setPath("/");
					cookies[i].setMaxAge(0);
				}
				response.addCookie(cookies[i]);
			}
		}

		// Remove authentication request    	
		if(request.getSession().getAttribute("authUOC")!=null) {
			request.getSession().removeAttribute("authUOC");
		}
		// Remove authentication token
		if(request.getSession().getAttribute("authTokenUOC")!=null) {
			request.getSession().removeAttribute("authTokenUOC");
		}
		request.getSession().removeAttribute("access_token");
	}

	public Properties getCredentials() {
		return credentials;
	}

	public void setCredentials(Properties properties) {
		this.credentials = properties;
	}



}
