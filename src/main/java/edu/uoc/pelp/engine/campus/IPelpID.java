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
 * Implementation for a generic identifier in the PeLP platform.
 * @author Xavier Baró
 */
public interface IPelpID extends Comparable<IPelpID> {
    /**
     * Generates an string representation of this identifier, compatible with the
     * method {@link parse(String str)}
     * @see parse(String str)
     * @return String representation for the identifier object
     */
    @Override
    String toString();
    
    /**
     * Parse the ID from an string representation of it. Format of string is 
     * expected to be compatible with the output format from the method toString
     * @param str String representation of the indentifier
     * @return Identifier object
     */
    IPelpID parse(String str);  
    
}
