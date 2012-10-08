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
 * Test result information
 * @author Xavier Baró
 */
public class TestResult {
    /**
     * Test index
     */
    private int _index;
    
    /**
     * Indicates if the test is public
     */
    private boolean _isPublic;
    
    /**
     * Indicates if the test is sucsesfully passed
     */
    private boolean _isPassed;
    
    /**
     * Contains the program output for failed tests
     */
    private String _output;
    
    /**
     * Elapsed time for this test
     */
    private long _elapsedTime;

    public int getIndex() {
        return _index;
    }

    public void setIndex(int _index) {
        this._index = _index;
    }

    public long getElapsedTime() {
        return _elapsedTime;
    }

    public void setElapsedTime(long _elapsedTime) {
        this._elapsedTime = _elapsedTime;
    }

    public boolean isIsPassed() {
        return _isPassed;
    }

    public void setIsPassed(boolean _isPassed) {
        this._isPassed = _isPassed;
    }

    public boolean isIsPublic() {
        return _isPublic;
    }

    public void setIsPublic(boolean _isPublic) {
        this._isPublic = _isPublic;
    }

    public String getOutput() {
        return _output;
    }

    public void setOutput(String _output) {
        this._output = _output;
    }
}
