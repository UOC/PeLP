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

/**
 * This class represents the result of a Code Project over a Test. 
 * @author Xavier Baró
 */
public class TestResult {
    
    /**
     * Indicates if the test is correctly passed or not
     */
    protected boolean _passed=false;
    
    /**
     * In case of test fail, stores the output of the program to this test
     */
    protected String _testOutput=null;
    
    /**
     * Elapsed time for this test
     */
    protected Long _elapsedTime=null;
    
    /**
     * Default copy constructor
     * @param testResult Object to by copied
     */
    public TestResult(TestResult testResult) {
        _passed=testResult._passed;
        _testOutput=testResult._testOutput;
        _elapsedTime=testResult._elapsedTime;
    }

    /**
     * Default constructor for an empty result
     */
    public TestResult() {
        
    }
    
    /**
     * Assign the results of a certain test for a given deliver
     * @param passed True if the test is successfully passed or Fals otherwise
     * @param output Output of the program over the test
     */
    public void setResult(boolean passed,String output) {
        _passed=passed;
        if(!passed) {
            _testOutput=output;
        } else {
            _testOutput=null;
        }
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
    
    /**
     * Get the elapsed time in miliseconds to run this test
     * @return Number of miliseconds
     */
    public Long getElapsedTime() {
        return _elapsedTime;
    }
    
    /**
     * Set the elapsed time in miliseconds to run this test
     * @param elapsedTime Number of miliseconds
     */
    public void setElapsedTime(Long elapsedTime) {
        _elapsedTime=elapsedTime;
    }
    
    /**
     * Remove information that should be hidded in private tests
     */
    public void removePrivateInformation() {
        _testOutput=null;
    }
    
    @Override
    public TestResult clone() {
        return new TestResult(this);
    }
}
