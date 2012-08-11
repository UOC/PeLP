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
package edu.uoc.pelp.test;

import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.test.conf.PCPelpConfiguration;
import edu.uoc.pelp.engine.auth.EngineAuthManager;
import edu.uoc.pelp.engine.campus.*;
import edu.uoc.pelp.engine.campus.UOC.ClassroomID;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.test.engine.campus.TestUOC.LocalCampusConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Perform all tests over the PeLP platform
 * @author Xavier Baró
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({edu.uoc.pelp.test.engine.campus.TestUOC.LocalAuthManager_NotAuth.class,
                     edu.uoc.pelp.test.engine.campus.TestUOC.LocalAuthManager_Campus.class,
                     edu.uoc.pelp.test.engine.campus.TestUOC.LocalAuthManager_Student.class,
                     edu.uoc.pelp.test.aem.CodeProject_Basics.class,
                     edu.uoc.pelp.test.aem.CodeProject_Compile.class}
        )
public class TestPeLP {
    
    /**
     * Access to an LocalAuthManager object which uses the  dummy local version of campus data
     */
    public static EngineAuthManager localDummyAuthManager=new EngineAuthManager();
    
    /**
     * Access to configuration parameters for developing environments
     */
    public static IPelpConfiguration localConfiguration=new PCPelpConfiguration();

    /**
     * Access to the bussines object
     */
    public static String _bussines;
    
    public static LocalCampusConnection getLocal(ICampusConnection campusConnection) {
        assert(LocalCampusConnection.class.isAssignableFrom(campusConnection.getClass()));
        return (LocalCampusConnection)campusConnection;
    }
    
    public static Person getUser(ICampusConnection campusConnection,int pos) {
        return getLocal(campusConnection).getTestPersonByPos(pos);
    }
    
    public static Subject getSubject(ICampusConnection campusConnection,int pos) {
        return getLocal(campusConnection).getTestSubjectByPos(pos);
    }
    
    public static Classroom getClassroom(Subject subject,int posClassroom) {
        Classroom ret=null;
        IClassroomID id=new ClassroomID((SubjectID)subject.getID(),posClassroom);
        if(subject.getClassrooms()!=null) {
            ret=subject.getClassrooms().get(id);
        }
        return ret;
    }
    
    public static Classroom getClassroom(ICampusConnection campusConnection,int posSubject,int posClassroom) {
        Subject s=getSubject(campusConnection,posSubject);
        return getClassroom(s,posClassroom);
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
