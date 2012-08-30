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
package edu.uoc.pelp.engine.activity;

import edu.uoc.pelp.engine.aem.TestResult;

/**
 * This class represents the result of a Code Project over a Test. 
 * @author Xavier Baró
 */
public class ActivityTestResult extends TestResult {
    
    /**
     * Identifyer of the test related to this results
     */
    protected TestID _testID=null;

    /**
     * Default copy constructor
     * @param testResult Object to be copied 
     */
    public ActivityTestResult(ActivityTestResult testResult) {
        super(testResult);
        _testID=testResult._testID;
    }
    
    /**
     * Default constructor for an empty result
     */
    public ActivityTestResult() {
        super();
    }
    
    /**
     * Gets the identifyer of the test related to this result
     * @return Identyfier for the test.
     */
    public TestID getTestID() {
        return _testID;
    }
    
    /**
     * Assigns a new identifyer to this result 
     * @param testID Identifyer of the test associed to this result
     */
    public void setTestID(TestID testID) {
        _testID=testID;
    }
    
    public int compareTo(Object t) {
        TestID id=((ActivityTestResult)t)._testID;
        if(_testID==null) {
            if(id==null) {
                return 0;
            } else {
                return -1;
            }
        }
        return _testID.compareTo(id);
    }
    
    @Override
    public ActivityTestResult clone() {
        return new ActivityTestResult(this);
    }
}
