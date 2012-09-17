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
package edu.uoc.pelp.model.dao;

import edu.uoc.pelp.engine.campus.ITimePeriod;
import java.util.List;

/**
 * This interface defines the basic operations of the DAO for Time periods
 * @author Xavier Baró
 */
public interface ITimePeriodDAO {
    /**
     * Creates a new register for the given object
     * @param object Object to be stored
     * @return True if the process finish successfully or Fals if any error occurred.
     */
    boolean save(ITimePeriod object);
    
    /**
     * Deletes the given object
     * @param object Object to be deleted
     * @return True if the process finish successfully or Fals if any error occurred. It fails if the object does not exist.
     */
    boolean delete(ITimePeriod object);
    
    /**
     * Update the stored object with the new object
     * @param object Object to be updated
     * @return True if the process finish successfully or Fals if any error occurred. It fails if the object does not exist.
     */
    boolean update(ITimePeriod object);
    
    /**
     * Obtain the list of all periods
     * @return List of periods
     */
    List<ITimePeriod> findAll();
    
    /**
     * Obtain the list of active periods
     * @return List of periods
     */
    List<ITimePeriod> findActive();
    
    /**
     * Find the information of a period
     * @param object The object to be searched
     * @return Object with all the information or null if not exists.
     */
    ITimePeriod find(ITimePeriod object);
}
