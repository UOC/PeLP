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
     * Retrieve all the subjects where the current logged used is inscribed,
     * both if is the teacher or if is student.
     * @return Array with the subjects.
     * @throws AuthPelpException There is no user authenticated.
     */
    Subject[] getUserSubjects() throws AuthPelpException;
    
    /**
     * Retrieve all the classrooms where the current logged used is inscribed,
     * both if is the teacher or if is student.
     * @return Array with the classrooms.
     * @throws AuthPelpException There is no user authenticated.
     */
    Classroom[] getUserClassrooms() throws AuthPelpException;
    
    /**
     * Checks if the given user has the given role in this subject.
     * @param role One of the available roles in the platform
     * @param subject A subject identifier
     * @param user A user identifier.
     * @return True if the user has the role in this subject or false otherwise.
     */
    boolean isRole(UserRoles role,ISubjectID subject,IUserID user);
    
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
     */
    boolean isRole(UserRoles role,IClassroomID classroom,IUserID user);
    
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
     * @return Array of the persons in this subject with this role.
     */
    Person[] getRolePersons(UserRoles role,ISubjectID subject);
    
    /**
     * Get the persons in the given classroom with the given role.
     * @param role One of the available roles in the platform
     * @param classroom A classroom identifier
     * @return Array of the persons in this classroom with this role.
     */
    Person[] getRolePersons(UserRoles role,IClassroomID classroom);
    
    /**
     * Ckechs if the given subject has some child subjects
     * @param subject A subject identifier
     * @return True if the subject has child subjects or false otherwise
     */
    boolean hasChildSubjects(ISubjectID subject);
    
    /**
     * Returns the list of child subjects for the given subject.
     * @param subject A subject identifier
     * @return Array of child subjects
     */
    ISubjectID[] getChildSubjects(ISubjectID subject);
    
    /**
     * Ckechs if the given subject has some equivalent subjects
     * @param subject A subject identifier
     * @return True if the subject has equivalent subjects or false otherwise
     */
    boolean hasEquivalentSubjects(ISubjectID subject);
    
    /**
     * Returns the list of equivalent subjects for the given subject.
     * @param subject A subject identifier
     * @return Array of equivalent subjects
     */
    ISubjectID[] getEquivalentSubjects(ISubjectID subject);
    
    /**
     * Check if the user is connecting to the platform from an internal campus
     * connection or not.
     * @return True if connection if from campus network or false for external connections.
     */
    boolean isCampusConnection();
}
