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
import edu.uoc.pelp.engine.campus.IClassroomID;
import edu.uoc.pelp.exception.PelpException;

/**
 * Implementation for the classroom identifier in the campus of the Universitat Oberta de Catalunya
 * @author Xavier Baró
 */
public class ClassroomID extends GenericID {

    /**
     * Classrooms are identified by the subjectID and a numeric index from 1 to N. 
     */
    private SubjectID _subjectCode;
    private Integer _classIdx;
    
    public ClassroomID(SubjectID subject, int classIdx) {
        _subjectCode=subject;
        _classIdx=classIdx;
    }
    
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(IClassroomID t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void copyData(GenericID genericID) throws PelpException {
        if (genericID instanceof ClassroomID) {
            _subjectCode=((ClassroomID)genericID)._subjectCode;
            _classIdx=((ClassroomID)genericID)._classIdx;
        } else {
            throw new PelpException("Object of type " + genericID.getClass() + " cannot be copided to an object of class " + this.getClass());
        }
    }
}
