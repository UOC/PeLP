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

import java.util.Date;

/**
 * Summary information for a user deliver
 * @author Xavier Baró
 */
public class DeliverSummary {
    /**
     * User information
     */
    private UserInformation _user;
    
    /**
     * Submission date
     */
    private Date _submissionDate;
    
    /**
     * Deliver index
     */
    private int _deliverIndex;
    
    /**
     * Maximum number of alowed delivers
     */
    private int _maxDelivers;
    
    /**
     * Compilation result
     */
    private boolean _compileOK;
    
    /**
     * Compilation message
     */
    private String _compileMessage;
    
    /**
     * Total number of public tests
     */
    private int _totalPublicTests;
            
    /**
     * Total number of private tests
     */
    private int _totalPrivateTests;
    
    /**
     * Total number of correctly passed public tests
     */
    private int _passedPublicTests;
            
    /**
     * Total number of correctly passed private tests
     */
    private int _passedPrivateTests;
    
    /**
     * Programming language code
     */
    private String _programmingLanguage;

    public String getCompileMessage() {
        return _compileMessage;
    }

    public void setCompileMessage(String _compileMessage) {
        this._compileMessage = _compileMessage;
    }

    public boolean isCompileOK() {
        return _compileOK;
    }

    public void setCompileOK(boolean _compileOK) {
        this._compileOK = _compileOK;
    }

    public int getDeliverIndex() {
        return _deliverIndex;
    }

    public void setDeliverIndex(int _deliverIndex) {
        this._deliverIndex = _deliverIndex;
    }

    public int getMaxDelivers() {
        return _maxDelivers;
    }

    public void setMaxDelivers(int _maxDelivers) {
        this._maxDelivers = _maxDelivers;
    }

    public int getPassedPrivateTests() {
        return _passedPrivateTests;
    }

    public void setPassedPrivateTests(int _passedPrivateTests) {
        this._passedPrivateTests = _passedPrivateTests;
    }

    public int getPassedPublicTests() {
        return _passedPublicTests;
    }

    public void setPassedPublicTests(int _passedPublicTests) {
        this._passedPublicTests = _passedPublicTests;
    }

    public String getProgrammingLanguage() {
        return _programmingLanguage;
    }

    public void setProgrammingLanguage(String _programmingLanguage) {
        this._programmingLanguage = _programmingLanguage;
    }

    public Date getSubmissionDate() {
        return _submissionDate;
    }

    public void setSubmissionDate(Date _submissionDate) {
        this._submissionDate = _submissionDate;
    }

    public int getTotalPrivateTests() {
        return _totalPrivateTests;
    }

    public void setTotalPrivateTests(int _totalPrivateTests) {
        this._totalPrivateTests = _totalPrivateTests;
    }

    public int getTotalPublicTests() {
        return _totalPublicTests;
    }

    public void setTotalPublicTests(int _totalPublicTests) {
        this._totalPublicTests = _totalPublicTests;
    }

    public UserInformation getUser() {
        return _user;
    }

    public void setUser(UserInformation _user) {
        this._user = _user;
    }
}
