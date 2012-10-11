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

import edu.uoc.pelp.engine.activity.Activity;
import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.activity.ActivityTest;
import edu.uoc.pelp.engine.activity.TestID;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.model.vo.ActivityDesc;
import edu.uoc.pelp.model.vo.ActivityDescPK;
import edu.uoc.pelp.model.vo.ActivityPK;
import edu.uoc.pelp.model.vo.ObjectFactory;
import edu.uoc.pelp.model.vo.UOC.SubjectPK;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Implements the DAO object for the Activities. 
 * @author Xavier Baró
 */
public class ActivityDAO implements IActivityDAO {
    
    protected SessionFactory _sessionFactory = null;
    
    /**
     * Dafault constructor for compatibility or Spring session factory assignment
     */
    public ActivityDAO() {
        super();
    }
    
    /**
     * Dafault constructor with session factory assignment
     * @param sessionFactory Session factory of DAO access to the database
     */
    public ActivityDAO(SessionFactory sessionFactory) {
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
     * Get a list of Activity objects from a list of low level objects, adding the correspondant descriptions
     * @param list List of low level objects
     * @return List of high level objects
     */
    protected List<Activity> getActivityList(List<edu.uoc.pelp.model.vo.Activity> list) {
        List<Activity> retList=new ArrayList<Activity>();
        for(edu.uoc.pelp.model.vo.Activity register:list) {
            // Get the list of descriptions
            List<ActivityDesc> descList=getActivityDescriptions(register.getActivityPK());
            ActivityDesc[] descArray=null;
            if(descList!=null) {
                descArray=new ActivityDesc[descList.size()];
                descList.toArray(descArray);
            }
            
            // Add the final register
            retList.add(ObjectFactory.getActivityObj(register,descArray));
        }
        return retList;
    }
    
    @Override
    public ActivityID add(ISubjectID subjectID, Activity object) {
        
        // Check the input object
        if(object==null || subjectID==null) {
            return null;
        }
        
        Transaction transaction=null;
        ActivityID newID;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return null;
            }
            
            // Get the id of the last deliver
            ActivityID lastID=getLastID(subjectID);
        
            // Create the new id
            if(lastID!=null) {
                newID=new ActivityID(lastID);
                newID.index++;
            } else {
                newID=new ActivityID(subjectID,1);
            }
        
            // Get the object
            edu.uoc.pelp.model.vo.Activity newObj=ObjectFactory.getActivityReg(object);
        
            // Add a new object from given object, to break the reference
            ActivityPK key=ObjectFactory.getActivityPK(newID);
            if(key==null) {
                return null;
            }
        
            // Add the new object
            newObj.setActivityPK(key);            
            getSession().saveOrUpdate(newObj);
                    
            // Add the descriptions for current object
            for(String lang:object.getLanguageCodes()) {
                String desc=object.getDescription(lang);
                if(desc!=null) {
                    ActivityDescPK descPK=new ActivityDescPK(key,lang);
                    getSession().saveOrUpdate(new ActivityDesc(descPK,desc));
                }
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
    public boolean delete(ActivityID id) {
        
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
            Activity activity=find(id);
            if(activity==null) {
                transaction.rollback();
                return false;
            }
            
            // Get the primary key
            ActivityPK key=ObjectFactory.getActivityPK(id);

            // Remove descriptions
            if(!deleteDescriptions(key)) {
                transaction.rollback();
                return false;
            }
        
            // Remove the object
            getSession().delete(ObjectFactory.getActivityReg(activity));
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
    public boolean update(Activity object) {
        // Check the input object
        if(object==null || object.getActivity()==null) {
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
            if(find(object.getActivity())==null) {
                transaction.rollback();
                return false;
            }
            
            // Get a low-level representation
            edu.uoc.pelp.model.vo.Activity newObj=ObjectFactory.getActivityReg(object);

            // Update the object
            getSession().merge(newObj);

            // Update the descriptions
            List<ActivityDesc> descList=this.getActivityDescriptions(newObj.getActivityPK());
            if(descList!=null) {
                // Check modified descriptions
                for(ActivityDesc desc:descList) {
                    String descVal=object.getDescription(desc.getActivityDescPK().getLangCode());
                    if(descVal==null) {
                        // Remove deleted descriptions
                        getSession().delete(desc);
                    } else if(!descVal.equals(desc.getDescription())) {
                        // Create the new description object
                        desc.setDescription(descVal);
                        // Changed value
                        getSession().merge(desc);
                    }
                }    
            }
            
            // Add new descriptions
            for(String lang:object.getLanguageCodes()) {
                String descStr=object.getDescription(lang);
                if(descStr!=null) {
                    
                    ActivityDescPK descPK=new ActivityDescPK(newObj.getActivityPK(),lang);
                    ActivityDesc desc=new ActivityDesc(descPK,descStr);
                    if(getActivityDescription(descPK)==null) {
                        getSession().saveOrUpdate(desc);
                    }
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
        
        return (object.equals(find(object.getActivity())));
    }

    @Override
    public List<Activity> findAll() {
        // Search for the semester key
        Query query=getSession().getNamedQuery("Activity.findAll");
        List<edu.uoc.pelp.model.vo.Activity> list=query.list();
        
        // Return the results
        return getActivityList(list); 
    }

    @Override
    public List<Activity> findActive() {
        // Search for the semester key
        Query query=getSession().getNamedQuery("Activity.findActive");
        List<edu.uoc.pelp.model.vo.Activity> list=query.list();
        
        // Return the results
        return getActivityList(list);
    }

    @Override
    public Activity find(ActivityID object) {
        // Check the input object
        if(object==null) {
            return null;
        }
        
        // Get the key
        ActivityPK key=ObjectFactory.getActivityPK(object);
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Activity.findActivity");
        query.setParameter("semester", key.getSemester());
        query.setParameter("subject", key.getSubject());
        query.setParameter("activityIndex", key.getActivityIndex());
        List<edu.uoc.pelp.model.vo.Activity> newObj=query.list();
        if(newObj==null || newObj.size()!=1) {
            return null;
        }
        edu.uoc.pelp.model.vo.Activity activityReg=newObj.get(0);
                
        // Get the list of descriptions
        List<ActivityDesc> descList=getActivityDescriptions(key);
        ActivityDesc[] descArray=null;
        if(descList!=null) {
            descArray=new ActivityDesc[descList.size()];
            descList.toArray(descArray);
        }

        // Add the final register
        return ObjectFactory.getActivityObj(activityReg,descArray);
    }

    @Override
    public List<Activity> findAll(ISubjectID subject) {        
        // Check the input object
        if(subject==null) {
            return null;
        }
        
        // Get the key
        SubjectPK key=ObjectFactory.getSubjectPK(subject);
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Activity.findAllBySubject");
        query.setParameter("semester", key.getSemester());
        query.setParameter("subject", key.getSubject());
        
        List<edu.uoc.pelp.model.vo.Activity> list=query.list();
        
        // Return the results
        return getActivityList(list);
    }

    @Override
    public List<Activity> findActive(ISubjectID subject) {
        // Check the input object
        if(subject==null) {
            return null;
        }
        
        // Get the key
        SubjectPK key=ObjectFactory.getSubjectPK(subject);
        
        // Get the activity register
        Query query=getSession().getNamedQuery("Activity.findActiveBySubject");
        query.setParameter("semester", key.getSemester());
        query.setParameter("subject", key.getSubject());
        
        List<edu.uoc.pelp.model.vo.Activity> list=query.list();
        
        // Return the results
        return getActivityList(list);
    }

    @Override
    public ActivityID getLastID(ISubjectID subject) {
        // Check the input object
        if(subject==null) {
            return null;
        }
        SubjectPK subjectPK=ObjectFactory.getSubjectPK(subject);
        if(subjectPK==null) {
            return null;
        }
        
        // Search the last identifier
        Query q=getSession().getNamedQuery("Activity.findLast");
        q.setParameter("semester", subjectPK.getSemester());
        q.setParameter("subject", subjectPK.getSubject());
        List<edu.uoc.pelp.model.vo.Activity> lastAct=q.list();
       
        ActivityID lastID=null;
        if(lastAct!=null) {
            if(lastAct.size()>0) {
                lastID=ObjectFactory.getActivityID(lastAct.get(0).getActivityPK());
            }
        }
        
        // Return last id
        return lastID;
    }

    /**
     * Get the list of descriptions for the given activity
     * @param key Activity primary key
     * @return List of activity description keys
     */
    private List<ActivityDesc> getActivityDescriptions(ActivityPK key) {
        // Check the input object
        if(key==null) {
            return null;
        }
                
        // Get the descriptions
        Query q=getSession().getNamedQuery("ActivityDesc.findByActivity");
        q.setParameter("semester", key.getSemester());
        q.setParameter("subject", key.getSubject());
        q.setParameter("activityIndex", key.getActivityIndex());

        // Get the list of descriptions
        List<ActivityDesc> list=q.list();
        
        return list;
    }
    
    /**
     * Delete all the descriptions for a certain activity
     * @param key Activity primary key
     * @return True if the operation ends successfully or Fals in case of error.
     */
    private boolean deleteDescriptions(ActivityPK key) {
        // Check the input object
        if(key==null) {
            return false;
        }
                
        // Get the list of descriptions
        List<ActivityDesc> descList=getActivityDescriptions(key);
        
        // Remove all descriptions
        for(ActivityDesc desc:descList) {
            getSession().delete(desc);
        }
        
        return true;
    }
    
    /**
     * Get the descriptions for the given activity in the given language
     * @param key Activity description primary key
     * @return Description or null if it is not defined
     */
    private String getActivityDescription(ActivityDescPK key) {
        String desc=null;
        
        // Check the input object
        if(key==null) {
            return null;
        }
                
        // Get the descriptions
        Query q=getSession().getNamedQuery("ActivityDesc.findByPK");
        q.setParameter("semester", key.getSemester());
        q.setParameter("subject", key.getSubject());
        q.setParameter("activityIndex", key.getActivityIndex());
        q.setParameter("langCode",key.getLangCode());

        // Get the list of descriptions
        List<ActivityDesc> list=q.list();
        if(list!=null && list.size()>0) {
            desc=list.get(0).getDescription();
        }
        
        return desc;
    }

    @Override
    public TestID add(ActivityID activityID, ActivityTest object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(TestID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean update(ActivityTest object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ActivityTest> findAllTests() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ActivityTest> findAll(ActivityID activity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ActivityTest find(TestID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TestID getLastID(ActivityID activityID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

