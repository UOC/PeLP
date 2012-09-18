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

import edu.uoc.pelp.engine.aem.exception.AEMPelpException;
import edu.uoc.pelp.engine.aem.exception.CompilerAEMPelpException;
import edu.uoc.pelp.engine.aem.exception.PathAEMPelpException;
import edu.uoc.pelp.engine.aem.exec.ExtExecUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.tools.*;

/**
 * This class implements the Code Analyzer for the Java programming language.
 * @author Xavier Baró
 */
public class JavaCodeAnalyzer extends BasicCodeAnalyzer {
    
    /**
     * List of generated files.
     */
    private ArrayList<File> _outputFiles=new ArrayList<File>();
    
     /**
     * List of input files.
     */
    private ArrayList<File> _inputFiles=new ArrayList<File>();
        
    @Override
    protected BuildResult buildFileProject(CodeProject project) throws PathAEMPelpException, CompilerAEMPelpException {
        BuildResult result=new BuildResult();
        File mainFile;
        
        // Remove old data
        _inputFiles.clear();
        _outputFiles.clear();
        
        // String Code based projects are not allowed
        if(project.getProjectSourceType()==CodeProject.ProjectSource.String) {
            throw new CompilerAEMPelpException("Wrong Project");
        }
        
        // Set the main file
        mainFile=project.getMainFile();

        // Create the list of input files and expected output files
        for(File f:project.getRelativeFiles()) {
            if(isAccepted(f)) {
                // Add the absolute path to the input files
                _inputFiles.add(project.getAbsolutePath(f, null));                

                // Add output path                
                if(getFileExtension(f).equalsIgnoreCase(".java")) {
                    _outputFiles.add(project.getAbsolutePath(changeExtension(f,".class"), _workingPath));
                }
                
                // Set the main file          
                if(mainFile==null && isMainFile(project.getAbsolutePath(f, _workingPath))) {  
                    mainFile=f;
                }
            }
        }
        
        // Get the main file output
        if(mainFile!=null) {
            mainFile=project.getAbsolutePath(changeExtension(mainFile,".class"), _workingPath);
        } else {
            throw new CompilerAEMPelpException("Cannot find a main file.");  
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

        // Prepare the input files
        File[] inFiles=new File[_inputFiles.size()];
        _inputFiles.toArray(inFiles);

        // Prepare the compiler
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> compilationUnits =
            fileManager.getJavaFileObjectsFromFiles(Arrays.asList(inFiles));       
        
        // Perform compilation
        result.start();
        boolean compRes=compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits).call();
        
        // Collect the errors
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream compilerMessage = new PrintStream(baos);
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            compilerMessage.format("Error on line %d in %s:\n", 
                    diagnostic.getLineNumber(),
                    diagnostic.getSource());
            compilerMessage.format("%s\n",diagnostic.getMessage(Locale.ENGLISH));
        }
        
        // Set the final results
        result.setResult(compRes, baos.toString());
        
        // Store output main file
        _buildingMainFile=mainFile;
        
        try {
            // Close the file manager
            fileManager.close();
        } catch (IOException ex) {
            result.setResult(false, "Unexpected error. \nERROR:" + ex.getMessage());
        }
        
        // Add generated files to temporal files to be removed
        _tmpFiles.add(mainFile);
        for(File outFile:_outputFiles) {
            if(outFile.exists()) {
                _tmpFiles.add(outFile);                    
            }
        }
        
        // Check that output files are generated 
        if(result.isCorrect()) {
            for(File outFile:_outputFiles) {
                if(!outFile.exists()) {
                    result.setResult(false, "Output file <" + outFile + "> does not exist.");
                    break;                    
                }
            }
        }
        
