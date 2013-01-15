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
package edu.uoc.pelp.model.dao;

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.campus.IClassroomID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.IUserID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverFileID;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.model.vo.*;
import edu.uoc.pelp.model.vo.UOC.ClassroomPK;
import edu.uoc.pelp.model.vo.UOC.SubjectPK;
import edu.uoc.pelp.model.vo.UOC.UserPK;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.hibernate.*;

/**
 * Implements the DAO object for the Delivers and their files. 
 * @author Xavier Baró
 */
public class DeliverDAO implements IDeliverDAO {
    
    protected SessionFactory _sessionFactory = null;
    
    /**
     * Dafault constructor for compatibility or Spring session factory assignment
     */
    public DeliverDAO() {
        super();
    }
    
    /**
     * Dafault constructor with session factory assignment
     * @param sessionFactory Session factory of DAO access to the database
     */
    public DeliverDAO(SessionFactory sessionFactory) {
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
    
    /**
     * Get a list of Deliver objects from a list of low level objects, adding the correspondant files
     * @param list List of low level objects
     * @return List of high level objects
     */
    protected List<Deliver> getDeliverList(List<edu.uoc.pelp.model.vo.Deliver> list) {
        List<Deliver> retList=new ArrayList<Deliver>();
        for(edu.uoc.pelp.model.vo.Deliver register:list) {  
            // Get the list of files
            List<edu.uoc.pelp.model.vo.DeliverFile> filesList=findDeliverFiles(register.getDeliverPK());
            edu.uoc.pelp.model.vo.DeliverFile[] filesArray=null;
            if(filesList!=null) {
                filesArray=new edu.uoc.pelp.model.vo.DeliverFile[filesList.size()];
                filesList.toArray(filesArray);
            }
            
            // Add the final register
            retList.add(ObjectFactory.getDeliverObj(register, filesArray));
        }
        return retList;
    }
    
    /**
     * Delete all the files of a certain deliver
     * @param key Deliver primary key
     * @return True if the operation ends successfully or Fals in case of error.
     */
    private boolean deleteDeliverFiles(DeliverPK key) {
        // Check the input object
        if(key==null) {
            return false;
        }
                
        // Get the list of files
        List<DeliverFile> deliverFiles=findDeliverFiles(key);
        
        // Remove all files
        for(DeliverFile file:deliverFiles) {
            getSession().delete(file);
        }
        
        return true;
    }
    
    /**
     * Get a list of Deliver objects from a list of low level object identifiers
     * @param list List of low level object keys
     * @return List of high level object identifiers
     */
    protected List<DeliverID> getDeliverIDList(List<edu.uoc.pelp.model.vo.Deliver> list) {
        List<DeliverID> retList=new ArrayList<DeliverID>();
        for(edu.uoc.pelp.model.vo.Deliver register:list) {  
            // Add the final register
            retList.add(ObjectFactory.getDeliverID(register.getDeliverPK()));
        }
        return retList;
    }
    
    /**
     * Get the list of files for a certain deliver.
     * @param key Deliver primary key
     * @return Low level file objects
     */
    private List<edu.uoc.pelp.model.vo.DeliverFile> findDeliverFiles(DeliverPK key) {
        // Check the input object
        if(key==null) {
            return null;
        }
               
        getSession().beginTransaction();
        // Get the descriptions
        Query q=getSession().getNamedQuery("DeliverFile.findByDeliverID");
        q.setParameter("semester", key.getSemester());
        q.setParameter("subject", key.getSubject());
        q.setParameter("activityIndex", key.getActivityIndex());
        q.setParameter("userID",key.getUserID());
        q.setParameter("deliverIndex",key.getDeliverIndex());
        
        List<edu.uoc.pelp.model.vo.DeliverFile> list = null;
        list = q.list();
        getSession().close();
        // Get the list of descriptions
        return list;
    }
    
    /**
     * Add the list of files for this deliver
     * @param files Array of files
     * @return True if the files are correctly added or false otherwise
     */
    private boolean addDeliverFiles(DeliverID deliverID,edu.uoc.pelp.engine.deliver.DeliverFile[] files) {
        
        if(deliverID==null) {
            return false;
        }
        
        try {
            // Add the files
            for(edu.uoc.pelp.engine.deliver.DeliverFile file:files) {
                DeliverFileID newID=addFile(deliverID,file);
                if(newID==null) {
                    return false;
                }
                file.setID(newID);
            }
            
        } catch (RuntimeException e) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Adds a file to a deliver. This method assumes that is called inside an open transaction, and is
     * used as auxiliar method.
     * @param deliver Deliver identifier
     * @param object File to be added to the deliver
     * @return Identifier for the new added file or null if an error occurs.
     */
    private DeliverFileID addFile(DeliverID deliver, edu.uoc.pelp.engine.deliver.DeliverFile object) {
       
        // Get the id of the last deliver
        DeliverFileID lastID=getLastID(deliver);

        // Create the new id
        DeliverFileID newID;
        if(lastID!=null) {
            newID=new DeliverFileID(lastID);
            newID.index++;
        } else {
            newID=new DeliverFileID(deliver,1);
        }

        // Get the object
        edu.uoc.pelp.model.vo.DeliverFile newObj=ObjectFactory.getDeliverFileReg(object);

        // Add a new object from given object, to break the reference
        DeliverFilePK key=ObjectFactory.getDeliverFilePK(newID);
        if(key==null) {
            return null;
        }

        // Add the new object
        newObj.setDeliverFilePK(key); 

        // Add current date
        getSession().saveOrUpdate(newObj);
        
        return newID;
    }

    @Override
    public DeliverID add(IUserID user, ActivityID activity, Deliver object) {
        
        // Check the input object
        if(object==null || user==null || activity==null) {
            return null;
        } 
        
        Transaction transaction=null;
        DeliverID newID;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return null;
            }
            
            // Get the id of the last deliver
            DeliverID lastID=getLastID(activity,user);
            
            // Create the new id
            if(lastID!=null) {
                newID=new DeliverID(lastID);
                newID.index++;
            } else {
                newID=new DeliverID(user,activity,1);
            }
        
            // Get the object
            edu.uoc.pelp.model.vo.Deliver newObj=ObjectFactory.getDeliverReg(object);
            
            // Add a new object from given object, to break the reference
            DeliverPK key=ObjectFactory.getDeliverPK(newID);
            if(key==null) {
                transaction.rollback();
                return null;
            }
        
            // Add the new object
            newObj.setDeliverPK(key); 
            
            // Add current date
            if(newObj.getSubmissionDate()==null) {
                newObj.setSubmissionDate(new Date());
            }
            transaction=getSession().beginTransaction();
            getSession().saveOrUpdate(newObj);
            
            
            // Add deliver files
            if(!addDeliverFiles(newID,object.getFiles())) {
                transaction.rollback();
                return null;
            }
            
            // Commit the results
            transaction.commit();
            
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
        
        return newID;
    }

    @Override
    public boolean delete(DeliverID id) {
        
        // Check the input object
        if(id==null) {
            return false;
        }
        
        Transaction transaction=null;        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
        
            // Check that this object exists
            Deliver deliver=find(id);
            if(deliver==null) {
                transaction.rollback();
                return false;
            }
            
            // Get the primary key
            DeliverPK key=ObjectFactory.getDeliverPK(id);
            
            // Remove the files
            if(!deleteDeliverFiles(key)) {
                transaction.rollback();
                return false;
            }
                                
            // Remove the object
            getSession().delete(ObjectFactory.getDeliverReg(deliver));
            if(find(id)!=null) {
                transaction.rollback();
                return false;
            }
            
            // Commit the results
            transaction.commit();
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        return true;
    }

    @Override
    public boolean update(Deliver object) {
        // Check the input object
        if(object==null || object.getID()==null) {
            return false;
        }
        
        Transaction transaction=null;        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
        
            // Check that this object exists
            if(find(object.getID())==null) {
                transaction.rollback();
                return false;
            }
            
            // Get a low-level representation
            edu.uoc.pelp.model.vo.Deliver newObj=ObjectFactory.getDeliverReg(object);

            // Update the object
            getSession().merge(newObj);
            
            // Get the object files
            edu.uoc.pelp.engine.deliver.DeliverFile[] objFiles=object.getFiles();
            
            // Compact the list of files (files are assumed to be sorted by index)
            long lastIdx=0;
            for(edu.uoc.pelp.engine.deliver.DeliverFile file:objFiles) {
                DeliverFileID id=file.getID();
                if(id!=null) {
                    lastIdx++;
                    if(id.index>lastIdx) {
                        id.index=lastIdx;
                    }
                    file.setID(id);
                }
            }
            
            // Assign new indexes
            for(edu.uoc.pelp.engine.deliver.DeliverFile file:objFiles) {
                DeliverFileID id=file.getID();
                if(id==null) {
                    lastIdx++;
                    id=new DeliverFileID(object.getID(),lastIdx);
                    if(id.index>lastIdx) {
                        id.index=lastIdx;
                    }
                    file.setID(id);
                }
            }
            
            // Update the database (object and database files sorted from 1 to N files
            for(edu.uoc.pelp.engine.deliver.DeliverFile file:objFiles) {                
                // Get the register
                DeliverFile fileReg=ObjectFactory.getDeliverFileReg(file);
                if(fileReg==null) {
                    transaction.rollback();
                    return false;
                }
                
                // Check if it exists
                edu.uoc.pelp.engine.deliver.DeliverFile dbFile=find(file.getID());                                
                if(dbFile!=null) {                    
                    // Update the file
                    getSession().merge(fileReg);
                } else {
                    // Add the file
                    getSession().saveOrUpdate(fileReg);
                }
            }
            
            // Get the files in the database
            List<DeliverFile> dbFiles=findDeliverFiles(newObj.getDeliverPK());
            for(DeliverFile dbFile:dbFiles) {
                if(dbFile.getDeliverFilePK().getFileIndex()>lastIdx) {
                    // Remove the file
                    getSession().delete(dbFile);
                }
            }            
                        
            // Commit the results
            transaction.commit();
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        return true;
    }

    @Override
    public List<Deliver> findAll() {
        // Search for the semester key
        Query query=getSession().getNamedQuery("Deliver.findAll");
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        // Return the results
        return getDeliverList(list); 
    }

    @Override
    public Deliver find(DeliverID object) {
        // Check the input object
        if(object==null) {
            return null;
        }
        getSession().beginTransaction();
        // Get the key
        DeliverPK key=ObjectFactory.getDeliverPK(object);
        
        // Get the deliver register
        Query query=getSession().getNamedQuery("Deliver.findById");
        query.setParameter("semester", key.getSemester());
        query.setParameter("subject", key.getSubject());
        query.setParameter("activityIndex", key.getActivityIndex());
        query.setParameter("userID", key.getUserID());
        query.setParameter("deliverIndex", key.getDeliverIndex());
        
        List<edu.uoc.pelp.model.vo.Deliver> newObj=query.list();
        if(newObj==null || newObj.size()!=1) {
            return null;
        }
        edu.uoc.pelp.model.vo.Deliver deliverReg=newObj.get(0);
        
        // Get the list of files
        List<DeliverFile> fileList=findDeliverFiles(key);
        DeliverFile[] fileArray=null;
        if(fileList!=null) {
            fileArray=new DeliverFile[fileList.size()];
            fileList.toArray(fileArray);
        }

        Deliver objDeliver = ObjectFactory.getDeliverObj(deliverReg,fileArray);
        getSession().close();
        // Add the final register
        return objDeliver;
    }
   
    @Override
    public List<Deliver> findAll(IUserID user) {
        // Get the user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Deliver.findByUserID");
        query.setParameter("userID", userPK.getIdp());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        // Return the results
        return getDeliverList(list); 
    }

    @Override
    public List<Deliver> findAll(ISubjectID subject, IUserID user) {
        
        // Get the subject ID and user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        SubjectPK subjectPK=ObjectFactory.getSubjectPK(subject);
        if(subjectPK==null) {
            return null;
        }
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Deliver.findBySubjectUserID");
        query.setParameter("semester", subjectPK.getSemester());
        query.setParameter("subject", subjectPK.getSubject());
        query.setParameter("userID", userPK.getIdp());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        // Return the results
        return getDeliverList(list); 
    }

    @Override
    public List<Deliver> findAll(ActivityID activity, IUserID user) {
        
        // Get the activity ID and user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Deliver.findByActivityUserID");
        query.setParameter("semester", activityPK.getSemester());
        query.setParameter("subject", activityPK.getSubject());
        query.setParameter("activityIndex", activityPK.getActivityIndex());
        query.setParameter("userID", userPK.getIdp());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        // Return the results
        return getDeliverList(list); 
    }

    @Override
    public DeliverID getLastID(ActivityID activity, IUserID user) {
        // Check parameters
        if(activity==null || user==null) {
            return null;
        }
        
        // Get low level representations
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        UserPK userPK=ObjectFactory.getUserPK(user);
        if(userPK==null) {
            return null;
        }
        getSession().beginTransaction();
        // Search the last identifier
        Query q=getSession().getNamedQuery("Deliver.findLast");
        q.setParameter("semester", activityPK.getSemester());
        q.setParameter("subject", activityPK.getSubject());
        q.setParameter("activityIndex", activityPK.getActivityIndex());
        q.setParameter("userID", userPK.getIdp());
        List<edu.uoc.pelp.model.vo.Deliver> lastDeliver=q.list();
       
        DeliverID lastID=null;
        if(lastDeliver!=null) {
            if(lastDeliver.size()>0) {
                lastID=ObjectFactory.getDeliverID(lastDeliver.get(0).getDeliverPK());
            }
        }
        getSession().close();
        // Return last id
        return lastID;
    }

    @Override
    public List<Deliver> findAll(ActivityID activity) {

        // Get the activity ID 
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Deliver.findByActivity");
        query.setParameter("semester", activityPK.getSemester());
        query.setParameter("subject", activityPK.getSubject());
        query.setParameter("activityIndex", activityPK.getActivityIndex());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        // Return the results
        return getDeliverList(list); 
    }
    
    @Override
    public List<Deliver> findAllClassroom(IClassroomID classroom, ActivityID activity) {
        // Get the activity ID and classroom ID
        ClassroomPK classPK=ObjectFactory.getClassroomPK(classroom);
        if(classPK==null) {
            return null;
        }
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        getSession().beginTransaction();
        // Get the activity register
        Query query=getSession().getNamedQuery("Deliver.findActivityDeliversByClass");
        query.setParameter("semester", activityPK.getSemester());
        query.setParameter("subject", activityPK.getSubject());
        query.setParameter("activityIndex", activityPK.getActivityIndex());
        query.setParameter("classroom", classPK.toString());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        getSession().close();
        // Return the results
        return getDeliverList(list); 
    }

    @Override
    public List<Deliver> findLastClassroom(IClassroomID classroom, ActivityID activity) {
        // Get the activity ID and classroom ID
        ClassroomPK classPK=ObjectFactory.getClassroomPK(classroom);
        if(classPK==null) {
            return null;
        }
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        
        // Get the activity register
        /*Query query=getSession().getNamedQuery("Deliver.findAtivityLastDeliverByClass");
        query.setParameter("semester", activityPK.getSemester());
        query.setParameter("subject", activityPK.getSubject());
        query.setParameter("activityIndex", activityPK.getActivityIndex());
        query.setParameter("classroom", classPK.toString());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();*/
        String SQL="SELECT d1.* " +
                "FROM pelp.deliver d1 " + 
                "LEFT JOIN pelp.deliver d2 ON d1.semester=d2.semester and d1.subject=d2.subject and d1.activityIndex=d2.activityIndex and d1.userID=d2.userID and d1.deliverIndex<d2.deliverIndex " +
                "WHERE d2.deliverIndex is null " + 
                    "and d1.semester=:semester " +
                    "and d1.subject=:subject " +
                    "and d1.activityIndex=:activityIndex " +
                    "and (d1.classroom=:classroom or d1.laboratory=:classroom) " +
                "ORDER BY d1.semester,d1.subject,d1.activityIndex,d1.userID;";
        SQLQuery query=getSession().createSQLQuery(SQL);
        query.addEntity(edu.uoc.pelp.model.vo.Deliver.class);
        //query.setEntity("d1", edu.uoc.pelp.model.vo.Deliver.class);
        query.setParameter("semester", activityPK.getSemester());
        query.setParameter("subject", activityPK.getSubject());
        query.setParameter("activityIndex", activityPK.getActivityIndex());
        query.setParameter("classroom", classPK.toString());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        // Return the results
        return getDeliverList(list); 
    }
    
    @Override
    public List<DeliverID> findAllKey(ActivityID activity) {
        // Get the activity ID 
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Deliver.findByActivity");
        query.setParameter("semester", activityPK.getSemester());
        query.setParameter("subject", activityPK.getSubject());
        query.setParameter("activityIndex", activityPK.getActivityIndex());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        // Return the results
        return getDeliverIDList(list); 
    }

    @Override
    public List<DeliverID> findAllKey() {
        // Search for the semester key
        Query query=getSession().getNamedQuery("Deliver.findAll");
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        // Return the results
        return getDeliverIDList(list); 
    }

    @Override
    public List<DeliverID> findAllKey(IUserID user) {
        // Get the user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Deliver.findByUserID");
        query.setParameter("userID", userPK.getIdp());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        // Return the results
        return getDeliverIDList(list); 
    }

    @Override
    public List<DeliverID> findAllKey(ISubjectID subject, IUserID user) {
        // Get the subject ID and user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        SubjectPK subjectPK=ObjectFactory.getSubjectPK(subject);
        if(subjectPK==null) {
            return null;
        }
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Deliver.findBySubjectUserID");
        query.setParameter("semester", subjectPK.getSemester());
        query.setParameter("subject", subjectPK.getSubject());
        query.setParameter("userID", userPK.getIdp());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        
        // Return the results
        return getDeliverIDList(list); 
    }

    @Override
    public List<DeliverID> findAllKey(ActivityID activity, IUserID user) {
        // Get the activity ID and user ID
        UserPK userPK=ObjectFactory.getUserPK(user); 
        if(userPK==null) {
            return null;
        }
        ActivityPK activityPK=ObjectFactory.getActivityPK(activity);
        if(activityPK==null) {
            return null;
        }
        getSession().beginTransaction();
        // Get the activity register
        Query query=getSession().getNamedQuery("Deliver.findByActivityUserID");
        query.setParameter("semester", activityPK.getSemester());
        query.setParameter("subject", activityPK.getSubject());
        query.setParameter("activityIndex", activityPK.getActivityIndex());
        query.setParameter("userID", userPK.getIdp());
        List<edu.uoc.pelp.model.vo.Deliver> list=query.list();
        getSession().close();
        // Return the results
        return getDeliverIDList(list);
    }

    @Override
    public DeliverFileID add(DeliverID deliver, edu.uoc.pelp.engine.deliver.DeliverFile object) {
        // Check the input object
        if(object==null || deliver==null) {
            return null;
        } 
        
        Transaction transaction=null;
        DeliverFileID newID;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return null;
            }
            
            // Call the auxiliary addFile method
            newID=addFile(deliver,object);
            if(newID==null) {
                transaction.rollback();
            } else {
                // Commit the results
                transaction.commit();
            } 
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
        
        return newID;
    }

