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
import edu.uoc.pelp.engine.campus.UOC.ClassroomID;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.engine.campus.UOC.UserID;
import edu.uoc.pelp.engine.deliver.Deliver;
import edu.uoc.pelp.engine.deliver.DeliverFile;
import edu.uoc.pelp.engine.deliver.DeliverFile.FileType;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.test.TestPeLP;
import java.io.File;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Perform all tests over the DAO object for the SemesterDO table of the database
 * @author Xavier Baró
 */
public class TDAO_Deliver {
    
    private LocalDeliverDAO _deliverDAO=new LocalDeliverDAO("hibernate_test.cfg.xml"); 
        
    @Test 
    public void testAddDeliversNoFiles() {
        
        // Create users
        UserID userID1=new UserID("111111");
        UserID userID2=new UserID("222222");
                     
        // Create subject identifiers
        SubjectID subjectID1=new SubjectID("05.062",new Semester("20121"));
        SubjectID subjectID2=new SubjectID("05.063",new Semester("20121"));
        SubjectID subjectID3=new SubjectID("05.062",new Semester("20111"));
        
        // Create classrooms identifiers
        ClassroomID classID1_1=new ClassroomID(subjectID1, 1);
        ClassroomID classID2_1=new ClassroomID(subjectID1, 2);
        ClassroomID classID2_3=new ClassroomID(subjectID3, 2);
        
        // Create activities
        ActivityID activityID1_1=new ActivityID(subjectID1,1);
        ActivityID activityID2_1=new ActivityID(subjectID1,2);
        ActivityID activityID1_2=new ActivityID(subjectID2,1);
        ActivityID activityID1_3=new ActivityID(subjectID3,1);
               
        // Remove all delivers data
        _deliverDAO.clearTableData();
        
        // Add a new deliver without files
        Deliver deliver1_1=new Deliver(new File("/test/deliver/deliver1_1"));  
        deliver1_1.setUserMainClassroom(classID1_1);
        deliver1_1.setUserLabClassroom(classID2_3);
        DeliverID deliverID1_1=_deliverDAO.add(userID1,activityID1_1, deliver1_1);  
        Assert.assertNotNull("Add a deliver without files", deliverID1_1);
        deliver1_1.setID(deliverID1_1);
        // Get all
        List<Deliver> list=_deliverDAO.findAll();
        Assert.assertEquals("Check delivers list All",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        // Filter by activities
        list=_deliverDAO.findAll(activityID1_1);
        Assert.assertEquals("Check delivers list Activity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAll(activityID2_1);
        Assert.assertEquals("Check delivers list Activity2_1",0,list.size());
        list=_deliverDAO.findAll(activityID1_2);
        Assert.assertEquals("Check delivers list Activity1_2",0,list.size());
        list=_deliverDAO.findAll(activityID1_3);
        Assert.assertEquals("Check delivers list Activity1_3",0,list.size());
        // Filter by user
        list=_deliverDAO.findAll(userID1);
        Assert.assertEquals("Check delivers list User1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAll(userID2);
        Assert.assertEquals("Check delivers list User2",0,list.size());    
        // Filter by actitivity and user
        list=_deliverDAO.findAll(activityID1_1, userID1);
        Assert.assertEquals("Check delivers list User1 and Activity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAll(activityID1_2, userID1);
        Assert.assertEquals("Check delivers list User1 and Activity1_2",0,list.size());
        list=_deliverDAO.findAll(activityID1_1, userID2);
        Assert.assertEquals("Check delivers list User2 and Activity1_1",0,list.size());
        // Filter by subject and user        
        list=_deliverDAO.findAll(subjectID1, userID1);                
        Assert.assertEquals("Check delivers list Subject1 and User1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAll(subjectID2, userID1);                
        Assert.assertEquals("Check delivers list Subject2 and User1",0,list.size());
        list=_deliverDAO.findAll(subjectID1, userID2);                
        Assert.assertEquals("Check delivers list Subject1 and User2",0,list.size());
        // Filter by classroom and activity
        list=_deliverDAO.findAllClassroom(classID1_1, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom1_1 (main) and Acivity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAllClassroom(classID2_3, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom2_3 (lab) and Acivity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAllClassroom(classID2_1, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom2_1 and Acivity1_1",0,list.size());
        list=_deliverDAO.findAllClassroom(classID2_3, activityID1_2);
        Assert.assertEquals("Check delivers list Classroom2_3 and Acivity1_2",0,list.size());
        // Last delivers by class
        list=_deliverDAO.findLastClassroom(classID1_1, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom1_1 (main) and Acivity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findLastClassroom(classID2_3, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom2_3 (lab) and Acivity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findLastClassroom(classID2_1, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom2_1 and Acivity1_1",0,list.size());
        list=_deliverDAO.findLastClassroom(classID2_3, activityID1_2);
        Assert.assertEquals("Check last delivers list Classroom2_3 and Acivity1_2",0,list.size());
        
        // Add two new delivers without files
        // Deliver of the same user to the same activity
        Deliver deliver2_1=new Deliver(new File("/test/deliver/deliver2_1"));  
        deliver2_1.setUserMainClassroom(classID1_1);
        deliver2_1.setUserLabClassroom(classID2_3);
        DeliverID deliverID2_1=_deliverDAO.add(userID1,activityID1_1, deliver2_1);  
        Assert.assertNotNull("Add another deliver for the same user without files", deliverID2_1);
        deliver2_1.setID(deliverID2_1);
        // Deliver of the another user to the same activity
        Deliver deliver3_1=new Deliver(new File("/test/deliver/deliver3_1"));  
        deliver3_1.setUserMainClassroom(classID1_1);
        deliver3_1.setUserLabClassroom(classID2_3);
        DeliverID deliverID3_1=_deliverDAO.add(userID2,activityID1_1, deliver3_1);  
        Assert.assertNotNull("Add another deliver for a diferent user without files", deliverID3_1);
        deliver3_1.setID(deliverID3_1);
        // Get all
        list=_deliverDAO.findAll();
        Assert.assertEquals("Check delivers list All",3,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(2));
        // Filter by activities
        list=_deliverDAO.findAll(activityID1_1);
        Assert.assertEquals("Check delivers list Activity1_1",3,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(2));
        list=_deliverDAO.findAll(activityID2_1);
        Assert.assertEquals("Check delivers list Activity2_1",0,list.size());
        list=_deliverDAO.findAll(activityID1_2);
        Assert.assertEquals("Check delivers list Activity1_2",0,list.size());
        list=_deliverDAO.findAll(activityID1_3);
        Assert.assertEquals("Check delivers list Activity1_3",0,list.size());
        // Filter by user
        list=_deliverDAO.findAll(userID1);
        Assert.assertEquals("Check delivers list User1",2,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        list=_deliverDAO.findAll(userID2);
        Assert.assertEquals("Check delivers list User2",1,list.size()); 
        Assert.assertEquals("Compare objects", deliver3_1, list.get(0));
        // Filter by actitivity and user
        list=_deliverDAO.findAll(activityID1_1, userID1);
        Assert.assertEquals("Check delivers list User1 and Activity1_1",2,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        list=_deliverDAO.findAll(activityID1_2, userID1);
        Assert.assertEquals("Check delivers list User1 and Activity1_2",0,list.size());
        list=_deliverDAO.findAll(activityID1_1, userID2);
        Assert.assertEquals("Check delivers list User2 and Activity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver3_1, list.get(0));
        // Filter by subject and user        
        list=_deliverDAO.findAll(subjectID1, userID1);                
        Assert.assertEquals("Check delivers list Subject1 and User1",2,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        list=_deliverDAO.findAll(subjectID2, userID1);                
        Assert.assertEquals("Check delivers list Subject2 and User1",0,list.size());
        list=_deliverDAO.findAll(subjectID1, userID2);                
        Assert.assertEquals("Check delivers list Subject1 and User2",1,list.size());
        Assert.assertEquals("Compare objects", deliver3_1, list.get(0));
        // Filter by classroom and activity
        list=_deliverDAO.findAllClassroom(classID1_1, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom1_1 (main) and Acivity1_1",3,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(2));
        list=_deliverDAO.findAllClassroom(classID2_3, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom2_3 (lab) and Acivity1_1",3,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(2));
        list=_deliverDAO.findAllClassroom(classID2_1, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom2_1 and Acivity1_1",0,list.size());
        list=_deliverDAO.findAllClassroom(classID2_3, activityID1_2);
        Assert.assertEquals("Check delivers list Classroom2_3 and Acivity1_2",0,list.size());
        // Last delivers by class
        list=_deliverDAO.findLastClassroom(classID1_1, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom1_1 (main) and Acivity1_1",2,list.size());
        Assert.assertEquals("Compare objects", deliver2_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(1));
        list=_deliverDAO.findLastClassroom(classID2_3, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom2_3 (lab) and Acivity1_1",2,list.size());
        Assert.assertEquals("Compare objects", deliver2_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(1));
        list=_deliverDAO.findLastClassroom(classID2_1, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom2_1 and Acivity1_1",0,list.size());
        list=_deliverDAO.findLastClassroom(classID2_3, activityID1_2);
        Assert.assertEquals("Check last delivers list Classroom2_3 and Acivity1_2",0,list.size());
        
        // Remove all semester data
        _deliverDAO.clearTableData();
    }
    
    @Test 
    public void testAddDeliversWithFiles() {
        
        // Create users
        UserID userID1=new UserID("111111");
        UserID userID2=new UserID("222222");
                     
        // Create subject identifiers
        SubjectID subjectID1=new SubjectID("05.062",new Semester("20121"));
        SubjectID subjectID2=new SubjectID("05.063",new Semester("20121"));
        SubjectID subjectID3=new SubjectID("05.062",new Semester("20111"));
        
        // Create classrooms identifiers
        ClassroomID classID1_1=new ClassroomID(subjectID1, 1);
        ClassroomID classID2_1=new ClassroomID(subjectID1, 2);
        ClassroomID classID1_2=new ClassroomID(subjectID2, 1);
        ClassroomID classID2_2=new ClassroomID(subjectID2, 2);
        ClassroomID classID1_3=new ClassroomID(subjectID3, 1);
        ClassroomID classID2_3=new ClassroomID(subjectID3, 2);
        
        // Create activities
        ActivityID activityID1_1=new ActivityID(subjectID1,1);
        ActivityID activityID2_1=new ActivityID(subjectID1,2);
        ActivityID activityID1_2=new ActivityID(subjectID2,1);
        ActivityID activityID1_3=new ActivityID(subjectID3,1);
        
        // Create delivers root path
        File rootPath_Del1=TestPeLP.createTemporalFolder("TDAO_Deliver" + File.separator + "deliver1_1");
        File rootPath_Del2=TestPeLP.createTemporalFolder("TDAO_Deliver" + File.separator + "deliver2_1");
        File rootPath_Del3=TestPeLP.createTemporalFolder("TDAO_Deliver" + File.separator + "deliver3_1");
        
        // Create deliver temporal files
        File newFile=new File("utils/file1_dev1_1.c");
        Assert.assertNotNull("Create temporal file 1_1",TestPeLP.createTempFile(newFile, rootPath_Del1));
        DeliverFile deliverFile1_1=new DeliverFile(newFile,FileType.Code);
        newFile=new File("file2_dev1_1.pdf");
        Assert.assertNotNull("Create temporal file 2_1",TestPeLP.createTempFile(newFile, rootPath_Del1));
        DeliverFile deliverFile2_1=new DeliverFile(newFile,FileType.Report);
        newFile=new File("ttt/file3_dev1_1.java");
        Assert.assertNotNull("Create temporal file 3_1",TestPeLP.createTempFile(newFile, rootPath_Del1));
        DeliverFile deliverFile3_1=new DeliverFile(newFile,FileType.Code);
        newFile=new File("testFile.java");
        Assert.assertNotNull("Create temporal file 4_1",TestPeLP.createTempFile(newFile, rootPath_Del1));
        DeliverFile deliverFile4_1=new DeliverFile(newFile,FileType.Code);
        deliverFile3_1.setMainProperty(true);
        
        newFile=new File("file1_dev2_1.c");
        Assert.assertNotNull("Create temporal file 1_2",TestPeLP.createTempFile(newFile, rootPath_Del2));
        DeliverFile deliverFile1_2=new DeliverFile(newFile,FileType.Code);
        newFile=new File("file2_dev2_1.pdf");
        Assert.assertNotNull("Create temporal file 2_2",TestPeLP.createTempFile(newFile, rootPath_Del2));
        DeliverFile deliverFile2_2=new DeliverFile(newFile,FileType.Report);
        deliverFile1_2.setMainProperty(true);
        
        newFile=new File("file1_dev3_1.c");
        Assert.assertNotNull("Create temporal file 1_3",TestPeLP.createTempFile(newFile, rootPath_Del3));
        DeliverFile deliverFile1_3=new DeliverFile(newFile,FileType.Code);
        newFile=new File("file2_dev3_1.pdf");
        Assert.assertNotNull("Create temporal file 2_3",TestPeLP.createTempFile(newFile, rootPath_Del3));
        DeliverFile deliverFile2_3=new DeliverFile(newFile,FileType.Report);
        newFile=new File("file3_dev3_1.java");
        Assert.assertNotNull("Create temporal file 3_3",TestPeLP.createTempFile(newFile, rootPath_Del3));
        DeliverFile deliverFile3_3=new DeliverFile(newFile,FileType.Code);
        newFile=new File("file4_dev3_1.java");
        Assert.assertNotNull("Create temporal file 4_3",TestPeLP.createTempFile(newFile, rootPath_Del3));
        DeliverFile deliverFile4_3=new DeliverFile(newFile,FileType.Code);
        newFile=new File("testFile.java");
        Assert.assertNotNull("Create temporal file 5_3",TestPeLP.createTempFile(newFile, rootPath_Del3));
        DeliverFile deliverFile5_3=new DeliverFile(newFile,FileType.Code);
        deliverFile5_3.setMainProperty(true);
        
        // Remove all delivers data
        _deliverDAO.clearTableData();
        
        // Add a new deliver without files
        Deliver deliver1_1=new Deliver(rootPath_Del1);  
        deliver1_1.setUserMainClassroom(classID1_1);
        deliver1_1.setUserLabClassroom(classID2_3);
        Assert.assertTrue("Add file 1_1",deliver1_1.addFile(deliverFile1_1));
        Assert.assertTrue("Add file 2_1",deliver1_1.addFile(deliverFile2_1));
        Assert.assertTrue("Add file 3_1",deliver1_1.addFile(deliverFile3_1));
        Assert.assertTrue("Add file 4_1",deliver1_1.addFile(deliverFile4_1));
        
        DeliverID deliverID1_1=_deliverDAO.add(userID1,activityID1_1, deliver1_1);  
        Assert.assertNotNull("Add a deliver with files", deliverID1_1);
        deliver1_1.setID(deliverID1_1);
        // Get all
        List<Deliver> list=_deliverDAO.findAll();
        Assert.assertEquals("Check delivers list All",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        // Filter by activities
        list=_deliverDAO.findAll(activityID1_1);
        Assert.assertEquals("Check delivers list Activity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAll(activityID2_1);
        Assert.assertEquals("Check delivers list Activity2_1",0,list.size());
        list=_deliverDAO.findAll(activityID1_2);
        Assert.assertEquals("Check delivers list Activity1_2",0,list.size());
        list=_deliverDAO.findAll(activityID1_3);
        Assert.assertEquals("Check delivers list Activity1_3",0,list.size());
        // Filter by user
        list=_deliverDAO.findAll(userID1);
        Assert.assertEquals("Check delivers list User1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAll(userID2);
        Assert.assertEquals("Check delivers list User2",0,list.size());    
        // Filter by actitivity and user
        list=_deliverDAO.findAll(activityID1_1, userID1);
        Assert.assertEquals("Check delivers list User1 and Activity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAll(activityID1_2, userID1);
        Assert.assertEquals("Check delivers list User1 and Activity1_2",0,list.size());
        list=_deliverDAO.findAll(activityID1_1, userID2);
        Assert.assertEquals("Check delivers list User2 and Activity1_1",0,list.size());
        // Filter by subject and user        
        list=_deliverDAO.findAll(subjectID1, userID1);                
        Assert.assertEquals("Check delivers list Subject1 and User1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAll(subjectID2, userID1);                
        Assert.assertEquals("Check delivers list Subject2 and User1",0,list.size());
        list=_deliverDAO.findAll(subjectID1, userID2);                
        Assert.assertEquals("Check delivers list Subject1 and User2",0,list.size());
        // Filter by classroom and activity
        list=_deliverDAO.findAllClassroom(classID1_1, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom1_1 (main) and Acivity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAllClassroom(classID2_3, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom2_3 (lab) and Acivity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findAllClassroom(classID2_1, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom2_1 and Acivity1_1",0,list.size());
        list=_deliverDAO.findAllClassroom(classID2_3, activityID1_2);
        Assert.assertEquals("Check delivers list Classroom2_3 and Acivity1_2",0,list.size());
        // Last delivers by class
        list=_deliverDAO.findLastClassroom(classID1_1, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom1_1 (main) and Acivity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findLastClassroom(classID2_3, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom2_3 (lab) and Acivity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        list=_deliverDAO.findLastClassroom(classID2_1, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom2_1 and Acivity1_1",0,list.size());
        list=_deliverDAO.findLastClassroom(classID2_3, activityID1_2);
        Assert.assertEquals("Check last delivers list Classroom2_3 and Acivity1_2",0,list.size());
        
        // Add two new delivers without files
        // Deliver of the same user to the same activity
        Deliver deliver2_1=new Deliver(rootPath_Del2);  
        deliver2_1.setUserMainClassroom(classID1_1);
        deliver2_1.setUserLabClassroom(classID2_3);
        Assert.assertTrue("Add file 1_2",deliver2_1.addFile(deliverFile1_2));
        Assert.assertTrue("Add file 2_2",deliver2_1.addFile(deliverFile2_2));                
        DeliverID deliverID2_1=_deliverDAO.add(userID1,activityID1_1, deliver2_1);  
        Assert.assertNotNull("Add another deliver for the same user with files", deliverID2_1);
        deliver2_1.setID(deliverID2_1);
        // Deliver of the another user to the same activity
        Deliver deliver3_1=new Deliver(rootPath_Del3);  
        deliver3_1.setUserMainClassroom(classID1_1);
        deliver3_1.setUserLabClassroom(classID2_3);
        Assert.assertTrue("Add file 1_3",deliver3_1.addFile(deliverFile1_3));
        Assert.assertTrue("Add file 2_3",deliver3_1.addFile(deliverFile2_3));
        Assert.assertTrue("Add file 3_3",deliver3_1.addFile(deliverFile3_3));
        Assert.assertTrue("Add file 4_3",deliver3_1.addFile(deliverFile4_3));
        Assert.assertTrue("Add file 5_3",deliver3_1.addFile(deliverFile5_3));
        DeliverID deliverID3_1=_deliverDAO.add(userID2,activityID1_1, deliver3_1);  
        Assert.assertNotNull("Add another deliver for a diferent user with files", deliverID3_1);
        deliver3_1.setID(deliverID3_1);
        // Get all
        list=_deliverDAO.findAll();
        Assert.assertEquals("Check delivers list All",3,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(2));
        // Filter by activities
        list=_deliverDAO.findAll(activityID1_1);
        Assert.assertEquals("Check delivers list Activity1_1",3,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(2));
        list=_deliverDAO.findAll(activityID2_1);
        Assert.assertEquals("Check delivers list Activity2_1",0,list.size());
        list=_deliverDAO.findAll(activityID1_2);
        Assert.assertEquals("Check delivers list Activity1_2",0,list.size());
        list=_deliverDAO.findAll(activityID1_3);
        Assert.assertEquals("Check delivers list Activity1_3",0,list.size());
        // Filter by user
        list=_deliverDAO.findAll(userID1);
        Assert.assertEquals("Check delivers list User1",2,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        list=_deliverDAO.findAll(userID2);
        Assert.assertEquals("Check delivers list User2",1,list.size()); 
        Assert.assertEquals("Compare objects", deliver3_1, list.get(0));
        // Filter by actitivity and user
        list=_deliverDAO.findAll(activityID1_1, userID1);
        Assert.assertEquals("Check delivers list User1 and Activity1_1",2,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        list=_deliverDAO.findAll(activityID1_2, userID1);
        Assert.assertEquals("Check delivers list User1 and Activity1_2",0,list.size());
        list=_deliverDAO.findAll(activityID1_1, userID2);
        Assert.assertEquals("Check delivers list User2 and Activity1_1",1,list.size());
        Assert.assertEquals("Compare objects", deliver3_1, list.get(0));
        // Filter by subject and user        
        list=_deliverDAO.findAll(subjectID1, userID1);                
        Assert.assertEquals("Check delivers list Subject1 and User1",2,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        list=_deliverDAO.findAll(subjectID2, userID1);                
        Assert.assertEquals("Check delivers list Subject2 and User1",0,list.size());
        list=_deliverDAO.findAll(subjectID1, userID2);                
        Assert.assertEquals("Check delivers list Subject1 and User2",1,list.size());
        Assert.assertEquals("Compare objects", deliver3_1, list.get(0));
        // Filter by classroom and activity
        list=_deliverDAO.findAllClassroom(classID1_1, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom1_1 (main) and Acivity1_1",3,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(2));
        list=_deliverDAO.findAllClassroom(classID2_3, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom2_3 (lab) and Acivity1_1",3,list.size());
        Assert.assertEquals("Compare objects", deliver1_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver2_1, list.get(1));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(2));
        list=_deliverDAO.findAllClassroom(classID2_1, activityID1_1);
        Assert.assertEquals("Check delivers list Classroom2_1 and Acivity1_1",0,list.size());
        list=_deliverDAO.findAllClassroom(classID2_3, activityID1_2);
        Assert.assertEquals("Check delivers list Classroom2_3 and Acivity1_2",0,list.size());
        // Last delivers by class
        list=_deliverDAO.findLastClassroom(classID1_1, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom1_1 (main) and Acivity1_1",2,list.size());
        Assert.assertEquals("Compare objects", deliver2_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(1));
        list=_deliverDAO.findLastClassroom(classID2_3, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom2_3 (lab) and Acivity1_1",2,list.size());
        Assert.assertEquals("Compare objects", deliver2_1, list.get(0));
        Assert.assertEquals("Compare objects", deliver3_1, list.get(1));
        list=_deliverDAO.findLastClassroom(classID2_1, activityID1_1);
        Assert.assertEquals("Check last delivers list Classroom2_1 and Acivity1_1",0,list.size());
        list=_deliverDAO.findLastClassroom(classID2_3, activityID1_2);
        Assert.assertEquals("Check last delivers list Classroom2_3 and Acivity1_2",0,list.size());
        
        // Remove all semester data
        _deliverDAO.clearTableData();
    }
   
    @Test 
    public void testUpdateDeliverData() {
        Assert.fail("TODO:Deliver update tests must be defined.");
    }
    
    @Test 
    public void testUpdateDeliverFiles() {
        Assert.fail("TODO:Deliver update tests must be defined.");
    }
    
    
}
