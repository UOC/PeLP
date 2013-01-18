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

import edu.uoc.pelp.engine.activity.TestID;
import edu.uoc.pelp.engine.deliver.ActivityTestResult;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.model.vo.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * This class implements a DAO object for table deliverResults
 * @author Xavier Baró
 */
public class DeliverResultsDAO implements IDeliverResultDAO {
    
    protected SessionFactory _sessionFactory = null;
    
    /**
     * Dafault constructor for compatibility or Spring session factory assignment
     */
    public DeliverResultsDAO() {
        super();
    }
    
    /**
     * Dafault constructor with session factory assignment
     * @param sessionFactory Session factory of DAO access to the database
     */
    public DeliverResultsDAO(SessionFactory sessionFactory) {
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
     * Add the list of test results for this deliver
     * @param testResults Array of results for each test
     * @return True if the files are correctly added or false otherwise
     */
    private boolean addTestResults(DeliverID deliverID,ActivityTestResult[] testResults) {
        
        if(deliverID==null) {
            return false;
        }
        
        try {
            // Add the files
            for(ActivityTestResult testResult:testResults) {                
                if(!addTestResult(deliverID,testResult)) {
                    return false;
                }
            }
        } catch (RuntimeException e) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Adds a test result to a deliver. This method assumes that is called inside an open transaction, and is
     * used as auxiliar method.
     * @param deliver Deliver identifier
     * @param object Test result to be added to the deliver results
     * @return True if the addition has been correctly done or false otherwise.
     */
    private boolean addTestResult(DeliverID deliver, ActivityTestResult object) {
        // Check input parameters
        if(deliver==null || object==null) {
            return false;
        }
        if(object.getTestID()==null) {
            return false;
        }
        
        // Get the object
        DeliverTestResult newObj=ObjectFactory.getTestResultReg(deliver, object);
        
        // Check that the file does not exist
        if(findTestResult(deliver, object.getTestID())!=null) {
            return false;
        }

        // Add current date
        getSession().saveOrUpdate(newObj);
        
        return true;
    }
    
    /**
     * Delete all the test results of a certain deliver
     * @param key Deliver primary key
     * @return True if the operation ends successfully or Fals in case of error.
     */
    private boolean deleteDeliverTestResult(DeliverPK key) {
        // Check the input object
        if(key==null) {
            return false;
        }
                
        // Get the list of test results
        List<DeliverTestResult> deliverFiles=findDeliverTestResults(key);
        
        // Remove all files
        for(DeliverTestResult file:deliverFiles) {
            getSession().delete(file);
        }
        
        return true;
    }
    
    /**
     * Get the list of test results for a certain deliver.
     * @param key Deliver primary key
     * @return Low level file objects
     */
    private List<edu.uoc.pelp.model.vo.DeliverTestResult> findDeliverTestResults(DeliverPK key) {
        // Check the input object
        if(key==null) {
            return null;
        }
                        
        // Get the descriptions
        Query q=getSession().getNamedQuery("DeliverTestResult.findByDeliverID");
        q.setParameter("semester", key.getSemester());
        q.setParameter("subject", key.getSubject());
        q.setParameter("activityIndex", key.getActivityIndex());
        q.setParameter("userID",key.getUserID());
        q.setParameter("deliverIndex",key.getDeliverIndex());
        
        // Get the list of test results
        return q.list();
    }
    
    /**
     * Get a list of Deliver objects from a list of low level objects, adding the correspondant files
     * @param list List of low level objects
     * @return List of high level objects
     */
    protected List<DeliverResults> getDeliverResultList(List<edu.uoc.pelp.model.vo.DeliverResult> list) {
        List<DeliverResults> retList=new ArrayList<DeliverResults>();
        for(edu.uoc.pelp.model.vo.DeliverResult register:list) {  
            // Get the list of test results
            List<edu.uoc.pelp.model.vo.DeliverTestResult> testList=findDeliverTestResults(register.getDeliverPK());
            edu.uoc.pelp.model.vo.DeliverTestResult[] testArray=null;
            if(testList!=null) {
                testArray=new edu.uoc.pelp.model.vo.DeliverTestResult[testList.size()];
                testList.toArray(testArray);
            }
            
            // Add the final register
            retList.add(ObjectFactory.getDeliverResultObj(register, testArray));
        }
        
        return retList;
    }
    
    /**
     * Get the list of test for a certain activity.
     * @param key Deliver primary key
     * @return Low level file objects
     */
    private List<edu.uoc.pelp.model.vo.ActivityTest> findActivityTests(ActivityPK key) {
        // Check the input object
        if(key==null) {
            return null;
        }
                        
        // Get the descriptions
        Query q=getSession().getNamedQuery("ActivityTest.findByActivityID");
        q.setParameter("semester", key.getSemester());
        q.setParameter("subject", key.getSubject());
        q.setParameter("activityIndex", key.getActivityIndex());
                
        // Get the list of test results
        return q.list();
    }
    
    @Override
    public boolean add(DeliverResults results) {
        // Check the input object
        if(results==null) {
            return false;
        } 
        
        // Get the low level representation for the data
        DeliverPK key=ObjectFactory.getDeliverPK(results.getDeliverID());
        if(key==null) {
            return false;
        }
        
        Transaction transaction=null;
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Check that the results does not exist
            if(find(results.getDeliverID())!=null) {
                transaction.rollback();
                return false;
            }
            transaction=getSession().beginTransaction();                   
            // Get the object
            DeliverResult newObj=ObjectFactory.getDeliverResultReg(results);
        
            // Store the object
            transaction=getSession().beginTransaction();
            getSession().saveOrUpdate(newObj);
            
            // Add test results
            if(!addTestResults(results.getDeliverID(),results.getResults())) {
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
        
        return (find(results.getDeliverID())!=null);
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
            DeliverResults deliverResults=find(id);
            if(deliverResults==null) {
                transaction.rollback();
                return false;
            }
            
            // Get the primary key
            DeliverPK key=ObjectFactory.getDeliverPK(id);
            
            // Remove the files
            if(!deleteDeliverTestResult(key)) {
                transaction.rollback();
                return false;
            }
                                
            // Remove the object
            getSession().delete(ObjectFactory.getDeliverResultReg(deliverResults));
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
    public boolean update(DeliverResults object) {
        // Check the input object
        if(object==null || object.getDeliverID()==null) {
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
            if(find(object.getDeliverID())==null) {
                transaction.rollback();
                return false;
            }
            
            // Get a low-level representation
            edu.uoc.pelp.model.vo.DeliverResult newObj=ObjectFactory.getDeliverResultReg(object);

            // Update the object
            getSession().merge(newObj);
            
            // Get the object result tests
            edu.uoc.pelp.engine.deliver.ActivityTestResult[] objTestResult=object.getResults();
            
            // Get the number of tests for current activity
            
                        
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
    public DeliverResults find(DeliverID id) {
        // Check the input object
        if(id==null) {
            return null;
        }
        getSession().beginTransaction();
        // Get the key
        DeliverPK key=ObjectFactory.getDeliverPK(id);
        
        // Get the activity register
        Query query=getSession().getNamedQuery("DeliverResult.findById");
        query.setParameter("semester", key.getSemester());
        query.setParameter("subject", key.getSubject());
        query.setParameter("activityIndex", key.getActivityIndex());
        query.setParameter("userID", key.getUserID());
        query.setParameter("deliverIndex", key.getDeliverIndex());
        
        List<edu.uoc.pelp.model.vo.DeliverResult> newObj=query.list();
        if(newObj==null || newObj.size()!=1) {
        	getSession().close();
            return null;
        }
        edu.uoc.pelp.model.vo.DeliverResult deliverResultsReg=newObj.get(0);
        
        // Get the list of test results
        List<DeliverTestResult> testResultList=findDeliverTestResults(key);
        DeliverTestResult[] testResultArray=null;
        if(testResultList!=null) {
            testResultArray=new DeliverTestResult[testResultList.size()];
            testResultList.toArray(testResultArray);
        }

        DeliverResults objResult = null;
        // Add the final register
        objResult = ObjectFactory.getDeliverResultObj(deliverResultsReg, testResultArray);
        getSession().close();
        return objResult;
        		
    }

    @Override
    public boolean add(DeliverID deliverID, ActivityTestResult testResult) {
        
        // Check input parameters
        if(deliverID==null || testResult==null) {
            return false;
        }
        if(testResult.getTestID()==null) {
            return false;
        }        
        
        Transaction transaction=null;        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Call auxiliary method
            if(!addTestResult(deliverID, testResult)) {
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
    public boolean delete(DeliverID deliverID, ActivityTestResult testResult) {
        // Check input parameters
        if(deliverID==null || testResult==null) {
            return false;
        }
        if(testResult.getTestID()==null) {
            return false;
        }        
        
        Transaction transaction=null;        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Check if this register exists
            if(findTestResult(deliverID,testResult.getTestID())==null) {
                transaction.rollback();
                return false;
            }
            
            // Get the object
            DeliverTestResult newObj=ObjectFactory.getTestResultReg(deliverID, testResult);
            
            // Update the object
            getSession().delete(newObj);
            
            // Commit the results
            transaction.commit();
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
                
        return (findTestResult(deliverID,testResult.getTestID())==null);
    }

    @Override
    public boolean update(DeliverID deliverID, ActivityTestResult testResult) {
        // Check input parameters
        if(deliverID==null || testResult==null) {
            return false;
        }
        if(testResult.getTestID()==null) {
            return false;
        }        
        
        Transaction transaction=null;        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Check if this register exists
            if(findTestResult(deliverID,testResult.getTestID())==null) {
                transaction.rollback();
                return false;
            }
            
            // Get the object
            DeliverTestResult newObj=ObjectFactory.getTestResultReg(deliverID, testResult);
            
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
                
        return testResult.equals(findTestResult(deliverID,testResult.getTestID()));
    }

    @Override
    public List<ActivityTestResult> findTestResults(DeliverID deliverID) {
        List<ActivityTestResult> retList=new ArrayList<ActivityTestResult>();
        
        // Check the input object
        if(deliverID==null) {
            return null;
        }
        
        // Get the key
        DeliverPK key=ObjectFactory.getDeliverPK(deliverID);
        
        // Get the low level registers
        List<edu.uoc.pelp.model.vo.DeliverTestResult> list=findDeliverTestResults(key);
        for(edu.uoc.pelp.model.vo.DeliverTestResult test:list) {
            retList.add(ObjectFactory.getTestResultObj(test));
        }
        
        // Return the object
        return retList;
    }
    
    @Override
    public ActivityTestResult findTestResult(DeliverID deliverID,TestID testID) {
        // Check the input object
        if(deliverID==null || testID==null) {
            return null;
        }
        
        // Get the key
        DeliverTestResultPK key=ObjectFactory.getDeliverTestResultPK(deliverID, testID);
        
        // Get the activity register
        Query query=getSession().getNamedQuery("DeliverTestResult.findById");
        query.setParameter("semester", key.getSemester());
        query.setParameter("subject", key.getSubject());
        query.setParameter("activityIndex", key.getActivityIndex());
        query.setParameter("userID", key.getUserID());
        query.setParameter("deliverIndex", key.getDeliverIndex());
        query.setParameter("testIndex", key.getTestIndex());
        
        List<edu.uoc.pelp.model.vo.DeliverTestResult> newObj=query.list();
        if(newObj==null || newObj.size()!=1) {
            return null;
        }
        edu.uoc.pelp.model.vo.DeliverTestResult deliverResultsReg=newObj.get(0);
        
        // Return the object
        return ObjectFactory.getTestResultObj(deliverResultsReg);
    }
}
