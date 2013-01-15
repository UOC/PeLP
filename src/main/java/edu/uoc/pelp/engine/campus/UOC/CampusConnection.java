/*
	Copyright 2011-2012 Fundació per a la Universitat Oberta de Catalunya

	This file is part of PeLP (Programming eLearning Plaform).

    PeLP is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    PeLP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.uoc.pelp.engine.campus.UOC;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import edu.uoc.pelp.engine.campus.Classroom;
import edu.uoc.pelp.engine.campus.ICampusConnection;
import edu.uoc.pelp.engine.campus.IClassroomID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.engine.campus.Subject;
import edu.uoc.pelp.engine.campus.UserRoles;
import edu.uoc.pelp.engine.campus.UOC.vo.ClassroomList;
import edu.uoc.pelp.engine.campus.UOC.vo.PersonList;
import edu.uoc.pelp.engine.campus.UOC.vo.User;
import edu.uoc.pelp.exception.AuthPelpException;

/**
 * Implements the campus access for the Universitat Oberta de Catalunya (UOC).
 * @author Xavier Baró
 */
public class CampusConnection implements ICampusConnection {
    
    /**
     * UOC Api properties
     */
    private Properties credentials;

    /**
     * UOC API token
     */    
    private String _token = null;
    
    private static final Logger log = Logger.getLogger(CampusConnection.class);
    
    public CampusConnection() {
        
    }

    public CampusConnection(String session) {
        _token=session;
    }
    
    public void setCampusSession(String campusSession) {
        if(_token==null) {
            _token=campusSession;
        }
    }
    
