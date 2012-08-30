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

import java.util.ArrayList;

/**
 * This class implements the results obtained after the analysis of a Deliver
 * @author Xavier Baró
 */
public class AnalysisResults {
       
    /**
     * Results of the building process
     */
    private BuildResult _buildResult=null;
    
    /**
     * Results for each test
     */
    private ArrayList<TestResult> _testResults=new ArrayList<TestResult>();
    
    /**
     * Default constructor with the basic information
     * @param buildResult Results of the building process
     */
    public AnalysisResults(BuildResult buildResult) {    
        _buildResult=buildResult;
    }
    
    /**
     * Add the building result for the project
     * @param result Object with the build results
     */
    public void setBuildResult(BuildResult result) {
        _buildResult=result;
    }
    
    /**
     * Get the building results for the project
     * @return Object with the build results
     */
    public BuildResult getBuildResult() {
        return _buildResult;
    }
    
    /**
     * Add the result of a certain test to the analysis results
     * @param result Object with the test results
     * @return True if the result has been correctly added or False otherwise
     */
    public boolean addTestResult(TestResult result) {
        return _testResults.add(result);
    }
    
    /**
     * Return the list of results for each test over this project
     * @return Array of results, ordered by test id
     */
    public TestResult[] getResults() {
        TestResult[] retValue;
        
        retValue=new TestResult[_testResults.size()];
        _testResults.toArray(retValue);
        
        return retValue;
    }
    
    @Override
    public AnalysisResults clone() {
        AnalysisResults newObject=new AnalysisResults(_buildResult.clone());
        
        // Add the results
        _testResults.clear();
        for(TestResult test:_testResults) {
            _testResults.add(test.clone());
        }
        return newObject;
    }
}
