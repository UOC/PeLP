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
package edu.uoc.pelp.engine.admin;

import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.model.dao.ITimePeriodDAO;
import edu.uoc.pelp.model.dao.admin.IAdministrationDAO;
import edu.uoc.pelp.model.vo.admin.PelpActiveSubjects;
import edu.uoc.pelp.model.vo.admin.PelpAdmins;
import edu.uoc.pelp.model.vo.admin.PelpLanguages;
import edu.uoc.pelp.model.vo.admin.PelpMainLabSubjects;
import java.util.Date;
import java.util.List;

/**
 * Implements a class to manage administration data.
 * @author Xavier Baró
 */
public class DAOAdministrationManager implements IAdministrationManager {
    
    /**
     * Administration DAO
     */
    private IAdministrationDAO _administration;
    
    /**
     * Semester DAO
     */
    private ITimePeriodDAO _timePeriodDAO;
    
    /**
     * Default constructor for the DAOActivityManager
     * @param adminDAO Object to access all the administration information
     * @param semesterDAO Object to access active semesters
     */
    public DAOAdministrationManager(IAdministrationDAO adminDAO,ITimePeriodDAO semesterDAO) {
        _administration=adminDAO;
        _timePeriodDAO=semesterDAO;        
    }

    @Override
    public boolean isAdministrator(Person person) {
        // Check the parameters
        if(person==null) {
            return false;
        }
        
        // Get the administrator details
        PelpAdmins adminData=_administration.getAdminData(person);
        if(adminData==null) {
            return false;
        }
        
        // Check if is active or not
        if(adminData.getActive()!=true) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean isSuperAdministrator(Person person) {
        // Check if the parameters
        if(person==null) {
            return false;
        }
        
        // Get the administrator details
        PelpAdmins adminData=_administration.getAdminData(person);
        if(adminData==null) {
            return false;
        }
        
        // Check if is active or not
        if(adminData.getActive()!=true) {
            return false;
        }
        
        // Check if it have grant permission
        if(adminData.getGrantAllowed()!=true) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean addAdministrator(Person person, boolean active, boolean grant) {
        // Delegate to the DAO administrator
        return _administration.addAdmin(person, active, grant);
    }

    @Override
    public boolean deleteAdministrator(Person person) {
        return _administration.delAdmin(person);
    }

    @Override
    public boolean updateAdministrator(Person person, boolean active, boolean grant) {
        return _administration.updateAdmin(person, active, grant);
    }
    
    @Override
    public boolean isValidLanguageCode(String languageCode) {
        // Check the parameter
        if(languageCode==null) {
            return false;
        }
        
        // Get the language registers
        return _administration.findPelpLanguage(languageCode)!=null;
    }
    
    @Override
    public boolean isActiveSubject(String semester, String subjectCode) {
        // Check input parameters
        if(semester==null || subjectCode==null) {
            return false;
        }
        
        // Check the semester
        if(!isActiveSemester(semester)) {
            return false;
        }
        
        // Get the active subject registers
        return _administration.findActiveSubjects(semester, subjectCode)!=null;
    }
    
    @Override
    public boolean isActiveSemester(String semester) {
        // Check input parameters
        if(semester==null) {
            return false;
        }
        
        Semester semesterObj=(Semester) _timePeriodDAO.find(new Semester(semester));
        if(semesterObj==null) {
            return false;
        }
        
        return semesterObj.isActive();
    }

    @Override
    public PelpLanguages[] getAvailableLanguages() {
        List<PelpLanguages> list=_administration.getAvailableLanguages();
        
        // Build the languages array
        PelpLanguages[] langList=new PelpLanguages[list.size()];
        list.toArray(langList);
        
        return langList;
    }

    @Override
    public List<PelpActiveSubjects> getActiveSubjects() {
        return _administration.getActiveSubjects();
    }

    @Override
    public List<PelpActiveSubjects> getActiveSubjects(String semester) {
        return _administration.getActiveSubjects(semester);
    }

    @Override
    public boolean addActiveSubject(String semester, String subjectCode, boolean active) {
        return _administration.addActiveSubject(semester, subjectCode, active);
    }

    @Override
    public boolean updateActiveSubject(String semester, String subjectCode, boolean active) {
        return _administration.updateActiveSubject(semester, subjectCode, active);
    }

    @Override
    public boolean deleteActiveSubject(String semester, String subjectCode) {
        return _administration.deleteActiveSubject(semester, subjectCode);
    }

    @Override
    public List<PelpMainLabSubjects> getMainSubjectOfLab(String labSubjectCode) {
        return _administration.getMainSubjectOfLab(labSubjectCode);
    }

    @Override
    public List<PelpMainLabSubjects> getLabSubjectOfMain(String mainSubjectCode) {
        return _administration.getLabSubjectOfMain(mainSubjectCode);
    }

    @Override
    public boolean addMainLabCorrespondence(String mainSubject, String labSubject) {
        return _administration.addMainLabCorrespondence(mainSubject, labSubject);
    }

    @Override
    public boolean deleteMainLabCorrespondence(String mainSubject, String labSubject) {
        return _administration.deleteMainLabCorrespondence(mainSubject, labSubject);
    }

    @Override
    public boolean addSemester(String semester, Date start, Date end) {
        Semester semesterObj=new Semester(semester,start,end);
        return _timePeriodDAO.save(semesterObj);
    }

    @Override
    public boolean updateSemester(String semester, Date start, Date end) {
        Semester semesterObj=new Semester(semester,start,end);
        return _timePeriodDAO.update(semesterObj);
    }

    @Override
    public boolean removeSemester(String semester) {
        Semester semesterObj=new Semester(semester);
        return _timePeriodDAO.delete(semesterObj);
    }
}
