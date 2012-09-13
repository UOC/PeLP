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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the Code Analyzer for the C programming language.
 * @author Xavier Baró
 */
public class CCodeAnalyzer extends BasicCodeAnalyzer {
    
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
        ArrayList<String> compilerArgs=new ArrayList<String>();
        ArrayList<String> linkerArgs=new ArrayList<String>();
        File mainFile;
        
        // Remove old data
        _inputFiles.clear();
        _outputFiles.clear();
        
        // Check that the compiler is present
        if(_confObject.getCompiler(getLanguageID())==null) {
            throw new CompilerAEMPelpException("C Compiler has not been configured.");   
        } else {
            if(!_confObject.getCompiler(getLanguageID()).exists() || !_confObject.getCompiler(getLanguageID()).canExecute()) {
                throw new CompilerAEMPelpException("C Compiler <" + _confObject.getCompiler(getLanguageID()) + "> does not exist or is not executable.");  
            }
        }
        
        // String Code based projects are not allowed
        if(project.getProjectSourceType()==CodeProject.ProjectSource.String) {
            throw new CompilerAEMPelpException("Wrong Project");
        }
        
        // Set the main file
        mainFile=project.getMainFile();

        // Create the list of input files and expected output files
        for(File f:project.getRelativeFiles()) {
            if(isAccepted(f)) {
                // Check the file                
                if(getFileExtension(f).equalsIgnoreCase(".c")) {
                    // Add the absolute path to the input files
                    _inputFiles.add(project.getAbsolutePath(f, null));
                    
                    // Add the compiled to the output                    
                    //_outputFiles.add(project.getAbsolutePath(changeExtension(f,".o"), _workingPath));
                    
                    // Set the main file          
                    if(mainFile==null && isMainFile(project.getAbsolutePath(f, _workingPath))) {  
                        mainFile=f;
                    }
                }
            }
        }
        
        // Adapt the main file to the platform
        if(mainFile!=null) {
            if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
                // Windows machine, add extension
                mainFile=project.getAbsolutePath(changeExtension(mainFile,".exe"), _workingPath);
            } else {
                // Other machine, no extension is added
                mainFile=project.getAbsolutePath(changeExtension(mainFile,null), _workingPath);
            }
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
                _tmpFiles.add(_workingPath);
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
        compilerArgs.add(_confObject.getCompiler(getLanguageID()).getPath());
        compilerArgs.add("-ansi");
        //compilerArgs.add("-c");
        compilerArgs.add("-o");
        compilerArgs.add(mainFile.getAbsolutePath());
        // TODO: Compile file by file
        for(File f:inFiles) {
            compilerArgs.add(f.getAbsolutePath());
        }
             
        // Create an array to call the compiler
        String[] compilationCmd=new String[compilerArgs.size()];
        compilerArgs.toArray(compilationCmd);

        // Run the compiler
        Process proc;
        
