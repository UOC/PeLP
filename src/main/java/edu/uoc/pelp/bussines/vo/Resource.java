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
 * Resource for an activity
 * @author Xavier Baró
 */
public class Resource {
    /**
     * Resource code for existing resources
     */
    private String _code;
    
    /**
     * Indicates the order of this resource when showed
     */
    private int order;
    
    /**
     * For new resources indicates the language code {"CAT","ESP","ENG",....}
     */
    private String _language;
    
    /**
     * For new resources indicates if is must be new
     */
    private boolean _public;
    
    /**
     * For new resources indicates the type of this resource {"URL","FILE", ...}
     */
    private String _type;
    
    /**
     * For new resources indicates the path to the resource
     */
    private String _path;

    public String getCode() {
        return _code;
    }

    public void setCode(String _code) {
        this._code = _code;
    }

    public String getLanguage() {
        return _language;
    }

    public void setLanguage(String _language) {
        this._language = _language;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String _path) {
        this._path = _path;
    }

    public boolean isPublic() {
        return _public;
    }

    public void setPublic(boolean _public) {
        this._public = _public;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
