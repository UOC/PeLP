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
package edu.uoc.pelp.model.dao.admin;

import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.model.vo.admin.PelpAdmins;

/**
 * This interface defines the basic operations of the DAO for admnistration tables
 * @author Xavier Baró
 */
public interface IAdministrationDAO {
    
    /** 
     * Adds a new administrator to the platform.
     * @param person Person object for the new administrator
     * @param active Indicates if the new administrator is created as active administrator or not
     * @param grant True for administrators with grant option and False for administrators without grant option.
     * @return True if the administrator is correctly added or False otherwise
     */
    boolean addAdmin(Person person, boolean active, boolean grant);
    
    /**
     * Get the information for the admin register related to given person
     * @param person Person object
     * @return Administration information for this person or null if it does not exist.
     */
    public PelpAdmins getAdminData(Person person);
    
    /**
     * Update the administration information for a given person
     * @param person Person object
     * @param active Active flag, that indicates that is currently an administrator or not
     * @param grant Grant flag, that indicates if the user can create new administrators or not
     * @return True if the administrator is correctly added or False otherwise
     */
    public boolean updateAdmin(Person person, boolean active, boolean grant);
    
    /**
     * Deletes the administration data for a given person
     * @param person Person object
     * @return True if the administrator is correctly added or False otherwise
     */
    public boolean delAdmin(Person person);
}
