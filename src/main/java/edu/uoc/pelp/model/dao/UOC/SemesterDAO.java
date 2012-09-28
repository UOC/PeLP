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
package edu.uoc.pelp.model.dao.UOC;

import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.model.dao.ITimePeriodDAO;
import edu.uoc.pelp.model.vo.ObjectFactory;
import edu.uoc.pelp.model.vo.UOC.Semester;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Implements the DAO object for the SemesterDO class
 * @author Xavier Baró
 */
public class SemesterDAO implements ITimePeriodDAO {
    
    protected SessionFactory _sessionFactory = null;
    
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
    
    /**
     * Get a Semester object from a generic time period
     * @param object Generic time period
     * @return Semester object or null if no conversion is possible
     */
    protected edu.uoc.pelp.engine.campus.UOC.Semester getSemester(ITimePeriod object) {
        edu.uoc.pelp.engine.campus.UOC.Semester semesterObj=null;
        if(object instanceof edu.uoc.pelp.engine.campus.UOC.Semester) {
            semesterObj=(edu.uoc.pelp.engine.campus.UOC.Semester) object;
        }
        return semesterObj;
    }
    
     /**
     * Get a list of generic Semester objects from a list of low level objects
     * @param list List of low level objects
     * @return List of high level objects
     */
    protected List<ITimePeriod> getSemesterList(List<edu.uoc.pelp.model.vo.UOC.Semester> list) {
        List<ITimePeriod> retList=new ArrayList<ITimePeriod>();
        for(edu.uoc.pelp.model.vo.UOC.Semester register:list) {
            retList.add(ObjectFactory.getSemesterObj(register));
        }
        return retList;
    }
    
    /**
     * Get a low level representation of a generic time period
     * @param object Generic time period
     * @return Semester register or null if no conversion is possible 
     */
    protected Semester getRegister(ITimePeriod object) {
        edu.uoc.pelp.engine.campus.UOC.Semester semesterObj=null;
        if(object instanceof edu.uoc.pelp.engine.campus.UOC.Semester) {
            semesterObj=(edu.uoc.pelp.engine.campus.UOC.Semester) object;
        }
        return ObjectFactory.getSemesterReg(semesterObj);
    }
   
    @Override
    public boolean save(ITimePeriod object) {
        Transaction transaction=null;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }

            // Check that this semeste does not exist
            if(find(object)!=null) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }

            // Get the low level register
            Semester semester=getRegister(object);
            if(semester==null) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }

            // Add the new register
            getSession().saveOrUpdate(semester);

            // Commit the results
            //getSession().getTransaction().commit();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        // Check that the object is correctly added
        return (find(object)!=null);
    }

    @Override
    public boolean delete(ITimePeriod object) {
        Transaction transaction=null;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
        
            // Check that this semeste does not exist
            if(find(object)!=null) {
                return false;
            }

            // Get the low level register
            Semester semester=getRegister(object);
            if(semester==null) {
                return false;
            }

            // Add the new register
            getSession().delete(semester);

            // Commit the results
            //getSession().getTransaction().commit();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        // Check that the object is correctly deleted
        return (find(object)==null);
    }

    @Override
    public boolean update(ITimePeriod object) {
        Transaction transaction=null;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
        
            // Check that this semeste exists
            if(find(object)==null) {
                return false;
            }

            // Get the low level register
            Semester semester=getRegister(object);
            if(semester==null) {
                return false;
            }

            // Add the new register
            getSession().merge(semester);

            // Commit the results
            //getSession().getTransaction().commit();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        // Check that the object is correctly updated
        return (object.equals(find(object)));
    }

    @Override
    public List<ITimePeriod> findAll() {
        // Search for the semester key
        Query query=getSession().getNamedQuery("Semester.findAll");
        List<Semester> list=query.list();
        
        // Return the results
        return getSemesterList(list);  
    }

    @Override
    public List<ITimePeriod> findActive() {
        // Search for the semester key
        Query query=getSession().getNamedQuery("Semester.findActive");
        List<Semester> list=query.list();
        
        // Return the results
        return getSemesterList(list);        
    }

    @Override
    public ITimePeriod find(ITimePeriod object) {
        
        // Get the semester register
        Semester semester=getRegister(object);
        if(semester==null) {
            return null;
        }
        
        // Search for the semester key
        Query query=getSession().getNamedQuery("Semester.findBySemester");
        query.setParameter("semester", semester.getSemester());
        List<Semester> list=query.list();
        
        // Return the semester
        if(list.size()<=0) {
            return null;
        }
        
        return (ITimePeriod)ObjectFactory.getSemesterObj(list.get(0));        
    }
}
