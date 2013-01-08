/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.pelp.servlets.UOC;

import edu.uoc.pelp.bussines.UOC.UOCPelpBussines;
import edu.uoc.pelp.bussines.UOC.exception.InvalidSessionException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Class that encodes campus connection information in the session. 
 * @author Xavier Baró
 */
class SessionTokenKey {    
    /**
     * UOC Token that enables campus query
     */
    protected String tokenKey;
    
    /**
     * IP of current user to avoid user suplantation
     */
    protected String userIP;
    
    /**
     * Digest of class data to ensure that information is not altered
     */
    protected byte[] signature;
    
    /**
     * Default constructor. Stores data and sign it
     */
    public SessionTokenKey(String secretKey,String userIP,String tokenKey) {
        this.userIP=userIP;
        this.tokenKey=tokenKey;
        signature=createSignature(secretKey);
    }
    
    /**
     * Creates the signature of the object
     * @param Get the secret key to initialize the signature object
     * @return Signature for current data
     */
    private byte[] createSignature(String secretKey) {
        try {
            // Check given key
            if(secretKey==null) {
                return null;
            }
            
            // Create a key pair using secretKey as random seed.
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.setSeed(secretKey.getBytes());
            keyGen.initialize(1024, random);
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            PublicKey pub = pair.getPublic();
                    
            // Create a signature object and initialize with secret key
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");             
            dsa.initSign(priv);
            
            // Sign current object
            dsa.update(tokenKey.getBytes());
            dsa.update(userIP.getBytes());
            
            // Return the signature
            return dsa.sign();
            
        } catch (SignatureException ex) {
            return null;
        } catch (InvalidKeyException ex) {
            return null;
        } catch (NoSuchAlgorithmException ex) {
            return null;
        } catch (NoSuchProviderException ex) {
            return null;
        }
    }

    /**
     * Check if the current object is valid.
     * @param secretKey Secret key for the application
     * @return True if current content is valid or false otherwise
     */
    public boolean isValid(String secretKey) {
        if(signature==null) {
            return false;
        }
        
        return Arrays.equals(signature, createSignature(secretKey));
    }
}

/**
 * Filter for UOC Campus login process. It allows to use the UOC campus to authenticate the user
 * and access its information.
 * @author Xavier Baro
 */


public class LoginFilter   {
    
    /**
     * Bussines object
     */
    //private UOCPelpBussines bussines;
        
    /**
     * UOC Api properties
     */
    private Properties credentials;
    
    /**
     * Enables or disablse cookies
     */
    private boolean _useCookies=true;
      
