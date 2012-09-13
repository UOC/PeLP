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

import edu.uoc.pelp.engine.aem.*;
import edu.uoc.pelp.exception.ExecPelpException;
import edu.uoc.pelp.test.TestPeLP;
import java.io.File;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Perform execution tests over the CodeProject data class.
 * @author Xavier Baró
 */
public class CodeProject_Execute {    
    
    private CodeProject _project=null;
    private File _tmpPath=null;    
    
    public CodeProject_Execute() {        
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
    
    @Test
    public void testCompileCOKStringHiWorld() {
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
            
            // Analyze the code
            AnalysisResults result=codeAnalyzer.analyzeProject(project,null);
            
            // Clear temporal data
            codeAnalyzer.clearData();
            
            // Check building result
            Assert.assertTrue("Compilation OK", result.getBuildResult().isCorrect());
            
            // Create some tests
            TestData[] tests={new TestData(null,"Hello World!\n"), //1
                              new TestData("Some text that will not be used","Hello World!\n"),//2
                              new TestData("","Hello World!\n"),//3
                              new TestData((String)null,null),//4
                              new TestData("",null),//5
                              new TestData("","")};//6
            boolean[] expval={true,//1
                              true,//2
                              true,//3
                              false,//4
                              false,//5
                              false};//6
            
            // Check test set
            Assert.assertEquals("Check test structures",tests.length, expval.length);
            
            // Analyze again the code
            result=codeAnalyzer.analyzeProject(project,tests);
            
            // Clear temporal data
            codeAnalyzer.clearData();
            
            // Get the list of test results
            TestResult[] testresList=result.getResults();
            
            // Check test set
            Assert.assertEquals("Check test structures",tests.length, testresList.length);
            
            for(int i=0;i<testresList.length;i++) {
                Assert.assertEquals("Check test result",expval[i], testresList[i].isPassed());
            }
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        }
    }
    
    @Test
    public void testCompileCOKStringEcho() {
        try {
            // Create a string with the code
            String code="#include <stdio.h>\n\n" +
                        "int main(int argc, char *argv[]) {\n" +
                            "\tchar buffer[32768];\n" +
                            "\tint nbytes;\n\n" +
                                "\twhile ((nbytes = fread(buffer, sizeof(char), sizeof(buffer), stdin)) > 0) {\n" +
                                    "\t\tfwrite(buffer, sizeof(char), nbytes, stdout);\n" +
                            "\t}\n" +
                            "\treturn 0;\n" +
                        "}\n";
            
            // Create the Code Project
            CodeProject project=new CodeProject("C",code);            
                        
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
            // Set the cofiguration object
            codeAnalyzer.setConfiguration(TestPeLP.localConfiguration);
            
            // Create some tests
            TestData[] tests={new TestData("Hello World!\n","Hello World!\n"), //1
                              new TestData("Some text that will be passed","Some text that will be passed"),//2
                              new TestData("text with system dependant newline \r\n and classical values \n to check what happend","text with system dependant newline \r\n and classical values \n to check what happend"),//3
                              //new TestData((String)null,null),//4 It gets idls in C
                              new TestData("fsa fsf dsf saf sa",null),//5
                              //new TestData("","") //6 It gets idle in C
                              };
            boolean[] expval={true,//1
                              true,//2
                              true,//3
                              //true,//4
                              false,//5
                              //true //6
                            };
            
            // Check test set
            Assert.assertEquals("Check test structures",tests.length, expval.length);
            
            // Analyze again the code
            AnalysisResults result=codeAnalyzer.analyzeProject(project,tests);
            
            // Clear temporal data
            codeAnalyzer.clearData();
            
            // Check building result
            Assert.assertTrue("Compilation OK", result.getBuildResult().isCorrect());
            
            // Get the list of test results
            TestResult[] testresList=result.getResults();
            
            // Check test set
            Assert.assertEquals("Check test structures",tests.length, testresList.length);
            
            for(int i=0;i<testresList.length;i++) {
                Assert.assertEquals("Check test result",expval[i], testresList[i].isPassed());
            }
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        }
    }
    
