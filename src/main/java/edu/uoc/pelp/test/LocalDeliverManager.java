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
package edu.uoc.pelp.test;

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.engine.deliver.IDeliverManager;
import java.io.File;
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
    protected HashMap<DeliverID,Deliver> _delivers=new HashMap<DeliverID,Deliver>();
    
    /**
     * Map storing all delivers test results
     */
    protected HashMap<DeliverID,DeliverResults> _testResults=new HashMap<DeliverID,DeliverResults>();
    
    /**
     * Path where delivers are stored
     */
    protected File _deliversPath=null;

    /**
     * Create the path where a delived should be stored using its information
     * @param deliverID Deliver identifier object
     * @return Path of the delivery
     */
    private File getDeliverPath(DeliverID deliverID) {
        // If no deliver information, return null
        if(deliverID==null) {
            return null;
        }
        
        // In no deliver path is given, store delivers in temporal system path
        File deliversPath=_deliversPath;
        if(_deliversPath==null) {
            deliversPath=new File(System.getProperty("java.io.tmpdir") + "PELP" + File.separator + "Delivers");
        }
        
        // Add subject information
        String subject=deliverID.activity.subjectID.toString();
        subject=subject.replace("\\","");
        subject=subject.replace("/","");
        subject=subject.replace(".","");
        subject=subject.trim();
        
        // Add activity information
        String activity=String.format("%04d", deliverID.activity.index);
        
        // Add user information
        String user= deliverID.user.toString();
        user=user.replace("\\","");
        user=user.replace("/","");
        user=user.replace(".","");
        user=user.trim();
        
        // Add deliver index
        String deliver=String.format("%04d", deliverID.index);
        
        // Create final folder
        deliversPath=new File(deliversPath.getAbsolutePath() + File.separator + subject + File.separator + activity+ File.separator + user + File.separator + deliver);
        
        
        return deliversPath;
    }
    
    @Override
    public synchronized DeliverID addDeliver(IUserID user, ActivityID activity, Deliver deliver) {
        
        //Check that the deliver has files
        if(deliver.getFiles().length==0) {
            return null;
        }
        
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
        
        // Move the files to the destination folder
        deliver.moveFiles(getDeliverPath(newID));
        
        // Create the new Activity
        _delivers.put(newID,new Deliver(newID,deliver));
        
        return newID.clone();
    }

    @Override
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

    @Override
    public boolean deleteDeliver(DeliverID deliverID) {
        // Check if the deliver exists
        if(_delivers.containsKey(deliverID)) {
            // Remove the deliver
            _delivers.remove(deliverID);
            return true;
        }
        
        return true;
    }

    @Override
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

    @Override
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

    @Override
    public boolean addResults(DeliverID deliverID, AnalysisResults results) {
        // Check if the deliver already has old results
        if(_testResults.containsKey(deliverID)) {
            return false;
        }
        
        // Create a new Results object
        DeliverResults newResults=new DeliverResults(deliverID,results.clone());
                
        // Add the results
        _testResults.put(deliverID, newResults);
        
        // Check that it is correctly added
        return _testResults.containsKey(deliverID);
    }

    @Override
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

    @Override
    public boolean deleteResults(DeliverID deliverID) {
        // Check if the results exist
        if(_testResults.containsKey(deliverID)) {
            // Remove the results
            _testResults.remove(deliverID);
            return true;
        }
        
        return false;
    }

    @Override
    public DeliverResults getResults(DeliverID deliverID) {
        // Check if the results exist
        if(_testResults.containsKey(deliverID)) {
            // Return a copy of the results
            return _testResults.get(deliverID).clone();
        }
        return null;
    }

    @Override
    public Deliver getDeliver(DeliverID deliverID) {
        Deliver deliver=_delivers.get(deliverID);
        if(deliver==null) {
            return null;
        }
        return deliver.clone();
    }

    @Override
    public int getNumUserDelivers(IUserID userID, ActivityID activityID) {
        return getUserDelivers(userID,activityID).length;
    }

    @Override
    public boolean setDeliverPath(File path) {
        // Check for null paths
        if(path==null) {
            return false;
        }
        
        // Create the delivers path
        if(!_deliversPath.exists()) {
            if(!_deliversPath.mkdirs()) {
                return false;
            }
        }
        
        // Check that the path is writable
        if(!_deliversPath.canWrite()) {
            return false;
        }
        
        return true;
    }
}
