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
package edu.uoc.pelp.test.engine.activity;

import edu.uoc.pelp.engine.activity.*;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.test.model.dao.MapActivityDAO;
import edu.uoc.pelp.test.model.dao.MapActivityTestDAO;
import java.util.Calendar;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 * Perform all tests over the Local implementation for the activity manager object
 * @author Xavier Baró
 */
public class LocalDAOActivityManager_Basic {
    
    private IActivityManager _activityManager=null;
    
    private void initActivityManager() {
        _activityManager=(IActivityManager) new ActivityManager(new MapActivityDAO(),new MapActivityTestDAO());
    }
    
    public LocalDAOActivityManager_Basic() {
        initActivityManager();
    }
    
    @Test 
    public void activityDatesTest() {
        
        // Create a new manager object to avoid old data
        initActivityManager();
        
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
        
        // Add a new active activity without dates
        ActivityID activityID=_activityManager.addActivity(subjectID1, null, null);
        Activity activity=_activityManager.getActivity(activityID);
        Assert.assertTrue("Active activity", activity.isActive());
        
        // Add a future activity
        activityID=_activityManager.addActivity(subjectID1, tomorrow, null);
        activity=_activityManager.getActivity(activityID);
        Assert.assertFalse("Future activity", activity.isActive());
        
        // Add a past activity
        activityID=_activityManager.addActivity(subjectID1, null, yesterday);
        activity=_activityManager.getActivity(activityID);
        Assert.assertFalse("Past activity", activity.isActive());
        
        // Add an active activity with dates
        activityID=_activityManager.addActivity(subjectID1, yesterday, tomorrow);
        activity=_activityManager.getActivity(activityID);
        Assert.assertTrue("Active activity with dates", activity.isActive());
    }
    
    @Test 
    public void addActivities() {
        // Create a new manager object to avoid old data
        initActivityManager();
                
        // Create subject identifiers
        SubjectID subjectID1=new SubjectID("05.062",new Semester("20111"));
        SubjectID subjectID2=new SubjectID("05.062",new Semester("20121"));
        
        // Create time objects
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.DATE, -1);        
        Date yesterday = c.getTime();        
        c.setTime(now); 
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();
        
        // Add a new active activity for subject 1
        Assert.assertNotNull("Adding new activity",_activityManager.addActivity(subjectID1, null, null));
        ActivityID[] activityList1={new ActivityID(subjectID1,1)};
        Assert.assertArrayEquals("Check subject addition",activityList1, _activityManager.getSubjectActivities(subjectID1));
        Assert.assertArrayEquals("Check subject addition",activityList1, _activityManager.getSubjectActiveActivities(subjectID1));
        
        // Add another active activity for subject 1
        Assert.assertNotNull("Adding new activity",_activityManager.addActivity(subjectID1, null, null));        
        ActivityID[] activityList2={new ActivityID(subjectID1,1),new ActivityID(subjectID1,2)};
        Assert.assertArrayEquals("Check subject addition",activityList2, _activityManager.getSubjectActivities(subjectID1));
        Assert.assertArrayEquals("Check subject addition",activityList2, _activityManager.getSubjectActiveActivities(subjectID1));
        
        // Add a past inactive activity for subject 1
        Assert.assertNotNull("Adding new activity",_activityManager.addActivity(subjectID1, null,yesterday));
        ActivityID[] activityList3a={new ActivityID(subjectID1,1),new ActivityID(subjectID1,2),new ActivityID(subjectID1,3)};
        ActivityID[] activityList3b={new ActivityID(subjectID1,1),new ActivityID(subjectID1,2)};
        Assert.assertArrayEquals("Check subject addition",activityList3a, _activityManager.getSubjectActivities(subjectID1));
        Assert.assertArrayEquals("Check subject addition",activityList3b, _activityManager.getSubjectActiveActivities(subjectID1));
        
