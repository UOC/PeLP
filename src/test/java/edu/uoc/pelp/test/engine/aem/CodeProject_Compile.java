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
package edu.uoc.pelp.test.engine.aem;

import edu.uoc.pelp.engine.aem.BasicCodeAnalyzer;
import edu.uoc.pelp.engine.aem.BuildResult;
import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.engine.aem.exception.LanguageAEMPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.test.TestPeLP;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Perform compilation tests over the CodeProject data class.
 * @author Xavier Baró
 */
public class CodeProject_Compile {    

    private File _tmpPath=null;    
    
    public CodeProject_Compile() {        
        // Create a temporal folder
        _tmpPath=createTemporalFolder("TestCodeProject");  
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
    
    @Test
    public void testCompileInfoJava() {
            
        // Create the code
        String code="public class Test1 {\n" +
                        "\tpublic static void main(String[] args) {\n" +
                            "\t\tSystem.out.println(\"Hello World!\\n\");\n" +
                        "\t}\n" +
                    "}\n";
            
        // Create the Code Project
        CodeProject project=new CodeProject("JAVA",code);
        
        try {
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            Assert.assertNotNull("Default information", codeAnalyzer.getSystemInfo());
            
            // Set the cofiguration object
            codeAnalyzer.setConfiguration(TestPeLP.localConfiguration);
            Assert.assertNotNull("Information after adding local configuration", codeAnalyzer.getSystemInfo());
        } catch (LanguageAEMPelpException ex) {
            Assert.fail("Cannot create Java compiler");
        }
    }
    
    @Test
    public void testCompileInfoC() {
            
        // Create the code
        String code="int main(void) {\n" +
                        "\tprintf(\"Hello World!\\n\");\n" + 
                    "}\n";
            
        // Create the Code Project
        CodeProject project=new CodeProject("C",code);
        
        try {
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            Assert.assertNull("Default information", codeAnalyzer.getSystemInfo());
            
            // Set the cofiguration object
            codeAnalyzer.setConfiguration(TestPeLP.localConfiguration);
            Assert.assertNotNull("Information after adding local configuration", codeAnalyzer.getSystemInfo());
        } catch (LanguageAEMPelpException ex) {
            Assert.fail("Cannot create Java compiler");
        }
    }
    
    @Test(expected=ExecPelpException.class)
    public void testCompileJavaFailPath() throws ExecPelpException {
        File srcFile=null;
        try {
            // Create a temporal folder for code
            File tmpDir=createTemporalFolder("TestCode");
            
            // Create a code file
            srcFile=new File(tmpDir.getAbsolutePath() + File.separator + "Test1.java");            
            PrintWriter srcFileWriter=new PrintWriter(new FileOutputStream(srcFile));
            
            // Add the code
            srcFileWriter.printf("public class Test1 {\n");
            srcFileWriter.printf("\tpublic static void main(String[] args) {\n");
            srcFileWriter.printf("\t\tSystem.out.println(\"Hello World!\\n\");\n");
            srcFileWriter.printf("\t}\n");
            srcFileWriter.printf("}\n");
            
            // Close the source file
            srcFileWriter.close();
            
            // Create the Code Project with a file outside the path
            CodeProject project=new CodeProject(_tmpPath);
            project.addFile(srcFile);
            
            // Remove input file
            srcFile.delete(); 
            
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
            
            // Remove input file
            srcFile.delete();    
        }
    }
    
    @Test
    public void testCompileJavaFAILCompilation() {
        try {
            // Create a temporal folder for code
            File tmpDir=createTemporalFolder("TestCode");
            
            // Create a code file
            File srcFile=new File(tmpDir.getAbsolutePath() + File.separator + "Test1.java");            
            PrintWriter srcFileWriter=new PrintWriter(new FileOutputStream(srcFile));
            
            // Add the code
            srcFileWriter.printf("public class Test1 {\n");
            srcFileWriter.printf("\tpublic static void main(String[] args) {\n");
            srcFileWriter.printf("\t\tSystem.out.println(\"Hello World!\\n\"\n"); // => ERROR IN THIS LINE
            srcFileWriter.printf("\t}\n");
            srcFileWriter.printf("}\n");
            
            // Close the source file
            srcFileWriter.close();
            
            // Create the Code Project
            CodeProject project=new CodeProject(tmpDir);
            project.addFile(srcFile);
            
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
            // Compile the code
            BuildResult result=codeAnalyzer.build(project);
            
            // Check result
            Assert.assertFalse("Compilation Fails", result.isCorrect());
            
            // Remove input file
            srcFile.delete();  
            
            // Clear temporal data
            codeAnalyzer.clearData();
            
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }
    
    @Test
    public void testCompileJavaOKFiles() {
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
            CodeProject project=new CodeProject(tmpDir);
            project.addFile(srcFile);
            
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
            // Compile the code
            BuildResult result=codeAnalyzer.build(project);
            
            // Check result
            Assert.assertTrue("Compilation OK", result.isCorrect());
                 
            // Remove input file
            srcFile.delete();    
            
            // Clear temporal data
            codeAnalyzer.clearData();
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }
    
    @Test
    public void testCompileJavaOKString() {
        try {
            
            // Create the code
            String code="public class Test1 {\n" +
                            "\tpublic static void main(String[] args) {\n" +
                                "\t\tSystem.out.println(\"Hello World!\\n\");\n" +
                            "\t}\n" +
                        "}\n";
            
            // Create the Code Project
            CodeProject project=new CodeProject("JAVA",code);
                       
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
            // Compile the code
            BuildResult result=codeAnalyzer.build(project);
            
            // Check result
            Assert.assertTrue("Compilation OK", result.isCorrect());
            
            // Clear temporal data
            codeAnalyzer.clearData();
                  
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        }
    }
    
    @Test(expected=ExecPelpException.class)
    public void testCompileCFailPath() throws ExecPelpException {
        File srcFile=null;
        try {
            // Create a temporal folder for code
            File tmpDir=createTemporalFolder("TestCode");
            
            // Create a code file
            srcFile=new File(tmpDir.getAbsolutePath() + File.separator + "test1.c");            
            PrintWriter srcFileWriter=new PrintWriter(new FileOutputStream(srcFile));
            
            // Add the code
            srcFileWriter.printf("\tint main(void) {\n");
            srcFileWriter.printf("\t\tprintf(\"Hello World!\\n\");\n");
            srcFileWriter.printf("\t}\n");
            
            // Close the source file
            srcFileWriter.close();
            
            // Create the Code Project with a file outside the path
            CodeProject project=new CodeProject(_tmpPath);
            project.addFile(srcFile);
            
            // Remove input file
            srcFile.delete(); 
            
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
            
            // Remove input file
            srcFile.delete();    
        }
    }
    
    @Test
    public void testCompileCFAILCompilation() {
        try {
            // Create a temporal folder for code
            File tmpDir=createTemporalFolder("TestCode");
            
            // Create a code file
            File srcFile=new File(tmpDir.getAbsolutePath() + File.separator + "test1.c");            
            PrintWriter srcFileWriter=new PrintWriter(new FileOutputStream(srcFile));
            
            // Add the code
            srcFileWriter.printf("\tint main(void) {\n");
            srcFileWriter.printf("\t\tprintf(\"Hello World!\\n\"\n"); // ==> ERROR in this line
            srcFileWriter.printf("\t}\n");
            
            // Close the source file
            srcFileWriter.close();
            
            // Create the Code Project
            CodeProject project=new CodeProject(tmpDir);
            project.addFile(srcFile);
            
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
            // Set the cofiguration object
            codeAnalyzer.setConfiguration(TestPeLP.localConfiguration);
            
            // Compile the code
            BuildResult result=codeAnalyzer.build(project);
            
            // Check result
            Assert.assertFalse("Compilation Fails", result.isCorrect());
            
            // Remove input file
            srcFile.delete();            
            
            // Clear temporal data
            codeAnalyzer.clearData();
            
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }
    
    @Test
    public void testCompileCOKFile() {
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
            CodeProject project=new CodeProject(tmpDir);            
            project.addFile(srcFile);
            
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
            // Set the cofiguration object
            codeAnalyzer.setConfiguration(TestPeLP.localConfiguration);
            
            // Compile the code
            BuildResult result=codeAnalyzer.build(project);
            
            // Check result
            Assert.assertTrue("Compilation OK", result.isCorrect());
                 
            // Remove input file
            srcFile.delete();    
            
            // Clear temporal data
            codeAnalyzer.clearData();
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        } catch (IOException ex) {
            Assert.fail("Cannot create the temporal file");
        }
    }
    
    @Test
    public void testCompileCOKString() {
        try {
            
            // Create a string with the code
            String code="int main(void) {\n" +
                            "\tprintf(\"Hello World!\\n\");\n" + 
                            "}\n";
            
            // Create the Code Project
            CodeProject project=new CodeProject("C",code);            
                        
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
            // Set the cofiguration object
            codeAnalyzer.setConfiguration(TestPeLP.localConfiguration);
            
            // Compile the code
            BuildResult result=codeAnalyzer.build(project);
            
            // Check result
            Assert.assertTrue("Compilation OK", result.isCorrect());
            
            // Clear temporal data
            codeAnalyzer.clearData();
   
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        }
    }
}
