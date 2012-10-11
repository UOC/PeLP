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
package edu.uoc.pelp.test.model.dao;

import edu.uoc.pelp.engine.deliver.ActivityTestResult;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.model.dao.DeliverResultsDAO;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Implements the DAO object for the Deliver class. It uses a local database connection
 * @author Xavier Baró
 */
public class LocalDeliverResultsDAO extends DeliverResultsDAO {
    
    private LocalDAO _localDAO=null;
    
    /**
     * Creates a new LocalDeliverResultsDAO object from given Hibernate SessionFactory
     * @param sessionFactory SessionFactory object
     */
    public LocalDeliverResultsDAO(SessionFactory sessionFactory) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(sessionFactory) {
            @Override
            public void clearTableData() {
                deleteTableData("DeliverTestResult");
                deleteTableData("DeliverResult");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalDeliverResultsDAO object from given Hibernate configuration resource
     * @param resource Resource with configuration for Hibernate Database Connection
     */
    public LocalDeliverResultsDAO(String resource) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(resource) {
            @Override
            public void clearTableData() {
                deleteTableData("DeliverTestResult");
                deleteTableData("DeliverResult");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalDeliverResultsDAO object from given Hibernate configuration file
     * @param confFile File with configuration for Hibernate Database Connection
     */
    public LocalDeliverResultsDAO(File confFile) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(confFile) {
            @Override
            public void clearTableData() {
                deleteTableData("DeliverTestResult");
                deleteTableData("DeliverResult");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalDeliverResultsDAO object from given Hibernate configuration url
     * @param url URL with configuration for Hibernate Database Connection
     */
    public LocalDeliverResultsDAO(URL url) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(url) {
            @Override
            public void clearTableData() {
                deleteTableData("DeliverTestResult");
                deleteTableData("DeliverResult");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    @Override
    protected Session getSession() {     
        if(_localDAO==null) {
            return null;
        }
        return _localDAO.getSession();
    }
        
    /**
     * Remove all the data in this table of the database
     */
    public void clearTableData() {
        _localDAO.clearTableData();
    }

    @Override
    public boolean add(DeliverResults results) {
        boolean retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                       
        // Perform action
        retVal=super.add(results);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean delete(DeliverID id) {
        boolean retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                
        // Perform action
        retVal=super.delete(id);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean update(DeliverResults object) {
        boolean retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                
        // Perform action
        retVal=super.update(object);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public DeliverResults find(DeliverID id) {
        DeliverResults retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
               
        // Perform action
        retVal=super.find(id);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean add(DeliverID deliverID, ActivityTestResult testResult) {
        boolean retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                       
        // Perform action
        retVal=super.add(deliverID,testResult);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean delete(DeliverID deliverID, ActivityTestResult testResult) {
        boolean retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                
        // Perform action
        retVal=super.delete(deliverID,testResult);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean update(DeliverID deliverID, ActivityTestResult testResult) {
        boolean retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                
        // Perform action
        retVal=super.update(deliverID,testResult);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<ActivityTestResult> findTestResults(DeliverID deliverID) {
        List<ActivityTestResult> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
               
        // Perform action
        retVal=super.findTestResults(deliverID);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    } 
}
