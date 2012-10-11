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
package edu.uoc.pelp.test.engine.delivery;

import edu.uoc.pelp.engine.activity.ActivityID;
import edu.uoc.pelp.engine.deliver.ActivityTestResult;
import edu.uoc.pelp.engine.activity.TestID;
import edu.uoc.pelp.engine.aem.AnalysisResults;
import edu.uoc.pelp.engine.aem.TestResult;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import edu.uoc.pelp.engine.campus.UOC.UserID;
import edu.uoc.pelp.engine.deliver.*;
import edu.uoc.pelp.engine.deliver.DeliverFile.FileType;
import edu.uoc.pelp.test.TestPeLP;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * Perform all tests over the Local implementation for the deliver manager object
 * @author Xavier Baró
 */
public class LocalDeliverManager_Basic {
    
    private IDeliverManager _deliverManager=null;
    private File _tmpPath=null;
    
    private void initDeliverManager() {
        _deliverManager=(IDeliverManager) new LocalDeliverManager();
    }
    
    public LocalDeliverManager_Basic() {
        // Create a temporal folder
        _tmpPath=createTemporalFolder("TestCodeProject");
        
        // 
        initDeliverManager();
    }
    
    @Test 
    public void deliveryTestAdd() {
        
        // Create a new manager object to avoid old changes
        initDeliverManager();
        
        // Create identifiers
        UserID userID1=new UserID("123456");
        UserID userID2=new UserID("789101");
        SubjectID subjectID=new SubjectID("05.062",new Semester("20111"));
        ActivityID activityID1=new ActivityID(subjectID,1);
        ActivityID activityID2=new ActivityID(subjectID,2);
        
        // Add new delivers
        Deliver deliver=new Deliver(new File(_tmpPath.getAbsolutePath() + File.separator + "1"));
        Assert.assertNull("Initial identifier is null", deliver.getID());
        DeliverID deliver1Id=_deliverManager.addDeliver(userID1, activityID1, deliver);
        Assert.assertEquals("Activity is stored",deliver1Id.activity,activityID1);
        Assert.assertEquals("User is stored",deliver1Id.user,userID1);
        Assert.assertEquals("Index is generated",deliver1Id.index,1);
        Deliver deliver1Obj=_deliverManager.getDeliver(deliver1Id);
        Assert.assertEquals("Root path is stored",deliver1Obj.getRootPath(), new File(_tmpPath.getAbsolutePath() + File.separator + "1"));
        
        // Add again the deliver
        DeliverID deliver2Id=_deliverManager.addDeliver(userID1, activityID1, deliver);
        
        Assert.assertEquals("Old activity is not modifyed",deliver1Id.activity,activityID1);
        Assert.assertEquals("Old user is not modifyed",deliver1Id.user,userID1);
        Assert.assertEquals("Index is not altered",deliver1Id.index,1);
        Deliver deliver2Obj=_deliverManager.getDeliver(deliver2Id);
        Assert.assertEquals("Old Root path is not modifyed",deliver2Obj.getRootPath(), new File(_tmpPath.getAbsolutePath() + File.separator + "1"));
       
        Assert.assertEquals("Activity is stored",deliver2Id.activity,activityID1);
        Assert.assertEquals("User is stored",deliver2Id.user,userID1);
        Assert.assertEquals("Index is generated",deliver2Id.index,2);
        Deliver deliver2Obj2=_deliverManager.getDeliver(deliver2Id);
        Assert.assertEquals("Root path is stored",deliver2Obj2.getRootPath(), new File(_tmpPath.getAbsolutePath() + File.separator + "1"));
        
        // Change the root path
        deliver2Obj2.setRootPath(new File(_tmpPath.getAbsolutePath() + File.separator + "2"));
        Assert.assertEquals("Old Root path is not modifyed",deliver2Obj.getRootPath(), new File(_tmpPath.getAbsolutePath() + File.separator + "1"));
        Assert.assertEquals("Old Root path is modifyed",deliver2Obj2.getRootPath(), new File(_tmpPath.getAbsolutePath() + File.separator + "2"));   
        
        // Add a deliver for another activity
        deliver=new Deliver(new File(_tmpPath.getAbsolutePath() + File.separator + "3"));
        Assert.assertNull("Initial identifier is null", deliver.getID());
        DeliverID deliver3Id=_deliverManager.addDeliver(userID1, activityID2, deliver);
        Assert.assertEquals("Activity is stored",deliver3Id.activity,activityID2);
        Assert.assertEquals("User is stored",deliver3Id.user,userID1);
        Assert.assertEquals("Index is generated",deliver3Id.index,1);
        
        // Add a deliver for another user
        deliver=new Deliver(new File(_tmpPath.getAbsolutePath() + File.separator + "4"));
        Assert.assertNull("Initial identifier is null", deliver.getID());
        DeliverID deliver4Id=_deliverManager.addDeliver(userID2, activityID1, deliver);
        Assert.assertEquals("Activity is stored",deliver4Id.activity,activityID1);
        Assert.assertEquals("User is stored",deliver4Id.user,userID2);
        Assert.assertEquals("Index is generated",deliver4Id.index,1);
    }

