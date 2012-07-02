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

import edu.uoc.pelp.engine.campus.ISubjectID;

/**
 * Implementation for the subject identifier in the campus of the Universitat Oberta de Catalunya
 * @author Xavier Baró
 */
public class SubjectID implements ISubjectID {

    /**
     * Subjects are identified by their code value. 
     */
    private String _code;
    
    /**
     * Subjects in different semesters are treated as different subjects
     */
    private Semester _semester;
    
    public SubjectID(String code,Semester semester) {
        _code=code;
        _semester=semester;
    }
    
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(ISubjectID t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Semester getSemester() {
        return _semester;
    }
    
    public String getCode() {
        return _code;
    }
}