        return result;
    }

    @Override
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
        //TODO: Remove comments before search for main function and use more sofisticated patterns (see getDestinationFileName)
        boolean isMain=false;
        Scanner scanner=null;
        try {
            scanner = new Scanner(new FileInputStream(file), "UTF-8");
            while (scanner.hasNextLine() && !isMain){
                String line=scanner.nextLine();
                if(line.trim().indexOf("public static void main(String".trim())>=0) {
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
    
    @Override
    protected String getDestinationFileName(String code) {
        String fileName="TempCode.java";
        
        //TODO: Consider package folders getPackage
                    
        // Extract the public class declaration from code
        Pattern pClassDec = Pattern.compile("public[ \t\n\r]+[[(abstract)|(static)|(final)|(strictfp)][ \t\n\r]+]*class[ \t\n\r]+");
        String[] parts=pClassDec.split(code);
        if(parts.length>1) {
            // A public class has been found
            fileName=parts[1];

            // Extract the name from the declaration
            Pattern pClassName = Pattern.compile("[ \t\n\r\\{]+");
            parts=pClassName.split(fileName);
            fileName=parts[0].trim() + ".java";
        } else {
            // Search for private class
            pClassDec = Pattern.compile("[ \t\n\r]+class[ \t\n\r]+");
            parts=pClassDec.split(code);
            if(parts.length>1) {
                // A private class has been found
                fileName=parts[1];

                // Extract the name from the declaration  
                Pattern pClassName = Pattern.compile("[ \t\n\r\\{]+");
                parts=pClassName.split(fileName);
                fileName=parts[0].trim() + ".java";
            }
        }
        
        return fileName;
    }
    
    @Override
    public int execute(InputStream input,StringBuffer output,Long timeout) throws AEMPelpException {
        int retVal=-1;
        Process p;
        try {
            
            // Get output class file and check that it exists
            File execFile=_buildingMainFile.getAbsoluteFile();
            if(!execFile.exists()) {
                return -1;
            }

            //TODO: Consider package folders getPackage
            // If working path is provided, use files location as working path            
            File execWorkingPath=_workingPath;
            if(execWorkingPath==null) {
                execWorkingPath=_buildingMainFile.getParentFile();
            }
            
            // Remove packages path
            File mainClassFile=makeRelative(_buildingMainFile,execWorkingPath);
            if(mainClassFile==null) {
                return -1;
            }
            
            // Remove the extension
            String mainClassName=mainClassFile.getPath();
            if(mainClassName.indexOf(".java")>=0) {
                mainClassName=mainClassName.substring(0, mainClassName.indexOf(".java"));
            }
            if(mainClassName.indexOf(".class")>=0) {
                mainClassName=mainClassName.substring(0, mainClassName.indexOf(".class"));
            }
            
            // Convete path to packages
            mainClassName=mainClassName.replace(File.separatorChar, '.');

            // Create an array of commands
            String[] cmdarray = new String[2];
            cmdarray[0]="java";
            cmdarray[1]=mainClassName;
            
            // Set the timeout
            long timeoutValue=_maxExecutionTimeout;
            if(timeout!=null) {
                timeoutValue=timeout;
            }
            
            // Create the process
            p=ExtExecUtils.exec(cmdarray, execWorkingPath, _timeoutStep, timeoutValue,input,output,null);

            // Check the output
            if(p==null) {
                retVal=-1;
                System.out.println("Cannot run the program or timeout");
            } else {
                retVal=p.exitValue();
            }
        } catch (IOException ex) {
            throw new AEMPelpException("Cannot run the program.\nERROR: " + ex.getMessage());
        } catch (InterruptedException ex) {
            throw new AEMPelpException("Cannot run the program.\nERROR: " + ex.getMessage());
        }

        return retVal;
    }
    
    @Override
    public String getSystemInfo() {
        StringBuffer output=new StringBuffer();       
        Process proc=null;        
        try {
            proc = ExtExecUtils.exec("java -version", null, 3, 1000, null, output, output);
        } catch (IOException ex) {
            // No extra operation
        } catch (InterruptedException ex) {
            // No extra operation
        }

        return output.toString();        
    }
}