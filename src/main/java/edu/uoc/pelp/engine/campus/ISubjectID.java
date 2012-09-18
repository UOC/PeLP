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
package edu.uoc.pelp.engine.campus;

/**
 * Interface that defines a generic identifier for a subject in the application. 
 * Since it will depends of the implementation for the campus, it may be 
 * implemented for each institution.
 * @author Xavier Baró
 */
public interface ISubjectID extends IPelpID {
    @Override
    public abstract boolean equals(Object object);
    
    @Override
    public abstract String toString();
    
    /**
     * Checks if the information stored in the object corresponds to a valid subject.
     * @return True if the information is correct or false otherwise.
     */
    boolean isValid();
}
