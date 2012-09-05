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

import edu.uoc.pelp.engine.activity.ActivityTestResult;
import edu.uoc.pelp.engine.activity.TestID;
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.aem.TestResult;

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
     * Results the analysis process
     */
    private AnalysisResults _analysisResult=null;
        
    /**
     * Default constructor with the basic information
     * @param deliver Deliver identifyer
     * @param analysisResult Results of the analysis process
     */
    public DeliverResults(DeliverID deliver,AnalysisResults analysisResult) {
        _deliverID=deliver;
        _analysisResult=analysisResult;
    }
        
    /**
     * Return the list of results for each test over a given deliver.
     * @param deliver Object identifying a deliver
     * @return Array of results, ordered by test id
     */
    public TestResult[] getResults(DeliverID deliver) {
        return _analysisResult.getResults();
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
        DeliverResults newObject=new DeliverResults(_deliverID.clone(),_analysisResult.clone());
        
        return newObject;
    }

    /**
     * Remove the information for a certain test.
     * @param testID Identifier of the test
     */
    public void removePrivateInformation(TestID testID) {
        ActivityTestResult result=getTestResult(testID);
        if(result!=null) {
            result.removePrivateInformation();
        }
    }
    
    /**
     * Retrieve the information for a certain test.
     * @param testID Identifier of the test
     * @return Object with the test result or null if it does not exist.
     */
    public ActivityTestResult getTestResult(TestID testID) {
        // Search in the list of results for result with given ID
        for(TestResult testResult:_analysisResult.getResults()) {
            if(testResult instanceof ActivityTestResult) {
                ActivityTestResult result=(ActivityTestResult)testResult;
                if(result.getTestID().equals(testID)) {
                    return result;
                }
            }
        }
        
        return null;
    }
}
