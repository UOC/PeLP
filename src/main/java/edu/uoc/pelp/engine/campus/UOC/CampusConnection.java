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

import edu.uoc.pelp.engine.campus.*;
import edu.uoc.pelp.exception.AuthPelpException;

/**
 * Implements the campus access for the Universitat Oberta de Catalunya (UOC).
 * @author Xavier Baró
 */
public class CampusConnection implements ICampusConnection{
    
    public boolean isUserAuthenticated() {
        // TODO: Comprovar si l'usuari actual està autenticat o no. Segurament caldrà accedir al Request.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IUserID getUserID() throws AuthPelpException {
        
        // Check if the user is authenticated
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("Authentication is required");
        }
        
        // TODO: Retorna l'identificador de l'usuari actual. Cal utilitzar la classe UserID per al cas de la UOC.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Subject[] getUserSubjects() throws AuthPelpException {
        
        // Check if the user is authenticated
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("Authentication is required");
        }
        
        // TODO: Retorna la llista d'assignatures de l'usuari actual.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Classroom[] getUserClassrooms() throws AuthPelpException {
        
        // Check if the user is authenticated
        if(!isUserAuthenticated()) {
            throw new AuthPelpException("Authentication is required");
        }
        
        // TODO: Retorna la llista d'aules de l'usuari actual.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRole(UserRoles role, ISubjectID subject, IUserID user) {
        /* 
         * TODO: Comprova si l'usuari donat té el rol indicat per aquest assignatura.
         * La equivalencia de rols son:
         *         
         */
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRole(UserRoles role, ISubjectID subject) throws AuthPelpException {
        return isRole(role,subject,getUserID());
    }

    public boolean isRole(UserRoles role, IClassroomID classroom, IUserID user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRole(UserRoles role, IClassroomID classroom) throws AuthPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Person[] getRolePersons(UserRoles role, ISubjectID subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Person[] getRolePersons(UserRoles role, IClassroomID classroom) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasChildSubjects(ISubjectID subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ISubjectID[] getChildSubjects(ISubjectID subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasEquivalentSubjects(ISubjectID subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ISubjectID[] getEquivalentSubjects(ISubjectID subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCampusConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
