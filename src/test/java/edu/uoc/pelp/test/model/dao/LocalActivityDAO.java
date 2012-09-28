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
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Implements the DAO object for the Activity class. It uses a local database connection
 * @author Xavier Baró
 */
public class LocalActivityDAO extends ActivityDAO {
    
    private static Session _session=null;
        
    private static SessionFactory buildSessionFactory() {
        try {
            // Assign the new session Factory using the local configuration
            return new AnnotationConfiguration().configure("hibernate_test.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    protected void closeSession() {
        if(_session!=null) {
            _session.flush();
            _session.close();
            _session=null;
        }
    }
    
    @Override
    protected Session getSession() {     
        // Close open sessions
        if(_session==null) {
            _session=_sessionFactory.openSession();
        }
        return _session;
    }
        
    /**
     * Remove all the data in this table of the database
     */
    public void clearTableData() {
        boolean createdNewSession=false;
        
        // Check current session status
        if(_session==null) {
            createdNewSession=true;
        }
        
        // Perform action
        Query q = getSession().createQuery("delete from ActivityDesc");      
        q.executeUpdate();
        q = getSession().createQuery("delete from Activity");      
        q.executeUpdate();
        
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
    }
    
    /**
     * Default constructor. Creates an ObjectFactory using local hibernate configuration file
     */
    public LocalActivityDAO() {
        setSessionFactory(buildSessionFactory());        
    }

    @Override
    public boolean delete(ActivityID id) {
        boolean retVal;
        boolean createdNewSession=false;
        
        // Check current session status
        if(_session==null) {
            createdNewSession=true;
        }
                
        // Perform action
        retVal=super.delete(id);
        
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public Activity find(ActivityID object) {
        Activity retVal;
        boolean createdNewSession=false;
        
        // Check current session status
        if(_session==null) {
            createdNewSession=true;
        }
               
        // Perform action
        retVal=super.find(object);
        
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<Activity> findActive() {
        List<Activity> retVal;
        boolean createdNewSession=false;
        
        // Check current session status
        if(_session==null) {
            createdNewSession=true;
        }
        
        // Perform action
        retVal=super.findActive();
        
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<Activity> findAll() {
        List<Activity> retVal;
        boolean createdNewSession=false;
        
        // Check current session status
        if(_session==null) {
            createdNewSession=true;
        }
        
        // Perform action
        retVal=super.findAll();
        
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public ActivityID add(ISubjectID subjectID, Activity object) {
        ActivityID retVal;
        boolean createdNewSession=false;
        
        // Check current session status
        if(_session==null) {
            createdNewSession=true;
        }
                       
        // Perform action
        retVal=super.add(subjectID,object);
        
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean update(Activity object) {
        boolean retVal;
        boolean createdNewSession=false;
        
        // Check current session status
        if(_session==null) {
            createdNewSession=true;
        }
        
        // Close open sessions
        if(_session!=null) {
            _session.close();
        }
                
        // Perform action
        retVal=super.update(object);
        
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }
}
