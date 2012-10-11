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
 * Subject information. This class should be extended using custom subject details
 * @author Xavier Baró
 */
public abstract class Subject {

    /**
     * Short description for the subject
     */
    protected String _shortName;
    
    /**
     * Full description of the subject
     */
    protected String _description;

    public Subject() {
        
    }
    
    public String getDescription() {
        return _description;
    }

    public void setDescription(String _description) {
        this._description = _description;
    }

    public String getShortName() {
        return _shortName;
    }

    public void setShortName(String _shortName) {
        this._shortName = _shortName;
    }
}