    private class DateDeserializer implements JsonDeserializer<java.util.Date> {
        @Override
        public java.util.Date deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return je == null ? null : new java.util.Date(je.getAsLong());
        }
    }
    
    
    private String Get(String operation) {
        // Check parameters
        if(_token==null || operation==null) {
            return null;
        }
        HttpClient httpClient = new DefaultHttpClient();
        String operationURL = credentials.getProperty("urlUOCApi") + credentials.getProperty("apiPath") + operation;
        HttpGet httpGet = new HttpGet(operationURL+"?access_token="+_token);
        httpGet.setHeader("content-type", "application/json");
        log.debug(operationURL);
        try {
            HttpResponse resp = httpClient.execute(httpGet);
            int statusCode = resp.getStatusLine().getStatusCode();
            if(statusCode != 200) {
            	log.warn("operationURL Response: " + operationURL + " -> " + statusCode);
                return null;
            }
            String responseString = EntityUtils.toString(resp.getEntity());
            log.debug(responseString);
            return responseString;
        } catch (Exception ex) {
            return null;
        }
    }
    
    private User getCampusUserData() {
        if(_token==null) {
            return null;
        }
        String userJSON=Get("user");
        Gson gson = new Gson();
        return gson.fromJson(userJSON, User.class);
    }
    
    private PersonList getCampusPersonData() {
        if(_token==null) {
            return null;
        }

        String userJSON=Get("people");       
        
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(java.util.Date.class, new DateDeserializer());
        
        Gson gson=gsonBuilder.create();        

        return gson.fromJson(userJSON, PersonList.class);
    }
    
    private edu.uoc.pelp.engine.campus.UOC.vo.Person getCampusPersonData(String id) {
        if(_token==null || id==null) {
            return null;
        }
        String userJSON=Get("people/" + id);
                
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(java.util.Date.class, new DateDeserializer());
        
        Gson gson=gsonBuilder.create();        

        return gson.fromJson(userJSON, edu.uoc.pelp.engine.campus.UOC.vo.Person.class);
    }
    
    private ClassroomList getCampusUserSubjects() {
        if(_token==null) {
            return null;
        }
       
        String userJSON=Get("subjects");
        
        
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(java.util.Date.class, new DateDeserializer());
        
        Gson gson=gsonBuilder.create();        

        return gson.fromJson(userJSON, ClassroomList.class);
    }
    
    private edu.uoc.pelp.engine.campus.UOC.vo.Classroom getSubjectData(String id) {
        if(_token==null || id==null) {
            return null;
        }
       
        String userJSON = Get("subjects/" + id);
        
        
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(java.util.Date.class, new DateDeserializer());
        
        Gson gson = gsonBuilder.create();        

        return gson.fromJson(userJSON, edu.uoc.pelp.engine.campus.UOC.vo.Classroom.class);
    }

    public Properties getCredentials() {
        return credentials;
    }

    public void setCredentials(Properties properties) {
        this.credentials = properties;
    }
    

    @Override
    public boolean isUserAuthenticated() throws AuthPelpException {
        if(getCampusUserData()!=null) {
            return true;
        }
        return false;
    }

    @Override
    public IUserID getUserID() throws AuthPelpException {
        User userData=getCampusUserData();
        if(userData!=null) {
            return new UserID(userData.getId());
        }
        return null;
    }
    
    @Override
    public Person getUserData() throws AuthPelpException {
        User userData=getCampusUserData();
        edu.uoc.pelp.engine.campus.UOC.vo.Person personData= getCampusPersonData(userData.getId());
        if(userData!=null) {
            Person newData=new Person(new UserID(userData.getId()));
            newData.setFullName(userData.getFullName());
            newData.setLanguage(userData.getLanguage());
            newData.setName(userData.getName());
            newData.setUserPhoto(userData.getPhotoUrl());
            newData.setUsername(userData.getUsername());
            if(personData!=null) {
                newData.seteMail(personData.getEmail());
            }
            return newData;
        }
        return null;
    }


    @Override
    public ISubjectID[] getUserSubjects(ITimePeriod timePeriod) throws AuthPelpException {
        
        ClassroomList classrooms=getCampusUserSubjects();
        if(classrooms==null || classrooms.getClassrooms()==null) {
            return null;
        }
        
        // Get the classrooms 
        edu.uoc.pelp.engine.campus.UOC.vo.Classroom[] classList=classrooms.getClassrooms();
        
        // Create the output list        
        SubjectID[] retList=new SubjectID[classList.length];        
        
        for(int i=0;i<classList.length;i++) {
            edu.uoc.pelp.engine.campus.UOC.vo.Classroom classObj=classList[i];
            String code = getCode(classObj.getCode());
            String semesterCode = getSemester( classObj.getCode() );
           
            // Create the semester object
            Semester semester=new Semester(semesterCode);
                    
            // Build new Subject identifier
            retList[i]=new SubjectID(code,semester);
        }
        
        return retList;
    }

    @Override
    public ISubjectID[] getUserSubjects(UserRoles userRole, ITimePeriod timePeriod) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IClassroomID[] getUserClassrooms(ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IClassroomID[] getUserClassrooms(UserRoles userRole, ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IClassroomID[] getSubjectClassrooms(ISubjectID subject, UserRoles userRole) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRole(UserRoles role, ISubjectID subject, IUserID user) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRole(UserRoles role, ISubjectID subject) throws AuthPelpException {
    	boolean isRole = false;
    	
    	ClassroomList classroomsList = getCampusUserSubjects();
    	if( classroomsList != null){
    		
    		SubjectID subjectID = (SubjectID) subject;
     		edu.uoc.pelp.engine.campus.UOC.vo.Classroom[] classrooms = classroomsList.getClassrooms();
    		for (edu.uoc.pelp.engine.campus.UOC.vo.Classroom classroom : classrooms) {
				if( getCode(classroom.getCode()).equals( subjectID.getCode() ) ){
					if( role == UserRoles.Student  ){
						if( isStudent( classroom.getAssignments() ) ){
							return true;
						}
					}
					if( role == UserRoles.Teacher  ){
						if( isTeacher( classroom.getAssignments() ) ){
							return true;
						}
					}					
				}
			}
    	}
    	
    	
    	return isRole;
    }

    @Override
    public boolean isRole(UserRoles role, IClassroomID classroom, IUserID user) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRole(UserRoles role, IClassroomID classroom) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IUserID[] getRolePersons(UserRoles role, ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IUserID[] getRolePersons(UserRoles role, IClassroomID classroom) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasLabSubjects(ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISubjectID[] getLabSubjects(ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasEquivalentSubjects(ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISubjectID[] getEquivalentSubjects(ISubjectID subject) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCampusConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Subject getSubjectData(ISubjectID subjectID) throws AuthPelpException {
        if(subjectID==null || !(subjectID instanceof SubjectID)) {
            return null;
        }
        
        // Get custom subject identifier
        SubjectID id=(SubjectID)subjectID;
        
        // Ask for subjec data
        edu.uoc.pelp.engine.campus.UOC.vo.Classroom classroom = getSubjectData( id.getCode() );
        
        // Create the output object
        Subject retVal=new Subject(subjectID);
        retVal.setDescription(classroom.getTitle());
        //retVal.setLabFlag(true);
        //retVal.setParent(subjectID);
        //retVal.setShortName(_token);
        
        return retVal;
    }

    @Override
    public Classroom getClassroomData(IClassroomID classroomID) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Person getUserData(IUserID userID) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ITimePeriod[] getPeriods() {
        ArrayList<Semester> retList=new ArrayList<Semester>();
        
        // Return the current period and if we are near the next one, return both
        Calendar cal = Calendar.getInstance();
        int month=cal.get(Calendar.MONTH);        
        int year=cal.get(Calendar.YEAR);
        
        Semester s1=new Semester((year-1) + "1");
        Semester s2=new Semester((year-1) + "2");
        Semester s3=new Semester(year + "1");
        
        // Create the list of semesters
        if(month>=1 && month<=2) {
            retList.add(s1);
            retList.add(s2);
        } else if(month<7) {
            retList.add(s2);
        } else if(month<=8) {
            retList.add(s2);
            retList.add(s3);
        } else {
            retList.add(s3);
        }
        
        // Create the output array
        Semester[] retVal=new Semester[retList.size()];
        retList.toArray(retVal);
        
        return retVal;
    }

    @Override
    public ITimePeriod[] getActivePeriods() {
        return getPeriods();
    }
    
    private static String getCode( String code ){
    	return  code.substring( code.lastIndexOf("_") + 1 ); 
    }
    
    // uoc_121_02.003
    // uoc2000_102_71.502
    private static String getSemester( String code ){
    	return  "20" + code.substring(code.indexOf("_") + 1, code.lastIndexOf("_")); 
    }
    
    private static boolean isStudent( String[] assignments ){
    	for (String assig : assignments) {
			if(assig.equalsIgnoreCase( Constants.ESTUDIANTE )){
				return true;
			}
		}
    	return false;
    }
    
    private static boolean isTeacher( String[] assignments ){
    	for (String assig : assignments) {
			if(assig.equalsIgnoreCase( Constants.RESPONSABLE ) || assig.equalsIgnoreCase( Constants.PROFESSOR )){
				return true;
			}
		}
    	return false;
    }
}
