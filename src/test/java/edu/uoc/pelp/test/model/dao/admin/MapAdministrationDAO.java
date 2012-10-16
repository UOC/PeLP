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
package edu.uoc.pelp.test.model.dao.admin;

import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.model.dao.admin.IAdministrationDAO;
import edu.uoc.pelp.model.vo.admin.PelpActiveSubjects;
import edu.uoc.pelp.model.vo.admin.PelpAdmins;
import edu.uoc.pelp.model.vo.admin.PelpLanguages;
import edu.uoc.pelp.model.vo.admin.PelpMainLabSubjects;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Implements the DAO object for the Activities. 
 * @author Xavier Baró
 */
public class MapAdministrationDAO implements IAdministrationDAO {
    
    protected SessionFactory _sessionFactory = null;
    
    /**
     * Dafault constructor for compatibility or Spring session factory assignment
     */
    public MapAdministrationDAO() {
        super();
    }
    
    /**
     * Dafault constructor with session factory assignment
     * @param sessionFactory Session factory of DAO access to the database
     */
    public MapAdministrationDAO(SessionFactory sessionFactory) {
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PelpAdmins getAdminData(Person person) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateAdmin(Person person, boolean active, boolean grant) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delAdmin(Person person) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PelpLanguages> getAvailableLanguages() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PelpActiveSubjects> getActiveSubjects() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PelpActiveSubjects> getActiveSubjects(String semester) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addActiveSubject(String semester, String subjectCode, boolean active) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateActiveSubject(String semester, String subjectCode, boolean active) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteActiveSubject(String semester, String subjectCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PelpMainLabSubjects> getMainSubjectOfLab(String labSubjectCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PelpMainLabSubjects> getLabSubjectOfMain(String mainSubjectCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addMainLabCorrespondence(String mainSubject, String labSubject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteMainLabCorrespondence(String mainSubject, String labSubject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PelpLanguages findPelpLanguage(String languageCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PelpActiveSubjects findActiveSubjects(String semester, String subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PelpMainLabSubjects findMainLabSubjects(String mainSubject, String labSubject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