    @Test 
    public void deliveryTestEdit() {
        
        // Create a new manager object to avoid old changes
        initDeliverManager();
        
        // Create identifiers
        UserID userID=new UserID("123456");
        SubjectID subjectID=new SubjectID("05.062",new Semester("20111"));
        ActivityID activityID1=new ActivityID(subjectID,1);
        
        // Create root paths
        File rootPath1=new File(_tmpPath.getAbsolutePath() + File.separator + "1");
        File rootPath2=new File(_tmpPath.getAbsolutePath() + File.separator + "2");
        
        // Add new deliver
        Deliver deliver=new Deliver(rootPath1);
        DeliverID deliver1Id=_deliverManager.addDeliver(userID, activityID1, deliver);
        Deliver deliver1Obj=_deliverManager.getDeliver(deliver1Id);
                
        // Modification of not added delivers is not possible
        Assert.assertFalse("Cannot modify not inserted delivers",_deliverManager.editDeliver(deliver));
        
        // Modify the root path
        deliver1Obj.setRootPath(rootPath2);
        Deliver deliver1b=_deliverManager.getDeliver(deliver1Id);
        
        // Check that no changes are made in the stored object
        Assert.assertEquals("Modification to object does not affect stored version",deliver1Obj.getRootPath(),rootPath2);
        Assert.assertEquals("Modification to object does not affect stored version",deliver1b.getRootPath(),rootPath1);

        // Change stored version
        Assert.assertTrue("Edition is done",_deliverManager.editDeliver(deliver1Obj));
        Deliver deliver1b2=_deliverManager.getDeliver(deliver1Id);
        Assert.assertEquals("Modification to object is done",deliver1b2.getRootPath(),rootPath2);
        Assert.assertEquals("Modification to object is done",deliver1b.getRootPath(),rootPath1);
    }
    
    @Test 
    public void deliveryTestAddExistingFiles() {
        // Create a new manager object to avoid old changes
        initDeliverManager();
        
        // Create identifiers
        UserID userID=new UserID("123456");
        SubjectID subjectID=new SubjectID("05.062",new Semester("20111"));
        ActivityID activityID=new ActivityID(subjectID,1);
        
        // Create root path
        File rootPath=new File(_tmpPath.getAbsolutePath() + File.separator + "1");
        if(!rootPath.exists()) {
            Assert.assertTrue("Create root path",rootPath.mkdirs());
        }
               
        // Create dummy files
        DeliverFile file1=new DeliverFile(new File("file1.txt"),FileType.Code);        
        Assert.assertTrue("Create dummy file",createFile(file1.getAbsolutePath(rootPath),"test file 1"));
        DeliverFile file2=new DeliverFile(new File("file2.txt"),FileType.Report);
        Assert.assertTrue("Create dummy file",createFile(file2.getAbsolutePath(rootPath),"test file 2"));
        DeliverFile file3=new DeliverFile(new File("file3.txt"),FileType.Report);
        Assert.assertTrue("Create dummy file",createFile(file3.getAbsolutePath(rootPath),"test file 3"));
       
        // Create the deliver
        Deliver deliver=new Deliver(rootPath);
        
        // Add files to the deliver
        Assert.assertTrue("Add file",deliver.addFile(file1));
        Assert.assertTrue("Add file",deliver.addFile(file3));
        Assert.assertTrue("Add file",deliver.addFile(file2));
        
        // Add the deliver
        DeliverID deliver1Id=_deliverManager.addDeliver(userID, activityID, deliver);
        Assert.assertEquals("Activity is stored",deliver1Id.activity,activityID);
        Assert.assertEquals("User is stored",deliver1Id.user,userID);
        Assert.assertEquals("Index is generated",deliver1Id.index,1);
        Deliver deliver1Obj=_deliverManager.getDeliver(deliver1Id);
        Assert.assertEquals("Root path is stored",deliver1Obj.getRootPath(), new File(_tmpPath.getAbsolutePath() + File.separator + "1"));
        DeliverFile[] files={file1,file2,file3};
        Assert.assertArrayEquals("Files added",files, deliver1Obj.getFiles());  
    }
    
