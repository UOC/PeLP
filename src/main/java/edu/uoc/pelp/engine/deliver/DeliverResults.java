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

import edu.uoc.pelp.engine.activity.TestID;
import edu.uoc.pelp.engine.aem.BuildResult;
import edu.uoc.pelp.engine.aem.TestResult;
import java.util.HashMap;

/**
 * This class implements the results obtained after the analysis of a Deliver
 * @author Xavier Baró
 */
public class DeliverResults {
    
    /**
     * Identifyer of the reliver for wich those results are extracted
     */
    private DeliverID _deliverID=null;
    
    /**
     * Results of the building process
     */
    private BuildResult _buildResult=null;
    
    /**
     * Results for each test
     */
    private HashMap<TestID,TestResult> _testResults=new HashMap<TestID,TestResult>();
    
    /**
     * Default constructor with the basic information
     * @param deliver Deliver identifyer
     * @param buildResult Results of the building process
     */
    public DeliverResults(DeliverID deliver,BuildResult buildResult) {
        _deliverID=deliver;
        _buildResult=buildResult;
    }
    
    /**
     * Default constructor
     */
    public DeliverResults() {
        
    }
    
    /**
     * Add the building result for the deliver
     * @param result Object with the build results
     */
    public void setBuildResult(BuildResult result) {
        _buildResult=result;
    }
    
    /**
     * Add the result of a certain test to the deliver results
     * @param result Object with the test results
     * @return True if the result has been correctly added or False otherwise
     */
    public boolean addTestResult(TestResult result) {
        if(_testResults.containsKey(result.getTestID())) {
            return false;
        }
        _testResults.put(result.getTestID(),result);
        return true;
    }
    
    /**
     * Return the list of results for each test over a given deliver.
     * @param deliver Object identifying a deliver
     * @return Array of results, ordered by test id
     */
    public TestResult[] getResults(DeliverID deliver) {
        TestResult[] retValue=null;
        
        retValue=new TestResult[_testResults.size()];
        _testResults.values().toArray(retValue);
        
        return retValue;
    }
    
    /**
     * Obtain the deliver related to those results
     * @return DeliverID object with the deliver information
     */
    public DeliverID getDeliverID() {
        return _deliverID;
    }
    
    /**
     * Assign a deliver ID to this results
     * @param deliverID Indentifyer of the deliver
     */
    public void setDeliverID(DeliverID deliverID) {
        _deliverID=deliverID.clone();
    }
    
    @Override
    public DeliverResults clone() {
        DeliverResults newObject=new DeliverResults();
        
        // Create a new Identifier
        newObject._deliverID=_deliverID.clone();
        
        // Add the results
        _testResults.clear();
        for(TestResult test:_testResults.values()) {
            _testResults.put(test.getTestID(), test.clone());
        }
        return newObject;
    }

    /**
     * Remove the information for a certain test.
     * @param testID Identifier of the test
     */
    public void removePrivateInformation(TestID testID) {
        for(TestResult test:_testResults.values()) {
            if(test.getTestID().equals(testID)) {
                test.removePrivateInformation();
            }
        }
    }
}