    final static String LOCAL_AUTH_URL = "http://localhost:8080";
       
    
    private OAuthClientRequest getOAuthRequest(String redirectURI) {
        OAuthClientRequest request;
        try {
                // Get the authorization uri
                String authURI=credentials.getProperty("urlUOCApi") + "/webapps/uocapi/oauth/authorize";
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
    
    /*@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain fc) throws ServletException, IOException {
        
        // Check if authorization is demanded
        if(request.getSession().getAttribute("authUOC")!=null) {
            // Get the value for current authUOC status
            String authUOC=(String)request.getSession().getAttribute("authUOC");
            
            // Perform actions
            if("close".compareToIgnoreCase(authUOC)==0) {
                closeAuthentication(request);
            } else if("request".compareToIgnoreCase(authUOC)==0) {
                requestAuthentication(request,response);
                return;
            } else if("authenticated".compareToIgnoreCase(authUOC)==0) {
                validateAuthentication(request);
            }
        } else {
            validateAuthentication(request);
        }    
        
        // Apply other filters
        fc.doFilter(request, response);
    }*/

    private void closeAuthentication(HttpServletRequest request) {
        /*try {
            // Disable campus connection
            bussines.setCampusSession("");
        } catch (InvalidSessionException ex) {
            Logger.getLogger(LoginFilter.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        // TODO: Close authentication session
        
        // Remove authentication request
        if(request.getSession().getAttribute("authUOC")!=null) {
            request.getSession().removeAttribute("authUOC");
        }
        // Remove authentication token
        if(request.getSession().getAttribute("authTokenUOC")!=null) {
            request.getSession().removeAttribute("authTokenUOC");
        }
    }

    private void requestAuthentication(HttpServletRequest request,HttpServletResponse response) throws IOException {
            
        // Get the application secret key
        String secretKey=credentials.getProperty("secret");
        
        // Create internal redirection
        String redirectURL=LOCAL_AUTH_URL + request.getRequestURI();
        
        // Check the current authentication status
        String tokenKey=getAPIToken(request, response,redirectURL);
        if(tokenKey!=null) {            
            // Get temporal user information from request
            String userIP=request.getRemoteAddr();
                        
            // If information is incorrect, stop authentication
            if(userIP==null) {
                closeAuthentication(request);
                return;
            }
            
            // Create authentication object
            SessionTokenKey tokenObject=new SessionTokenKey(secretKey,userIP,tokenKey);
            request.getSession().setAttribute("authTokenUOC", tokenObject);   
            
            // Remove the authorization request
            request.getSession().removeAttribute("authUOC");
            
            // Get original destination page
            String dstLocalURI=(String)request.getSession().getAttribute("authDst");
            request.getSession().removeAttribute("authDst");
            /* FIXME
            // Set the campus key
            if(bussines!=null) {
                try {
                    bussines.setCampusSession(tokenKey);
                } catch (InvalidSessionException ex) {
                    Logger.getLogger(LoginFilter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            */
            // Redirect to initial destination URL
            String dstURI=LOCAL_AUTH_URL + dstLocalURI;
            response.sendRedirect(dstURI);
            return;
        } 
                
        // Store current destination URL
        String dest=request.getRequestURI();
        request.getSession().setAttribute("authDst", dest);

        // Ask for api code
        response.sendRedirect(getOAuthRequest(redirectURL).getLocationUri()+"&response_type=code&scope=READ");
    }

    private void validateAuthentication(HttpServletRequest request) {
        
        SessionTokenKey authTokenKey=null;
        
        // Get the application secret key
        String secretKey=credentials.getProperty("secret");
        
        // Get authentication token
        if(request.getSession().getAttribute("authTokenUOC")!=null) {
            Object param=request.getSession().getAttribute("authTokenUOC");
            if(param instanceof SessionTokenKey) {
                authTokenKey=(SessionTokenKey) param;
            }
        } 
                
        // Check authentication object and remove it if is not valid
        if(authTokenKey==null || !authTokenKey.isValid(secretKey)) {
            closeAuthentication(request);
        }
    }
    
    private String getAPIToken(HttpServletRequest request,HttpServletResponse response,String redirectURL) {
            
        OAuthClientRequest tokenRequest;
        String token=null;
        String refreshToken=null;
        Long expiresIn=null;
        
        // Get token server URI
        String srvURI=credentials.getProperty("urlUOCApi") + "/webapps/uocapi/oauth/token";
        
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
            if(token!=null) {
                // Get the expiration date
                Date expDate=null;
                if(expiresIn!=null) {
                    expDate=new Date(expiresIn);
                }
                
                // If token is expired, refresh the token
                if(expDate!=null && expDate.before(new Date())){
                    // Ask for token refresh
                    try {
                        tokenRequest = OAuthClientRequest
                            .tokenLocation(srvURI)
                            .setGrantType(GrantType.REFRESH_TOKEN)
                            .setClientId(clientKey).setClientSecret(secretKey)
                            .setRedirectURI(redirectURL).setRefreshToken(refreshToken)
                            .buildBodyMessage();

                        // Obtain the toket
                        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
                        OAuthJSONAccessTokenResponse tokenResponse;
                        tokenResponse = oAuthClient.accessToken(tokenRequest);
                        token = tokenResponse.getAccessToken();   
                        refreshToken=tokenResponse.getRefreshToken();
                        expiresIn=tokenResponse.getExpiresIn();
                    } catch (OAuthSystemException ex) {
                        token=null;
                    } catch(OAuthProblemException ex) {
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
        
        // Get local IP address
        String localAddr;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            localAddr=ip.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
        
        // Get the code parameter, injected by OAuth2 authentication server
        String[] codes=request.getParameterValues("code");
        
        // Check current request
        if(codes==null || (!localAddr.equals(request.getRemoteAddr()) && !"127.0.0.1".equals(request.getRemoteAddr()))) {
            return null;
        }
        
        // Send a new request to the authentication server asking for the key
	try {
            tokenRequest = OAuthClientRequest
                .tokenLocation(srvURI)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(clientKey).setClientSecret(secretKey)
                .setRedirectURI(redirectURL).setCode(codes[0])
                .buildBodyMessage();

                // Obtain the toket
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
            return null;
        } catch (OAuthSystemException e) {
            return null;
        }   
        
        return token;
    }

    public Properties getCredentials() {
        return credentials;
    }

    public void setCredentials(Properties properties) {
        this.credentials = properties;
    }
    
/*
    public UOCPelpBussines getBussines() {
        return bussines;
    }

    public void setBussines(UOCPelpBussines bussines) {
        this.bussines = bussines;
    }
*/
    
}
