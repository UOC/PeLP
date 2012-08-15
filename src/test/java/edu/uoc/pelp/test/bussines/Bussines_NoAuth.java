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
package edu.uoc.pelp.test.bussines;

import edu.uoc.pelp.engine.aem.BasicCodeAnalyzer;
import edu.uoc.pelp.engine.aem.BuildResult;
import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.test.TestPeLP;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Perform tests over the bussines class without authentication.
 * @author Xavier Baró
 */
public class Bussines_NoAuth {    
    
    private CodeProject _project=null;
    private File _tmpPath=null;    
    
    public Bussines_NoAuth() {        
        // Create a temporal folder
        _tmpPath=createTemporalFolder("TestCodeProject");
        
        // Create a new project in the new created temporal folder        
        _project=new CodeProject(_tmpPath);   
    }
    
    private static File createTemporalFolder(String path) {
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
    
    /*
    @Test
    public void testExecuteJavaOK() {
        try {
            // Create a temporal folder for code
            File tmpDir=createTemporalFolder("TestCode");
            
            // Create a code file
            File srcFile=new File(tmpDir.getAbsolutePath() + File.separator + "Test1.java");            
            PrintWriter srcFileWriter=new PrintWriter(new FileOutputStream(srcFile));
            
            // Add the code
            srcFileWriter.printf("public class Test1 {\n");
            srcFileWriter.printf("\tpublic static void main(String[] args) {\n");
            srcFileWriter.printf("\t\tSystem.out.println(\"Hello World!\\n\");\n");
            srcFileWriter.printf("\t}\n");
            srcFileWriter.printf("}\n");
            
            // Close the source file
            srcFileWriter.close();
            
            // Create the Code Project
            _project.changeRootPath(tmpDir);
            _project.addFile(srcFile);
            
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(_project);
            
            // Compile the code
            BuildResult result=codeAnalyzer.build(_project);
            
            // Check result
            Assert.assertTrue("Compilation OK", result.isCorrect());
                 
            // Remove input file
            srcFile.delete();    
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }
    */
    
    /*
    @Test
    public void testExecuteCOK() {
        try {
            // Create a temporal folder for code
            File tmpDir=createTemporalFolder("TestCode");
            
            // Create a code file
            File srcFile=new File(tmpDir.getAbsolutePath() + File.separator + "test1.c");            
            PrintWriter srcFileWriter=new PrintWriter(new FileOutputStream(srcFile));
            
            // Add the code
            srcFileWriter.printf("\tint main(void) {\n");
            srcFileWriter.printf("\t\tprintf(\"Hello World!\\n\");\n");
            srcFileWriter.printf("\t}\n");
            
            // Close the source file
            srcFileWriter.close();
            
            // Create the Code Project
            _project.changeRootPath(tmpDir);
            _project.addFile(srcFile);
            
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(_project);
            
            // Set the cofiguration object
            codeAnalyzer.setConfiguration(TestPeLP.localConfiguration);
            
            // Compile the code
            BuildResult result=codeAnalyzer.build(_project);
            
            // Check result
            Assert.assertTrue("Compilation OK", result.isCorrect());
                 
            // Remove input file
            srcFile.delete();    
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }
    
    */
}
