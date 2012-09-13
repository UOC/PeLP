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

import edu.uoc.pelp.engine.activity.ActivityTestResult;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.model.dao.IDeliverResultDAO;
import edu.uoc.pelp.model.dao.IDeliverTestResultDAO;
import edu.uoc.pelp.model.vo.DeliverPK;
import edu.uoc.pelp.model.vo.DeliverResult;
import edu.uoc.pelp.model.vo.DeliverTestResult;
import edu.uoc.pelp.model.vo.ObjectFactory;
import java.util.HashMap;
import java.util.List;

/**
 * This class implements a DAO object for table deliverResults
 * @author Xavier Baró
 */
public class DeliverResultsDAO implements IDeliverResultDAO {
    
    /**
     * Deliver Test Results DAO
     */
    protected IDeliverTestResultDAO _deliverTestResults=null;
    
    /**
     * Table simulating the database table
     */
    private HashMap<DeliverPK,DeliverResult> _deliverResults=new HashMap<DeliverPK,DeliverResult>();
    
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
        List<DeliverTestResult> results=_deliverTestResults.find(id);
        
        // Add the results to the output object
        DeliverTestResult[] deliverTests=null;
        if(results!=null) {
            deliverTests=new DeliverTestResult[results.size()];
            results.toArray(deliverTests);
        }
        
        // Return the object
        return ObjectFactory.getDeliverResulsObj(result, deliverTests);
    }
    
}
