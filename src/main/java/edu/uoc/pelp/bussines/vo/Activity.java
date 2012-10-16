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
 * Activity information
 * @author Xavier Baró
 */
public class Activity {
    /**
     * Subject object
     */
    private Subject _subject;
    
    /**
     * Activity index in this subject
     */
    private int _index;
    
    /**
     * Activity description
     */
    private String _description;
    
    public Activity() {
        
    }
    
    public Activity(Subject subject, int index) {
        this._subject=subject;
        this._index=index;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String _description) {
        this._description = _description;
    }

    public int getIndex() {
        return _index;
    }

    public void setIndex(int _index) {
        this._index = _index;
    }

    public Subject getSubject() {
        return _subject;
    }

    public void setSubject(Subject _subject) {
        this._subject = _subject;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Activity other = (Activity) obj;
        if (this._subject != other._subject && (this._subject == null || !this._subject.equals(other._subject))) {
            return false;
        }
        if (this._index != other._index) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this._subject != null ? this._subject.hashCode() : 0);
        hash = 71 * hash + this._index;
        return hash;
    }
    
    
}
