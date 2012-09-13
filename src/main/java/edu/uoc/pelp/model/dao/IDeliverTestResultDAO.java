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

import edu.uoc.pelp.engine.activity.ActivityTestResult;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.model.vo.DeliverTestResult;
import java.util.List;

/**
 * This interface defines the basic operations of the DAO for deliver test results
 * @author Xavier Baró
 */
public interface IDeliverTestResultDAO {
    /**
     * Adds a new test result to the deliver
     * @param deliverID Deliver identifier
     * @param object Object to be stored
     * @return True if the results has been correctly added or false if an error occurred
     */
    boolean add(DeliverID deliverID,ActivityTestResult testResult);
    
    /**
     * Deletes the results for the given deliver
     * @param deliverID Deliver idengifier
     * @param testResult Object to be deleted
     * @return True if the process finish successfully or Fals if any error occurred. It fails if the object does not exist.
     */
    boolean delete(DeliverID deliverID,ActivityTestResult testResult);
    
    /**
     * Update the stored object with the new object
     * @param deliverID Deliver idengifier
     * @param testResult Object to be updated
     * @return True if the process finish successfully or False if any error occurred. It fails if the object does not exist.
     */
    boolean update(DeliverID deliverID,ActivityTestResult testResult);
       
    /**
     * Find all the test results for the given deliver
     * @param deliverID The identifier of the object to be searched
     * @return Object with all the information or null if not exists.
     */
    List<DeliverTestResult> find(DeliverID deliverID);
}
