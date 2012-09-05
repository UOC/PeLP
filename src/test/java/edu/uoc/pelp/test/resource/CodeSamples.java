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
package edu.uoc.pelp.test.resource;

import edu.uoc.pelp.engine.aem.TestData;
import java.util.HashMap;

/**
 * This class provides code samples for testing the PeLP platform
 * @author Xavier Baró
 */
public class CodeSamples {
    /**
     * Map with all samples
     */
    private HashMap<CodeSampleID,CodeSample> _samples=new HashMap<CodeSampleID,CodeSample>();
    
    /**
     * Default constructor
     */
    public CodeSamples() {
        // Add test samples
        addSamples();
    }
    
    /**
     * Get a code for the selected language.
     * @param languageID Language identifier
     * @param index Index of the code
     * @return Object with the complete code of the sample and its properties and tests
     */
    public CodeSample getCode(String languageID,long index) {        
        return _samples.get(new CodeSampleID(languageID,index));
    }
    
    /**
     * Add a new sample code
     * @param languageID Identifier for the language
     * @param code Code of the samble
     * @param description Description of the sample
     * @param compilable Complilable flag, indicating if the code is compilable
     * @param executable Execution flag, indicating if the code is executable
     * @return Indentifier of the new added sample
     */
    public CodeSampleID addCode(String languageID,String code,String description,boolean compilable,boolean executable,String suggestedFilename) {
        long lastIndex=0;
        
        // Search for samples of this language
        for(CodeSampleID id:_samples.keySet()) {
            if(languageID.equals(id.getLanguage()) && lastIndex<id.getIndex()) {
                lastIndex=id.getIndex();                
            }
        }    
        
        // Create the new sample
        CodeSample newCode=new CodeSample(new CodeSampleID(languageID,lastIndex+1),code);
        newCode.setCompilable(compilable);
        newCode.setExecutable(executable);
        newCode.setDescription(description);
        newCode.setSuggestedFilename(suggestedFilename);
        
        // Add the code
        _samples.put(newCode.getID(), newCode);
        
        return newCode.getID();
    }
    
    /**
     * Add a new test to an existing code sample
     * @param codeID Code Sample identifier
     * @param test New test to be added
     */
    public void addTest(CodeSampleID codeID,TestData test) {
        CodeSample code=_samples.get(codeID);
        if(code!=null) {
            code.addTest(test);
        }
    }
       
    /**
     * Add samples to the object
     */
    private void addSamples() {
        CodeSampleID codeID=null;
        
        // Hello World sample in C
        codeID=addCode("C","int main(void) {\n" +
                                "\tprintf(\"Hello World!\\n\");\n" +
                           "}\n",
                       "Hello World in C",true,true,"hiworld.c");
        addTest(codeID,new TestData("","Hello World!\\n"));
        
        // Hello World sample in JAVA
        codeID=addCode("JAVA","public class HiWorld{\n" +
                                "\tpublic static void main(String[] args) {\n" +
                                    "\t\tSystem.out.println(\"Hello World!\\n\");\n" +
                                "\t}\n" +
                             "}\n",
                       "Hello World in Java",true,true,"HiWorld.java");
        addTest(codeID,new TestData("","Hello World!\\n"));
        
        // Echo program in C
        codeID=addCode("C","#include <stdio.h>\n\n" +
                            "int main(int argc, char *argv[]) {\n" +
                            "\tchar buffer[32768];\n" +
                            "\tint nbytes;\n\n" +
                            "\twhile ((nbytes = fread(buffer, sizeof(char), sizeof(buffer), stdin)) > 0) {\n" +
                            "\t\tfwrite(buffer, sizeof(char), nbytes, stdout);\n" +
                            "\t}\n" +
                            "\treturn 0;\n" +
                            "}\n",
                       "ECHO program in C",true,true,"echo.c");
        String val="Hello World!\\n";
        addTest(codeID,new TestData(val,val));
        val=val + "\n" + val + val + "\r\n";
        addTest(codeID,new TestData(val,val));
    }
}


