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

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.campus.IClassroomID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverFileID;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.model.dao.DeliverDAO;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Implements the DAO object for the Deliver class. It uses a local database connection
 * @author Xavier Baró
 */
public class LocalDeliverDAO extends DeliverDAO {
    
    private LocalDAO _localDAO=null;
    
    /**
     * Creates a new LocalDeliverDAO object from given Hibernate SessionFactory
     * @param sessionFactory SessionFactory object
     */
    public LocalDeliverDAO(SessionFactory sessionFactory) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(sessionFactory) {
            @Override
            public void clearTableData() {
                deleteTableData("DeliverFile");
                deleteTableData("Deliver");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalDeliverDAO object from given Hibernate configuration resource
     * @param resource Resource with configuration for Hibernate Database Connection
     */
    public LocalDeliverDAO(String resource) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(resource) {
            @Override
            public void clearTableData() {
                deleteTableData("DeliverFile");
                deleteTableData("Deliver");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalDeliverDAO object from given Hibernate configuration file
     * @param confFile File with configuration for Hibernate Database Connection
     */
    public LocalDeliverDAO(File confFile) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(confFile) {
            @Override
            public void clearTableData() {
                deleteTableData("DeliverFile");
                deleteTableData("Deliver");
            }
        };
        // Assign the session Factory to the parent object
        _sessionFactory=_localDAO.getSessionFactory();
    }
    
    /**
     * Creates a new LocalDeliverDAO object from given Hibernate configuration url
     * @param url URL with configuration for Hibernate Database Connection
     */
    public LocalDeliverDAO(URL url) {
        // Call parent constructor
        super();
        
        // Create object to access local database
        _localDAO=new LocalDAO(url) {
            @Override
            public void clearTableData() {
                deleteTableData("DeliverFile");
                deleteTableData("Deliver");
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
    public Deliver find(DeliverID object) {
        Deliver retVal;
        
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
    public List<Deliver> findAll(IUserID user) {
        List<Deliver> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAll(user);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }
    
    @Override
    public List<Deliver> findAll() {
        List<Deliver> retVal;
        
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
    public DeliverID add(IUserID user, ActivityID activity, Deliver object) {
        DeliverID retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                       
        // Perform action
        retVal=super.add(user,activity,object);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean update(Deliver object) {
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
    public List<Deliver> findAll(ISubjectID subject,IUserID user) {
        List<Deliver> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAll(subject,user);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }
    

    @Override
    public List<Deliver> findAll(ActivityID activity) {
        List<Deliver> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAll(activity);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }
    
    @Override
    public List<Deliver> findAllClassroom(IClassroomID classroom, ActivityID activity) {
        List<Deliver> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAllClassroom(classroom,activity);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }
    
    @Override
    public List<Deliver> findLastClassroom(IClassroomID classroom, ActivityID activity) {
        List<Deliver> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findLastClassroom(classroom,activity);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<DeliverID> findAllKey() {
        List<DeliverID> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAllKey();
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<DeliverID> findAllKey(IUserID user) {
        List<DeliverID> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAllKey(user);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<DeliverID> findAllKey(ISubjectID subject,IUserID user) {
        List<DeliverID> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAllKey(subject,user);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<DeliverID> findAllKey(ActivityID activity,IUserID user) {
        List<DeliverID> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAllKey(activity,user);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<DeliverID> findAllKey(ActivityID activity) {
        List<DeliverID> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAllKey(activity);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }
    
    @Override
    public DeliverFileID add(DeliverID deliver, edu.uoc.pelp.engine.deliver.DeliverFile object) {
        DeliverFileID retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
                       
        // Perform action
        retVal=super.add(deliver,object);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public boolean delete(DeliverFileID id) {
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
    public boolean update(edu.uoc.pelp.engine.deliver.DeliverFile object) {
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
    public List<edu.uoc.pelp.engine.deliver.DeliverFile> findAllFiles() {
        List<edu.uoc.pelp.engine.deliver.DeliverFile> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAllFiles();
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public List<edu.uoc.pelp.engine.deliver.DeliverFile> findAll(DeliverID deliver) {
        List<edu.uoc.pelp.engine.deliver.DeliverFile> retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.findAll(deliver);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }

    @Override
    public DeliverFileID getLastID(DeliverID deliver) {
        DeliverFileID retVal;
        
        // Check current session status
        boolean createdNewSession=!_localDAO.hasOpenSession();
        
        // Perform action
        retVal=super.getLastID(deliver);
        
        // Close new created session
        if(createdNewSession) {
            _localDAO.closeSession();
        }
        
        //Return parent method returned value
        return retVal;
    }
}
