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

import edu.uoc.pelp.engine.aem.TestData;
import java.io.File;
import java.util.HashMap;

/**
 * This class implements a test for a certain activity. Tests will be used to check  
 * if delivers for this activity are properly working.
 * @author Xavier Baró
 */
public class ActivityTest extends TestData {
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
     * Default constructor for basic tests
     * @param input String with the input
     * @param output String with the expected output
     */
    public ActivityTest(String input,String output) {
        super(input,output);
    }
    
    /**
     * Default constructor for basic tests
     * @param input File with the input
     * @param output File with the expected output
     */
    public ActivityTest(File input,File output) {
        super(input,output);
    }
    /**
     * Default constructor for an empty test
     * @param id ActivityTest identifier
     */
    public ActivityTest(TestID id) {
        super(null);
        _testID=id;
    }
    
    /**
     * Default constructor for basic tests
     * @param id ActivityTest identifier
     * @param testData Object with test data
     */
    public ActivityTest(TestID id,TestData testData) {
        super(testData);
        _testID=id;
    }
    
    /**
     * Default constructor for basic tests
     * @param id ActivityTest identifier
     * @param input File with the input
     * @param output File with the expected output
     */
    public ActivityTest(TestID id,File input,File output) {
        super(input,output);
        _testID=id;
    }
    
    /**
     * Default constructor for basic tests
     * @param test Object with the test information
     */
    public ActivityTest(TestData test) {
        super(test);
    }
    
    /**
     * Default constructor for empty tests
     */
    public ActivityTest() {
        super();
    }
    
    /**
     * Default copy constructor 
     * @param test Object with the test information
     */
    public ActivityTest(ActivityTest test) {
        super(test);
        _testID=new TestID(test._testID);
        _public=test._public;
        for(String lang:test.getLanguageCodes()) {
            setDescription(lang, test.getDescription(lang));
        }
    }
    @Override
    public ActivityTest clone() {
        ActivityTest newTest=new ActivityTest(_testID);
        
        newTest._public=_public;
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
    public final void setDescription(String languageCode,String description) {
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
    
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActivityTest other = (ActivityTest) obj;
        if (this._testID != other._testID && (this._testID == null || !this._testID.equals(other._testID))) {
            return false;
        }
        if (this._public != other._public) {
            return false;
        }
        if (this._description != other._description && (this._description == null || !this._description.equals(other._description))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this._testID != null ? this._testID.hashCode() : 0);
        hash = 29 * hash + (this._public ? 1 : 0);
        hash = 29 * hash + (this._description != null ? this._description.hashCode() : 0);
        return hash;
    }
    
    
    
}

