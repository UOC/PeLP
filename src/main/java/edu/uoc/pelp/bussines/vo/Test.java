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
package edu.uoc.pelp.bussines.vo;

/**
 * Test information
 * @author Xavier Baró
 */
public class Test {
    /**
     * Text to be passed as input
     */
    private String _inputText;
    
    /**
     * Text expected as output
     */
    private String _expectedOutput;
    
    /**
     * Indicates if this is a public test
     */
    private boolean _public;
    
    /**
     * Maximun time expected for this test
     */
    private int _maxExpectedTime;
    
    /**
     * Full path of the file used as input for this test
     */
    private String _inputFilePath;
    
    /** 
     * Full path with the expected output for this test
     */
    private String _expectedOutputFilePath;

    public String getExpectedOutput() {
        return _expectedOutput;
    }

    public void setExpectedOutput(String _expectedOutput) {
        this._expectedOutput = _expectedOutput;
    }

    public String getExpectedOutputFilePath() {
        return _expectedOutputFilePath;
    }

    public void setExpectedOutputFilePath(String _expectedOutputFilePath) {
        this._expectedOutputFilePath = _expectedOutputFilePath;
    }

    public String getInputFilePath() {
        return _inputFilePath;
    }

    public void setInputFilePath(String _inputFilePath) {
        this._inputFilePath = _inputFilePath;
    }

    public String getInputText() {
        return _inputText;
    }

    public void setInputText(String _inputText) {
        this._inputText = _inputText;
    }

    public int getMaxExpectedTime() {
        return _maxExpectedTime;
    }

    public void setMaxExpectedTime(int _maxExpectedTime) {
        this._maxExpectedTime = _maxExpectedTime;
    }

    public boolean isPublic() {
        return _public;
    }

    public void setPublic(boolean _public) {
        this._public = _public;
    }
}
