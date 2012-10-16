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

import edu.uoc.pelp.bussines.vo.Classroom;

/**
 * Subject information for UOC classrooms
 * @author Xavier Baró
 */
public class UOCClassroom extends Classroom {
        
    public UOCClassroom(String semester, String subject, int index) {
        super(new UOCSubject(semester,subject),index);
    }
    
    public UOCClassroom(UOCSubject subject, int index) {
        super(subject,index);
    }
    
    public UOCClassroom(Classroom parentObject) {
        super(parentObject);
    }

    @Override
    public UOCSubject getSubject() {
        if(super.getSubject() instanceof UOCSubject) {
            return (UOCSubject)super.getSubject();
        }
        return new UOCSubject(super.getSubject());
    }
    
}
