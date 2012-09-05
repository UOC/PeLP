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

import java.util.Date;

/**
 * This class represents the results obtained building a Code Project
 * @author Xavier Baró
 */
public class BuildResult {
    
    /**
     * Moment when building process is initiated
     */
    private Date _startTime=null;
    
    /**
     * Moment when building process is finished
     */
    private Date _endTime=null;
    
    /**
     * Flag that indicates if the building process has successfully finished
     */
    private boolean _correct=false;
    
    /**
     * Messages produced during the building process
     */
    private String _message=null;
    
    /**
     * Language identifier considered for the building process.
     */
    private String _language=null;
    
    /**
     * Obtain the elapsed time in miliseconds for the building procedure.
     * @return Elapsed time in miliseconds.
     */
    public long getElapsedTime() {
        if(_startTime==null || _endTime==null) {
            return -1l;
        }        
        return _endTime.getTime()-_startTime.getTime();
    }
    
    /**
     * Get the moment when the building process was initiated.
     * @return Initial date and time
     */
    public Date getStartTime() {
        return _startTime;        
    }
    
    /**
     * Get the moment when the building process was finished.
     * @return Final date and time
     */
    public Date getEndTime() {
        return _endTime;        
    }
    
    /**
     * Indicates that the building process is ready to start
     */
    public void start() {
        _startTime=new Date();
    }
    
    /**
     * Indicates that the building process is finished
     */
    public void end() {
        _endTime=new Date();
    }
    
    /**
     * Set the programming language considered for the building process
     * @param language String identifier for the programming language
     */
    public void setLanguage(String language) {
        _language=language;
    }
    
    /**
     * Get the programming language considered for the building process
     * @return String identifier for the programming language
     */
    public String getLanguage() {
        return _language;
    }
    
    /**
     * Assign the result of the building process
     * @param correct Indicates if the process was successfully finished or not.
     * @param message Messages produced during the building process
     */
    public void setResult(boolean correct,String message) {
        _correct=correct;
        _message=message;
        if(_endTime==null) {
            end();
        }
    }
    
    /**
     * Indicates if the last building process was correctly finished or not.
     * @return True if it was correct and false otherwise.
     */
    public boolean isCorrect() {
        return _correct;
    }
    
    /**
     * Obtain the message of the last building process.
     * @return Messages generated during the building process.
     */
    public String getMessge() {
        return _message;
    }
    
    @Override
    public BuildResult clone() {
        BuildResult newObj=new BuildResult();
        
        newObj._correct=_correct;
        newObj._startTime=_startTime;
        newObj._endTime=_endTime;
        newObj._language=_language;
        newObj._message=_message;
        
        return newObj;
    }
}