    @Test 
    public void deliveryTestAddNonExistingFiles() {
        // Create a new manager object to avoid old changes
        initDeliverManager();
        
        // Create identifiers
        UserID userID=new UserID("123456");
        SubjectID subjectID=new SubjectID("05.062",new Semester("20111"));
        ActivityID activityID=new ActivityID(subjectID,1);
        
        // Create root path
        File rootPath=new File(_tmpPath.getAbsolutePath() + File.separator + "1");
        if(!rootPath.exists()) {
            Assert.assertTrue("Create root path",rootPath.mkdirs());
        }
               
        // Create dummy files
        DeliverFile file1=new DeliverFile(new File("file_noExist.txt"),FileType.Code);        
               
        // Create the deliver
        Deliver deliver=new Deliver(rootPath);
        
        // Add files to the deliver
        Assert.assertFalse("Add non existent file",deliver.addFile(file1));
        
        // Add the deliver
        DeliverID deliver1Id=_deliverManager.addDeliver(userID, activityID, deliver);
        Assert.assertEquals("Activity is stored",deliver1Id.activity,activityID);
        Assert.assertEquals("User is stored",deliver1Id.user,userID);
        Assert.assertEquals("Index is generated",deliver1Id.index,1);
        Deliver deliver1Obj=_deliverManager.getDeliver(deliver1Id);
        DeliverFile[] files={};
        Assert.assertArrayEquals("Files added",files, deliver1Obj.getFiles());  
    }
    
    @Test 
    public void deliveryTestAddResults() {
        
        // Create a new manager object to avoid old changes
        initDeliverManager();
        
        // Create identifiers
        UserID userID=new UserID("123456");
        SubjectID subjectID=new SubjectID("05.062",new Semester("20111"));
        ActivityID activityID=new ActivityID(subjectID,1);
                
        // Add new deliver
        Deliver deliver=new Deliver(new File(_tmpPath.getAbsolutePath() + File.separator + "1"));
        DeliverID deliverId=_deliverManager.addDeliver(userID, activityID, deliver);
        
        // Create new results
        TestResult testResults1=new TestResult();
        testResults1.setResult(true, "test 1 passed");
        
        ActivityTestResult testResults2=new ActivityTestResult();
        testResults2.setTestID(new TestID(activityID,2));
        testResults2.setResult(false, "test 2 not passed");
        
        // Add the test results
        AnalysisResults analysisResults=new AnalysisResults(null);
        Assert.assertTrue("Add results",analysisResults.addTestResult(testResults1));
        Assert.assertTrue("Add results",analysisResults.addTestResult(testResults2));
        
        // Add a repeated result
        Assert.assertFalse("Add repeated result",analysisResults.addTestResult(testResults1));
        
        // Assign the results to the deliver
        Assert.assertNull("Initial results are null", _deliverManager.getResults(deliverId));
        _deliverManager.addResults(deliverId, analysisResults);
        DeliverResults results2=_deliverManager.getResults(deliverId);
        Assert.assertEquals("Result objects are correctly stored",subjectID, results2);
        
        // Check that objects are independent
        
        
        // Check the results
        
    }
    
    private boolean createFile(File file,String content) {
        try {
            PrintWriter writer=new PrintWriter(file);
            writer.print(content);
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LocalDeliverManager_Basic.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
   
    private static File createTemporalFolder(String path) {
        // Remove extra separators
        if(path.charAt(0)==File.separatorChar) {
            path.substring(1);
        }
        
        // Create a temporal folder
        File tmpPath=new File(TestPeLP.localConfiguration.getTempPath().getPath() + "PELP" + File.separator + path);  
        if(!tmpPath.exists()) {
            junit.framework.Assert.assertTrue("Create temporal path",tmpPath.mkdirs());
        }
        junit.framework.Assert.assertTrue("Temporal folder exists",tmpPath.exists() && tmpPath.isDirectory() && tmpPath.canWrite() && tmpPath.canRead() && tmpPath.canExecute());
        tmpPath.deleteOnExit();
        
        return tmpPath;
    }
}
