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
package edu.uoc.pelp.engine.auth;

import edu.uoc.pelp.engine.campus.ICampusConnection;
import edu.uoc.pelp.exception.AuthPelpException;

/**
 * This class provides all the mechanisms for user and applications authentication.
 * @author Xavier Baró
 */
final public class EngineAuthManager {
    
    private ICampusConnection _campusConnection;
    private ISecurityDataManager _securityDataManager;
    private SecurityLevels _securityLevels;
            
    /**
     * Check security considerations for given level, and throws an exception if 
     * are not fitted.
     * @param levelCode String with the code of 
     * @throws AuthPelpException if the security considerationa are not met. 
     */
    void assertSecurityLevel(SecurityLevels levelCode) throws AuthPelpException {
        // TODO: Use the SecurityLevels class to check all the restrictions
        //_securityLevels.checkLevel(levelCode, this, );
        
    }
    
    /**
     * Check if the user is authenticated.
     * @return True if it is authenticated and False if it is not.
     */
    boolean isUserAuthenticated() throws AuthPelpException{
        
        // Ensure that access to campus is provided
        assert _campusConnection!=null;
        
        // Call the campus connection to authenticate current session
        return _campusConnection.isUserAuthenticated();
        
        // TODO: Add connection to local database in order to enable application roles.
    }
}
