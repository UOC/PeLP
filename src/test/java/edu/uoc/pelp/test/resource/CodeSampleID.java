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


/**
 * This class implements the index of a code sample
 * @author Xavier Baró
 */
public class CodeSampleID {
    /**
     * Code language
     */
    private String _language;
       
    /**
     * Compilation flag, that indicates if the code can be correctly compiled
     */
    private long _index;

    /**
     * Default constructor
     * @param languageID Identifier for the language
     * @param index Index of this code. For each languageID the index starts with 1.
     */
    CodeSampleID(String languageID, long index) {
        _language = languageID;
        _index = index;
    }
    
    /**
     * Obtain the language ID
     * @return String representation of the language identifier
     */
    public String getLanguage() {
        return _language;
    }
    
    /**
     * Obtain the index
     * @return Index value.
     */
    public long getIndex() {
        return _index;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CodeSampleID other = (CodeSampleID) obj;
        if ((this._language == null) ? (other._language != null) : !this._language.equals(other._language)) {
            return false;
        }
        if (this._index != other._index) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this._language != null ? this._language.hashCode() : 0);
        hash = 59 * hash + (int) (this._index ^ (this._index >>> 32));
        return hash;
    }
}

