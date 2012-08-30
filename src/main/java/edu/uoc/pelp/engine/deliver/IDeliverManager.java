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
package edu.uoc.pelp.engine.deliver;

import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.campus.IUserID;

/**
 * This interface describes the methods for delivers managing.
 * @author Xavier Baró
 */
public interface IDeliverManager {
    /**
     * Add a new delivery of a user for an existing activity.
     * @param user User identifier
     * @param activity Activity identifier
     * @param deliver Deliver object with all information.
     * @return Deliver identifyer.
     */
    DeliverID addDeliver(IUserID user, ActivityID activity, Deliver deliver);
    
    /**
     * Modifies the information of a certain delivery.
     * @param deliver Deliver with new information. The deliverID cannot be modifyed, the rest of the information is updated.
     * @return True if the new information has been correctly modified or False if any error occurred. 
     */
    boolean editDeliver(Deliver deliver);
    
    /**
     * Delete a certain deliver
     * @param deliverID Identifier of the deliver to be reomoved.
     * @return True if the deliver been correctly deleted or False if any error occurred.
     */
    boolean deleteDeliver(DeliverID deliverID);
    
    /**
     * Get the list of delivers for a given activity
     * @param activity Object identifying a concrete activity.
     * @return Array of deliver identifyers for given activity
     */
    DeliverID[] getActivityDelivers(ActivityID activity);
    
    /**
     * Get the list of delivers for a given user and activity.
     * @param user Object identifying a concrete user
     * @param activity Object identifying a concrete activity.
     * @return Array of deliver identifyers of this user for the given activity 
     */
    DeliverID[] getUserDelivers(IUserID user, ActivityID activity);
    
    /**
     * Add the results of the analysis of this deliver
     * @param deliverID Object identifying a delivery
     * @param results Object with the results of the code analysis
     * @return True if the results has been correctly added or False if it fails.
     */    
    boolean addResults(DeliverID deliverID, AnalysisResults results);
    
    /**
     * Modifies the information of a certain delivery results.
     * @param results Results object with new information. The deliverID cannot be modifyed, the rest of the information is updated.
     * @return True if the new information has been correctly modified or False if any error occurred. 
     */
    boolean editResults(DeliverResults results);
    
    /**
     * Delete a certain deliver results
     * @param deliverID Identifier of the deliver which results will be reomoved.
     * @return True if the deliver results been correctly deleted or False if any error occurred.
     */
    boolean deleteResults(DeliverID deliverID);
    
    /**
     * Get the results obtained when this deliver was analyzed
     * @param deliverID Object identifying a certain deliver
     * @return Results of analyzing the deliver or null if there are not available results
     */
    DeliverResults getResults(DeliverID deliverID);
    
    /**
     * Obtain the deliver data
     * @param deliverID Deliver Identifyer
     * @return The object with the deliver information or null if it does not exist.
     */
    Deliver getDeliver(DeliverID deliverID);

    /**
     * Obtain the number of delivers performed by a certain user to a certain activity
     * @param userID User identifier
     * @param activityID Activity identifier
     * @return Number of delivers of the user to the activity
     */
    int getNumUserDelivers(IUserID userID, ActivityID activityID);
}
