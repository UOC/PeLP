package edu.uoc.pelp.engine.campus.UOC;

import java.util.ArrayList;
import java.util.List;

import org.campusproject.components.AgentComponent;
import org.campusproject.components.AuthenticationAdminComponent;
import org.campusproject.components.AuthorizationComponent;
import org.osid.OsidContext;
import org.osid.OsidException;
import org.osid.agent.Agent;
import org.osid.agent.AgentIterator;
import org.osid.agent.Group;

import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.te.osid.environment.UserAgentFacade;

public class OKIUtils {

	private static final String REMOTE_IP =   "127.0.0.1";
	private static final String AUTORIZATION_KEY =   "uocPHP_mblog";
	
	private static AuthenticationAdminComponent getAuthenticationAdminComponent( String username, String password) throws Exception, OsidException{
		
		AuthenticationAdminComponent authNAdmin;
		try {
			OsidContext osidContext = getOsidContext(username, password);			
			authNAdmin = new AuthenticationAdminComponent(osidContext);
			
		} catch (OsidException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return authNAdmin;
	}
	
	public AuthenticationAdminComponent getAuthenticationAdminComponent( String sesion) throws Exception, OsidException{
		
		AuthenticationAdminComponent authNAdmin;
		try {
			OsidContext osidContext = getOsidContext(sesion);
			
			authNAdmin = new AuthenticationAdminComponent(osidContext);
		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} catch (OsidException e) {
			e.printStackTrace();
			throw e;
		}
		
		return authNAdmin;
	}

	private static OsidContext getOsidContext() throws OsidException {
		OsidContext osidContext = new OsidContext();
		osidContext.assignContext("remote_ip", REMOTE_IP);
		osidContext.assignContext("authorization_key", AUTORIZATION_KEY);
		return osidContext;
	}

	private static OsidContext getOsidContext( String sesion ) throws OsidException {
		OsidContext osidContext = getOsidContext();
		osidContext.assignContext("s", sesion);
		return osidContext;
	}
	
	private static OsidContext getOsidContext( String username, String password ) throws OsidException {
		OsidContext osidContext = getOsidContext();
		osidContext.assignContext("username", username);
		osidContext.assignContext("password", password);
		return osidContext;
	}
	
	public static String authUser(String username,String password){
		String sessionCampus=null;
		AuthenticationAdminComponent authObj;
		try {
			authObj = getAuthenticationAdminComponent(username, password);
			authObj.authenticateUser(authObj.getUserPasswordType());
			if(authObj.isUserAuthenticated()){
				sessionCampus = authObj.getSessionId().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OsidException e) {
			e.printStackTrace();
		}
		
		return sessionCampus;
		
	}
	
	public static IUserID[] getTeachers(String sesion){

		List<IUserID> personas = new ArrayList<IUserID>();
		
		try {
			OsidContext osidContext = getOsidContext(sesion);
			AuthenticationAdminComponent authN = new AuthenticationAdminComponent(osidContext);
			AgentComponent agc = new AgentComponent(osidContext);
			
			if(authN.isUserAuthenticated()) { 

				Group group =  agc.getInstanceContainerGroup(); 
				for(AgentIterator roles = group.getMembers(false );	roles.hasNextAgent();){  
					Agent sa = roles.nextAgent(); 
					if (sa instanceof Group) { 
						Group ga = (Group) sa; 
						
						if (AgentComponent.isTeacher(ga)) { 
							for(AgentIterator members = ga.getMembers( false ); members.hasNextAgent();) { 
								Agent member = members.nextAgent(); 
								AuthorizationComponent auc = new AuthorizationComponent(osidContext);
								member.getProperties();
								UserAgentFacade fac = new UserAgentFacade(agc, auc, member);
								UserID userID = new UserID(  fac.getInternalId() );								
								personas.add(userID);
							}
						}
					}
				}
			}
		}  catch (OsidException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return personas.toArray( new IUserID[personas.size()] );
	}
	
/*	public static Person getPersonData( IUserID userID ){
		Person person = new Person(userID);

		AgentManager agentManager = OsidLoader.getManager("org.osid.agent.AgentManager", "edu.uoc.campusgateway.osid.agent", getOsidContext(sesion), new Properties());

		// Get type
		org.osid.shared.Type searchType = new Type("CAMPUSPROJECT.ORG", "Agent", "UserSearch");

		// Get users
		AgentIterator agentIterator = agentManager.getAgentsBySearch("200", searchType);
		while (agentIterator.hasNextAgent()) {
			Agent agent = agentIterator.nextAgent();
			System.out.println("User display name: ", agent.getDisplayName());
		}

		
		return person;
	}*/
}
