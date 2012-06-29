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

/**
 * This class contains basic information about a person.
 * @author Xavier Baró
 */
public class Person {
    /**
     * Person identifier
     */
    private IUserID _userID;
    
    /**
     * Person given name
     */
    public String _name;
    
    /**
     * Person full name
     */
    public String _fullName;
    
    /**
     * EMail
     */
    public String _eMail;
    
    /**
     * Username used in the system
     */
    public String _username;
    
    /** 
     * Default constuctor.
     * @param userID Unique identifier for the user
     */
    public Person(IUserID userID) {
        _userID=userID;
    }
    
    /**
     * Get the user unique identifier
     * @return User identifier
     */
    public IUserID getUserID() {
        return _userID;
    }
}
