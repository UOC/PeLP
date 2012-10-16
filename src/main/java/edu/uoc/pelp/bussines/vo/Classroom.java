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
 * Classroom information
 * @author Xavier Baró
 */
public class Classroom {
    private Subject _subject;
    private int _index;
    
    public Classroom(Subject subject, int index) {
        this._subject=subject;
        this._index=index;
    }
    
    public Classroom(Classroom object) {
        this._subject=object._subject;
        this._index=object._index;
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
        final Classroom other = (Classroom) obj;
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
        hash = 11 * hash + (this._subject != null ? this._subject.hashCode() : 0);
        hash = 11 * hash + this._index;
        return hash;
    }
    
    
}
