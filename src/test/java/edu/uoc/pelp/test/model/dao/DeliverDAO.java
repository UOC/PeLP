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

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.model.dao.IDeliverDAO;
import edu.uoc.pelp.model.vo.ActivityPK;
import edu.uoc.pelp.model.vo.DeliverPK;
import edu.uoc.pelp.model.vo.ObjectFactory;
import edu.uoc.pelp.model.vo.UOC.SubjectPK;
import edu.uoc.pelp.model.vo.UOC.UserPK;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Implements the DAO object for the Delivers. It uses memory structures to store de data
 * and copy objects to break the references.
 * @author Xavier Baró
 */
public class DeliverDAO implements IDeliverDAO {
    
    /**
     * Table simulating the database table
     */
    private HashMap<DeliverPK,edu.uoc.pelp.model.vo.Deliver> _deliver=new HashMap<DeliverPK,edu.uoc.pelp.model.vo.Deliver>();

    @Override
    public DeliverID add(IUserID user, ActivityID activity, Deliver object) {
        
        // Check the input object
        if(object==null || user==null || activity==null) {
            return null;
        } 
        
        // Get the id of the last deliver
        DeliverID lastID=getLastID(activity,user);
        
        // Create the new id
        DeliverID newID;
        if(lastID!=null) {
            newID=new DeliverID(lastID);
            newID.index++;
        } else {
            newID=new DeliverID(user,activity,1);
        }
        
        // Get the object
        edu.uoc.pelp.model.vo.Deliver newObj=ObjectFactory.getDeliverReg(object);
        
        // Add a new object from given object, to break the reference
        DeliverPK key=ObjectFactory.getDeliverPK(newID);
        if(key==null) {
            return null;
        }
                
        // Add a new object from given object, to break the reference
        _deliver.put(key, newObj);
        
        return newID;
    }

