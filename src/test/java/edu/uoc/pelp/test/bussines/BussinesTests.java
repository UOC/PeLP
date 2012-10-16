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

import edu.uoc.pelp.bussines.vo.MultilingualText;
import edu.uoc.pelp.bussines.vo.MultilingualTextArray;
import edu.uoc.pelp.bussines.vo.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Set of tests for PeLP bussines objects access
 * @author Xavier Baró
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({edu.uoc.pelp.test.bussines.Bussines_NormalWorkflow.class})
public class BussinesTests {
    
    public static String getCode_Echo(String language) {
        // Create a string with the code
        String codeC="#include <stdio.h>\n\n" +
                    "int main(int argc, char *argv[]) {\n" +
                        "\tchar buffer[32768];\n" +
                        "\tint nbytes;\n\n" +
                            "\twhile ((nbytes = fread(buffer, sizeof(char), sizeof(buffer), stdin)) > 0) {\n" +
                                "\t\tfwrite(buffer, sizeof(char), nbytes, stdout);\n" +
                        "\t}\n" +
                        "\treturn 0;\n" +
                    "}\n";
        String codeJAVA="import java.io.*;\n\n" + 
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
        if("JAVA".equalsIgnoreCase(language)) {
            return codeJAVA;
        } else if("C".equalsIgnoreCase(language)) {
            return codeC;
        }
        return null;
    }
    
    public static String getCodeFilename_Echo(String language) {
        if("JAVA".equalsIgnoreCase(language)) {
            return "Echo.java";
        } else if("C".equalsIgnoreCase(language)) {
            return "echo.c";
        }
        return null;
    }
    public static Test[] getTests_Echo() {
        // Create some tests
        Test[] tests={new Test("Hello World!\n","Hello World!\n",true), //1
                    new Test("Some text that will be passed","Some text that will be passed",true),//2
                    new Test("text with system dependant newline \r\n and classical values \n to check what happend","text with system dependant newline \r\n and classical values \n to check what happend",false),//3
                    new Test("test to be failed","",false),//4
        };
        return tests;
    }
    
    public static MultilingualTextArray[] getTestDescriptions_Echo() {
        MultilingualTextArray test1Descriptions=new MultilingualTextArray(3);
            test1Descriptions.setText(0,new MultilingualText("CAT","Test1: Hola mon)"));
            test1Descriptions.setText(1,new MultilingualText("ESP","Test1: Hola mundo"));
            test1Descriptions.setText(2,new MultilingualText("ENG","Test1: Hello world"));
            
            MultilingualTextArray test2Descriptions=new MultilingualTextArray(3);
            test2Descriptions.setText(0,new MultilingualText("CAT","Test2: Text aleatori"));
            test2Descriptions.setText(1,new MultilingualText("ESP","Test2: Texto aleatorio"));
            test2Descriptions.setText(2,new MultilingualText("ENG","Test2: Random test"));
            
            MultilingualTextArray test3Descriptions=new MultilingualTextArray(3);
            test3Descriptions.setText(0,new MultilingualText("CAT","Test3: Text amb simbols de salt de línea depenents de sistema"));
            test3Descriptions.setText(1,new MultilingualText("ESP","Test3: Texto con simbolos de salto de línea dependientes de sistema"));
            test3Descriptions.setText(2,new MultilingualText("ENG","Test3: Text with system dependant newline"));
            
            MultilingualTextArray test4Descriptions=new MultilingualTextArray(3);
            test4Descriptions.setText(0,new MultilingualText("CAT","Test4: Prova per ser fallat"));
            test4Descriptions.setText(1,new MultilingualText("ESP","Test4: Prueba para ser fallada"));
            test4Descriptions.setText(2,new MultilingualText("ENG","Test4: Test to be failed"));
            
            MultilingualTextArray[] testDescriptions=new MultilingualTextArray[4];
            testDescriptions[0]=test1Descriptions;
            testDescriptions[1]=test2Descriptions;
            testDescriptions[2]=test3Descriptions;
            testDescriptions[3]=test4Descriptions;
            
            return testDescriptions;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
}
