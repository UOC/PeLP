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

import edu.uoc.pelp.engine.campus.IClassroomID;
import edu.uoc.pelp.engine.campus.IPelpID;
import edu.uoc.pelp.exception.PelpException;

/**
 * Implementation for the classroom identifier in the campus of the Universitat Oberta de Catalunya
 * @author Xavier Baró
 */
public class ClassroomID implements IClassroomID {

    /**
     * Classrooms are identified by the subjectID and a numeric index from 1 to N. 
     */
    private SubjectID _subjectCode;
    private Integer _classIdx;
    
    public ClassroomID(SubjectID subject, int classIdx) {
        _subjectCode=subject;
        _classIdx=classIdx;
    }
    
    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(IClassroomID t) {
        ClassroomID arg=(ClassroomID) t;
        if(_subjectCode!=null) {
            if(_subjectCode.compareTo(arg._subjectCode)!=0) {
                return _subjectCode.compareTo(arg._subjectCode);
            }
        } else {
            if(arg._subjectCode!=null) {
                return -1;
            }
        }
        if(_classIdx!=null) {
            return _classIdx.compareTo(arg._classIdx);
        } else {
            if(arg._classIdx!=null) {
                return -1;
            }
        }
        
        return 0;
    }

    protected void copyData(IPelpID genericID) throws PelpException {
        if (genericID instanceof ClassroomID) {
            _subjectCode=((ClassroomID)genericID)._subjectCode;
            _classIdx=((ClassroomID)genericID)._classIdx;
        } else {
            throw new PelpException("Object of type " + genericID.getClass() + " cannot be copided to an object of class " + this.getClass());
        }
    }

    /**
     * Get the index of this classroom inside the subject
     * @return Classroom index
     */
    public Integer getClassIdx() {
        return _classIdx;
    }

    /**
     * Get the subject that contains this classroom
     * @return The subject identifier for this class
     */
    public SubjectID getSubject() {
        return _subjectCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClassroomID other = (ClassroomID) obj;
        if (this._subjectCode != other._subjectCode && (this._subjectCode == null || !this._subjectCode.equals(other._subjectCode))) {
            return false;
        }
        if (this._classIdx != other._classIdx && (this._classIdx == null || !this._classIdx.equals(other._classIdx))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this._subjectCode != null ? this._subjectCode.hashCode() : 0);
        hash = 59 * hash + (this._classIdx != null ? this._classIdx.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(IPelpID id) {
        ClassroomID arg=(ClassroomID)id;
        if(_subjectCode!=null) {
            if(_subjectCode.compareTo(arg._subjectCode)!=0) {
                return _subjectCode.compareTo(arg._subjectCode);
            }
        } else {
            if(arg._subjectCode!=null) {
                return -1;
            }
        }
        if(_classIdx!=null) {
            return _classIdx.compareTo(arg._classIdx);
        } else {
            if(arg._classIdx!=null) {
                return -1;
            }
        }
        
        return 0;
    }

    @Override
    public IPelpID parse(String str) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