    @Override
    public boolean delete(DeliverID id) {
        
        // Check the input object
        if(id==null) {
            return false;
        }
        
        // Get the primary key
        DeliverPK key=ObjectFactory.getDeliverPK(id);
        
        // Check that the object does not exists
        if(_deliver.remove(key)==null) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean update(Deliver object) {
        // Check the input object
        if(object==null) {
            return false;
        }
        
        // Get the primary key
        DeliverPK key=ObjectFactory.getDeliverPK(object.getID());
        
        // Get a low-level representation
        edu.uoc.pelp.model.vo.Deliver newObj=ObjectFactory.getDeliverReg(object);
        
        // Check that the object does not exists
        if(newObj!=null) {
            if(_deliver.remove(key)==null) {
                return false;
            }
            // Store the new object
            _deliver.put(key, newObj);
        } else {
            return false;
        }
                
        return _deliver.containsKey(key);
    }

    @Override
    public List<Deliver> findAll() {
        ArrayList<Deliver> list=new ArrayList<Deliver>();
        
        // Build the output list
        for(DeliverPK deliverPK:_deliver.keySet()) { 
            // Get the Id 
            DeliverID deliverID=ObjectFactory.getDeliverID(deliverPK);
            
            // Create the activity object
            Deliver deliverObj=find(deliverID);
           
            // Add the object to the output list
            if(deliverObj!=null) {
                list.add(deliverObj);
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public Deliver find(DeliverID object) {
        // Check the input object
        if(object==null) {
            return null;
        }
                
        // Get the key
        DeliverPK key=ObjectFactory.getDeliverPK(object);
                
        // Get the deliver register
        edu.uoc.pelp.model.vo.Deliver deliverReg=_deliver.get(key);
        
        // Get the files
        edu.uoc.pelp.model.vo.DeliverFile[] files=null;
            
        // Create the activity object
        Deliver actObj=ObjectFactory.getDeliverObj(deliverReg,files);
           
        return actObj;
    }
   
    @Override
    public List<Deliver> findAll(IUserID user) {
        ArrayList<Deliver> list=new ArrayList<Deliver>();
        
        // Get the user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        
        // Build the output list
        for(DeliverPK deliverPK:_deliver.keySet()) {
            UserPK deliverUser=new UserPK(deliverPK.getUserID());
            if(deliverUser.equals(userPK)) {
                DeliverID deliverID=ObjectFactory.getDeliverID(deliverPK);
                if(deliverID==null) {
                    continue;
                }
            
                // Create the activity object
                Deliver actObj=find(deliverID);
                if(actObj!=null) {
                    list.add(actObj);
                }
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public List<Deliver> findAll(ISubjectID subject, IUserID user) {
        ArrayList<Deliver> list=new ArrayList<Deliver>();
        
        // Get the subject ID and user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        SubjectPK subjectPK=ObjectFactory.getSubjectPK(subject);
        if(subjectPK==null) {
            return null;
        }
        
        // Build the output list
        for(DeliverPK deliverPK:_deliver.keySet()) {
            UserPK deliverUser=new UserPK(deliverPK.getUserID());
            SubjectPK deliverSubject=new SubjectPK(deliverPK.getActivityPK());
            if(deliverUser.equals(userPK) && subjectPK.equals(deliverSubject)) {
                DeliverID deliverID=ObjectFactory.getDeliverID(deliverPK);
                if(deliverID==null) {
                    continue;
                }
            
                // Create the activity object
                Deliver actObj=find(deliverID);
                if(actObj!=null) {
                    list.add(actObj);
                }
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public List<Deliver> findAll(ActivityID activity, IUserID user) {
        ArrayList<Deliver> list=new ArrayList<Deliver>();
        
        // Get the activity ID and user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        
        // Build the output list
        for(DeliverPK deliverPK:_deliver.keySet()) {
            UserPK deliverUser=new UserPK(deliverPK.getUserID());
            ActivityPK deliverActivity=deliverPK.getActivityPK();
            if(deliverUser.equals(userPK) && activityPK.equals(deliverActivity)) {
                DeliverID deliverID=ObjectFactory.getDeliverID(deliverPK);
                if(deliverID==null) {
                    continue;
                }
            
                // Create the activity object
                Deliver actObj=find(deliverID);
                if(actObj!=null) {
                    list.add(actObj);
                }
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public DeliverID getLastID(ActivityID activity, IUserID user) {
        // Check parameters
        if(activity==null || user==null) {
            return null;
        }
        
        // Get low level representations
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        UserPK userPK=ObjectFactory.getUserPK(user);
        if(userPK==null) {
            return null;
        }
        
        // Search the last identifier
        DeliverPK lastID=null;
        for(DeliverPK deliverPK:_deliver.keySet()) {
            UserPK deliverUser=new UserPK(deliverPK.getUserID());
            if(activityPK.equals(deliverPK.getActivityPK()) && userPK.equals(deliverUser)){
                if(lastID==null) {
                    lastID=deliverPK;
                } else {
                    if(lastID.getDeliverIndex()<deliverPK.getDeliverIndex()) {
                        lastID=deliverPK;
                    }
                }
            }
        }
                
        // Return last id
        return ObjectFactory.getDeliverID(lastID);
    }

    @Override
    public List<Deliver> findAll(ActivityID activity) {
        ArrayList<Deliver> list=new ArrayList<Deliver>();
        
        // Get the activity ID 
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        
        // Build the output list
        for(DeliverPK deliverPK:_deliver.keySet()) {
            ActivityPK deliverActivity=deliverPK.getActivityPK();
            if(activityPK.equals(deliverActivity)) {
                DeliverID deliverID=ObjectFactory.getDeliverID(deliverPK);
                if(deliverID==null) {
                    continue;
                }
            
                // Create the activity object
                Deliver actObj=find(deliverID);
                if(actObj!=null) {
                    list.add(actObj);
                }
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }
    
    @Override
    public List<DeliverID> findAllKey(ActivityID activity) {
        ArrayList<DeliverID> listIDs=new ArrayList<DeliverID>();
        
        // Get the activity ID 
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        
        // Build the output list
        for(DeliverPK deliverPK:_deliver.keySet()) {
            UserPK deliverUser=new UserPK(deliverPK.getUserID());
            ActivityPK deliverActivity=deliverPK.getActivityPK();
            if(activityPK.equals(deliverActivity)) {
                DeliverID deliverID=ObjectFactory.getDeliverID(deliverPK);
                if(deliverID==null) {
                    continue;
                }
            
                // Add the key
                listIDs.add(deliverID);               
            }
        }
        
        // Sort the list of delivers
        Collections.sort(listIDs);
        
        return listIDs;
    }

    @Override
    public List<DeliverID> findAllKey() {
        ArrayList<DeliverID> list=new ArrayList<DeliverID>();
        
        // Build the output list
        for(DeliverPK deliverPK:_deliver.keySet()) { 
            // Get the Id 
            DeliverID deliverID=ObjectFactory.getDeliverID(deliverPK);
            
            // Add the identifier object to the output list
            if(deliverID!=null) {
                list.add(deliverID);
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public List<DeliverID> findAllKey(IUserID user) {
        ArrayList<DeliverID> list=new ArrayList<DeliverID>();
        
        // Get the user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        
        // Build the output list
        for(DeliverPK deliverPK:_deliver.keySet()) {
            UserPK deliverUser=new UserPK(deliverPK.getUserID());
            if(deliverUser.equals(userPK)) {
                DeliverID deliverID=ObjectFactory.getDeliverID(deliverPK);
                if(deliverID==null) {
                    continue;
                }
            
                // Add the deliver identifier
                list.add(deliverID);
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public List<DeliverID> findAllKey(ISubjectID subject, IUserID user) {
        ArrayList<DeliverID> list=new ArrayList<DeliverID>();
        
        // Get the subject ID and user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        SubjectPK subjectPK=ObjectFactory.getSubjectPK(subject);
        if(subjectPK==null) {
            return null;
        }
        
        // Build the output list
        for(DeliverPK deliverPK:_deliver.keySet()) {
            UserPK deliverUser=new UserPK(deliverPK.getUserID());
            SubjectPK deliverSubject=new SubjectPK(deliverPK.getActivityPK());
            if(deliverUser.equals(userPK) && subjectPK.equals(deliverSubject)) {
                DeliverID deliverID=ObjectFactory.getDeliverID(deliverPK);
                if(deliverID==null) {
                    continue;
                }
            
                // Add the deliver id
                list.add(deliverID);
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public List<DeliverID> findAllKey(ActivityID activity, IUserID user) {
        ArrayList<DeliverID> list=new ArrayList<DeliverID>();
        
        // Get the activity ID and user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        
        // Build the output list
        for(DeliverPK deliverPK:_deliver.keySet()) {
            UserPK deliverUser=new UserPK(deliverPK.getUserID());
            ActivityPK deliverActivity=deliverPK.getActivityPK();
            if(deliverUser.equals(userPK) && activityPK.equals(deliverActivity)) {
                DeliverID deliverID=ObjectFactory.getDeliverID(deliverPK);
                if(deliverID==null) {
                    continue;
                }
            
                // Add the identifier
                list.add(deliverID);                
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }
    
}

