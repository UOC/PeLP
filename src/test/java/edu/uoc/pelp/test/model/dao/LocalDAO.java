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

import java.io.File;
import java.net.URL;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Abstract class for local DAO objects, providing basic methods
 * @author Xavier Baró
 */
public abstract class LocalDAO {
    
    private static SessionFactory _sessionFactory=null;
    private Session _session=null;
    
    /**
     * Creates a new Local DAO access object from given already created SessionFactory
     * @param sessionFactory SessionFactoru object
     */
    public LocalDAO(SessionFactory sessionFactory) {
        _sessionFactory=sessionFactory;
    }
    
    /**
     * Creates a new Local DAO access object from given Hibernate configuration resource
     * @param resource Resource with configuration for Hibernate Database Connection
     */
    public LocalDAO(String resource) {
        _sessionFactory=buildSessionFactory(resource);
    }
    
    /**
     * Creates a new Local DAO access object from given Hibernate configuration file
     * @param confFile File with configuration for Hibernate Database Connection
     */
    public LocalDAO(File confFile) {
        _sessionFactory=buildSessionFactory(confFile);
    }
    
    /**
     * Creates a new Local DAO access object from given Hibernate configuration url
     * @param url URL with configuration for Hibernate Database Connection
     */
    public LocalDAO(URL url) {
        _sessionFactory=buildSessionFactory(url);
    }
    
    /**
     * Create an Hibernate Session Factory from given configuration resource
     * @param resource Resource with configuration for Hibernate Database Connection
     * @return SessionFactory object
     */
    public static SessionFactory buildSessionFactory(String resource) {
        try {
            // Assign the new session Factory using the given configuration
            return new AnnotationConfiguration().configure(resource).buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    /**
     * Create an Hibernate Session Factory from given configuration file
     * @param configFile File with configuration for Hibernate Database Connection
     * @return SessionFactory object
     */
    public static SessionFactory buildSessionFactory(File configFile) {
        try {
            // Assign the new session Factory using the given configuration
            return new AnnotationConfiguration().configure(configFile).buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    /**
     * Create an Hibernate Session Factory from given configuration url
     * @param url URL with configuration for Hibernate Database Connection
     * @return SessionFactory object
     */
    public static SessionFactory buildSessionFactory(URL url) {
        try {
            // Assign the new session Factory using the given configuration
            return new AnnotationConfiguration().configure(url).buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    /**
     * Indicates if there is an open session or not
     * @return True if there is an open session or not othersise
     */
    public boolean hasOpenSession() {
        return _session!=null;
    }
    
    /**
     * Close the current session
     */
    public void closeSession() {
        if(_session!=null) {
            _session.flush();
            _session.close();
            _session=null;
        }
    }
    
    /**
     * Retrieves current open session. If no open session exists, it creates a new one.
     * @return Current session
     */
    public Session getSession() {     
        // Close open sessions
        if(_session==null) {
            _session=_sessionFactory.openSession();
        }
        return _session;
    }
    
    /**
     * Remove all the data in the given table
     * @param tableName 
     */
    public void deleteTableData(String tableName) {
               
        // Check current session status
        boolean createdNewSession=!hasOpenSession();
        
        // Perform action
        Query q = getSession().createQuery("delete from " + tableName.trim());
        q.executeUpdate();
               
        // Close new created session
        if(createdNewSession) {
            closeSession();
        }
    }
    
    /**
     * Get the session factory object
     * @return Session Factory Object
     */
    public SessionFactory getSessionFactory() {
        return _sessionFactory;
    }
        
    /**
     * Remove all the data in this table of the database
     */
    public abstract void clearTableData();
}