        // Add a future inactive activity for subject 1
        Assert.assertNotNull("Adding new activity",_activityManager.addActivity(subjectID1, tomorrow,null));
        ActivityID[] activityList4a={new ActivityID(subjectID1,1),new ActivityID(subjectID1,2),new ActivityID(subjectID1,3),new ActivityID(subjectID1,4)};
        ActivityID[] activityList4b={new ActivityID(subjectID1,1),new ActivityID(subjectID1,2)};
        Assert.assertArrayEquals("Check subject addition",activityList4a, _activityManager.getSubjectActivities(subjectID1));
        Assert.assertArrayEquals("Check subject addition",activityList4b, _activityManager.getSubjectActiveActivities(subjectID1));        
        
        // Add a new active activity for subject 2
        Assert.assertNotNull("Adding new activity",_activityManager.addActivity(subjectID2, null, null));
        ActivityID[] activityList5={new ActivityID(subjectID2,1)};
        Assert.assertArrayEquals("Check subject addition",activityList4a, _activityManager.getSubjectActivities(subjectID1));
        Assert.assertArrayEquals("Check subject addition",activityList4b, _activityManager.getSubjectActiveActivities(subjectID1));        
        Assert.assertArrayEquals("Check subject addition",activityList5, _activityManager.getSubjectActivities(subjectID2));
           
