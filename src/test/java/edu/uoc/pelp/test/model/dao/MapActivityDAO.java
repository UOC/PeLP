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
import edu.uoc.pelp.engine.activity.ActivityTest;
import edu.uoc.pelp.engine.activity.TestID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.model.dao.IActivityDAO;
import edu.uoc.pelp.model.vo.*;
import edu.uoc.pelp.model.vo.UOC.SubjectPK;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implements the DAO object for the Activities. It uses memory structures to store de data
 * and copy objects to break the references.
 * @author Xavier Baró
 */
public class MapActivityDAO implements IActivityDAO {
    
    /**
     * Table simulating the database table
     */
    private HashMap<ActivityPK,edu.uoc.pelp.model.vo.Activity> _activity=new HashMap<ActivityPK,edu.uoc.pelp.model.vo.Activity>();
    
    /**
     * Table simulating the database table
     */
    private HashMap<ActivityDescPK,ActivityDesc> _activityDesc=new HashMap<ActivityDescPK,ActivityDesc>();
    
    /**
     * Table simulating the database table
     */
    private HashMap<ActivityTestPK,edu.uoc.pelp.model.vo.ActivityTest> _activityTest=new HashMap<ActivityTestPK,edu.uoc.pelp.model.vo.ActivityTest>();
    
    /**
     * Table simulating the database table
     */
    private HashMap<TestDescPK,TestDesc> _testDesc=new HashMap<TestDescPK,TestDesc>();
    

    @Override
    public synchronized ActivityID add(ISubjectID subjectID, Activity object) {
        
        // Check the input object
        if(object==null || subjectID==null) {
            return null;
        }
        
        // Get the id of the last deliver
        ActivityID lastID=getLastID(subjectID);
        
        // Create the new id
        ActivityID newID;
        if(lastID!=null) {
            newID=new ActivityID(lastID);
            newID.index++;
        } else {
            newID=new ActivityID(subjectID,1);
        }
        
        // Get the object
        edu.uoc.pelp.model.vo.Activity newObj=ObjectFactory.getActivityReg(object);
        
        // Add a new object from given object, to break the reference
        ActivityPK key=ObjectFactory.getActivityPK(newID);
        if(key==null) {
            return null;
        }
        
        // Add the new object
        newObj.setActivityPK(key);
        _activity.put(key, newObj);
        
        // Add the descriptions for current object
        for(String lang:object.getLanguageCodes()) {
            String desc=object.getDescription(lang);
            if(desc!=null) {
                ActivityDescPK descPK=new ActivityDescPK(key,lang);
                _activityDesc.put(descPK,new ActivityDesc(descPK,desc));
            }
        }
        
        return newID;
    }

