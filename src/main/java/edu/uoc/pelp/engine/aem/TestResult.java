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
package edu.uoc.pelp.engine.aem;

import edu.uoc.pelp.engine.activity.TestID;

/**
 * This class represents the result of a Code Project over a Test. 
 * @author Xavier Baró
 */
public class TestResult implements Comparable {
    
    /**
     * Identifyer of the test related to this results
     */
    private TestID _testID=null;

    /**
     * Indicates if the test is correctly passed or not
     */
    private boolean _passed=false;
    
    /**
     * In case of test fail, stores the output of the program to this test
     */
    private String _testOutput=null;
    
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
    
    /**
     * Assign the results of a certain test for a given deliver
     * @param passed True if the test is successfully passed or Fals otherwise
     * @param output Output of the program over the test
     */
    public void setResult(boolean passed,String output) {
        _passed=passed;
        _testOutput=output;
    }
    
    /**
     * Indicates if a test was successfully passed by a given deliver
     * @return True if the test was passed or false otherwise
     */
    public boolean isPassed() {
        return _passed;
    }
    
    /**
     * Obtain the output of a test
     * @return String of the output
     */
    public String getOutput() {
        return _testOutput;
    }
    
    public int compareTo(Object t) {
        TestID id=((TestResult)t)._testID;
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
    public TestResult clone() {
        TestResult newResult=new TestResult();
        newResult._testID=_testID;
        newResult._passed=_passed;
        newResult._testOutput=_testOutput;
        return newResult;
    }
}
