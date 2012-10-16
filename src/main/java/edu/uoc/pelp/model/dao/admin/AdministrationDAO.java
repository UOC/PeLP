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
import edu.uoc.pelp.model.vo.admin.PelpActiveSubjects;
import edu.uoc.pelp.model.vo.admin.PelpAdmins;
import edu.uoc.pelp.model.vo.admin.PelpLanguages;
import edu.uoc.pelp.model.vo.admin.PelpMainLabSubjects;
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
            adminData.setUserName(person.getUsername());
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
        query.setParameter("userName", person.getUsername());
                
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
            newObj.setUserName(person.getUsername());
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

    @Override
    public List<PelpLanguages> getAvailableLanguages() {
        // Get the language registers
        Query query=getSession().getNamedQuery("PelpLanguages.findAll");            
        List<PelpLanguages> list=query.list();
        
        if(list==null) {
            return null;
        }
                
        // Return the data                
        return list;
    }

    @Override
    public List<PelpActiveSubjects> getActiveSubjects() {
                
        // Get the language registers
        Query query=getSession().getNamedQuery("PelpActiveSubjects.findAllActive");
                
        return query.list();
    }
    
    @Override
    public List<PelpActiveSubjects> getActiveSubjects(String semester) {
        // Check input parameters
        if(semester==null) {
            return null;
        }   
        // Get the language registers
        Query query=getSession().getNamedQuery("PelpActiveSubjects.findActiveBySemester");
        query.setParameter("semester", semester);
                
        return query.list();
    }

    @Override
    public boolean addActiveSubject(String semester, String subjectCode, boolean active) {
        // Check input parameters
        if(semester==null || subjectCode==null) {
            return false;
        }
        
        Transaction transaction=null;
        PelpActiveSubjects subjectsData;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Check that this field does not exist
            subjectsData=findActiveSubjects(semester,subjectCode);
            if(subjectsData!=null) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }
            
            // Create the new object
            PelpActiveSubjects newRegister=new PelpActiveSubjects(semester,subjectCode);
            newRegister.setActive(active);
            
            // Add the new administrator
            getSession().saveOrUpdate(newRegister);
            
            // Commit the results
            transaction.commit();
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        return findActiveSubjects(semester,subjectCode)!=null;
    }

    @Override
    public boolean updateActiveSubject(String semester, String subjectCode, boolean active) {
        // Check input parameters
        if(semester==null || subjectCode==null) {
            return false;
        }
        
        Transaction transaction=null;
        PelpActiveSubjects subjectsData;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Check that this field does not exist
            subjectsData=findActiveSubjects(semester,subjectCode);
            if(subjectsData==null) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }
            
            // Add the new administrator
            getSession().merge(subjectsData);
            
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
    public boolean deleteActiveSubject(String semester, String subjectCode) {
        // Check input parameters
        if(semester==null || subjectCode==null) {
            return false;
        }
        
        Transaction transaction=null;
        PelpActiveSubjects subjectsData;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Check that this field does not exist
            subjectsData=findActiveSubjects(semester,subjectCode);
            if(subjectsData==null) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }
            
            // Add the new administrator
            getSession().delete(subjectsData);
            
            // Commit the results
            transaction.commit();
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        return findActiveSubjects(semester,subjectCode)==null;
    }

    @Override
    public List<PelpMainLabSubjects> getMainSubjectOfLab(String labSubjectCode) {
        // Check input parameters
        if(labSubjectCode==null) {
            return null;
        }   
        // Get the language registers
        Query query=getSession().getNamedQuery("PelpMainLabSubjects.findByLabSubjectCode");
        query.setParameter("labSubjectCode", labSubjectCode);
                
        return query.list();
    }

    @Override
    public List<PelpMainLabSubjects> getLabSubjectOfMain(String mainSubjectCode) {
        // Check input parameters
        if(mainSubjectCode==null) {
            return null;
        }   
        // Get the language registers
        Query query=getSession().getNamedQuery("PelpMainLabSubjects.findByMainSubjectCode");
        query.setParameter("mainSubjectCode", mainSubjectCode);
                
        return query.list();
    }

    @Override
    public boolean addMainLabCorrespondence(String mainSubject, String labSubject) {
        // Check input parameters
        if(mainSubject==null || labSubject==null) {
            return false;
        }
        
        Transaction transaction=null;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Check that this field does not exist
            if(findMainLabSubjects(mainSubject,labSubject)!=null) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }

            // Creat the new object
            PelpMainLabSubjects newObj=new PelpMainLabSubjects(mainSubject,labSubject);
            
            // Add the subjects relation
            getSession().saveOrUpdate(newObj);
            
            // Commit the results
            transaction.commit();
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        return findMainLabSubjects(mainSubject,labSubject)!=null;
    }

    @Override
    public boolean deleteMainLabCorrespondence(String mainSubject, String labSubject) {
        // Check input parameters
        if(mainSubject==null || labSubject==null) {
            return false;
        }
        
        Transaction transaction=null;
        PelpMainLabSubjects subjectsData;
        
        try {
            // Start a new transaction
            transaction=getSession().beginTransaction();
            if (transaction == null) {
                return false;
            }
            
            // Check that this field exists
            subjectsData=findMainLabSubjects(mainSubject,labSubject);
            if(subjectsData==null) {
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }
            
            // Delete the subjects relation
            getSession().delete(subjectsData);
            
            // Commit the results
            transaction.commit();
            
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        
        return findMainLabSubjects(mainSubject,labSubject)==null;
    }
    
    @Override
    public PelpLanguages findPelpLanguage(String languageCode) {
        // Check the parameter
        if(languageCode==null) {
            return null;
        }
        
        // Get the language registers
        Query query=getSession().getNamedQuery("PelpLanguages.findByLangCode");
        query.setParameter("userName", languageCode);
        List<PelpLanguages> list=query.list();
        
        if(list==null || list.size()!=1) {
            return null;
        }
                
        return list.get(0);
    }
    
    @Override
    public PelpActiveSubjects findActiveSubjects(String semester,String subject) {
        
        // Check input parameters
        if(semester==null || subject==null) {
            return null;
        }
        
        // Get the language registers
        Query query=getSession().getNamedQuery("PelpActiveSubjects.findByPK");
        query.setParameter("semester", semester);
        query.setParameter("subject", subject);
        List<PelpActiveSubjects> list=query.list();
        
        // Check the output list
        if(list==null || list.size()!=1) {
            return null;
        }
        
        return list.get(0);
    }
    
    @Override
    public PelpMainLabSubjects findMainLabSubjects(String mainSubject,String labSubject) {
        
        // Check input parameters
        if(mainSubject==null || labSubject==null) {
            return null;
        }
        
        // Get the language registers
        Query query=getSession().getNamedQuery("PelpMainLabSubjects.findByPK");
        query.setParameter("mainSubjectCode", mainSubject);
        query.setParameter("labSubjectCode", labSubject);
        List<PelpMainLabSubjects> list=query.list();
        
        // Check the output list
        if(list==null || list.size()!=1) {
            return null;
        }
        
        return list.get(0);
    }
}