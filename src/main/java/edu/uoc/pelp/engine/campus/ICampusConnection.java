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
package edu.uoc.pelp.engine.campus;

import edu.uoc.pelp.exception.AuthPelpException;

/**
 * This interface describes the methods to retrieve information from the campus, 
 * such as persons information, rooms where persons are registered and subjects. 
 * An implementation of this interface should be done for each institution or
 * campus.
 * @author Xavier Baró
 */
public interface ICampusConnection {
    /**
     * Verify that it is an authenticated user to the campus. The implementation 
     * may provide de mechanisms to make this check.
     * @return True if the user is authenticated or false otherwise.
     */
    boolean isUserAuthenticated();
    
    /**
     * Retrieve the logged user identifier
     * @return IUserID class the user identifier
     * @throws AuthPelpException There is no user authenticated.
     */
    IUserID getUserID() throws AuthPelpException;
    
    /**
     * Retrieve all the subjects where the current logged user is inscribed,
     * both if is the teacher or if is student.
     * @return Array with the identifier for each subject.
     * @throws AuthPelpException There is no user authenticated.
     */
    ISubjectID[] getUserSubjects() throws AuthPelpException;
        
    /**
     * Retrieve all the subjects where the current logged user is inscribed,
     * as the given role.
     * @param userRole User role to filter the query. If null, teacher and student roles are used.
     * @return Array with the identifier for each subject.
     * @throws AuthPelpException There is no user authenticated.
     */
    ISubjectID[] getUserSubjects(UserRoles userRole) throws AuthPelpException;
    
    /**
     * Retrieve all the classrooms where the current logged user is inscribed,
     * both if is the teacher or if is student.
     * @return Array with the identifier for each classroom.
     * @throws AuthPelpException There is no user authenticated.
     */
    IClassroomID[] getUserClassrooms() throws AuthPelpException;
        
    /**
     * Retrieve all the classrooms where the current logged user is inscribed with
     * the given role. If the roll is null, teacher and student roles are considered.
     * @param userRole User role to filter the query. If null, teacher and student roles are used.
     * @return Array with the identifier for each classroom.
     * @throws AuthPelpException There is no user authenticated.
     */
    IClassroomID[] getUserClassrooms(UserRoles userRole) throws AuthPelpException;
    
    /**
     * Retrieve all the classrooms for the given subject, where the current logged user 
     * is inscribed with the given role. If the roll is null, teacher and student roles are
     * considered.
     * @param userRole User role to filter the query. If null, teacher and student roles are used.
     * @return Array with the identifier for each classroom.
     * @throws AuthPelpException There is no user authenticated.
     */
    IClassroomID[] getSubjectClassrooms(ISubjectID subject, UserRoles userRole) throws AuthPelpException;
        
    /**
     * Checks if the given user has the given role in this subject.
     * @param role One of the available roles in the platform
     * @param subject A subject identifier
     * @param user A user identifier.
     * @return True if the user has the role in this subject or false otherwise.
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    boolean isRole(UserRoles role,ISubjectID subject,IUserID user) throws AuthPelpException;
    
    /**
     * Checks if the current user has the given role in this subject.
     * @param role One of the available roles in the platform
     * @param subject A subject identifier
     * @return True if the user has the role in this subject or false otherwise.
     * @throws AuthPelpException There is no user authenticated.
     */
    boolean isRole(UserRoles role,ISubjectID subject) throws AuthPelpException;
    
    /**
     * Checks if the given user has the given role in this classroom.
     * @param role One of the available roles in the platform
     * @param classroom A classroom identifier
     * @param user A user identifier.
     * @return True if the user has the role in this classroom or false otherwise.
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    boolean isRole(UserRoles role,IClassroomID classroom,IUserID user) throws AuthPelpException;
    
    /**
     * Checks if the current user has the given role in this classroom.
     * @param role One of the available roles in the platform
     * @param classroom A classroom identifier
     * @return True if the user has the role in this classroom or false otherwise.
     * @throws AuthPelpException There is no user authenticated.
     */
    boolean isRole(UserRoles role,IClassroomID classroom) throws AuthPelpException;
    
    /**
     * Get the persons in the given subject with the given role.
     * @param role One of the available roles in the platform
     * @param subject A subject identifier
     * @return Array of the person ids in this subject with this role.
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    IUserID[] getRolePersons(UserRoles role,ISubjectID subject) throws AuthPelpException;
    
    /**
     * Get the persons in the given classroom with the given role.
     * @param role One of the available roles in the platform
     * @param classroom A classroom identifier
     * @return Array of the person ids in this classroom with this role.
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    IUserID[] getRolePersons(UserRoles role,IClassroomID classroom) throws AuthPelpException;
    
    /**
     * Ckechs if the given subject has some laboratories. Each laboratory is treated as a subject with different classrooms.
     * @param subject A subject identifier
     * @return True if the subject has laboratores or false otherwise
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    boolean hasLabSubjects(ISubjectID subject) throws AuthPelpException;
    
    /**
     * Returns the list of laboratories for the given subject. Each laboratory is treated as a subject with different classrooms.
     * @param subject A subject identifier
     * @return Array of laboratories
     */
    ISubjectID[] getLabSubjects(ISubjectID subject) throws AuthPelpException;
    
    /**
     * Ckechs if the given subject has some equivalent subjects
     * @param subject A subject identifier
     * @return True if the subject has equivalent subjects or false otherwise
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    boolean hasEquivalentSubjects(ISubjectID subject) throws AuthPelpException;
    
    /**
     * Returns the list of equivalent subjects for the given subject.
     * @param subject A subject identifier
     * @return Array of equivalent subjects
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    ISubjectID[] getEquivalentSubjects(ISubjectID subject) throws AuthPelpException;
    
    /**
     * Check if the user is connecting to the platform from an internal campus
     * connection or not.
     * @return True if connection if from campus network or false for external connections.
     */
    boolean isCampusConnection();
    
    /**
     * Retrieve all the information for the given subject.
     * @param subjectID Subject identifier.
     * @return Object with the information allowed for the current logged user.
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    Subject getSubjectData(ISubjectID subjectID) throws AuthPelpException;
    
    /**
     * Retrieve all the information for the given classroom.
     * @param classroomID Classroom identifier.
     * @return Object with the information allowed for the current logged user.
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    Classroom getClassroomData(IClassroomID classroomID) throws AuthPelpException;
    
    /**
     * Retrieve all the information for the current user.
     * @return Object with the information allowed for the current logged user.
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    Person getUserData() throws AuthPelpException;
    
    /**
     * Retrieve all the information for the given user.
     * @param userID User identifier.
     * @return Object with the information allowed for the current logged user.
     * @throws AuthPelpException There is no user authenticated or have not enought rights to obtain this infomation
     */
    Person getUserData(IUserID userID) throws AuthPelpException;
}
