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

import java.io.*;
import java.util.HashMap;

/**
 * This class implements a test for a certain activity. Tests will be used to check  
 * if delivers for this activity are properly working.
 * @author Xavier Baró
 */
public class Test {
    /**
     * Unique identifier for this test. Used when this test is linked to a certain activity
     */
    private TestID _testID=null;
    
    /**
     * Flag that indicates if the test is public or private
     */
    private boolean _public=false;
    
    /**
     * String describing this test in multiple languages
     */
    private HashMap<String,String> _description=new HashMap<String,String>();
    
    /**
     * String to be used as input to the program
     */
    private String _strInput=null;
    
    /**
     * String extected as the output of the program
     */
    private String _strOutput=null;
    
    /**
     * File to be used as input to the program
     */
    private File _fileInput=null;
    
    /**
     * File with the extected output of the program
     */
    private File _fileOutput=null;
    
    /** 
     * File input stream used to read the input file
     */
    private FileInputStream _fileIStream=null;
    
    /**
     * Basic constuctor with empty inputs and outputs
     */
    public Test() {
        
    }
    
    /**
     * Default constructor for basic tests
     * @param id Test identifier
     * @param input String with the input
     * @param output String with the expected output
     */
    public Test(TestID id,String input,String output) {
        _testID=id;
        _strInput=input;
        _strOutput=output;
    }
    
    /**
     * Default constructor for basic tests
     * @param id Test identifier
     * @param input File with the input
     * @param output File with the expected output
     */
    public Test(TestID id,File input,File output) {
        _testID=id;
        _fileInput=input;
        _fileOutput=output;
    }
    
    @Override
    public Test clone() {
        Test newTest=new Test(_testID,_strInput,_strOutput);
        newTest._public=_public;
        newTest._fileInput=_fileInput;
        newTest._fileOutput=_fileOutput;
        for(String lang:getLanguageCodes()) {
            newTest.setDescription(lang, getDescription(lang));
        }
        return newTest;
    }
    
    /**
     * Gets the identifier for this test. 
     * @return Object identifying this test
     */
    public TestID getID() {
        return _testID;
    }
    
    /**
     * Assigns a new identifier to this test.
     * @param testID Identifier to be assigned to this test
     */
    public void setTestID(TestID testID) {
        _testID=testID;
    }
    
    /**
     * Change the public property of the test
     * @param value True to set the test as public or False to make it private.
     */
    public void setPublic(boolean value) {
        _public=value;
    }
    
    /** 
     * Indicates if the test is public or not.
     * @return True if is public or False if it is private.
     */
    public boolean isPublic() {
        return _public;
    }
    
    /**
     * Get the description of this test for a certain language
     * @param languageCode Code for desired language (CAT for Catalan, ES for Spanish, ENG for English, ...)
     * @return Description of the test
     */
    public String getDescription(String languageCode) {
        return _description.get(languageCode);
    }
    
    /**
     * Adds a description for the test in a certain language.
     * @param languageCode Code for desired language (CAT for Catalan, ES for Spanish, ENG for English, ...)
     * @param description Description of the test
     */
    public void setDescription(String languageCode,String description) {
        // Remove old descriptions
        if(_description.containsKey(languageCode)) {
            _description.remove(languageCode);
        }
        // Add new description
        _description.put(languageCode, description);
    }
    
    /**
     * Obtain the list of available language codes
     * @return Array with the codes of the languages for which a description is provided
     */
    public String[] getLanguageCodes() {
        String[] retList=new String[_description.keySet().size()];
        _description.keySet().toArray(retList);
        return retList;
    }
    
    /**
     * Returns an Input stream to read input data, either if it is strings or file.
     * @return Input stream accessing to the input test data
     * @throws FileNotFoundException If the test contains an unaccessible file.
     */
    public InputStream getInputStream() throws FileNotFoundException {
        if(_fileInput!=null) {
            return new FileInputStream(_fileInput);
        }
        if(_strInput!=null) {
            return new ByteArrayInputStream(_strInput.getBytes());
        } 
        return null;
    }
    
    /**
     * Returns an Input stream to read output data, either if it is strings or file.
     * @return Input stream accessing to the expected output for test data
     * @throws FileNotFoundException If the test contains an unaccessible file.
     */
    public InputStream getExpectedOutputStream() throws FileNotFoundException {
        if(_fileOutput!=null) {
            return new FileInputStream(_fileOutput);
        }
        if(_strOutput!=null) {
            return new ByteArrayInputStream(_strOutput.getBytes());
        } 
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Test other = (Test) obj;
        if (this._testID != other._testID && (this._testID == null || !this._testID.equals(other._testID))) {
            return false;
        }
        if (this._public != other._public) {
            return false;
        }
        if (this._description != other._description && (this._description == null || !this._description.equals(other._description))) {
            return false;
        }
        if ((this._strInput == null) ? (other._strInput != null) : !this._strInput.equals(other._strInput)) {
            return false;
        }
        if ((this._strOutput == null) ? (other._strOutput != null) : !this._strOutput.equals(other._strOutput)) {
            return false;
        }
        if (this._fileInput != other._fileInput && (this._fileInput == null || !this._fileInput.equals(other._fileInput))) {
            return false;
        }
        if (this._fileOutput != other._fileOutput && (this._fileOutput == null || !this._fileOutput.equals(other._fileOutput))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this._testID != null ? this._testID.hashCode() : 0);
        hash = 71 * hash + (this._public ? 1 : 0);
        hash = 71 * hash + (this._description != null ? this._description.hashCode() : 0);
        hash = 71 * hash + (this._strInput != null ? this._strInput.hashCode() : 0);
        hash = 71 * hash + (this._strOutput != null ? this._strOutput.hashCode() : 0);
        hash = 71 * hash + (this._fileInput != null ? this._fileInput.hashCode() : 0);
        hash = 71 * hash + (this._fileOutput != null ? this._fileOutput.hashCode() : 0);
        return hash;
    }
}