        // Perform compilation
        StringBuffer output=new StringBuffer();
        boolean compRes=false;
        result.start();
        try {    
            proc = ExtExecUtils.exec(compilationCmd, _confObject.getCompiler(getLanguageID()).getAbsoluteFile().getParentFile(), 3, 1000, null, output, output);
            if(proc!=null) {
                if(proc.exitValue()==0) {
                    // Store succesful value
                    compRes=true;
                    // Store output main file
                    _buildingMainFile=mainFile;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CCodeAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            throw new CompilerAEMPelpException("Unexpected error while compiling."); 
            
        } catch (InterruptedException ex) {
            Logger.getLogger(CCodeAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            throw new CompilerAEMPelpException("Time out exception during compile process.");
        }
        
        // Linker
        /*
        if(compRes) {
            compRes=false;
            
            // Prepare the input files
            File[] outFiles=new File[_outputFiles.size()];
            _outputFiles.toArray(outFiles);
            
            // Prepare the compiler
            linkerArgs.add(_confObject.getCompiler(getLanguageID()).getPath());
            linkerArgs.add("-o");
            linkerArgs.add(mainFile.getName());
            if(_workingPath!=null) {
                linkerArgs.add("-B");
                linkerArgs.add(_workingPath.getAbsolutePath());
            } else {
                linkerArgs.add("-B");
                linkerArgs.add(project.getRootPath().getAbsolutePath());
            }
            for(File f:outFiles) {
                linkerArgs.add(f.getAbsolutePath());
            }
            
            // Create an array to call the linker
            String[] linkCmd=new String[linkerArgs.size()];
            linkerArgs.toArray(linkCmd);
            
            // Run the linker
            try {    
                proc = ExtExecUtils.exec(linkCmd, _confObject.getCompiler(getLanguageID()).getAbsoluteFile().getParentFile(), 3, 1000, null, output, output);
                if(proc.exitValue()==0) {
                    compRes=true;
                }
            } catch (IOException ex) {
                Logger.getLogger(CCodeAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
                result.setResult(false, "Unexpected error while linking.");
            } catch (InterruptedException ex) {
                Logger.getLogger(CCodeAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
                result.setResult(false, "Time out exception during link process.");
            }
        }
        */
        // Set the final results
        result.setResult(compRes, output.toString());
        
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
    
    public String getLanguageID() {
        return "C";
    }

    @Override
    protected String[] getAllowedExtensions() {
        String[] exts={".c",".h"};
        return exts;
    }

    @Override
    protected String getDestinationFileName(String code) {
        return "tempCode.c";
    }

    @Override
    protected int execute(InputStream input, StringBuffer output,Long timeOut) throws AEMPelpException {
        int retVal=-1;
        Process p;
        
        try {
            // Get executable file and check that it exists
            File execFile=_buildingMainFile.getAbsoluteFile();
            if(execFile==null) {
                return -1;
            }
            if(!execFile.exists() || !execFile.canExecute()) {
                return -1;
            }

            // Create an array of commands
            String[] cmdarray = new String[1];
            cmdarray[0]=execFile.getAbsolutePath();
            
            // Set the timeout
            long timeOutValue=_maxExecutionTimeout;
            if(timeOut!=null) {
                timeOutValue=timeOut;
            }
            
            // Create the process            
            p=ExtExecUtils.exec(cmdarray, _workingPath, _timeoutStep, timeOutValue,input,output,null);
            
            // Check the output
            if(p==null) {
                retVal=-1;
            } else {
                retVal=p.exitValue();
            }
        }
        catch (IOException ex) {
            throw new AEMPelpException("Cannot run the program.\nERROR: " + ex.getMessage());
        } catch (InterruptedException ex) {
            throw new AEMPelpException("Cannot run the program.\nERROR: " + ex.getMessage());
        }
        return retVal;
    }

    public String getSystemInfo() {
        StringBuffer output=new StringBuffer();
        
        // Check that compiler is configured
        if(_confObject==null) {
            return null;
        }
        if(_confObject.getCompiler(getLanguageID())==null) {
            return null;
        }
        if(!_confObject.getCompiler(getLanguageID()).exists() || !_confObject.getCompiler(getLanguageID()).canExecute()) {
            return null;
        }
        
        try {
            Process proc = ExtExecUtils.exec(_confObject.getCompiler(getLanguageID()).getPath() + " --version", null, _timeoutStep, _maxBuildingTimeout, null, output, output);
        } catch (IOException ex) {
            // No extra operation
        } catch (InterruptedException ex) {
            // No extra operations
        }

        return output.toString();
    }
    
    @Override
    protected boolean isMainFile(File file) {
        //TODO: Remove comments before search for main function and use more sofisticated patterns (see JAVA getDestinationFileName example)
        boolean isMain=false;
        Scanner scanner=null;
        try {
            scanner = new Scanner(new FileInputStream(file), "UTF-8");
            while (scanner.hasNextLine() && !isMain){
                String line=scanner.nextLine();                
                if(line.trim().indexOf("void main(".trim())>=0 ||
                   line.trim().indexOf("int main(".trim())>=0) {
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
