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

import edu.uoc.pelp.exception.AuthPelpException;
import java.util.ArrayList;

/**
 * This class represents the Security Considerations to take into account for each
 * security level. It defines the access to the services and resources, and checks 
 * the basic 
 * @author Xavier Baró
 */
public enum SecurityLevels {
    SC1("Services Access"),
    SC2("Task Access"),
    SC3("Delivery Access"),
    SC4("Resources Access"),
    SC5("Students Information Access");
    
    /**
     * Description of the security level
     */
    private String _description=null;
    
    /**
     * Define a new security consideration given a description.
     * @param desc Description for this security consideration
     */
    SecurityLevels(String desc) {
        _description=desc;
    }
    
    /**
     * Check security considerations for given level, and throws an exception if 
     * are not fitted.
     * @param levelCode String with the sercurity consideration level
     * @param referencedElements List of all elements included in the verification or null if no elements are passed.
     * @throws AuthPelpException if the security considerationa are not met. 
     */
    public void checkLevel(SecurityLevels level,EngineAuthManager authManager,ArrayList referencedElements) throws AuthPelpException {
        // TODO: Apply security considerations, taking into account the following information:
        /**
         * In this class, we check the basic properties of all elements related to the given 
         * security level. Specific methods can be created for specific consideration levels, such as:
         * 
         *           checkCS2UseLevel(Mode mode, EngineAuthManager authManager, ArrayList tasks)
         * or 
         * 
         *  create a type CSParameter and pass an arraylist of this new class, where each element has the element and the 
         * requested access (USE/WRITE/READ/REVIEW).
         */
        throw new AuthPelpException(level._description + ": Denied");
    }
}
