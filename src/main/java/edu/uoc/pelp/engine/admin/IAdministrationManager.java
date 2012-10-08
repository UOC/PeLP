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
package edu.uoc.pelp.engine.admin;

import edu.uoc.pelp.engine.campus.Person;

/**
 * This interface describes the methods for administration managing.
 * @author Xavier Baró
 */
public interface IAdministrationManager {
    /**
     * Check if the given person has administraton rights
     * @param person Person information object
     * @return True if this person has administrative rights or False otherwise
     */
    public boolean isAdministrator(Person person);
    
    /**
     * Check if the given person has administraton rights with grant option
     * @param person Person information object
     * @return True if this person has administrative rights with grant option or False otherwise
     */
    public boolean isSuperAdministrator(Person person);
    
    /** 
     * Adds a new administrator to the platform. Only active administrators with grant option can add new administrators
     * @param person Person object for the new administrator
     * @param active Indicates if the new administrator is created as active administrator or not
     * @param grant True for administrators with grant option and False for administrators without grant option.
     * @return True if the administrator is correctly added or False otherwise
     */
    public boolean addAdministrator(Person person,boolean active,boolean grant);
    
    /**
     * Removes an administrator from the platform. Only active administrators with grant option can add new administrators.
     * @param person Person object for the new administrator
     * @return True if the administrator is correctly removed or False otherwise 
     */
    public boolean deleteAdministrator(Person person);
    
    /**
     * Updates an administrator from the platform. Only active administrators with grant option can add new administrators.
     * @param person Person object for the administrator to be updated
     * @param active Indicates if the new administrator is created as active administrator or not
     * @param grant True for administrators with grant option and False for administrators without grant option.
     * @return True if the administrator is correctly updated or False otherwise 
     */
    public boolean updateAdministrator(Person person,boolean active,boolean grant);
}
