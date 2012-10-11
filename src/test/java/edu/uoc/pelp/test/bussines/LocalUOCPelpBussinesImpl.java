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
package edu.uoc.pelp.test.bussines;

import edu.uoc.pelp.bussines.UOC.UOCPelpBussinesImpl;
import edu.uoc.pelp.bussines.exception.InvalidEngineException;
import edu.uoc.pelp.bussines.exception.InvalidSessionFactoryException;
import edu.uoc.pelp.engine.DAOPELPEngine;
import edu.uoc.pelp.engine.activity.DAOActivityManager;
import edu.uoc.pelp.engine.admin.DAOAdministrationManager;
import edu.uoc.pelp.engine.deliver.DAODeliverManager;
import edu.uoc.pelp.engine.information.DAOInformationManager;
import edu.uoc.pelp.test.model.dao.*;
import edu.uoc.pelp.test.model.dao.UOC.LocalSemesterDAO;
import edu.uoc.pelp.test.model.dao.admin.LocalAdministrationDAO;
import java.io.File;
import java.net.URL;
import org.hibernate.SessionFactory;

/**
 * Implements a PeLP bussines class that uses a local database connection
 * @author Xavier Baró
 */
public class LocalUOCPelpBussinesImpl extends UOCPelpBussinesImpl {
        
    private LocalActivityDAO _activityDAO;
    private LocalDeliverDAO _deliverDAO;
    private LocalDeliverResultsDAO _deliverResultsDAO;
    private LocalAdministrationDAO _adminDAO;
    private LocalSemesterDAO _semesterDAO;
    private LocalLoggingDAO _logDAO;
    private LocalStatisticsDAO _statsDAO;
    
    /**
     * Creates a new LocalPelpBussinesImpl object from given Hibernate SessionFactory
     * @param sessionFactory SessionFactory object
     */
    public LocalUOCPelpBussinesImpl(SessionFactory sessionFactory) throws InvalidSessionFactoryException, InvalidEngineException {
        // Call parent constructor
        super();
        
        // Assign the session Factory to the parent object
        setSessionFactory(sessionFactory);
    }
    
    /**
     * Creates a new LocalPelpBussinesImpl object from given Hibernate configuration resource
     * @param resource Resource with configuration for Hibernate Database Connection
     */
    public LocalUOCPelpBussinesImpl(String resource) throws InvalidSessionFactoryException, InvalidEngineException {
        // Call parent constructor
        super();
        
        // Assign the session Factory to the parent object
        setSessionFactory(LocalDAO.buildSessionFactory(resource));
    }
    
    /**
     * Creates a new LocalPelpBussinesImpl object from given Hibernate configuration file
     * @param confFile File with configuration for Hibernate Database Connection
     */
    public LocalUOCPelpBussinesImpl(File confFile) throws InvalidSessionFactoryException, InvalidEngineException {
        // Call parent constructor
        super();
        
        // Assign the session Factory to the parent object
        setSessionFactory(LocalDAO.buildSessionFactory(confFile));
    }
    
    /**
     * Creates a new LocalPelpBussinesImpl object from given Hibernate configuration url
     * @param url URL with configuration for Hibernate Database Connection
     */
    public LocalUOCPelpBussinesImpl(URL url) throws InvalidSessionFactoryException, InvalidEngineException {
        // Call parent constructor
        super();
        
        // Assign the session Factory to the parent object
        setSessionFactory(LocalDAO.buildSessionFactory(url));
    }
    
    /**
     * Remove all the data in the local database
     */
    public void clearDatabaseData() {        
        _adminDAO.clearTableData();
        _logDAO.clearTableData();
        _statsDAO.clearTableData();
        _activityDAO.clearTableData();
        _deliverResultsDAO.clearTableData();
        _deliverDAO.clearTableData();
        _semesterDAO.clearTableData();
    }
    
    @Override
    public final void setSessionFactory(SessionFactory sessionFactory) throws InvalidSessionFactoryException { 
        // Check the session factory object
        if(sessionFactory==null) {
            throw new InvalidSessionFactoryException("Null session factory is detected");
        }
        // Store new object object
        _sessionFactory=sessionFactory;
        
        // Create the DAO objects
        _activityDAO=new LocalActivityDAO(sessionFactory);
        _deliverDAO=new LocalDeliverDAO(sessionFactory);
        _deliverResultsDAO=new LocalDeliverResultsDAO(sessionFactory);        
        _adminDAO=new LocalAdministrationDAO(sessionFactory);
        _semesterDAO=new LocalSemesterDAO(sessionFactory);
        _logDAO=new LocalLoggingDAO(sessionFactory);
        _statsDAO=new LocalStatisticsDAO(sessionFactory);
            
        // Update the engine
        if(_engine!=null) {            
            // Create the managers
            _engine.setDeliverManager(new DAODeliverManager(_deliverDAO,_deliverResultsDAO));
            _engine.setActivityManager(new DAOActivityManager(_activityDAO));
            _engine.setAdministrationManager(new DAOAdministrationManager(_adminDAO,_semesterDAO));
            _engine.setInformationManager(new DAOInformationManager(_logDAO,_statsDAO));
        }
    }
    
    public void initializeTestEngine() throws InvalidEngineException {
        // Call parent engine creation method
        initializeEngine();
                       
        // Update managers with local managers
        _engine.setDeliverManager(new DAODeliverManager(_deliverDAO,_deliverResultsDAO));
        _engine.setActivityManager(new DAOActivityManager(_activityDAO));
        _engine.setAdministrationManager(new DAOAdministrationManager(_adminDAO,_semesterDAO));
        _engine.setInformationManager(new DAOInformationManager(_logDAO,_statsDAO));

    }
    
    public LocalActivityDAO getActivityDAO() {
        return _activityDAO;
    }
    public LocalDeliverDAO getDeliverDAO() {
        return _deliverDAO;
    }
    public LocalDeliverResultsDAO getDeliverResultsDAO() {
        return _deliverResultsDAO;
    }
    public LocalAdministrationDAO getAdminDAO() {
        return _adminDAO;
    }
    public LocalSemesterDAO getSemesterDAO() {
        return _semesterDAO;
    }
    public LocalLoggingDAO getLoggingDAO() {
        return _logDAO;
    }
    public LocalStatisticsDAO getStatsDAO() {
        return _statsDAO;
    }
}
