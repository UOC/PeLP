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

import edu.uoc.pelp.engine.activity.Activity;
import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverFile;
import edu.uoc.pelp.engine.deliver.DeliverFileID;
import edu.uoc.pelp.engine.deliver.DeliverID;
import java.util.List;

/**
 * This interface defines the basic operations of the DAO for deliver files
 * @author Xavier Baró
 */
public interface IDeliverFilesDAO {
    /**
     * Adds a new file to the given deliver
     * @param deliver Deliver identifier
     * @param object Object to be stored
     * @return The identifier for new object or null if an error occurred
     */
    DeliverFileID add(DeliverID deliver,DeliverFile object);
    
    /**
     * Deletes the given object
     * @param id Identifier of the object to be deleted
     * @return True if the process finish successfully or Fals if any error occurred. It fails if the object does not exist.
     */
    boolean delete(DeliverFileID id);
    
    /**
     * Update the stored object with the new object
     * @param object Object to be updated
     * @return True if the process finish successfully or False if any error occurred. It fails if the object does not exist.
     */
    boolean update(DeliverFile object);
    
    /**
     * Obtain the list of all delivers files
     * @return List of Deliver files
     */
    List<DeliverFile> findAll();
    
    /**
     * Obtain the list of all files for the given deliver
     * @param deliver Deliver identifier
     * @return List of Deliver files
     */
    List<DeliverFile> findAll(DeliverID deliver);
       
    /**
     * Find the information of a deliver file
     * @param id The identifier of the object to be searched
     * @return Object with all the information or null if not exists.
     */
    DeliverFile find(DeliverFileID id);
    
    /**
     * Obtain the identifier of the last stored deliver file for a certain deliver
     * @param deliver Information for the deliver
     * @return Object identifier
     */
    DeliverFileID getLastID(DeliverID deliver);
}
