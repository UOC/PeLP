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

import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.model.vo.ObjectFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import junit.framework.Assert;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Perform all tests over the DAO object for the SemesterDO table of the database
 * @author Xavier Baró
 */
public class TDAO_Semester {
    
    private static final SessionFactory sessionFactory = buildSessionFactory();
  
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new AnnotationConfiguration().configure("hibernate_test.cfg.xml")
                    .buildSessionFactory();
 
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
  
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public TDAO_Semester() {
        //EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("pelpDS");
        //_semesterDAO=new SemesterJPA(null,emf);
    }
    
    @Test 
    public void testNotAuthUserRightsToCampus() {
        Semester s1=new Semester("20111",null,null);
                
        SessionFactory sf = getSessionFactory();
        Session session = sf.openSession();
        
        Query q = session.createQuery("delete from Semester");      
        q.executeUpdate();
        
        try {
            session.beginTransaction();   
            session.saveOrUpdate(ObjectFactory.getSemesterReg(s1));
            session.getTransaction().commit();
        } catch (HibernateException ex) {
            Assert.fail("Cannot save semester");
        } finally {
            session.close();
        }
        
        session = sf.openSession();
        q = session.getNamedQuery("Semester.findAll");      
        
        //q.setDate( "date", aMonthAgo );
        List<edu.uoc.pelp.model.vo.UOC.Semester> list1 = q.list();   
        session.close();
        
        Assert.assertEquals("Only one semester exists", list1.size(),1);
        Assert.assertEquals("Chech the semester data with null fields",ObjectFactory.getSemesterObj(list1.get(0)), s1);
        
        // Create time objects
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.DATE, -1);        
        Date yesterday = c.getTime();        
        c.setTime(now); 
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();
        
        // Add all information
        s1.setStartDate(now);
        s1.setEndDate(tomorrow);
        
        // Update the object
        session = sf.openSession();
        session.beginTransaction();
        session.update(ObjectFactory.getSemesterReg(s1));
        session.getTransaction().commit();
        
        q=session.getNamedQuery("Semester.findAll"); 
        list1 = q.list();   
        Assert.assertEquals("Only one semester exists", list1.size(),1);
        Assert.assertEquals("Chech the semester data with non empty fields",ObjectFactory.getSemesterObj(list1.get(0)), s1);
        
        Semester s2=new Semester("20121",tomorrow,null);
        Semester s3=new Semester("20122",null,yesterday);
        Semester s4=new Semester("20131",yesterday,null);
        Semester s5=new Semester("20132",null,tomorrow);
        
                
        session = sf.openSession();
        try {
            session.beginTransaction();        
            session.saveOrUpdate(ObjectFactory.getSemesterReg(s2));
            session.saveOrUpdate(ObjectFactory.getSemesterReg(s3));
            session.saveOrUpdate(ObjectFactory.getSemesterReg(s4));
            session.saveOrUpdate(ObjectFactory.getSemesterReg(s5));
            session.getTransaction().commit();
        } catch (HibernateException ex) {
            Assert.fail("Cannot save semesters");
        } finally {
            session.close();
        }
        
        session = sf.openSession();
        q=session.getNamedQuery("Semester.findAll"); 
        list1 = q.list();   
        Assert.assertEquals("All four semester exists", 5, list1.size());
        Assert.assertEquals("Chech the semester data with non empty fields 1",s1,ObjectFactory.getSemesterObj(list1.get(0)));
        Assert.assertEquals("Chech the semester data with non empty fields 2",s2,ObjectFactory.getSemesterObj(list1.get(1)));
        Assert.assertEquals("Chech the semester data with non empty fields 3",s3,ObjectFactory.getSemesterObj(list1.get(2)));
        Assert.assertEquals("Chech the semester data with non empty fields 4",s4,ObjectFactory.getSemesterObj(list1.get(3)));
        Assert.assertEquals("Chech the semester data with non empty fields 5",s5,ObjectFactory.getSemesterObj(list1.get(4)));
        
        q=session.getNamedQuery("Semester.findActive"); 
        list1 = q.list();   
        Assert.assertEquals("Only one semester exists", 3,list1.size());
        Assert.assertEquals("Chech the semester data with non empty fields 1",s1,ObjectFactory.getSemesterObj(list1.get(0)));
        Assert.assertEquals("Chech the semester data with non empty fields 4",s4,ObjectFactory.getSemesterObj(list1.get(1)));
        Assert.assertEquals("Chech the semester data with non empty fields 5",s5,ObjectFactory.getSemesterObj(list1.get(2)));
        session.close();

        /*
        session.save(employee);
 
         
        List<Employee> employees = session.createQuery("from Employee").list();
        for (Employee employee1 : employees) {
            System.out.println(employee1.getFirstname() + " , "
                    + employee1.getLastname() + ", "
                    + employee1.getEmployeeDetail().getState());
        }
        * 
        */
        
        
        session = sf.openSession();
        try {
            session.beginTransaction();        
            session.delete(ObjectFactory.getSemesterReg(s1));
            session.delete(ObjectFactory.getSemesterReg(s2));
            session.delete(ObjectFactory.getSemesterReg(s3));
            session.delete(ObjectFactory.getSemesterReg(s4));
            session.delete(ObjectFactory.getSemesterReg(s5));
            session.getTransaction().commit();
        } catch (HibernateException ex) {
            Assert.fail("Cannot delete semesters");
        } finally {
            session.close();
        }
    }
}
