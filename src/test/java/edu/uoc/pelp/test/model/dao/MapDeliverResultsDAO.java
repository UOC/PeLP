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

import edu.uoc.pelp.engine.activity.TestID;
import edu.uoc.pelp.engine.deliver.ActivityTestResult;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.model.dao.IDeliverResultDAO;
import edu.uoc.pelp.model.vo.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class implements a DAO object for table deliverResults
 * @author Xavier Baró
 */
public class MapDeliverResultsDAO implements IDeliverResultDAO {
    
    /**
     * Table simulating the database table
     */
    private HashMap<DeliverPK,DeliverResult> _deliverResults=new HashMap<DeliverPK,DeliverResult>();
    
    /**
     * Table simulating the database table
     */
    private HashMap<DeliverTestResultPK,DeliverTestResult> _deliverTestResults=new HashMap<DeliverTestResultPK,DeliverTestResult>();
    
    /**
     * Get the list of test results for a certain deliver.
     * @param key Deliver primary key
     * @return Low level file objects
     */
    private List<edu.uoc.pelp.model.vo.DeliverTestResult> findDeliverTestResults(DeliverPK key) {
        // Check the deliver primary key
        if(key==null) {
            return null;
        }
        
        // Search in the database
        List<DeliverTestResult> returnList=new ArrayList<DeliverTestResult>();
        for(DeliverTestResult test:_deliverTestResults.values()) {
            if(test.getDeliverTestResultPK().getDeliverPK().equals(key)) {
                returnList.add(test);
            }
        }
        
        return returnList;
    }

    
    @Override
    public boolean add(DeliverResults results) {
        DeliverPK key=ObjectFactory.getDeliverPK(results.getDeliverID());
        DeliverResult register=ObjectFactory.getDeliverResultReg(results);
        
        // Check the key
        if(key==null) {
            return false;
        }
        
        // Check for duplicates
        if(_deliverResults.containsKey(key)) {
            return false;
        }
        
        // Add the new object
        _deliverResults.put(key, register);
        
        return _deliverResults.containsKey(key);
    }

    @Override
    public boolean delete(DeliverID id) {
        DeliverPK key=ObjectFactory.getDeliverPK(id);
        
        // Check the key
        if(key==null) {
            return false;
        }
        
        // Check if the results exist
        if(_deliverResults.containsKey(key)) {
            // Remove the results
            _deliverResults.remove(key);
        }
        
        // Check if the object is correctly removed
        return !_deliverResults.containsKey(key);
    }

    @Override
    public boolean update(DeliverResults object) {
        // Check if the results exist
       /* if(_testResults.containsKey(results.getDeliverID())) {
            // Remove old entry and add the new one
            _testResults.remove(results.getDeliverID());
            _testResults.put(results.getDeliverID(),results.clone());
            return true;
        }*/
        
        return false;
    }

    @Override
    public DeliverResults find(DeliverID id) {
        DeliverPK key=ObjectFactory.getDeliverPK(id);
        
        // Check the key
        if(key==null) {
            return null;
        }
        
        // Get the object form the database
        DeliverResult result=_deliverResults.get(key);
        if(result==null) {
            return null;
        }
        
        // Get the test results
        List<DeliverTestResult> results=findDeliverTestResults(key);
        
        // Add the results to the output object
        DeliverTestResult[] deliverTests=null;
        if(results!=null) {
            deliverTests=new DeliverTestResult[results.size()];
            results.toArray(deliverTests);
        }
        
        // Return the object
        return ObjectFactory.getDeliverResultObj(result, deliverTests);
    }
    
    @Override
    public boolean add(DeliverID deliverID, ActivityTestResult testResult) {
        // Check input parameters
        if(deliverID==null || testResult==null) {
            return false;
        }
        if(testResult.getTestID()==null) {
            return false;
        }
        
        // Check that the results for this deliver exist
        if(find(deliverID)==null) {
            return false;
        }
                
        // Check that no older test result exists
        if(findTestResult(deliverID,testResult.getTestID())!=null) {
            return false;
        }
        
        // Create the low level representation for the object
        DeliverTestResultPK key=ObjectFactory.getDeliverTestResultPK(deliverID, testResult.getTestID());
        DeliverTestResult newObj=ObjectFactory.getTestResultReg(deliverID, testResult);
        
        // Add the new object
        _deliverTestResults.put(key, newObj);
                
        return (findTestResult(deliverID,testResult.getTestID())!=null);
    }

    @Override
    public boolean delete(DeliverID deliverID, ActivityTestResult testResult) {
        // Check input parameters
        if(deliverID==null || testResult==null) {
            return false;
        }
        if(testResult.getTestID()==null) {
            return false;
        }
                
        // Check that test result exists
        if(findTestResult(deliverID,testResult.getTestID())==null) {
            return false;
        }
        
        // Create the low level representation for the object
        DeliverTestResultPK key=ObjectFactory.getDeliverTestResultPK(deliverID, testResult.getTestID());
        DeliverTestResult newObj=ObjectFactory.getTestResultReg(deliverID, testResult);
        
        // Update the register
        _deliverTestResults.remove(key);
        _deliverTestResults.put(key,newObj);
        
        return true;
    }

    @Override
    public boolean update(DeliverID deliverID, ActivityTestResult testResult) {
        // Check input parameters
        if(deliverID==null || testResult==null) {
            return false;
        }
        if(testResult.getTestID()==null) {
            return false;
        }
                
        // Check that test result exists
        if(findTestResult(deliverID,testResult.getTestID())==null) {
            return false;
        }
        
        // Create the low level representation for the object
        DeliverTestResultPK key=ObjectFactory.getDeliverTestResultPK(deliverID, testResult.getTestID());
        
        // Add the new object
        _deliverTestResults.remove(key);
                
        return (findTestResult(deliverID,testResult.getTestID())==null);
    }

    @Override
    public List<ActivityTestResult> findTestResults(DeliverID deliverID) {
        // Obtain the deliver primary key
        DeliverPK deliverPK=ObjectFactory.getDeliverPK(deliverID);
        if(deliverPK==null) {
            return null;
        }
        
        // Search in the database
        List<ActivityTestResult> returnList=new ArrayList<ActivityTestResult>();
        for(DeliverTestResult test:_deliverTestResults.values()) {
            if(test.getDeliverTestResultPK().getDeliverPK().equals(deliverPK)) {
                returnList.add(ObjectFactory.getTestResultObj(test));
            }
        }
        
        return returnList;
    }

    @Override
    public ActivityTestResult findTestResult(DeliverID deliverID, TestID testID) {

        // Check the input object
        if(deliverID==null || testID==null) {
            return null;
        }
        
        // Get the key
        DeliverTestResultPK key=ObjectFactory.getDeliverTestResultPK(deliverID, testID);
        
        // Get the key
        DeliverTestResult register=_deliverTestResults.get(key);
        
        // Return the object
        return ObjectFactory.getTestResultObj(register);
    }
}