        // Add a past inactive activity for subject 2
        Assert.assertNotNull("Adding new activity",_activityManager.addActivity(subjectID2, null, yesterday));
        ActivityID[] activityList6a={new ActivityID(subjectID2,1),new ActivityID(subjectID2,2)};
        ActivityID[] activityList6b={new ActivityID(subjectID2,1)};
        Assert.assertArrayEquals("Check subject addition",activityList4a, _activityManager.getSubjectActivities(subjectID1));
        Assert.assertArrayEquals("Check subject addition",activityList4b, _activityManager.getSubjectActiveActivities(subjectID1));        
        Assert.assertArrayEquals("Check subject addition",activityList6a, _activityManager.getSubjectActivities(subjectID2));
        Assert.assertArrayEquals("Check subject addition",activityList6b, _activityManager.getSubjectActiveActivities(subjectID2));
    }
    
    @Test 
    public void modifyActivities() {
        // Create a new manager object to avoid old data
        initActivityManager();
        
        // Create subject identifiers
        SubjectID subjectID1=new SubjectID("05.062",new Semester("20111"));
        
        // Create an inactive activity for subject 1
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.DATE, -1);        
        Date yesterday = c.getTime();
        
        c.setTime(now); 
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();
        
        // Add a new active activity for subject 1
        ActivityID a1ID=_activityManager.addActivity(subjectID1, null, null);
        Assert.assertNotNull("Adding new activity",a1ID);
        Activity a1=_activityManager.getActivity(a1ID);
        Assert.assertNotNull("Retrieve the activity",a1);
        ActivityID[] activityList1={new ActivityID(subjectID1,1)};
        Assert.assertArrayEquals("Check subject addition",activityList1, _activityManager.getSubjectActivities(subjectID1));
        Assert.assertArrayEquals("Check subject addition",activityList1, _activityManager.getSubjectActiveActivities(subjectID1));
        
        // Add another active activity for subject 1
        ActivityID a2ID=_activityManager.addActivity(subjectID1, null, null); 
        Assert.assertNotNull("Adding new activity",a2ID);
        Activity a2=_activityManager.getActivity(a2ID);
        Assert.assertNotNull("Retrieve the activity",a1);
        ActivityID[] activityList2={new ActivityID(subjectID1,1),new ActivityID(subjectID1,2)};
        Assert.assertArrayEquals("Check subject addition",activityList2, _activityManager.getSubjectActivities(subjectID1));
        Assert.assertArrayEquals("Check subject addition",activityList2, _activityManager.getSubjectActiveActivities(subjectID1));
        
        // Modify the first activity to make it inactive
        Activity modA=_activityManager.getActivity(a1.getActivity());
        Assert.assertTrue("Check initial active value", modA.isActive());
        modA.setEnd(yesterday);
        Assert.assertFalse("Check object active value after modification", modA.isActive()); 
        Assert.assertTrue("Check method return", _activityManager.editActivity(modA));
        Activity modA2=_activityManager.getActivity(a1.getActivity());
        Assert.assertFalse("Check final active value after modification", modA2.isActive());  
        
        // Modify the programming language and maximum delivers attribute
        Assert.assertNull("Undefined language",modA.getLanguage());
        Assert.assertNull("Infinite number of delivers",modA.getMaxDelivers());
        modA.setLanguage("JAVA");
        modA.setMaxDelivers(new Integer(4));
        Assert.assertTrue("Check method return", _activityManager.editActivity(modA));
        Activity modA3=_activityManager.getActivity(modA.getActivity());
        Assert.assertEquals("Language","JAVA",modA3.getLanguage());
        Assert.assertEquals("Max Delivers",new Integer(4),modA3.getMaxDelivers());
        modA3.setLanguage("C");
        modA3.setMaxDelivers(new Integer(25));
        Assert.assertEquals("Language","C",modA3.getLanguage());
        Assert.assertEquals("Max Delivers",new Integer(25),modA3.getMaxDelivers());
        Activity modA3b=_activityManager.getActivity(modA3.getActivity());
        Assert.assertEquals("Language","JAVA",modA3b.getLanguage());
        Assert.assertEquals("Max Delivers",new Integer(4),modA3b.getMaxDelivers());
        Assert.assertTrue("Check method return", _activityManager.editActivity(modA3));
        Activity modA4=_activityManager.getActivity(modA3b.getActivity());
        Assert.assertEquals("Language","C",modA4.getLanguage());
        Assert.assertEquals("Max Delivers",new Integer(25),modA4.getMaxDelivers());
    }
    
    @Test
    public void addIncorrectActivity() {
        // Create a new manager object to avoid old data
        initActivityManager();
        
        // Create subject identifiers
        SubjectID subjectID1=new SubjectID("05.062",new Semester("20111"));
        
        // Create an inactive activity for subject 1
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.DATE, -1);        
        Date yesterday = c.getTime();
        
        // Add a new activity for subject 1
        ActivityID a1ID=_activityManager.addActivity(subjectID1, now, yesterday);
    }
    
    @Test 
    public void editIncorrectActivity() {
        // Create a new manager object to avoid old data
        initActivityManager();
        
        // Create subject identifiers
        SubjectID subjectID1=new SubjectID("05.062",new Semester("20111"));
        
        // Create an inactive activity for subject 1
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.DATE, -1);        
        Date yesterday = c.getTime();
        
        // Add a new activity for subject 1, with incorrect information
        ActivityID a1ID=_activityManager.addActivity(subjectID1, now, null);
        Assert.assertNotNull("Adding new activity",a1ID);
        Activity a1=_activityManager.getActivity(a1ID);
        Assert.assertNotNull("Retrieve the activity",a1);
        a1.setEnd(yesterday);
        Assert.assertFalse("Update with incorrect information", _activityManager.editActivity(a1));
        Activity aMod=_activityManager.getActivity(a1.getActivity());
        Assert.assertNull("Verify that no change is done", aMod.getEnd());
    }
    
    @Test 
    public void addValidTests() {
        
        // Create a new manager object to avoid old data
        initActivityManager();
        
        // Create subject identifiers
        SubjectID subjectID1=new SubjectID("05.062",new Semester("20111"));
                
        // Add a new active activity without dates
        ActivityID activityID=_activityManager.addActivity(subjectID1, null, null);
        Assert.assertNotNull("Adding new activity",activityID);
        Activity activity=_activityManager.getActivity(activityID);
        Assert.assertNotNull("Retrieve the activity",activity);
        Assert.assertTrue("Active activity", activity.isActive());
        
        // Add a new test to the activity
        ActivityTest t1=new ActivityTest();
        TestID t2ID=_activityManager.addTest(activity.getActivity(), t1);
        Assert.assertNotNull("Adding new test",t2ID);
        ActivityTest t2=_activityManager.getTest(t2ID);
        Assert.assertNotNull("Adding new test",t2);
        Assert.assertEquals("Retrieve a test",t2ID, t2.getID());
        TestID[] testList1={new TestID(activity.getActivity(),1)};
        Assert.assertArrayEquals("Check subject addition",testList1, _activityManager.getActivityTests(activity.getActivity()));
    }
}
