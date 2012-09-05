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

import java.io.*;

/**
 * This class implements a test data object, used to store all the information for a certain test.
 * @author Xavier Baró
 */
public class TestData {
    /**
     * Maximum time for the execution of this test. Allow to provide extra time for this test or restrict
     * the execution time. If null, default timeout is used.
     */
    protected Long _maxTime=null;
    
    /**
     * String containing the intput information to the program
     */
    protected String _inputStr=null;
    
    /**
     * String containing the expected output information from the program
     */
    protected String _expectedOutputStr=null;
    
    /**
     * File containing the intput information to the program
     */
    protected File _inputFile=null;
    
    /**
     * File containing the expected output information from the program
     */
    protected File _expectedOutputFile=null;
    
    /**
     * Default constructor for empty tests
     */
    public TestData() {
  
    }
    
    /**
     * Default constructor for basic tests
     * @param input String with the input
     * @param output String with the expected output
     */
    public TestData(String input,String output) {
        _inputStr=input;
        _expectedOutputStr=output;
    }
    
    /**
     * Default constructor for basic tests
     * @param input File with the input
     * @param output File with the expected output
     */
    public TestData(File input,File output) {
        _inputFile=input;
        _expectedOutputFile=output;
    }
    
    /**
     * Default copy constructor
     * @param input File with the input
     * @param output File with the expected output
     */
    public TestData(TestData testData) {
        if(testData!=null) {
            _inputStr=testData._inputStr;
            _expectedOutputStr=testData._expectedOutputStr;
            _inputFile=testData._inputFile;
            _expectedOutputFile=testData._expectedOutputFile;  
            _maxTime=testData._maxTime;
        }
    }
    
    @Override
    public TestData clone() {
        return new TestData(this);
    }
    
    /**
     * Compares the program output with the expected one
     * @param programOutput Output of the execution of the programm with the input information
     * @return True if both informations are equal or False otherwise
     * @throws FileNotFoundException If some parameters contains files that doesn't exist
     */
    public boolean checkResult(String programOutput) throws FileNotFoundException {
        
        String expected=readData(getExpectedOutputStream());
        
        // Empty strings and null strings are considered the same
        if(expected==null) {
            expected="";
        }
        
        // Check real output data
        if(programOutput==null) {
            programOutput="";
        }
        
        // To avoid diferences because of system new line char, replace \r\n by \n
        // Changing also the sequence in the expected value, avoid errors when tests are created automatically.
        // programOutput=programOutput.replace(System.getProperty("line.separator"),"\n");
        expected=expected.replace("\r\n","\n");
        programOutput=programOutput.replace("\r\n","\n");

        
        // Compare the real and expected data
        return expected.equals(programOutput);
    }
    
    /**
     * Returns an Input stream to read input data, either if it is strings or file.
     * @return Input stream accessing to the input test data
     * @throws FileNotFoundException If the test contains an unaccessible file.
     */
    public InputStream getInputStream() throws FileNotFoundException {
        if(_inputFile!=null) {
            return new FileInputStream(_inputFile);
        }
        if(_inputStr!=null) {
            return new ByteArrayInputStream(_inputStr.getBytes());
        } 
        return null;
    }
    
    /**
     * Returns an Input stream to read expected output data, either if it is strings or file.
     * @return Input stream accessing to the expected output for test data
     * @throws FileNotFoundException If the test contains an unaccessible file.
     */
    public InputStream getExpectedOutputStream() throws FileNotFoundException {
        if(_expectedOutputFile!=null) {
            return new FileInputStream(_expectedOutputFile);
        }
        if(_expectedOutputStr!=null) {
            return new ByteArrayInputStream(_expectedOutputStr.getBytes());
        } 
        return null;
    }
    
    /**
     * Get the maximum time allowed for this test
     * @return Time in miliseconds
     */
    public Long getMaxTime() {
        return _maxTime;
    }
    
    /**
     * Assign the maximum time allowed for this test. If null, default time is used
     * @param timeout 
     */
    public void setMaxTime(Long timeout) {
        _maxTime=timeout;
    }
    
    /**
     * Read all the information from an InputStream and stores it in a String
     * @param is Input stream used as source
     * @return String with all readed data
     */
    private String readData(InputStream is) {
        if (is != null) {
            Writer writer = new StringWriter();
 
            char[] buffer = new char[1024];
            
            // Create a reader. If encoding is not valid, use default one
            Reader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                reader = new BufferedReader(new InputStreamReader(is));
            }
                
            // Read all the bytes
            try {                
                int n;                
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (IOException ex) {
                // Reading process is stoped and program executed with obtained input
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    // Nothing is done
                }
            }
            return writer.toString();
        } else {       
            return "";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TestData other = (TestData) obj;
        if (this._maxTime != other._maxTime && (this._maxTime == null || !this._maxTime.equals(other._maxTime))) {
            return false;
        }
        if ((this._inputStr == null) ? (other._inputStr != null) : !this._inputStr.equals(other._inputStr)) {
            return false;
        }
        if ((this._expectedOutputStr == null) ? (other._expectedOutputStr != null) : !this._expectedOutputStr.equals(other._expectedOutputStr)) {
            return false;
        }
        if (this._inputFile != other._inputFile && (this._inputFile == null || !this._inputFile.equals(other._inputFile))) {
            return false;
        }
        if (this._expectedOutputFile != other._expectedOutputFile && (this._expectedOutputFile == null || !this._expectedOutputFile.equals(other._expectedOutputFile))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this._maxTime != null ? this._maxTime.hashCode() : 0);
        hash = 89 * hash + (this._inputStr != null ? this._inputStr.hashCode() : 0);
        hash = 89 * hash + (this._expectedOutputStr != null ? this._expectedOutputStr.hashCode() : 0);
        hash = 89 * hash + (this._inputFile != null ? this._inputFile.hashCode() : 0);
        hash = 89 * hash + (this._expectedOutputFile != null ? this._expectedOutputFile.hashCode() : 0);
        return hash;
    }
}
