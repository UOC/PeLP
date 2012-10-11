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
package edu.uoc.pelp.test.model.dao.UOC;

import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Perform all tests over the DAO object for the SemesterDO table of the database
 * @author Xavier Baró
 */
public class TDAO_Semester {
    
    private LocalSemesterDAO _semesterDAO=new LocalSemesterDAO("hibernate_test.cfg.xml"); 
    
    @Test 
    public void testAddSemester() {
               
        // Create semesters
        Semester s1=new Semester("20111",null,null);
        
        // Remove all semester data
        _semesterDAO.clearTableData();
                
        // Add a new semester
        Assert.assertTrue("Add a semester", _semesterDAO.save(s1));
        
        // Check that only one semester is added        
        List<ITimePeriod> list=_semesterDAO.findAll();
        
        Assert.assertEquals("Check findAll", 1, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
        
        // Remove all semester data
        _semesterDAO.clearTableData();
    }
    
    @Test 
    public void testAddMultipleSemesters() {
        // Create time objects
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.DATE, -1);        
        Date yesterday = c.getTime();        
        c.setTime(now); 
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();
        
        // Create semesters
        Semester s1=new Semester("20111",null,null);
        Semester s2=new Semester("20121",tomorrow,null);
        Semester s3=new Semester("20122",null,yesterday);
        Semester s4=new Semester("20131",yesterday,null);
        Semester s5=new Semester("20132",null,tomorrow);
        
        // Remove all semester data
        _semesterDAO.clearTableData();
                
        // Add a new semester
        Assert.assertTrue("Add a semester", _semesterDAO.save(s1));
        
        // Check that only one semester is added        
        List<ITimePeriod> list=_semesterDAO.findAll();        
        Assert.assertEquals("Check findAll", 1, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
        
        // Add another new semester
        Assert.assertTrue("Add other semester (1)", _semesterDAO.save(s2));
        
        // Check that the semester is added        
        list=_semesterDAO.findAll();        
        Assert.assertEquals("Check findAll", 2, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
        Assert.assertEquals("Check the semester with initial date",(Semester)list.get(1), s2);
        list=_semesterDAO.findActive();        
        Assert.assertEquals("Check findActive", 1, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
        
        // Add another new semester
        Assert.assertTrue("Add other semester (2)", _semesterDAO.save(s3));
        
        // Check that the semester is added        
        list=_semesterDAO.findAll();        
        Assert.assertEquals("Check findAll", 3, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
        Assert.assertEquals("Check the semester with initial date",(Semester)list.get(1), s2);
        Assert.assertEquals("Check the semester with final date",(Semester)list.get(2), s3);
        list=_semesterDAO.findActive();        
        Assert.assertEquals("Check findActive", 1, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
        
        // Add another new semester
        Assert.assertTrue("Add other semester (3)", _semesterDAO.save(s4));
        
        // Check that the semester is added        
        list=_semesterDAO.findAll();        
        Assert.assertEquals("Check findAll", 4, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
        Assert.assertEquals("Check the semester with initial date",(Semester)list.get(1), s2);
        Assert.assertEquals("Check the semester with final date",(Semester)list.get(2), s3);
        Assert.assertEquals("Check the semester with initial date",(Semester)list.get(3), s4);
        list=_semesterDAO.findActive();        
        Assert.assertEquals("Check findActive", 2, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
        Assert.assertEquals("Check the semester with initial date",(Semester)list.get(1), s4);
        
        // Add another new semester
        Assert.assertTrue("Add other semester (4)", _semesterDAO.save(s5));
        
        // Check that the semester is added        
        list=_semesterDAO.findAll();        
        Assert.assertEquals("Check findAll", 5, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
        Assert.assertEquals("Check the semester with initial date",(Semester)list.get(1), s2);
        Assert.assertEquals("Check the semester with final date",(Semester)list.get(2), s3);
        Assert.assertEquals("Check the semester with initial date",(Semester)list.get(3), s4);
        Assert.assertEquals("Check the semester with initial and final date",(Semester)list.get(4), s5);
        list=_semesterDAO.findActive();        
        Assert.assertEquals("Check findActive", 3, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
        Assert.assertEquals("Check the semester with initial date",(Semester)list.get(1), s4);
        Assert.assertEquals("Check the semester with initial and final date",(Semester)list.get(2), s5);
        
        // Remove all semester data
        _semesterDAO.clearTableData();
    }
    
    @Test 
    public void testUpdateSemester() {
    
        // Create time objects
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.DATE, -1);        
        Date yesterday = c.getTime();        
        c.setTime(now); 
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();
        
        // Create semesters
        Semester s1=new Semester("20111",null,null);
        
        // Remove all semester data
        _semesterDAO.clearTableData();
                
        // Add a new semester
        Assert.assertTrue("Add a semester", _semesterDAO.save(s1));
        
        // Check that only one semester is added        
        List<ITimePeriod> list=_semesterDAO.findAll();        
        Assert.assertEquals("Check findAll", 1, list.size());
        Assert.assertEquals("Check the semester data with null fields",(Semester)list.get(0), s1);
    
        // Add all information
        s1.setStartDate(now);
        s1.setEndDate(tomorrow);   
        
        // Update the data
        Assert.assertTrue("Update the semester register", _semesterDAO.update(s1));
        
        // Check the final register
        list=_semesterDAO.findAll();
        Assert.assertEquals("Only one semester exists", list.size(),1);
        Assert.assertEquals("Check the semester data with non empty fields",(Semester)list.get(0), s1);        
        
        // Remove all semester data
        _semesterDAO.clearTableData();
    }
}
