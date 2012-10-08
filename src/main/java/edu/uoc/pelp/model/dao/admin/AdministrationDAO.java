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
package edu.uoc.pelp.model.dao.admin;

import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.model.vo.admin.PelpAdmins;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Implements the DAO object for the Activities. 
 * @author Xavier Baró
 */
public class AdministrationDAO implements IAdministrationDAO {
    
    protected SessionFactory _sessionFactory = null;
    
    /**
     * Dafault constructor for compatibility or Spring session factory assignment
     */
    public AdministrationDAO() {
        super();
    }
    
    /**
     * Dafault constructor with session factory assignment
     * @param sessionFactory Session factory of DAO access to the database
     */
    public AdministrationDAO(SessionFactory sessionFactory) {
        super();
        _sessionFactory=sessionFactory;
    }
    
    /**
     * Gets the session factory object
     * @return Session factory
     */
    protected SessionFactory getSessionFactory() {
        return _sessionFactory;
    }
    
    /**
     * Assign a session factory object
     * @param sessionFactory Session factory object
     */
    protected void setSessionFactory(SessionFactory sessionFactory) {
        _sessionFactory=sessionFactory;
    }
    
    /**
     * Get the connection session
     * @return Connection session
     */
    protected Session getSession() {
        if(_sessionFactory==null) {
            return null;
        }
        return _sessionFactory.getCurrentSession();
    }

    @Override
    public boolean addAdmin(Person person, boolean active, boolean grant) {
        // Check the person parameter
        if(person==null) {
            return false;
        }
        
        Transaction transaction=null;
        PelpAdmins adminData;
        
         try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Check that does not exits the row for this administrator
            if(getAdminData(person)!=null) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }
            
            // Create the low level representation
            adminData=new PelpAdmins();
            adminData.setUserName(person._username);
            adminData.setActive(active);
            adminData.setGrantAllowed(grant);

            // Add the new administrator
            getSession().save(adminData);
            
            // Commit the results
            transaction.commit();
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        return getAdminData(person).equals(adminData);
    }
    
    @Override
    public PelpAdmins getAdminData(Person person) {
        // Check the input object
        if(person==null) {
            return null;
        }
        
        // Get the admin register
        Query query=getSession().getNamedQuery("PelpAdmins.findByUser");
        query.setParameter("userName", person._username);
                
        List<PelpAdmins> newObj=query.list();
        if(newObj==null || newObj.size()!=1) {
            return null;
        }
        
        // Return the data
        return newObj.get(0);
    }
    
    @Override
    public boolean updateAdmin(Person person, boolean active, boolean grant) {
        // Check the input object
        if(person==null) {
            return false;
        }
        
        Transaction transaction=null;        
        PelpAdmins newObj;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
        
            // Check that this object exists
            if(getAdminData(person)==null) {
                transaction.rollback();
                return false;
            }
            
            // Get a low-level representation
            newObj=new PelpAdmins();
            newObj.setUserName(person._username);
            newObj.setActive(active);
            newObj.setGrantAllowed(grant);

            // Update the object
            getSession().merge(newObj);
                        
            // Commit the results
            transaction.commit();
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        return getAdminData(person).equals(newObj);
    }

    @Override
    public boolean delAdmin(Person person) {
        // Check the person parameter
        if(person==null) {
            return false;
        }
        
        Transaction transaction=null;
        PelpAdmins adminData;
        
         try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Check that does not exits the row for this administrator
            adminData=getAdminData(person);
            if(adminData==null) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }
            
            // Add the new administrator
            getSession().delete(adminData);
            
            // Commit the results
            transaction.commit();
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        return getAdminData(person)==null;
    }
}

