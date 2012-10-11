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
package edu.uoc.pelp.engine.deliver;

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.campus.IClassroomID;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.model.dao.IDeliverDAO;
import edu.uoc.pelp.model.dao.IDeliverResultDAO;
import java.io.File;
import java.util.List;

/**
 * Implements a basic implementation to manage delivers. 
 * @author Xavier Baró
 */
public class DAODeliverManager implements IDeliverManager {
        
    /**
     * Path where delivers are stored
     */
    protected File _deliversPath=null;
    
    /** 
     * Delivers DAO
     */
    protected IDeliverDAO _delivers=null;
        
    /**
     * Deliver Results DAO
     */
    protected IDeliverResultDAO _deliverResults=null;
    
    /**
     * Basic constructor with DAO objects
     * @param deliverDAO Object to access the deliver details
     * @param deliverFilesDAO Object to access the deliver files
     */
    public DAODeliverManager(IDeliverDAO deliverDAO,IDeliverResultDAO deliverResultsDAO) {
        _delivers=deliverDAO;
        _deliverResults=deliverResultsDAO;
    }
    
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
    public DeliverID addDeliver(IUserID user, ActivityID activity, Deliver deliver) {
        
        //Check that the deliver has files
        if(deliver.getFiles().length==0) {
            return null;
        }
        
        // Add the new deliver
        DeliverID newID=_delivers.add(user, activity, deliver);
        
        // Move the files to the destination folder
        if(newID!=null) {
            // Move the files
            if(deliver.moveFiles(getDeliverPath(newID))) {
                // Update the deliver information
                _delivers.update(deliver);
            } else {
                // Remove remaining original files
                for(File f:deliver.getRootPath().listFiles()) {
                    f.delete();
                }
                deliver.getRootPath().delete();
                
                // Remove moved files
                for(File f:getDeliverPath(newID).listFiles()) {
                    f.delete();
                }
                getDeliverPath(newID).delete();
                
                // Remove the inserted deliver from DB
                _delivers.delete(newID);
                newID=null;
            }
        }
                
        return newID;
    }

    @Override
    public boolean editDeliver(Deliver deliver) {
                
        // Check if this deliver is correct
        if(deliver.getID()==null || !deliver.correct()) {
            return false;
        }
        
        // Update the deliver information
        return _delivers.update(deliver);
    }

    @Override
    public boolean deleteDeliver(DeliverID deliverID) {
        
        // Remove files from disc
        Deliver deliver=_delivers.find(deliverID);
        if(deliver==null) {
            return false;
        }
        
        // Get the path of all files
        for(DeliverFile file:deliver.getFiles()) {
            File f=file.getAbsolutePath(deliver.getRootPath());
            if(f.exists()) {
                f.delete();
            } else {
                return false;
            }
        }
        
        // Remove the root path for this deliver
        deliver.getRootPath().delete();
        
        // Finally remove the deliver from the database
        return _delivers.delete(deliverID);
    }

    @Override
    public DeliverID[] getActivityDelivers(ActivityID activity) {
        // Get the list of ID's
        List<DeliverID> listIDs=_delivers.findAllKey(activity);
        
        // Create the output array
        DeliverID[] retList=new DeliverID[listIDs.size()];
        listIDs.toArray(retList);
        
        return retList;
    }

    @Override
    public DeliverID[] getUserDelivers(IUserID user, ActivityID activity) {
        
        List<DeliverID> listIDs=_delivers.findAllKey(activity, user);
                
        // Create the output array
        DeliverID[] retList=new DeliverID[listIDs.size()];
        listIDs.toArray(retList);
        
        return retList;
    }

    @Override
    public boolean addResults(DeliverID deliverID, AnalysisResults deliverResults) {
        // Check the input parameters
        if(deliverID==null || deliverResults==null) {
            return false;
        }
        
        // Create the deliver results object
        DeliverResults results=new DeliverResults(deliverID,deliverResults);
                
        // Add the results
        return _deliverResults.add(results);
    }

    @Override
    public boolean editResults(DeliverResults results) {
        // Delegate the method
        return _deliverResults.update(results);
    }

    @Override
    public boolean deleteResults(DeliverID deliverID) {
        // Delegate the method
        return _deliverResults.delete(deliverID);
    }

    @Override
    public DeliverResults getResults(DeliverID deliverID) {
        // Delegate the method
        return _deliverResults.find(deliverID);
    }

    @Override
    public Deliver getDeliver(DeliverID deliverID) {
        // Delegate the method
        return _delivers.find(deliverID);
    }

    @Override
    public int getNumUserDelivers(IUserID userID, ActivityID activityID) {
        DeliverID lastID=_delivers.getLastID(activityID, userID);
        if(lastID==null) {
            return 0;
        }
        return (int)lastID.index;
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

    @Override
    public Deliver[] getClassroomDelivers(IClassroomID classroom, ActivityID activity) {
        List<Deliver> list=_delivers.findAllClassroom(classroom,activity);
                
        // Create the output array
        Deliver[] retList=new Deliver[list.size()];
        list.toArray(retList);
        
        return retList;
    }

    @Override
    public Deliver[] getClassroomLastDelivers(IClassroomID classroom, ActivityID activity) {
        List<Deliver> list=_delivers.findLastClassroom(classroom,activity);
                
        // Create the output array
        Deliver[] retList=new Deliver[list.size()];
        list.toArray(retList);
        
        return retList;
    }
}
