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
package edu.uoc.pelp.engine.campus.UOC.vo;

public class Profile {

    private java.lang.String appId;
    private java.lang.String app;
    private java.lang.String id;
    private java.lang.String userSubtypeId;
    private java.lang.String userType;
    private java.lang.String usertypeId;
    private java.lang.String userSubtype;
    private java.lang.String language;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserSubtypeId() {
        return userSubtypeId;
    }

    public void setUserSubtypeId(String userSubtypeId) {
        this.userSubtypeId = userSubtypeId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsertypeId() {
        return usertypeId;
    }

    public void setUsertypeId(String usertypeId) {
        this.usertypeId = usertypeId;
    }

    public String getUserSubtype() {
        return userSubtype;
    }

    public void setUserSubtype(String userSubtype) {
        this.userSubtype = userSubtype;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
