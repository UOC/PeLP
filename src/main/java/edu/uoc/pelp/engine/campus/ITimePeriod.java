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

import java.util.Date;

/**
 * Interface that defines a generic identifier for a time period (year, semester, ...)
 * in the application. Since it will depends of the implementation for the campus, 
 * it may be implemented for each institution.
 * @author Xavier Baró
 */
public interface ITimePeriod extends IPelpID{
    /**
     * Check if the period is active in this moment or not.
     * @return True if it is active or false if not (future or past period)
     */
    boolean isActive();
    
    /**
     * Get the starting moment of this period
     * @return Date object with the starting moment
     */
    Date getInitialDate();
    
    /**
     * Get the ending moment of this period
     * @return Date object with the ending moment
     */
    Date getFinalDate();
}
