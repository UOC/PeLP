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
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Implements the DAO object for the Semester class
 * @author Xavier Baró
 */
public class LocalSemesterDAO extends SemesterDAO {
    
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
        Query q = getSession().createQuery("delete from Semester");      
        q.executeUpdate();
        
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
    }
    
    /**
     * Default constructor. Creates an ObjectFactory using local hibernate configuration file
     */
    public LocalSemesterDAO() {
        setSessionFactory(buildSessionFactory());        
    }

    @Override
    public boolean delete(ITimePeriod object) {
        boolean retVal;
        boolean createdNewSession=false;
        
        // Check current session status
        if(_session==null) {
            createdNewSession=true;
        }
                
        // Perform action
        retVal=super.delete(object);
        
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public ITimePeriod find(ITimePeriod object) {
        ITimePeriod retVal;
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
    public List<ITimePeriod> findActive() {
        List<ITimePeriod> retVal;
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
    public List<ITimePeriod> findAll() {
        List<ITimePeriod> retVal;
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
    public boolean save(ITimePeriod object) {
        boolean retVal;
        boolean createdNewSession=false;
        
        // Check current session status
        if(_session==null) {
            createdNewSession=true;
        }
                       
        // Perform action
        retVal=super.save(object);
        
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean update(ITimePeriod object) {
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
