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

import edu.uoc.pelp.engine.activity.Activity;
import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.model.dao.ActivityDAO;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Implements the DAO object for the Activity class. It uses a local database connection
 * @author Xavier Baró
 */
public class LocalActivityDAO extends ActivityDAO {
    
    private LocalDAO _localDAO=null;
    
    /**
     * Creates a new LocalActivityDAO object from given Hibernate SessionFactory
     * @param sessionFactory SessionFactory object
     */
    public LocalActivityDAO(SessionFactory sessionFactory) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(sessionFactory) {
            @Override
            public void clearTableData() {
                deleteTableData("ActivityDesc");
                deleteTableData("ActivityTest");
                deleteTableData("Activity");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalActivityDAO object from given Hibernate configuration resource
     * @param resource Resource with configuration for Hibernate Database Connection
     */
    public LocalActivityDAO(String resource) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(resource) {
            @Override
            public void clearTableData() {
                deleteTableData("ActivityDesc");
                deleteTableData("ActivityTest");
                deleteTableData("Activity");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalActivityDAO object from given Hibernate configuration file
     * @param confFile File with configuration for Hibernate Database Connection
     */
    public LocalActivityDAO(File confFile) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(confFile) {
            @Override
            public void clearTableData() {
                deleteTableData("ActivityDesc");
                deleteTableData("ActivityTest");
                deleteTableData("Activity");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalActivityDAO object from given Hibernate configuration url
     * @param url URL with configuration for Hibernate Database Connection
     */
    public LocalActivityDAO(URL url) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(url) {
            @Override
            public void clearTableData() {
                deleteTableData("ActivityDesc");
                deleteTableData("ActivityTest");
                deleteTableData("Activity");
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
    public boolean delete(ActivityID id) {
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
    public Activity find(ActivityID object) {
        Activity retVal;
                
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
    public List<Activity> findActive() {
        List<Activity> retVal;
                
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
    public List<Activity> findAll() {
        List<Activity> retVal;
        
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
    public ActivityID add(ISubjectID subjectID, Activity object) {
        ActivityID retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                       
        // Perform action
        retVal=super.add(subjectID,object);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean update(Activity object) {
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
