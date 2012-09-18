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
package edu.uoc.pelp.test.engine;

import edu.uoc.pelp.engine.PELPEngine;
import edu.uoc.pelp.engine.activity.Activity;
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.engine.aem.exception.AEMPelpException;
import edu.uoc.pelp.engine.campus.Classroom;
import edu.uoc.pelp.engine.campus.Person;
import edu.uoc.pelp.engine.campus.Subject;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverResults;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.exception.InvalidActivityPelpException;
import edu.uoc.pelp.test.TestPeLP;
import java.io.File;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Perform information retrieval tests over the engine.
 * @author Xavier Baró
 */
public class Engine_InfoRetrival {    
    
    private PELPEngine _engine=null;
    private File _tmpPath=null;    
    
    public Engine_InfoRetrival() {        
        // Obtain the engine to test
        _engine= TestPeLP.getEngineObject();
    }
    
    @Test(expected=AuthPelpException.class) //1
    public void noAuthAccessGetUserInfo() throws AuthPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        Person person=_engine.getUserInfo();
    }
    
    @Test(expected=AuthPelpException.class) //2
    public void noAuthAccessGetActiveSubjects() throws AuthPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        Subject[] subjects=_engine.getActiveSubjects();
    }
    
    @Test(expected=AuthPelpException.class) //3
    public void noAuthAccessGetSubjectClassrooms() throws AuthPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        Classroom[] classes=_engine.getSubjectClassrooms(TestPeLP.getDummySubjectID());
    }
    
    @Test(expected=AuthPelpException.class) //4.1
    public void noAuthAccessGetSubjectActivityOnlyActive() throws AuthPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        Activity[] classes=_engine.getSubjectActivity(TestPeLP.getDummySubjectID(),true);
    }
    
    @Test(expected=AuthPelpException.class) //4.2
    public void noAuthAccessGetSubjectActivityAll() throws AuthPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        Activity[] classes=_engine.getSubjectActivity(TestPeLP.getDummySubjectID(),false);
    }
        
    @Test(expected=AuthPelpException.class) // 5
    public void noAuthAccessGetActivityDelivers() throws AuthPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        Deliver[] delivers=_engine.getActivityDelivers(TestPeLP.getDummyUserID(),TestPeLP.getDummyActivityID(null));
    }
    
    @Test(expected=AuthPelpException.class) // 6
    public void noAuthAccessGetDeliverResults() throws AuthPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        DeliverResults deliverResults=_engine.getDeliverResults(TestPeLP.getDummyDeliverID());
    }
    
    @Test(expected=AuthPelpException.class) // 7
    public void noAuthAccessGetTestInformation() throws AuthPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        edu.uoc.pelp.engine.activity.ActivityTest test=_engine.getTestInformation(TestPeLP.getDummyTestID());
    }
    
    @Test(expected=AuthPelpException.class) // 8
    public void noAuthAccessCreateNewDeliver() throws AuthPelpException, InvalidActivityPelpException, ExecPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        DeliverResults deliverResults=_engine.createNewDeliver(TestPeLP.getDummyDeliver(),TestPeLP.getDummyActivityID(null));
    }
    
    @Test(expected=AEMPelpException.class) // 9.1
    public void noAuthAccessAnalyzeCodeInvalidProject1() throws AEMPelpException {
        CodeProject project=null;
        
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        
        try {
            // Project without files and string code, obtained from an empty deliver
            project=TestPeLP.getDummyDeliver().getCodeProject();
        } catch (ExecPelpException ex) {
            Assert.fail("Error creating the code project.");
        }
        
        // Call analyzer
        Assert.assertNotNull("Null CodeProject",project);
        AnalysisResults deliverResults=_engine.analyzeCode(project,null);
    }
    
    @Test(expected=AEMPelpException.class) // 9.2
    public void noAuthAccessAnalyzeCodeInvalidProject2() throws AEMPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
            
        // Project with out files and string code, obtained from an empty deliver
        File errPath=new File(TestPeLP.createTemporalFolder("TESTS").getPath() + File.separator + "noExistDir");
        Assert.assertFalse("Build incorrect path",errPath.exists());
        CodeProject project=new CodeProject(errPath);
            
        // Call analyzer
        Assert.assertNotNull("Null CodeProject",project);
        AnalysisResults deliverResults=_engine.analyzeCode(project,null);
    }
    
    @Test(expected=AuthPelpException.class) // 10
    public void noAuthAccessIsTeacher() throws AuthPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        _engine.isTeacher(TestPeLP.getDummySubjectID());
    }
    
    @Test(expected=AuthPelpException.class) // 11
    public void noAuthAccessIsStudent() throws AuthPelpException {
        Assert.assertFalse("Test authentication", _engine.isUserAuthenticated());
        _engine.isStudent(new SubjectID("05.554",new Semester("20111")));
    }
}
