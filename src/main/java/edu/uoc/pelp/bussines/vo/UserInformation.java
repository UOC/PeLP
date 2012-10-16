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
package edu.uoc.pelp.bussines.vo;

/**
 * User information
 * @author Xavier Baró
 */
public class UserInformation {
    /**
     * User identifier
     */
    private String _userID;
    
    /**
     * User photo
     */
    private String _userPhoto;
    
    /**
     * Full name of the user
     */
    private String _userFullName;  
    
    /**
     * Username of the user
     */
    private String _username;
    
    /**
     * Email of the user
     */
    private String _eMail;
    
    /**
     * User language
     */
    private String _language;

    public String getUserPhoto() {
        return _userPhoto;
    }

    public void setUserPhoto(String _userPhoto) {
        this._userPhoto = _userPhoto;
    }

    public String geteMail() {
        return _eMail;
    }

    public void seteMail(String _eMail) {
        this._eMail = _eMail;
    }

    public String getUserFullName() {
        return _userFullName;
    }

    public void setUserFullName(String _userFullName) {
        this._userFullName = _userFullName;
    }

    public String getUserID() {
        return _userID;
    }

    public void setUserID(String _userID) {
        this._userID = _userID;
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String _username) {
        this._username = _username;
    }

    public String getLanguage() {
        return _language;
    }

    public void setLanguage(String _language) {
        this._language = _language;
    }
    
    
}