    @Test
    public void testCompileJAVAOKStringHiWorld() {
        try {
            
            // Create a string with the code
            String code="public class HelloWorld {\n" +
                            "\tpublic static void main(String[] args) {\n" +
                                "\t\tSystem.out.print(\"Hello World!\\n\");\n" +
                            "\t}\n" +
                        "}\n";
            
            // Create the Code Project
            CodeProject project=new CodeProject("JAVA",code);            
                        
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
            // Set the cofiguration object
            codeAnalyzer.setConfiguration(TestPeLP.localConfiguration);
            
            // Create some tests
            TestData[] tests={new TestData(null,"Hello World!\n"), //1
                              new TestData("Some text that will not be used","Hello World!\n"),//2
                              new TestData("","Hello World!\n"),//3
                              new TestData((String)null,null),//4
                              new TestData("",null),//5
                              new TestData("","")};//6
            boolean[] expval={true,//1
                              true,//2
                              true,//3
                              false,//4
                              false,//5
                              false};//6
            
            // Check test set
            Assert.assertEquals("Check test structures",tests.length, expval.length);
            
            // Analyze again the code
            AnalysisResults result=codeAnalyzer.analyzeProject(project,tests);
            
            // Clear temporal data
            codeAnalyzer.clearData();
            
            // Check building result
            Assert.assertTrue("Compilation OK", result.getBuildResult().isCorrect());
            
            // Get the list of test results
            TestResult[] testresList=result.getResults();
            
            // Check test set
            Assert.assertEquals("Check test structures",tests.length, testresList.length);
            
            for(int i=0;i<testresList.length;i++) {
                Assert.assertEquals("Check test result",expval[i], testresList[i].isPassed());
            }
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        }
    }
    
    @Test
    public void testCompileJAVAOKStringEcho() {
        try {
            // Create a string with the code
            String code="import java.io.*;\n\n" + 
                        "public class Echo {\n" +
                         "\npublic static void main(String[] args) throws IOException {\n" +
                            "\t\tchar buffer[]=new char[32768];\n" +
                            "\t\tint nbytes;\n\n" +
                            "\t\tBufferedReader in= new BufferedReader(new InputStreamReader(System.in),8 * 1024);\n" +
                            "\t\tBufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out),8 * 1024);\n" +
                            "\t\tif(in.ready()) {\n" +
                                "\t\t\twhile ((nbytes = in.read(buffer)) != -1) {\n" +
                                    "\t\t\t\tout.write(buffer, 0, nbytes);\n" +
                                "\t\t\t}\n" +
                                "\t\t\tout.close();\n" +
                            "\t\t}\n" +
                          "\t}\n" + 
                         "}\n";
            
            // Create the Code Project
            CodeProject project=new CodeProject("JAVA",code);            
                        
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
            // Set the cofiguration object
            codeAnalyzer.setConfiguration(TestPeLP.localConfiguration);
            
            // Create some tests
            TestData[] tests={new TestData("Hello World!\n","Hello World!\n"), //1
                              new TestData("Some text that will be passed","Some text that will be passed"),//2
                              new TestData("text with system dependant newline \r\n and classical values \n to check what happend","text with system dependant newline \r\n and classical values \n to check what happend"),//3
                              new TestData((String)null,null),//4
                              new TestData("fsa fsf dsf saf sa",null),//5
                              new TestData("","")};//6
            boolean[] expval={true,//1
                              true,//2
                              true,//3
                              true,//4
                              false,//5
                              true};//6
            
            // Check test set
            Assert.assertEquals("Check test structures",tests.length, expval.length);
            
            // Analyze again the code
            AnalysisResults result=codeAnalyzer.analyzeProject(project,tests);
            
            // Clear temporal data
            codeAnalyzer.clearData();
            
            // Check building result
            Assert.assertTrue("Compilation OK", result.getBuildResult().isCorrect());
            
            // Get the list of test results
            TestResult[] testresList=result.getResults();
            
            // Check test set
            Assert.assertEquals("Check test structures",tests.length, testresList.length);
            
            for(int i=0;i<testresList.length;i++) {
                Assert.assertEquals("Check test result",expval[i], testresList[i].isPassed());
            }
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        }
    }
    
    @Test
    public void testJAVAExecERRInfiniteBucle() {
        try {
            
            // Create a string with the code
            String code="public class InfiniteBucle {\n" +
                            "\tpublic static void main(String[] args) {\n" +
                                "\t\twhile(true) {\n" +
                                "\t\t}\n" +   
                            "\t}\n" +
                        "}\n";
            
            // Create the Code Project
            CodeProject project=new CodeProject("JAVA",code);            
                        
            // Create the analyzer
            BasicCodeAnalyzer codeAnalyzer=BasicCodeAnalyzer.getInstance(project);
            
            // Set the cofiguration object
            codeAnalyzer.setConfiguration(TestPeLP.localConfiguration);
            
            // Build the code
            BuildResult buildResult=codeAnalyzer.build(project);
            
            // Check test set
            Assert.assertTrue("Check compilation",buildResult.isCorrect());
            
            // Analyze again the code
            TestResult result=codeAnalyzer.test(new TestData("",""));
            
            // Clear temporal data
            codeAnalyzer.clearData();
            
            Assert.assertFalse("Check final exectution",result.isPassed());
            
        } catch (ExecPelpException ex) {
            Assert.fail("Compilation error");
        }
    }
}
