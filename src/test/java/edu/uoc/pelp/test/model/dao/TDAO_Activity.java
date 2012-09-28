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

import edu.uoc.pelp.engine.activity.Activity;
import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Perform all tests over the DAO object for the SemesterDO table of the database
 * @author Xavier Baró
 */
public class TDAO_Activity {
    
    private LocalActivityDAO _activityDAO=new LocalActivityDAO(); 
    
    @Test 
    public void testAddActivities() {
                     
        // Create subject identifiers
        SubjectID subjectID1=new SubjectID("05.062",new Semester("20111"));
        SubjectID subjectID2=new SubjectID("05.062",new Semester("20121"));
        SubjectID subjectID3=new SubjectID("05.063",new Semester("20111"));
        
        // Create time objects
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.DATE, -1);        
        Date yesterday = c.getTime();        
        c.setTime(now); 
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();
        
        // Remove all semester data
        _activityDAO.clearTableData();
        
        // Add a new active activity without dates
        Activity act1=new Activity();
        ActivityID actID1=_activityDAO.add(subjectID1, act1);
        Assert.assertNotNull("Add an activity with null dates", actID1);
        act1.setActivityID(actID1);
        List<Activity> list=_activityDAO.findAll();
        Assert.assertEquals("Check activities list",1,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        list=_activityDAO.findAll(subjectID1);
        Assert.assertEquals("Check subject activities list",1,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        list=_activityDAO.findActive();
        Assert.assertEquals("Check active activities list",1,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        list=_activityDAO.findActive(subjectID1);
        Assert.assertEquals("Check subject active activities list",1,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
              
        // Add a future activity
        Activity act2=new Activity();
        act2.setStart(tomorrow);
        ActivityID actID2=_activityDAO.add(subjectID1, act2);
        Assert.assertNotNull("Add an activity with start date", actID2);
        act2.setActivityID(actID2);
        list=_activityDAO.findAll();
        Assert.assertEquals("Check activities list",2,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act2, list.get(1));
        list=_activityDAO.findAll(subjectID1);
        Assert.assertEquals("Check subject activities list",2,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act2, list.get(1));
        list=_activityDAO.findActive();
        Assert.assertEquals("Check active activities list",1,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        list=_activityDAO.findActive(subjectID1);
        Assert.assertEquals("Check subject active activities list",1,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        
        // Add a past activity
        Activity act3=new Activity();
        act3.setEnd(yesterday);
        ActivityID actID3=_activityDAO.add(subjectID1, act3);
        Assert.assertNotNull("Add an activity with end date", actID3);
        act3.setActivityID(actID3);
        list=_activityDAO.findAll();
        Assert.assertEquals("Check activities list",3,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act2, list.get(1));
        Assert.assertEquals("Compare objects", act3, list.get(2));
        list=_activityDAO.findAll(subjectID1);
        Assert.assertEquals("Check subject activities list",3,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act2, list.get(1));
        Assert.assertEquals("Compare objects", act3, list.get(2));
        list=_activityDAO.findActive();
        Assert.assertEquals("Check active activities list",1,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        list=_activityDAO.findActive(subjectID1);
        Assert.assertEquals("Check subject active activities list",1,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        
        // Add an active activity with all fields and descriptions
        Activity act4=new Activity();
        act4.setStart(yesterday);
        act4.setEnd(tomorrow);
        act4.setLanguage("JAVA");
        act4.setMaxDelivers(7);
        act4.setDescription("CAT", "Descripció en Català");
        act4.setDescription("ESP", "Descripción en Español");
        act4.setDescription("ENG", "Description in English");
        ActivityID actID4=_activityDAO.add(subjectID1, act4);
        Assert.assertNotNull("Add an activity with all information", actID4);
        act4.setActivityID(actID4);
        list=_activityDAO.findAll();
        Assert.assertEquals("Check activities list",4,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act2, list.get(1));
        Assert.assertEquals("Compare objects", act3, list.get(2));
        Assert.assertEquals("Compare objects", act4, list.get(3));
        list=_activityDAO.findAll(subjectID1);
        Assert.assertEquals("Check subject activities list",4,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act2, list.get(1));
        Assert.assertEquals("Compare objects", act3, list.get(2));
        Assert.assertEquals("Compare objects", act4, list.get(3));
        list=_activityDAO.findActive();
        Assert.assertEquals("Check active activities list",2,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act4, list.get(1));
        list=_activityDAO.findActive(subjectID1);
        Assert.assertEquals("Check subject active activities list",2,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act4, list.get(1));
        
        // Add activities to other semesters/subjects
        Activity act5=new Activity();
        ActivityID actID5=_activityDAO.add(subjectID2, act5);
        Assert.assertNotNull("Add an activity with all information", actID5);
        act5.setActivityID(actID5);
        Activity act6=new Activity();
        ActivityID actID6=_activityDAO.add(subjectID3, act6);        
        Assert.assertNotNull("Add an activity with all information", actID6);
        act6.setActivityID(actID6);
        list=_activityDAO.findAll();
        Assert.assertEquals("Check activities list",6,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act2, list.get(1));
        Assert.assertEquals("Compare objects", act3, list.get(2));
        Assert.assertEquals("Compare objects", act4, list.get(3));
        Assert.assertEquals("Compare objects", act6, list.get(4));
        Assert.assertEquals("Compare objects", act5, list.get(5));
        list=_activityDAO.findAll(subjectID1);
        Assert.assertEquals("Check subject activities list",4,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act2, list.get(1));
        Assert.assertEquals("Compare objects", act3, list.get(2));
        Assert.assertEquals("Compare objects", act4, list.get(3));
        list=_activityDAO.findActive();
        Assert.assertEquals("Check active activities list",4,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act4, list.get(1));
        Assert.assertEquals("Compare objects", act6, list.get(2));
        Assert.assertEquals("Compare objects", act5, list.get(3));
        list=_activityDAO.findActive(subjectID1);
        Assert.assertEquals("Check subject active activities list",2,list.size());
        Assert.assertEquals("Compare objects", act1, list.get(0));
        Assert.assertEquals("Compare objects", act4, list.get(1));
        
        // Remove all semester data
        _activityDAO.clearTableData();
    }
   
    @Test 
    public void testUpdateActivity() {
        // Create subject identifiers
        SubjectID subjectID1=new SubjectID("05.062",new Semester("20111"));
        
        // Create time objects
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.DATE, -1);        
        Date yesterday = c.getTime();        
        c.setTime(now); 
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();
        
        // Remove all semester data
        _activityDAO.clearTableData();
        
        // Add a new active activity without dates
        Activity act1=new Activity();
        ActivityID actID1=_activityDAO.add(subjectID1, act1);
        Assert.assertNotNull("Add an activity with null dates", actID1);
        act1.setActivityID(actID1);
        
        // Get the registered object
        Activity srcObj=_activityDAO.find(actID1);
        Assert.assertEquals("Check that the stored object is correct", act1, srcObj);
        
        // Add all fields
        act1.setStart(yesterday);
        act1.setEnd(tomorrow);
        act1.setLanguage("JAVA");
        act1.setMaxDelivers(7);
        act1.setDescription("CAT", "Descripció en Català");
        act1.setDescription("ESP", "Descripción en Español");
        act1.setDescription("ENG", "Description in English");
               
        // Update the register
        Assert.assertTrue("Call update method",_activityDAO.update(act1));
        
        // Get the updated registered object
        Activity updObj=_activityDAO.find(actID1);
        Assert.assertEquals("Check that the stored udpated object is correct", act1, updObj);
        
        // Change one of the descriptions
        act1.setDescription("CAT", "Descripció actualitzada");
        
        // Update the register
        Assert.assertTrue("Call update method",_activityDAO.update(act1));
        
        // Get the updated registered object
        updObj=_activityDAO.find(actID1);
        Assert.assertEquals("Check that the stored udpated object is correct", act1, updObj);
        
        // Delete one of the descriptions
        act1.setDescription("CAT", null);
        
        // Update the register
        Assert.assertTrue("Call update method",_activityDAO.update(act1));
        
        // Get the updated registered object
        updObj=_activityDAO.find(actID1);
        Assert.assertEquals("Check that the stored udpated object is correct", act1, updObj);
        
        // Remove all semester data
        _activityDAO.clearTableData();
    }
}
