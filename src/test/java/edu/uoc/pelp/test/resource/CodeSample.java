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
package edu.uoc.pelp.test.resource;

import edu.uoc.pelp.engine.aem.TestData;
import java.util.ArrayList;

/**
 * This class implements a code sample
 * @author Xavier Baró
 */
public class CodeSample {
    /**
     * Sample Identifier
     */
    private CodeSampleID _id;
       
    /**
     * Compilation flag, that indicates if the code can be correctly compiled
     */
    private boolean _compile=false;
    
    /**
     * Execution flag, that indicates if the code can be correctly executed
     */
    private boolean _execute=false;
    
    /**
     * Code of the sample
     */
    private String _code=null;
    
    /**
     * Description of the code
     */
    private String _description;
    
    /**
     * Suggested name for the file containing this code
     */
    private String _suggestedFilename=null;
    
    /**
     * Array with all tests
     */
    private ArrayList<TestData> _tests=new ArrayList<TestData>();

    /**
     * Default constructor
     * @param codeID Identifier for the sample
     * @param code Code of the sample
     */
    public CodeSample(CodeSampleID codeID, String code) {
        _id=codeID;
        _code=code;
    }
    
    /**
     * Get the code sample identifier
     * @return Code sample identifier
     */
    public CodeSampleID getID() {
        return _id;
    }
    
    /**
     * Assigns the compilable flag, that indicates if the code can be correctly compiled
     * @param value True if it can be compiled of false if it has compilation errors
     */
    public void setCompilable(boolean value) {
        _compile=value;
    }
    
    /**
     * Assigns the executable flag, that indicates if the code can be correctly executed
     * @param value True if it can be executed of false if it has execution errors
     */
    public void setExecutable(boolean value) {
        _compile=value;
    }
    
    /**
     * Assigns a description to the code
     * @param description Description to be added
     */
    public void setDescription(String description) {
        _description=description;
    }
    
    /**
     * Gets the description of this code
     * @return Description of the code
     */
    public String getDescription() {
        return _description;
    }
    
    /**
     * Indicates if this sample can be compiled
     * @return True if the code can be corretly compiled and False if it has compilation errors.
     */
    public boolean isCompilable() {
        return _compile;
    }
    
    /**
     * Indicates if this sample can be executed
     * @return True if the code can be corretly executed and False if it has execution errors.
     */
    public boolean isExecutable() {
        return _execute;
    }
    
    /**
     * Add a new test to the code sample
     * @param test Test to be added
     */
    public void addTest(TestData test) {
        _tests.add(test);
    }
    
     /**
     * Get the tests of this sample
     * @return Array of tests
     */
    public TestData[] getSampleTests() {
        TestData[] tests=new TestData[_tests.size()];
        _tests.toArray(tests);
        
        return tests;
    }

    /**
     * Obtains the suggested filename for the file containing this code
     * @return The suggested filename, or null if it is not provided.
     */
    public String getSuggestedFilename() {
        return _suggestedFilename;
    }

    /**
     * Assigns a suggested filename for the file containing this code
     * @param _suggestedFilename The suggested filename, or null if it is not necessary.
     */
    public void setSuggestedFilename(String suggestedFilename) {
        _suggestedFilename = suggestedFilename;
    }    
}