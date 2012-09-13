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
package edu.uoc.pelp.test.model.dao;

import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.model.dao.UOC.SemesterDAO;
import javax.annotation.Resource;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Perform all tests over the DAO object for the SemesterDO table of the database
 * @author Xavier Baró
 */
@ContextConfiguration("/test-daos.xml")
public class TDAO_Semester {
    
    @Resource
    SemesterDAO semesterDAO;
    
    //private SemesterJPA _semesterDAO;
    
    public TDAO_Semester() {
        //EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("pelpDS");
        //_semesterDAO=new SemesterJPA(null,emf);
    }
    
    @Test 
    public void testNotAuthUserRightsToCampus() {
        Semester s1=new Semester("20111",null,null);
        
        //Assert.assertTrue("Add a new semester",semesterDAO.save(s1));
    }
}
