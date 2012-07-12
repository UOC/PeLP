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
package edu.uoc.pelp.test.aem;

import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import java.io.File;
import java.io.IOException;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Perform all tests over the CodeProject data class.
 * @author Xavier Baró
 */
public class CodeProject_Basics {    
    
    private CodeProject _project=null;
    private File _tmpPath=null;    
    
    public CodeProject_Basics() {        
        // Create a temporal folder
        _tmpPath=createTemporalFolder("TestCodeProject");
        
        // Create a new project in the new created temporal folder        
        _project=new CodeProject(_tmpPath);        
    }
    
    @Test 
    public void testAddCorrectFiles() {
        try {
            // Create 3 temporal files
            File f1=File.createTempFile("tmpCodeProjectBasicsTest", null, _tmpPath);
            f1.deleteOnExit();
            File f2=File.createTempFile("tmpCodeProjectBasicsTest", null, _tmpPath);
            f2.deleteOnExit();
            File f3=File.createTempFile("tmpCodeProjectBasicsTest", null, _tmpPath);
            f3.deleteOnExit();            
            
            // Add the tree files
            _project.addFile(f1);
            _project.addFile(f2);
            _project.addFile(f3);
            
            // Check that the added files are in the project
            File[] relFiles=_project.getRelativeFiles();
            Assert.assertTrue("All files are added", relFiles.length==3);
            
            // Add a repeated file
            _project.addFile(f1);
            
            // Check that the last added files is not in the list
            relFiles=_project.getRelativeFiles();
            Assert.assertTrue("Repeated files are not added", relFiles.length==3);
            
            // Add a non existing main file
            File f4=File.createTempFile("tmpCodeProjectBasicsTest", null, _tmpPath);
            f4.deleteOnExit();            
            _project.addMainFile(f4);
            Assert.assertEquals("Check the main file", f4.getName(), _project.getMainFile().getName());
            relFiles=_project.getRelativeFiles();
            Assert.assertTrue("Repeated files are not added", relFiles.length==4);
                        
            // Add an existing main file
            _project.addMainFile(f2);
            Assert.assertEquals("Check the main file", f2.getName(), _project.getMainFile().getName());
            relFiles=_project.getRelativeFiles();
            Assert.assertTrue("Repeated files are not added", relFiles.length==4);
            
        } catch (ExecPelpException ex) {
            Assert.fail("Cannot create the file");
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }
    
    @Test(expected=ExecPelpException.class)
    public void testAddIncorrectFiles1() throws ExecPelpException {        
        try {
            // Create 2 temporal files
            File f1=File.createTempFile("tmpCodeProjectBasicsTest", null, _tmpPath);            
            f1.deleteOnExit();
            File f2=File.createTempFile("tmpCodeProjectBasicsTest", null, _tmpPath);            
            
            // Delete the temporal files                        
            Assert.assertTrue("Delete temporal file", f2.delete());
            
            // Add a file
            _project.addFile(f1);
            
            // Add inexistent files
            _project.addFile(f2);            
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }
    
    @Test(expected=ExecPelpException.class)
    public void testAddIncorrectFiles2() throws ExecPelpException {        
        try {
            // Create a temporal file outside the root path
            File f1=File.createTempFile("tmpCodeProjectBasicsTest", null, null); 
            f1.deleteOnExit();
            
            // Add a new temporal file outside the temporal path
            _project.addFile(f1);
        
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }
    
    @Test(expected=ExecPelpException.class)
    public void testAddIncorrectFiles3() throws ExecPelpException {        
        try {
            // Create a temporal file outside the root path
            File f1=File.createTempFile("tmpCodeProjectBasicsTest", null, null); 
            f1.deleteOnExit();
            
            // Add a new temporal file outside the temporal path
            _project.addMainFile(f1);
        
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }
    
    @Test
    public void testMoveOkFiles() throws ExecPelpException, AuthPelpException {        
        try {
            // Create 3 temporal files
            File f1=File.createTempFile("tmpCodeProjectBasicsTest", null, _tmpPath);
            f1.deleteOnExit();
            File f2=File.createTempFile("tmpCodeProjectBasicsTest", null, _tmpPath);
            f2.deleteOnExit();
            File f3=File.createTempFile("tmpCodeProjectBasicsTest", null, _tmpPath);
            f3.deleteOnExit();            
            
            // Add the tree files
            _project.addFile(f1);
            _project.addFile(f2);
            _project.addFile(f3);
            
            // Check that the added files are in the project
            File[] relFiles=_project.getRelativeFiles();
            Assert.assertTrue("All files are added", relFiles.length==3);
            
            // Create a temporal folder
            File newPathFile=createTemporalFolder("DestFolder");
            
            // Move to other valid temporal folder
            File[] absFiles=_project.getAbsoluteFiles();
            _project.moveFiles(newPathFile);            
            for(File f:absFiles) {
                Assert.assertFalse("Old file does not exist.",f.exists());
            }
            absFiles=_project.getAbsoluteFiles();
            for(File f:absFiles) {
                Assert.assertTrue("New file exists.",f.exists());
            }
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }

    private File createTemporalFolder(String path) {
        // Remove extra separators
        if(path.charAt(0)==File.separatorChar) {
            path.substring(1);
        }
        
        // Create a temporal folder
        File tmpPath=new File(System.getProperty("java.io.tmpdir")+ "PELP" + File.separator + path);  
        if(!tmpPath.exists()) {
            Assert.assertTrue("Create temporal path",tmpPath.mkdirs());
        }
        Assert.assertTrue("Temporal folder exists",tmpPath.exists() && tmpPath.isDirectory() && tmpPath.canWrite() && tmpPath.canRead() && tmpPath.canExecute());
        tmpPath.deleteOnExit();
        
        return tmpPath;
    }
}
