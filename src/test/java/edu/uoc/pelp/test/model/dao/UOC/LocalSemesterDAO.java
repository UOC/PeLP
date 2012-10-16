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
package edu.uoc.pelp.test.model.dao.UOC;

import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.model.dao.UOC.SemesterDAO;
import edu.uoc.pelp.test.model.dao.LocalDAO;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Implements the DAO object for the Semester class
 * @author Xavier Baró
 */
public class LocalSemesterDAO extends SemesterDAO {
    
    private LocalDAO _localDAO=null;
    
    /**
     * Creates a new LocalSemesterDAO object from given Hibernate SessionFactory
     * @param sessionFactory SessionFactory object
     */
    public LocalSemesterDAO(SessionFactory sessionFactory) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(sessionFactory) {
            @Override
            public void clearTableData() {
                deleteTableData("Semester");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalSemesterDAO object from given Hibernate configuration resource
     * @param resource Resource with configuration for Hibernate Database Connection
     */
    public LocalSemesterDAO(String resource) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(resource) {
            @Override
            public void clearTableData() {
                deleteTableData("Semester");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalSemesterDAO object from given Hibernate configuration file
     * @param confFile File with configuration for Hibernate Database Connection
     */
    public LocalSemesterDAO(File confFile) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(confFile) {
            @Override
            public void clearTableData() {
                deleteTableData("Semester");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalSemesterDAO object from given Hibernate configuration url
     * @param url URL with configuration for Hibernate Database Connection
     */
    public LocalSemesterDAO(URL url) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(url) {
            @Override
            public void clearTableData() {
                deleteTableData("Semester");
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
    public boolean delete(ITimePeriod object) {
        boolean retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                
        // Perform action
        retVal=super.delete(object);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public ITimePeriod find(ITimePeriod object) {
        ITimePeriod retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
               
        // Perform action
        retVal=super.find(object);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<ITimePeriod> findActive() {
        List<ITimePeriod> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findActive();
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<ITimePeriod> findAll() {
        List<ITimePeriod> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAll();
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean save(ITimePeriod object) {
        boolean retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                       
        // Perform action
        retVal=super.save(object);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean update(ITimePeriod object) {
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
}
