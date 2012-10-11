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

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.campus.IClassroomID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverFile;
import edu.uoc.pelp.engine.deliver.DeliverFileID;
import edu.uoc.pelp.engine.deliver.DeliverID;
import java.util.List;

/**
 * This interface defines the basic operations of the DAO for delivers
 * @author Xavier Baró
 */
public interface IDeliverDAO {
    /**
     * Adds a new user deliver to the given activity
     * @param user Owner of the deliver
     * @param actuvuty Activity related to the deliver
     * @param object Object to be stored
     * @return The identifier for new object or null if an error occurred
     */
    DeliverID add(IUserID user, ActivityID activity,Deliver object);
    
    /**
     * Deletes the given object
     * @param id Identifier of the object to be deleted
     * @return True if the process finish successfully or Fals if any error occurred. It fails if the object does not exist.
     */
    boolean delete(DeliverID id);
    
    /**
     * Update the stored object with the new object
     * @param object Object to be updated
     * @return True if the process finish successfully or Fals if any error occurred. It fails if the object does not exist.
     */
    boolean update(Deliver object);
    
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
     * Obtain the list of all delivers
     * @return List of Activities
     */
    List<Deliver> findAll();
    
    /**
     * Obtain the list of all delivers for a given user
     * @param user Information for the user
     * @return List of Delivers
     */
    List<Deliver> findAll(IUserID user);
    
    /**
     * Obtain the list of all delivers for a given user in a subject
     * @param subject Information for the subject
     * @param user Information for the user
     * @return List of Delivers
     */
    List<Deliver> findAll(ISubjectID subject,IUserID user);
    
    /**
     * Obtain the list of all delivers for a given user in a certain activity
     * @param activity Information for the activity
     * @param user Information for the user
     * @return List of Delivers
     */
    List<Deliver> findAll(ActivityID activity,IUserID user);
    
    /**
     * Obtain the list of all delivers for a given activity
     * @param activity Information for the activity
     * @return List of Delivers
     */
    List<Deliver> findAll(ActivityID activity);
    
    /**
     * Obtain the list of all delivers
     * @return List of deliver identifiers
     */
    List<DeliverID> findAllKey();
    
    /**
     * Obtain the list of all delivers for a given user
     * @param user Information for the user
     * @return List of Deliver identifiers
     */
    List<DeliverID> findAllKey(IUserID user);
    
    /**
     * Obtain the list of all delivers for a given user in a subject
     * @param subject Information for the subject
     * @param user Information for the user
     * @return List of Deliver identifiers
     */
    List<DeliverID> findAllKey(ISubjectID subject,IUserID user);
    
    /**
     * Obtain the list of all delivers for a given user in a certain activity
     * @param activity Information for the activity
     * @param user Information for the user
     * @return List of Deliver identifiers
     */
    List<DeliverID> findAllKey(ActivityID activity,IUserID user);
    
    /**
     * Obtain the list of all delivers for a given activity
     * @param activity Information for the activity
     * @return List of Deliver identifiers
     */
    List<DeliverID> findAllKey(ActivityID activity);
    
    /**
     * Find the information of a deliver
     * @param id The identifier of the object to be searched
     * @return Object with all the information or null if not exists.
     */
    Deliver find(DeliverID id);
    
    /**
     * Obtain the list of all delivers files
     * @return List of Deliver files
     */
    List<DeliverFile> findAllFiles();
    
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
     * Obtain the identifier of the last stored deliver for a certain activity and user
     * @param activity Information for the activity
     * @param user Information for the user
     * @return Object identifier
     */
    DeliverID getLastID(ActivityID activity,IUserID user);
    
    /**
     * Obtain the identifier of the last stored deliver file for a certain deliver
     * @param deliver Information for the deliver
     * @return Object identifier
     */
    DeliverFileID getLastID(DeliverID deliver);

    /**
    * Obtain the list of all the delivers of a certain classroom for a certain activity. Only a teacher
    * of the classroom can access to this information. Both, laboratory and main classrooms are checked.
    * @param classroom Identifier of the classroom for which delivers are requested.
    * @param activity Identifier of the activity delivers are requested from.
    * @return Array of Delivers.
    * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
    */
    public List<Deliver> findAllClassroom(IClassroomID classroom, ActivityID activity);

    /**
    * Obtain the last submitted deliver for each user of a certain classroom for a certain activity. 
    * @param classroom Identifier of the classroom for which delivers are requested.
    * @param activity Identifier of the activity delivers are requested from.
    * @return Array of Delivers.
    * @throws AuthPelpException If no user is authenticated or does not have enough rights to obtain this information.
    */
    public List<Deliver> findLastClassroom(IClassroomID classroom, ActivityID activity);
}
