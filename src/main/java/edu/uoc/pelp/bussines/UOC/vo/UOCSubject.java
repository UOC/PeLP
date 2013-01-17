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
package edu.uoc.pelp.bussines.UOC.vo;

import edu.uoc.pelp.bussines.vo.Subject;

/**
 * Subject information for UOC subjects
 * @author Xavier Baró
 */
public class UOCSubject extends Subject {
	
	
        
    public UOCSubject(String semester, String subject) {
        super(getStrSubjectID(semester,subject));
    }
    
    public UOCSubject(Subject parentObject) {
        super(parentObject);
    }

    public String getSemesterCode() {
        return getSemesterFromID(getSubjectID());
    }

    public void setSemesterCode(String semesterCode) {
        setSubjectID(getStrSubjectID(semesterCode,getSubjectCode()));
    }

    @Override
    public void setSubjectID(String subjectID) {
        super.setSubjectID(subjectID);
    }

    public String getSubjectCode() {
        return getSubjectFromID(getSubjectID());
    }

    public void setSubjectCode(String subjectCode) {
        setSubjectID(getStrSubjectID(getSemesterCode(),subjectCode));
    }
    
    private static String getSubjectFromID(String subjectID) {
        if(subjectID==null) {
            return null;
        }
        
        int pos=subjectID.indexOf("__");
        if(pos<0) {
            return null;
        }

        String ret=subjectID.substring(pos+2);
        if(ret.length()==0) {
            return null;
        }
        
        return ret;
    }
    
    private static String getSemesterFromID(String subjectID) {
        if(subjectID==null) {
            return null;
        }
        
        int pos=subjectID.indexOf("__");
        if(pos<0) {
            return null;
        }
        
        String ret=subjectID.substring(0,pos);
        if(ret.length()==0) {
            return null;
        }
        
        return ret;
    }
    
    private static String getStrSubjectID(String semester,String subject) {
        if(semester==null) {
            semester="";
        }
        if(subject==null) {
            subject="";
        }
        return semester + "__" + subject;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UOCSubject other = (UOCSubject) obj;
        if ((this.getSubjectCode() == null) ? (other.getSubjectCode() != null) : !this.getSubjectCode().equals(other.getSubjectCode())) {
            return false;
        }
        if ((this.getSemesterCode() == null) ? (other.getSemesterCode() != null) : !this.getSemesterCode().equals(other.getSemesterCode())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.getSubjectCode() != null ? this.getSubjectCode().hashCode() : 0);
        hash = 71 * hash + (this.getSemesterCode() != null ? this.getSemesterCode().hashCode() : 0);
        return hash;
    }
    
    
}
