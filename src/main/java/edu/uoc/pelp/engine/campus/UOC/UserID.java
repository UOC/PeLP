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

import edu.uoc.pelp.engine.campus.GenericID;
import edu.uoc.pelp.engine.campus.IUserID;

/**
 * Implementation for the user identifier in the campus of the Universitat Oberta de Catalunya
 * @author Xavier Baró
 */
public class UserID extends GenericID implements IUserID {
    
    /**
     * Users are identified by their id value. 
     */
    public String idp = null;
    
    public UserID(String idp) {
        this.idp=idp;
    }
    
    public int compareTo(IUserID userID) {
        String strUserID=((UserID)userID).idp;
        
        // Try to delegate NULL users to the end of sorted lists
        if(idp==null) {      
            if(strUserID==null) {
                return 0;
            } else {
                return 1;
            }
        }
        
        return idp.compareTo(strUserID);
    }

    @Override
    protected void copyData(GenericID genericID) {
        idp=((UserID)genericID).idp;
    }
}