    @Override
    public boolean delete(ActivityID id) {
        
        // Check the input object
        if(id==null) {
            return false;
        }
        
        // Get the primary key
        ActivityPK key=ObjectFactory.getActivityPK(id);
        
        // Remove descriptions
        if(!deleteDescriptions(key)) {
            return false;
        }
        
        // Check that the object does not exists
        if(_activity.remove(key)==null) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean update(Activity object) {
        // Check the input object
        if(object==null) {
            return false;
        }
        
        // Get the primary key
        ActivityPK key=ObjectFactory.getActivityPK(object.getActivity());
        
        // Get a low-level representation
        edu.uoc.pelp.model.vo.Activity newObj=ObjectFactory.getActivityReg(object);
        
        // Check that the object does not exists
        if(newObj!=null) {
            if(_activity.remove(key)==null) {
                return false;
            }
            // Store the new object
            _activity.put(key, newObj);
        } else {
            return false;
        }
        
        // Delete old descriptions
        if(!deleteDescriptions(key)) {
            return false;
        }
        
        // Add new descriptions
        for(String lang:object.getLanguageCodes()) {
            String desc=object.getDescription(lang);
            if(desc!=null) {
                ActivityDescPK descPK=new ActivityDescPK(key,lang);
                _activityDesc.put(descPK,new ActivityDesc(descPK,desc));
            }
        }
        
        
        return _activity.containsKey(key);
    }

    @Override
    public List<Activity> findAll() {
        ArrayList<Activity> list=new ArrayList<Activity>();
        
        // Build the output list
        for(ActivityPK activityPK:_activity.keySet()) { 
            // Get the Id 
            ActivityID activityID=ObjectFactory.getActivityID(activityPK);
            
            // Create the activity object
            Activity actObj=find(activityID);
           
            // Add the object to the output list
            if(actObj!=null) {
                list.add(actObj);
            }
        }
        
        return list;
    }

    @Override
    public List<Activity> findActive() {
        ArrayList<Activity> list=new ArrayList<Activity>();
        
        // Build the output list
        for(ActivityPK activityPK:_activity.keySet()) { 
            // Get the Id 
            ActivityID activityID=ObjectFactory.getActivityID(activityPK);
            if(activityID==null) {
                continue;
            }
            
            // Create the activity object
            Activity actObj=find(activityID);
            
            // Add the object to the output list
            if(actObj!=null) {
                if(actObj.isActive()) {
                    list.add(actObj);
                }
            }
        }
        
        return list;
    }

    @Override
    public Activity find(ActivityID object) {
        // Check the input object
        if(object==null) {
            return null;
        }
        
        // Get the key
        ActivityPK key=ObjectFactory.getActivityPK(object);
        
        // Create the list of descriptions
        List<ActivityDescPK> descList=getActivityDescriptions(key);
        ActivityDesc[] descriptions=null;
        if(descList.size()>0) {
            // Create the descriptions array
            descriptions=new ActivityDesc[descList.size()];
            for(int i=0;i<descList.size();i++) {                    
                descriptions[i]=_activityDesc.get(descList.get(i));
            }
        }
        
        // Get the activity register
        edu.uoc.pelp.model.vo.Activity activityReg=_activity.get(key);
            
        // Create the activity object
        Activity actObj=ObjectFactory.getActivityObj(activityReg, descriptions);
           
        return actObj;
    }

    @Override
    public List<Activity> findAll(ISubjectID subject) {
        ArrayList<Activity> list=new ArrayList<Activity>();
        
        // Get the subject ID
        SubjectPK subjectPK=ObjectFactory.getSubjectPK(subject); 
        if(subjectPK==null) {
            return null;
        }
        
        // Build the output list
        for(ActivityPK activityPK:_activity.keySet()) {
            SubjectPK activitySubject=new SubjectPK(activityPK);
            if(activitySubject.equals(subjectPK)) {
                ActivityID activityID=ObjectFactory.getActivityID(activityPK);
                if(activityID==null) {
                    continue;
                }
            
                // Create the activity object
                Activity actObj=find(activityID);
                if(actObj!=null) {
                    list.add(actObj);
                }
            }
        }
        
        return list;
    }

    @Override
    public List<Activity> findActive(ISubjectID subject) {
        
        ArrayList<Activity> list=new ArrayList<Activity>();
        
        // Get the subject ID
        SubjectPK subjectPK=ObjectFactory.getSubjectPK(subject); 
        if(subjectPK==null) {
            return null;
        }
        
        // Build the output list
        for(ActivityPK activityPK:_activity.keySet()) {
            SubjectPK activitySubject=new SubjectPK(activityPK);
            if(activitySubject.equals(subjectPK)) {
                ActivityID activityID=ObjectFactory.getActivityID(activityPK);
                if(activityID==null) {
                    continue;
                }
            
                // Create the activity object
                Activity actObj=find(activityID);
                if(actObj!=null) {
                    if(actObj.isActive()) {
                        list.add(actObj);
                    }
                }
            }
        }
        
        return list;
    }

    @Override
    public ActivityID getLastID(ISubjectID subject) {
        // Check the input object
        if(subject==null) {
            return null;
        }
        SubjectPK subjectPK=ObjectFactory.getSubjectPK(subject);
        if(subjectPK==null) {
            return null;
        }
        
        // Search the last identifier
        ActivityPK lastID=null;
        for(ActivityPK id:_activity.keySet()) {
            if(subjectPK.equals(new SubjectPK(id))) {
                if(lastID==null) {
                    lastID=id;
                } else {
                    if(lastID.getActivityIndex()<id.getActivityIndex()) {
                        lastID=id;
                    }
                }
            }
        }
        
        // Return last id
        return ObjectFactory.getActivityID(lastID);
        
    }

    /**
     * Get the list of descriptions for the given activity
     * @param key Activity primary key
     * @return List of activity description keys
     */
    private List<ActivityDescPK> getActivityDescriptions(ActivityPK key) {
        ArrayList<ActivityDescPK> descList=new ArrayList<ActivityDescPK>();
        for(ActivityDescPK descPK:_activityDesc.keySet()) {
            if(descPK.getActivityPK().equals(key)) {
                descList.add(descPK);
            }
        }
        
        return descList;
    }
    
    /**
     * Delete all the descriptions for a certain activity
     * @param key Activity primary key
     * @return True if the operation ends successfully or Fals in case of error.
     */
    private boolean deleteDescriptions(ActivityPK key) {
        List<ActivityDescPK> activityDescs=getActivityDescriptions(key);
        for(ActivityDescPK descPK:activityDescs) {
            if(_activityDesc.remove(descPK)==null) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public TestID add(ActivityID activityID, ActivityTest object) {
        
        // Check the input object
        if(object==null || activityID==null) {
            return null;
        }
        
        // Obtain the new ID
        TestID lastID=getLastID(activityID);
        
        // Create the new id
        TestID newID;
        if(lastID!=null) {
            newID=new TestID(lastID);
            newID.index++;
        } else {
            newID=new TestID(activityID,1);
        }
        
        // Get the object
        edu.uoc.pelp.model.vo.ActivityTest newObj=ObjectFactory.getActivityTestReg(object);
        
        // Add a new object from given object, to break the reference
        ActivityTestPK key=ObjectFactory.getActivityTestPK(newID);
        if(key==null) {
            return null;
        }
        
        // Add the new object
        newObj.setActivityTestPK(key);
        _activityTest.put(key, newObj);
        
        // Add the descriptions for current object
        for(String lang:object.getLanguageCodes()) {
            String desc=object.getDescription(lang);
            if(desc!=null) {
                TestDescPK descPK=new TestDescPK(key,lang);
                _testDesc.put(descPK,new TestDesc(descPK,desc));
            }
        }
        
        return newID;
    }

    @Override
    public boolean delete(TestID id) {
        
        // Check the input object
        if(id==null) {
            return false;
        }

        // Get the primary key
        ActivityTestPK key=ObjectFactory.getActivityTestPK(id);
        
        // Remove descriptions
        if(!deleteDescriptions(key)) {
            return false;
        }
        
        // Check that the object does not exists
        if(_activityTest.remove(key)==null) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean update(ActivityTest object) {
        // Check the input object
        if(object==null) {
            return false;
        }
        
        // Get the primary key
        ActivityTestPK key=ObjectFactory.getActivityTestPK(object.getID());
        
        // Get a low-level representation
        edu.uoc.pelp.model.vo.ActivityTest newObj=ObjectFactory.getActivityTestReg(object);
        
        // Check that the object does not exists
        if(newObj!=null) {
            if(_activityTest.remove(key)==null) {
                return false;
            }
            // Store the new object
            _activityTest.put(key, newObj);
        } else {
            return false;
        }
        
        // Delete old descriptions
        if(!deleteDescriptions(key)) {
            return false;
        }
        
        // Add new descriptions
        for(String lang:object.getLanguageCodes()) {
            String desc=object.getDescription(lang);
            if(desc!=null) {
                TestDescPK descPK=new TestDescPK(key,lang);
                _testDesc.put(descPK,new TestDesc(descPK,desc));
            }
        }
        
        return _activityTest.containsKey(key);
    }

    @Override
    public List<ActivityTest> findAllTests() {
        ArrayList<ActivityTest> list=new ArrayList<ActivityTest>();
        
        // Build the output list
        for(ActivityTestPK activityTestPK:_activityTest.keySet()) { 
            // Get the Id 
            TestID testID=ObjectFactory.getActivityTestID(activityTestPK);
            
            // Create the activity object
            ActivityTest actObj=find(testID);
           
            // Add the object to the output list
            if(actObj!=null) {
                list.add(actObj);
            }
        }
        
        return list;
        
    }

    @Override
    public ActivityTest find(TestID object) {
        // Check the input object
        if(object==null) {
            return null;
        }
        
        // Get the key
        ActivityTestPK key=ObjectFactory.getActivityTestPK(object);
        
        // Create the list of descriptions
        List<TestDescPK> descList=getActivityDescriptions(key);
        TestDesc[] descriptions=null;
        if(descList.size()>0) {
            // Create the descriptions array
            descriptions=new TestDesc[descList.size()];
            for(int i=0;i<descList.size();i++) {                    
                descriptions[i]=_testDesc.get(descList.get(i));
            }
        }
        
        // Get the activity test register
        edu.uoc.pelp.model.vo.ActivityTest activityTestReg=_activityTest.get(key);
            
        // Create the activity object
        ActivityTest actObj=ObjectFactory.getActivityTestObj(activityTestReg, descriptions);
           
        return actObj;
    }

    @Override
    public List<ActivityTest> findAll(ActivityID activity) {
        ArrayList<ActivityTest> list=new ArrayList<ActivityTest>();
        
        // Get the subject ID
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity); 
        if(activityPK==null) {
            return null;
        }
        
        // Build the output list
        for(ActivityTestPK activityTestPK:_activityTest.keySet()) {
            ActivityPK testActivity=activityTestPK.getActivityPK();
            if(activityPK.equals(testActivity)) {
                TestID testID=ObjectFactory.getActivityTestID(activityTestPK);
                if(testID==null) {
                    continue;
                }
            
                // Create the activity object
                ActivityTest actObj=find(testID);
                if(actObj!=null) {
                    list.add(actObj);
                }
            }
        }
              
        return list;
    }

    @Override
    public TestID getLastID(ActivityID activityID) {
        // Check the input object
        if(activityID==null) {
            return null;
        }
        ActivityPK activityPK=ObjectFactory.getActivityPK(activityID);
        if(activityPK==null) {
            return null;
        }
        
        // Search the last identifier
        ActivityTestPK lastID=null;
        for(ActivityTestPK id:_activityTest.keySet()) {
            if(activityPK.equals(new ActivityPK(id))) {
                if(lastID==null) {
                    lastID=id;
                } else {
                    if(lastID.getTestIndex()<id.getTestIndex()) {
                        lastID=id;
                    }
                }
            }
        }
        
        // Return last id
        return ObjectFactory.getActivityTestID(lastID);
    }
    
    /**
     * Get the list of descriptions for the given activity test
     * @param key Activity test primary key
     * @return List of activity test description keys
     */
    private List<TestDescPK> getActivityDescriptions(ActivityTestPK key) {
        ArrayList<TestDescPK> descList=new ArrayList<TestDescPK>();
        for(TestDescPK descPK:_testDesc.keySet()) {
            if(descPK.getActivityTestPK().equals(key)) {
                descList.add(descPK);
            }
        }
        
        return descList;
    }
    
    /**
     * Delete all the descriptions for a certain activity test
     * @param key Activity test primary key
     * @return True if the operation ends successfully or Fals in case of error.
     */
    private boolean deleteDescriptions(ActivityTestPK key) {
        List<TestDescPK> activityDescs=getActivityDescriptions(key);
        for(TestDescPK descPK:activityDescs) {
            if(_testDesc.remove(descPK)==null) {
                return false;
            }
        }
        
        return true;
    }
}

