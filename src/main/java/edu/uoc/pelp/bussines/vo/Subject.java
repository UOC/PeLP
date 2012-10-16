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
public class Subject {
    
    /**
     * Subject identifier
     */
    protected String _subjectID;
    
    /**
     * Short description for the subject
     */
    protected String _shortName;
    
    /**
     * Full description of the subject
     */
    protected String _description;
    
    /**
     * Default constructor with identifier
     * @param subjectID Subject identifier
     */
    public Subject(String subjectID) {
        _subjectID=subjectID;
    }
    
    /**
     * Default copy constructor
     * @param Object to be copied
     */
    public Subject(Subject object) {
        _subjectID=object._subjectID;
        _shortName=object._shortName;
        _description=object._description;
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

    public String getSubjectID() {
        return _subjectID;
    }

    public void setSubjectID(String subjectID) {
        this._subjectID = subjectID;
    }    
}
