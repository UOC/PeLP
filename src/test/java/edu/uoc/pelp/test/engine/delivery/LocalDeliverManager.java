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
package edu.uoc.pelp.test.engine.delivery;

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.engine.deliver.IDeliverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Implements a dummy class to manage delivers. It uses dynamic memory structures to
 * store the objects.
 * @author Xavier Baró
 */
public class LocalDeliverManager implements IDeliverManager {
    
    /**
     * Map storing all the delivers
     */
    private HashMap<DeliverID,Deliver> _delivers=new HashMap<DeliverID,Deliver>();
    
    /**
     * Map storing all delivers test results
     */
    private HashMap<DeliverID,DeliverResults> _testResults=new HashMap<DeliverID,DeliverResults>();

    public DeliverID addDeliver(IUserID user, ActivityID activity, Deliver deliver) {
        // Get all delivers of a certain user and activity
        DeliverID[] delivers=getUserDelivers(user,activity);
        
        // Search the last identifier
        long lastID=0;
        for(DeliverID id:delivers) {
            if(id.index>lastID) {
                lastID=id.index;
            }
        }
        
        // Create a new identifier
        DeliverID newID=new DeliverID(user,activity,lastID+1);
        
        // Create the new Activity
        _delivers.put(newID,new Deliver(newID,deliver));
        
        return newID.clone();
    }

    public boolean editDeliver(Deliver deliver) {
        // Check if the deliver exists
        if(_delivers.containsKey(deliver.getID())) {
            // Check if this deliver is correct
            if(deliver.getID()==null || !deliver.correct()) {
                return false;
            }
            // Remove old entry and add the new one
            _delivers.remove(deliver.getID());
            _delivers.put(deliver.getID(),deliver.clone());
            return true;
        }
        
        return false;
    }

    public boolean deleteDeliver(DeliverID deliverID) {
        // Check if the deliver exists
        if(_delivers.containsKey(deliverID)) {
            // Remove the deliver
            _delivers.remove(deliverID);
            return true;
        }
        
        return true;
    }

    public DeliverID[] getActivityDelivers(ActivityID activity) {
        ArrayList<DeliverID> listIDs=new ArrayList<DeliverID>();
        
        // Create the list of identifiers
        for(DeliverID deliverID:_delivers.keySet()) {
            if(deliverID.activity.equals(activity)) {
                listIDs.add(deliverID);
            }
        }
        
        // Sort the list of delivers
        Collections.sort(listIDs);
        
        // Create the output array
        DeliverID[] retList=new DeliverID[listIDs.size()];
        listIDs.toArray(retList);
        
        return retList;
    }

    public DeliverID[] getUserDelivers(IUserID user, ActivityID activity) {
        ArrayList<DeliverID> listIDs=new ArrayList<DeliverID>();
        
        // Create the list of identifiers
        for(DeliverID deliverID:_delivers.keySet()) {
            if(deliverID.activity.equals(activity) && deliverID.user.equals(user)) {
                listIDs.add(deliverID);
            }
        }
        
        // Sort the list of delivers
        Collections.sort(listIDs);
        
        // Create the output array
        DeliverID[] retList=new DeliverID[listIDs.size()];
        listIDs.toArray(retList);
        
        return retList;
    }

    public boolean addResults(DeliverID deliverID, DeliverResults results) {
        // Check if the deliver already has old results
        if(_testResults.containsKey(deliverID)) {
            return false;
        }
        
        // Create a new Results object
        DeliverResults newResults=results.clone();
        newResults.setDeliverID(deliverID);
        
        // Add the results
        _testResults.put(deliverID, newResults);
        
        // Check that it is correctly added
        return _testResults.containsKey(deliverID);
    }

    public boolean editResults(DeliverResults results) {
        // Check if the results exist
        if(_testResults.containsKey(results.getDeliverID())) {
            // Remove old entry and add the new one
            _testResults.remove(results.getDeliverID());
            _testResults.put(results.getDeliverID(),results.clone());
            return true;
        }
        
        return false;
    }

    public boolean deleteResults(DeliverID deliverID) {
        // Check if the results exist
        if(_testResults.containsKey(deliverID)) {
            // Remove the results
            _testResults.remove(deliverID);
            return true;
        }
        
        return false;
    }

    public DeliverResults getResults(DeliverID deliverID) {
        // Check if the results exist
        if(_testResults.containsKey(deliverID)) {
            // Return a copy of the results
            return _testResults.get(deliverID).clone();
        }
        return null;
    }

    public Deliver getDeliver(DeliverID deliverID) {
        return _delivers.get(deliverID).clone();
    }

    public int getNumUserDelivers(IUserID userID, ActivityID activityID) {
        return getUserDelivers(userID,activityID).length;
    }
}
