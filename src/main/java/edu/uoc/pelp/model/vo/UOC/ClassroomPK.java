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
package edu.uoc.pelp.model.vo.UOC;

import edu.uoc.pelp.model.vo.DeliverPK;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Entity class representing internal representation for classrooms
 * @author Xavier Baró
 */
@Embeddable
public class ClassroomPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "semester",length = 5, columnDefinition="char(5)")
    private String semester;
    @Basic(optional = false)
    @Column(name = "subject",length = 6, columnDefinition="char(6)")
    private String subject;
    @Basic(optional = false)
    @Column(name = "index")
    private int index;
    
    public ClassroomPK() {

    }
    
    public ClassroomPK(SubjectPK subject, int index) {
        this.semester=subject.getSemester();
        this.subject=subject.getSubject();
        this.index=index;
    }
    
    public ClassroomPK(String codedClassroom) {
        if(codedClassroom!=null) {
            String[] semesterFields=codedClassroom.split("_\\.");
            for(String field:semesterFields) {
                if(field.startsWith("sem=_") && field.length()>4) {
                    semester=field.substring(5).trim();
                }
                if(field.startsWith("sub=_") && field.length()>4) {
                    subject=field.substring(5).trim();
                }
                if(field.startsWith("idx=_") && field.length()>4) {
                    String strIdx=field.substring(5);
                    if(!strIdx.trim().isEmpty()) {
                        index=Integer.parseInt(strIdx);
                    }
                }
            }
        }
    }

    public SubjectPK getSubject() {
        return new SubjectPK(semester,subject);
    }

    public void setSubject(SubjectPK subject) {
        this.semester = subject.getSemester();
        this.subject=subject.getSubject();
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    @Override
    public String toString() {
        return "sem=_" + semester + "_.sub=_" + subject + "_.idx=_" + index; 
    }
}
