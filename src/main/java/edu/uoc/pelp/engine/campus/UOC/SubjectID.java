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

import edu.uoc.pelp.engine.campus.IPelpID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.exception.PelpException;

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
    
    /**
     * Domain Identifier for this object in the UOC Campus implementation
     */
    private String _domainID;
    
    public SubjectID(String code,Semester semester) {
        _code=code;
        _semester=semester;
    }
    
    public SubjectID(String code,Semester semester,String domainID) {
        _code=code;
        _semester=semester;
        _domainID=domainID;
    }
    
    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(ISubjectID t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setSemester(Semester semester) {
        _semester=semester;
    }
    
    public Semester getSemester() {
        return _semester;
    }
    
    public void setCode(String code) {
        _code=code;
    }
    
    public String getCode() {
        return _code;
    }
    
    public void setDomainID(String domainID) {
        _domainID=domainID;
    }
    
    public String getDomainID() {
        return _domainID;
    }

    @Override
    public int compareTo(IPelpID arg0) {
        SubjectID arg=(SubjectID)arg0;
        if(_semester.compareTo(arg._semester)!=0) {
            return _semester.compareTo(arg._semester);
        }
        return _code.compareTo(arg._code);
    }

    protected void copyData(IPelpID genericID) throws PelpException {
        if (genericID instanceof SubjectID) {
            _semester=((SubjectID)genericID)._semester;
            _code=((SubjectID)genericID)._code;
            _domainID=((SubjectID)genericID)._domainID;
        } else {
            throw new PelpException("Object of type " + genericID.getClass() + " cannot be copided to an object of class " + this.getClass());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubjectID other = (SubjectID) obj;
        if ((this._code == null) ? (other._code != null) : !this._code.equals(other._code)) {
            return false;
        }
        if ((this._domainID == null) ? (other._domainID != null) : !this._domainID.equals(other._domainID)) {
            return false;
        }
        if (this._semester != other._semester && (this._semester == null || !this._semester.equals(other._semester))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this._code != null ? this._code.hashCode() : 0);
        hash = 23 * hash + (this._semester != null ? this._semester.hashCode() : 0);
        hash = 23 * hash + (this._domainID != null ? this._domainID.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        String msg=_semester + "__" + _code;
        
        if(_domainID!=null) {
            msg += "__" + _domainID;
        }
        return msg;
    }

    @Override
    public IPelpID parse(String str) {
        if(str==null) {
            return null;
        }
        int pos=str.indexOf("__");
        if(pos<0) {
            return null;
        }
        Semester semester=new Semester(str.substring(0,pos));
        String code=str.substring(pos+2);
        
        String domainID=null;
        pos=code.indexOf("__");
        if(pos>=0) {
            domainID=code.substring(pos+2);
            code=code.substring(0,pos);
        }
        
        return new SubjectID(code,semester,domainID);
    }
}
