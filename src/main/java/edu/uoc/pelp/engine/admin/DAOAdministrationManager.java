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
import edu.uoc.pelp.model.dao.admin.IAdministrationDAO;
import edu.uoc.pelp.model.vo.admin.PelpAdmins;
import org.hibernate.Query;

/**
 * Implements a class to manage administration data.
 * @author Xavier Baró
 */
public class DAOAdministrationManager implements IAdministrationManager {
    
    /**
     * Administration DAO
     */
    private IAdministrationDAO _administration;
    
    /**
     * Default constructor for the DAOActivityManager
     * @param adminDAO Object to access all the administration information
     */
    public DAOAdministrationManager(IAdministrationDAO adminDAO) {
        _administration=adminDAO;
    }

    @Override
    public boolean isAdministrator(Person person) {
        // Check the parameters
        if(person==null) {
            return false;
        }
        
        // Get the administrator details
        PelpAdmins adminData=_administration.getAdminData(person);
        if(adminData==null) {
            return false;
        }
        
        // Check if is active or not
        if(adminData.getActive()!=true) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean isSuperAdministrator(Person person) {
        // Check if the parameters
        if(person==null) {
            return false;
        }
        
        // Get the administrator details
        PelpAdmins adminData=_administration.getAdminData(person);
        if(adminData==null) {
            return false;
        }
        
        // Check if is active or not
        if(adminData.getActive()!=true) {
            return false;
        }
        
        // Check if it have grant permission
        if(adminData.getGrantAllowed()!=true) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean addAdministrator(Person person, boolean active, boolean grant) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteAdministrator(Person person) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateAdministrator(Person person, boolean active, boolean grant) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
