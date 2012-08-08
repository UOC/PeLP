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
package edu.uoc.pelp.engine.aem;

import edu.uoc.pelp.engine.aem.exception.CompilerAEMPelpException;
import edu.uoc.pelp.engine.aem.exception.PathAEMPelpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class implements the Code Analyzer for the Java programming language.
 * @author Xavier Baró
 */
public class JavaCodeAnalyzer extends BasicCodeAnalyzer {
    
    /**
     * List of generated files.
     */
    private ArrayList<File> _outputFiles=new ArrayList<File>();
        
    public BuildResult build(CodeProject project) throws PathAEMPelpException, CompilerAEMPelpException {
        BuildResult result=new BuildResult();
               
        // Create the list of input files and expected output files
        for(File f:project.getRelativeFiles()) {
            if(isAccepted(f)) {
                
            }
        }
        
        // Check that working path exists and is valid
        if(_workingPath!=null) {
            // If not exist create it
            if(!_workingPath.exists()) {
                if(!_workingPath.mkdirs()) {
                    throw new PathAEMPelpException("Cannot create the working path <" + _workingPath.getAbsolutePath() + ">");
                }
            }
            
            // Check that it is a directory 
            if(!_workingPath.isDirectory()) {
                throw new PathAEMPelpException("Working path <" + _workingPath.getAbsolutePath() + "> is not a directory");
            }
            
            // Check that is writable
            if(!_workingPath.canWrite()) {
                throw new PathAEMPelpException("Working path <" + _workingPath.getAbsolutePath() + "> is not writable");
            }
        }
        
        return result;
    }

    public String getLanguageID() {
        return "JAVA";
    }

    @Override
    protected String[] getAllowedExtensions() {
        String[] exts={".java"};
        return exts;
    }

    @Override
    protected boolean isMainFile(File file) {
        //TODO: Remove comments before search for main function and use more sofisticated patterns
        boolean isMain=false;
        Scanner scanner=null;
        try {
            scanner = new Scanner(new FileInputStream(file), "UTF-8");
            while (scanner.hasNextLine() && !isMain){
                if(scanner.nextLine().trim().indexOf("publicstaticvoidmain(String")>=0) {
                    isMain=true;
                }
            }
        } catch (FileNotFoundException ex) {
            isMain=false;
        } finally{
            if(scanner!=null) {
                scanner.close();
            }
        }
        
        return isMain;
    }
}