    @Override
    public boolean delete(DeliverFileID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean update(edu.uoc.pelp.engine.deliver.DeliverFile object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<edu.uoc.pelp.engine.deliver.DeliverFile> findAllFiles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<edu.uoc.pelp.engine.deliver.DeliverFile> findAll(DeliverID deliver) {
        
        // Check the input object
        if(deliver==null) {
            return null;
        }
        DeliverPK key=ObjectFactory.getDeliverPK(deliver);
        List<edu.uoc.pelp.model.vo.DeliverFile> filesList=findDeliverFiles(key);
        if(filesList==null) {
            return null;
        }
        
        // Create the output list
        List<edu.uoc.pelp.engine.deliver.DeliverFile> retList=new ArrayList<edu.uoc.pelp.engine.deliver.DeliverFile>();
        for(edu.uoc.pelp.model.vo.DeliverFile file:filesList) {
            retList.add(ObjectFactory.getDeliverFileObj(file));
        }
        
        // Sort the lits
        Collections.sort(retList);
              
        // Get the list of descriptions
        return retList;
    }

    @Override
    public edu.uoc.pelp.engine.deliver.DeliverFile find(DeliverFileID id) {
        // Check the input object
        if(id==null) {
            return null;
        }
        
        // Get the key
        DeliverFilePK key=ObjectFactory.getDeliverFilePK(id);
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Deliver.findById");
        query.setParameter("semester", key.getSemester());
        query.setParameter("subject", key.getSubject());
        query.setParameter("activityIndex", key.getActivityIndex());
        query.setParameter("userID", key.getUserID());
        query.setParameter("deliverIndex", key.getDeliverIndex());
        query.setParameter("fileIndex", key.getFileIndex());
                
        List<edu.uoc.pelp.model.vo.DeliverFile> newObj=query.list();
        if(newObj==null || newObj.size()!=1) {
            return null;
        }
        edu.uoc.pelp.model.vo.DeliverFile deliverFileReg=newObj.get(0);
        
        // Get the high level object
        return ObjectFactory.getDeliverFileObj(deliverFileReg);
    }

    @Override
    public DeliverFileID getLastID(DeliverID deliver) {
        // Check parameters
        if(deliver==null) {
            return null;
        }
        
        // Get low level representations
        DeliverPK deliverPK=ObjectFactory.getDeliverPK(deliver);
        if(deliverPK==null) {
            return null;
        }
                
        // Search the last identifier
        Query q=getSession().getNamedQuery("DeliverFile.findLast");
        q.setParameter("semester", deliverPK.getSemester());
        q.setParameter("subject", deliverPK.getSubject());
        q.setParameter("activityIndex", deliverPK.getActivityIndex());
        q.setParameter("userID", deliverPK.getUserID());
        q.setParameter("deliverIndex", deliverPK.getDeliverIndex());
        List<edu.uoc.pelp.model.vo.DeliverFile> lastDeliver=q.list();
       
        DeliverFileID lastID=null;
        if(lastDeliver!=null) {
            if(lastDeliver.size()>0) {
                lastID=ObjectFactory.getDeliverFileID(lastDeliver.get(0).getDeliverFilePK());
            }
        }
        
        // Return last id
        return lastID;
    }

    @Override
    public boolean updateRootPath(DeliverID deliverID, File newPath) {
        // Check input parameters
        if(deliverID==null || newPath==null) {
            return false;
        }
        
        Transaction transaction=null;
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Find current register
            DeliverPK key=ObjectFactory.getDeliverPK(deliverID);

            // Get the deliver register
            Query query=getSession().getNamedQuery("Deliver.findById");
            query.setParameter("semester", key.getSemester());
            query.setParameter("subject", key.getSubject());
            query.setParameter("activityIndex", key.getActivityIndex());
            query.setParameter("userID", key.getUserID());
            query.setParameter("deliverIndex", key.getDeliverIndex());

            List<edu.uoc.pelp.model.vo.Deliver> newObj=query.list();
            if(newObj==null || newObj.size()!=1) {
                return false;
            }
            edu.uoc.pelp.model.vo.Deliver deliverReg=newObj.get(0);
            
            // Change the value
            deliverReg.setRootPath(newPath.getAbsolutePath());
            
            // Update register
            getSession().merge(deliverReg);
            
            // Commit the results
            transaction.commit(); 
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        return true;
        
    }

    
}

