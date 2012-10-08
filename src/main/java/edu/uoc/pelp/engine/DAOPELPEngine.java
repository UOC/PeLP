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
package edu.uoc.pelp.engine;

import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.activity.DAOActivityManager;
import edu.uoc.pelp.engine.admin.DAOAdministrationManager;
import edu.uoc.pelp.engine.campus.ICampusConnection;
import edu.uoc.pelp.engine.deliver.DAODeliverManager;
import edu.uoc.pelp.engine.information.DAOInformationManager;
import edu.uoc.pelp.model.dao.*;
import edu.uoc.pelp.model.dao.admin.AdministrationDAO;
import org.hibernate.SessionFactory;

/**
 * This class implements the engine of the PELP system. 
 * @author Xavier Baró
 */
public class DAOPELPEngine extends PELPEngine {
        
    protected SessionFactory _sessionFactory;
    
    /**
     * Default constructor for a an engine using Hibernate DAO persistence.
     * @param sessionFactory 
     * @param campusConnection
     * @param configObject 
     */
    public DAOPELPEngine(SessionFactory sessionFactory,ICampusConnection campusConnection,IPelpConfiguration configObject) {
        // Call parent constructor
        super();
        
        // Assign the campus connection object
        _campusConnection=campusConnection;
               
        // Assign the configuration object
        _configuration=configObject;
        
        // Create the DAO objects
        updateSessionFactory(sessionFactory);
    }
    
    /**
     * Update all the objects depending on the sessin factory
     * @param sessionFactory Session factory
     */
    public final void updateSessionFactory(SessionFactory sessionFactory) {
        // Create the DAO objects
        ActivityDAO activityDAO=new ActivityDAO(sessionFactory);
        DeliverDAO deliverDAO=new DeliverDAO(sessionFactory);
        DeliverResultsDAO deliverResultsDAO=new DeliverResultsDAO(sessionFactory);        
        AdministrationDAO adminDAO=new AdministrationDAO(sessionFactory);
        LoggingDAO logDAO=new LoggingDAO(sessionFactory);
        StatisticsDAO statsDAO=new StatisticsDAO(sessionFactory);
               
        // Create the managers
        _deliverManager=new DAODeliverManager(deliverDAO,deliverResultsDAO);
        _activityManager=new DAOActivityManager(activityDAO);
        _administrationManager=new DAOAdministrationManager(adminDAO);
        _informationManager=new DAOInformationManager(logDAO,statsDAO);
    }
}
