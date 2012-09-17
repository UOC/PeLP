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
    private HashMap<DeliverID,Deliver> _deliver=new HashMap<DeliverID,Deliver>();

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
        
        // Add a new object from given object, to break the reference
        _deliver.put(newID, new Deliver(object));
        
        
        return newID.clone();
    }

    @Override
    public boolean delete(DeliverID id) {
        
        // Check the input object
        if(id==null) {
            return false;
        }
        
        // Check that the object does not exists
        if(_deliver.remove(id)==null) {
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
        
        // Check that the object does not exists
        if(_deliver.remove(object.getID())==null) {
            return false;
        }
        
        // Store the new object
        _deliver.put(object.getID(), new Deliver(object.getID(),object));
        
        return _deliver.containsKey(object.getID());
    }

    @Override
    public List<Deliver> findAll() {
        ArrayList<Deliver> list=new ArrayList<Deliver>();
        
        // Build the output list
        for(Deliver deliver:_deliver.values()) {
            list.add(new Deliver(deliver));
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
        
        return new Deliver(_deliver.get(object));
    }
   
    @Override
    public List<Deliver> findAll(IUserID user) {
        ArrayList<Deliver> list=new ArrayList<Deliver>();
        
        // Build the output list
        for(Deliver deliver:_deliver.values()) {
            if(deliver.getID().user.equals(user)) {
                list.add(new Deliver(deliver));
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public List<Deliver> findAll(ISubjectID subject, IUserID user) {
        ArrayList<Deliver> list=new ArrayList<Deliver>();
        
        // Build the output list
        for(Deliver deliver:_deliver.values()) {
            if(deliver.getID().activity.subjectID.equals(subject) && 
               deliver.getID().user.equals(user)) {
                list.add(new Deliver(deliver));
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public List<Deliver> findAll(ActivityID activity, IUserID user) {
        ArrayList<Deliver> list=new ArrayList<Deliver>();
        
        // Build the output list
        for(Deliver deliver:_deliver.values()) {
            if(deliver.getID().activity.equals(activity) && 
               deliver.getID().user.equals(user)) {
                list.add(new Deliver(deliver));
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
        
        // Search the last identifier
        DeliverID lastID=null;
        for(DeliverID id:_deliver.keySet()) {
            if(id.activity.equals(activity) && 
               id.user.equals(user)) {
                if(lastID==null) {
                    lastID=id;
                } else {
                    if(lastID.index<id.index) {
                        lastID=id;
                    }
                }
            }
        }
        
        // Return last id
        if(lastID!=null) {
            return new DeliverID(lastID);
        }
        
        // If no deliver is found, return null
        return lastID;
    }

    @Override
    public List<Deliver> findAll(ActivityID activity) {
        ArrayList<Deliver> list=new ArrayList<Deliver>();
        
        // Create the list of identifiers
        for(Deliver deliver:_deliver.values()) {
            if(deliver.getID().activity.equals(activity)) {
                list.add(deliver);
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }
    
    @Override
    public List<DeliverID> findAllKey(ActivityID activity) {
        ArrayList<DeliverID> listIDs=new ArrayList<DeliverID>();
        
        // Create the list of identifiers
        for(DeliverID deliverID:_deliver.keySet()) {
            if(deliverID.activity.equals(activity)) {
                listIDs.add(new DeliverID(deliverID));
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
        for(DeliverID deliverID:_deliver.keySet()) {
            list.add(new DeliverID(deliverID));
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public List<DeliverID> findAllKey(IUserID user) {
        ArrayList<DeliverID> list=new ArrayList<DeliverID>();
        
        // Build the output list
        for(DeliverID deliverID:_deliver.keySet()) {
            if(deliverID.user.equals(user)) {
                list.add(new DeliverID(deliverID));
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public List<DeliverID> findAllKey(ISubjectID subject, IUserID user) {
        ArrayList<DeliverID> list=new ArrayList<DeliverID>();
        
        // Build the output list
        for(DeliverID deliverID:_deliver.keySet()) {
            if(deliverID.activity.subjectID.equals(subject) && 
               deliverID.user.equals(user)) {
                list.add(new DeliverID(deliverID));
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }

    @Override
    public List<DeliverID> findAllKey(ActivityID activity, IUserID user) {
        ArrayList<DeliverID> list=new ArrayList<DeliverID>();
        
        // Build the output list
        for(DeliverID deliverID:_deliver.keySet()) {
            if(deliverID.activity.equals(activity) && 
               deliverID.user.equals(user)) {
                list.add(new DeliverID(deliverID));
            }
        }
        
        // Sort the list of delivers
        Collections.sort(list);
        
        return list;
    }
}

