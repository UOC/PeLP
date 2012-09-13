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

import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.aem.exception.AEMPelpException;
import edu.uoc.pelp.engine.aem.exception.CompilerAEMPelpException;
import edu.uoc.pelp.engine.aem.exception.LanguageAEMPelpException;
import edu.uoc.pelp.engine.aem.exception.PathAEMPelpException;
import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.springframework.util.Assert;

/**
 * This class provides a generic implementation for the interface ICodeAnalyzer. It provides
 * an easy starting point to implement specific classes for each language.
 * @author Xavier Baró
 */
public abstract class BasicCodeAnalyzer implements ICodeAnalyzer {
    
    /**
     * Stores the working path.
     */
    protected File _workingPath=null;
    
    /** 
     * Stores the configuration object
     */
    protected IPelpConfiguration _confObject=null;
    
    /**
     * If a temporal files/folders has been created, they is stored in this variable to be removed
     */
    protected ArrayList<File> _tmpFiles=new ArrayList<File>();
    
    /**
     * Main file resulting from the building process
     */
    protected File _buildingMainFile=null;
    
    /**
     * Maximum timeout in miliseconds for the the bulding process
     */
    protected long _maxBuildingTimeout=5000;
    
    /**
     * Maximum timeout in miliseconds for the execution of the code
     */
    protected long _maxExecutionTimeout=5000;
    
    /**
     * Timeout step. Is the freqüency used to check timeout criteria
     */
    protected long _timeoutStep=3;
    
    /**
     * Table of classes implementing specific analyzers. In the future, store this table outside.
     */
    private static HashMap<String,Class> _specificImplementations=new HashMap<String,Class>() {
        {
            put("C",CCodeAnalyzer.class);
            put("JAVA",JavaCodeAnalyzer.class);
        }
    };
    
        /**
     * Get the list of allowed file extensions. It is used to check the files in the project and
     * avoid the use of non code files.
     * @return Array of file extensions
     */
    protected abstract String[] getAllowedExtensions();
    
    /**
     * Analyze the content of the file looking for the standard properties of a main file 
     * @param file File to be analyzed
     * @return True if the given file is a main file or False otherwise.
     */
    protected abstract boolean isMainFile(File file);
    
    /**
     * Return a name for the given code.
     * @param code String containing a source code
     * @return Filename compatible with the received code
     */
    protected abstract String getDestinationFileName(String code);
    
    /**
     * Build a file based project, using the concrete knowledge of each language.
     * @param project Code Project which will be builded
     * @return Returns the result of the project building process.
     * @throws PathAEMPelpException If working directory is incorrect
     * @throws CompilerAEMPelpException Cannot be reached
     */
    protected abstract BuildResult buildFileProject(CodeProject project) throws PathAEMPelpException, CompilerAEMPelpException;

    /**
     * Execute the program resulting from the last building process
     * @param input Data used as input to the program execution
     * @param output Data generated during the program execution
     * @param timeOut Timeout value for this execution. If null, default value is used.
     * @return Execution result. Value different from 0 means errors.
     * @throws AEMPelpException If the execution is not possible
     */
    protected abstract int execute(InputStream input,StringBuffer output,Long timeOut) throws AEMPelpException;
    
    /**
     * Obtain an instance that implements a proper Code Analyzer for the programming
     * language used in the given project.
     * @param project Code project to be analyzed
     * @return Implementation of a code analyzer object compatible with the given project.
     * @throws LanguageAEMPelpException There no exists any implementation for the given programming language.
     */
    public static BasicCodeAnalyzer getInstance(CodeProject project) throws LanguageAEMPelpException {
        BasicCodeAnalyzer instance=null;
        
        // If the project has an assigned language, create an instance of the class for this language
        if(project.getLanguage()!=null) {
            if(!_specificImplementations.containsKey(project.getLanguage())) {
                throw new LanguageAEMPelpException("Analyzer for language <" + project.getLanguage() + "> is not available.");
            }
            try {
                // Create the instance for this project
                instance=(BasicCodeAnalyzer) _specificImplementations.get(project.getLanguage()).newInstance();
            } catch (Exception ex) {
                throw new LanguageAEMPelpException("Cannot create an object for the language <" + project.getLanguage() + ">");
            }
        } else {
            // Create an instance for each available implementation and check the project
            for(Class c:_specificImplementations.values()) {
                try {
                    // Create a new instance
                    instance=(BasicCodeAnalyzer) c.newInstance();
                                        
                    // Check the files of this project
                    if(instance.isValidProject(project)) {
                        break;
                    }
                    
                    // Invalid project, remove the instance
                    instance=null;
                } catch (Exception ex) {
                    throw new LanguageAEMPelpException("Cannot create an object of the class <" + c.getCanonicalName() + ">");
                }
            }
        }
        
        return instance;
    }
    
    /**
     * Return the extension of a given file
     * @param file File to be analyzed.
     * @return String with the extension, including the point. If no extension, an empty string is returned.
     */
    protected String getFileExtension(File file) {
        String filename;

        // Remove the path from the filename.
        int lastSeparatorIndex = file.getName().lastIndexOf(File.separator);
        if (lastSeparatorIndex == -1) {
            filename = file.getName();
        } else {
            filename = file.getName().substring(lastSeparatorIndex + 1);
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return "";

        return filename.substring(extensionIndex);
    }
    
    /**
     * Check if the file is accepted by this implementation of Code Analyzer. It is performed
     * by checking the extension of the file.
     * @param file Input File
     * @return True if the file has an accepted extension or False otherwise.
     */
    protected boolean isAccepted(File file) {
        String ext=getFileExtension(file);
        
        // Compare with all allowed extensions
        for(String e:getAllowedExtensions()) {
            if(e.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Changes the exension of a the given file to the given extension
     * @param file Source file with or without extension
     * @param newExt New extension to be applied, or null to remove extension.
     * @return New file with the new extension
     */
    protected File changeExtension(File file, String newExt) {
        
        // Obtain the file
        String filename=file.getPath();

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex >=0) {
            filename=filename.substring(0,extensionIndex);
        }
        
        // Add new extension
        if(newExt!=null) {
            if(newExt.indexOf('.')==0) {
                filename += newExt;
            } else {
                filename += "." + newExt;
            }
        }
        
        return new File(filename);
    }
    
    /**
     * Assign a new timeout value for the building process
     * @param time Number of miliseconds
     * @return Old timeout value
     */
    public long setBuildingTimeout(long time) {
        long oldValue=_maxBuildingTimeout;
        _maxBuildingTimeout=time;
        return oldValue;
    }
    
    /**
     * Assign a new timeout value for the execution process
     * @param time Number of miliseconds
     * @return Old timeout value
     */
    public long setExecutionTimeout(long time) {
        long oldValue=_maxExecutionTimeout;
        _maxExecutionTimeout=time;
        return oldValue;
    }
    
    /**
     * Assign a new timeout step value, applicable to execution and building processes
     * @param time Number of miliseconds
     * @return Old timeout step value
     */
    public long setTimeoutStep(long time) {
        long oldValue=_timeoutStep;
        _timeoutStep=time;
        return oldValue;
    }
    
    public void setConfiguration(IPelpConfiguration confObject) {
        _confObject=confObject;
    }
    
    public TestResult test(TestData test) {
        StringBuffer exeOutput=new StringBuffer();
        
        // Perform the execution
        Long startTime=new Long(System.currentTimeMillis());
        int retVal;
        try {
            retVal = execute(test.getInputStream(),exeOutput,test.getMaxTime());
        } catch (FileNotFoundException ex) {
            retVal=-1;
            exeOutput.append("\nTEST ERROR:\n");
            exeOutput.append(ex.getMessage());
        } catch (AEMPelpException ex) {
            retVal=-1;
            exeOutput.append("\nERROR:\n");
            exeOutput.append(ex.getMessage());
        }
        Long endTime=new Long(System.currentTimeMillis());
                        
        // Store the results
        TestResult result=new TestResult(); 
        result.setElapsedTime(endTime-startTime);
        
        // Compare the output and exepcted output
        if(retVal<0) {
            result.setResult(false, "ERROR: Timeout or execution error");
        } else {
            boolean sameOutput;
            try {
                sameOutput = test.checkResult(exeOutput.toString());
            } catch (FileNotFoundException ex) {
                sameOutput=false;
            }
            result.setResult(sameOutput, exeOutput.toString());
        }
        
        return result;
    }

    public void setWorkingPath(File path) {
        _workingPath=path;
    }

    public boolean isValidProject(CodeProject project) {
        // Check the assigned language
        if(project.getLanguage()!=null) {
            if(!getLanguageID().equals(project.getLanguage())) {
                return false;
            }
        }
        
        // Check the list of extensions
        for(File f:project.getRelativeFiles()) {
            if(!isAccepted(f)) {
                return false;
            }
        }
        
        // Check the main file
        if(project.getMainFile()!=null) {
            if(!isMainFile(project.getMainFile())) {
                return false;
            } 
        } else {
            boolean hasMain=false;
            for(File f:project.getAbsoluteFiles()) {                
                if(isMainFile(f)) {
                    hasMain=true;
                    break;
                }
            }
            if(!hasMain) {
                return false;
            }
        }
        
        return true;
    }
    
   
    public BuildResult build(CodeProject project) throws PathAEMPelpException, CompilerAEMPelpException {
        
        // Remove old temporal information
        clearData();
        
        // Consider string based projects
        if(project.getProjectSourceType()==CodeProject.ProjectSource.String) {
            return build(project.getProjectCode());
        }
        
        // If working path is provided, copy project to the new path
        if(_workingPath!=null) {
            try {
                project=project.copyFiles(_workingPath);
            } catch (AuthPelpException ex) {
                throw new PathAEMPelpException(ex.getMessage());
            } catch (ExecPelpException ex) {
                throw new PathAEMPelpException(ex.getMessage());
            }
        }
        
        // Call file based project analysis
        return buildFileProject(project);
    }
    
    /**
     * Create a temporal folder in the temporal directory. It
     * @param path Root folder
     * @return New created temporal folder
     * @throws PathAEMPelpException If temporal folder cannot be created
     */
    private synchronized File createTemporalFolder(String path) throws PathAEMPelpException{
        // Remove extra separators
        if(path.charAt(0)==File.separatorChar) {
            path.substring(1);
        }
        
        // Create a temporal folder
        File tmpPathRoot=null;
        if(_confObject!=null) {
            tmpPathRoot=_confObject.getTempPath();
        }
        if(tmpPathRoot==null) {
            // If no temporal path, use system temporal path
            tmpPathRoot=new File(System.getProperty("java.io.tmpdir"));  
        }
        
        // Add the given subfolder
        tmpPathRoot=new File(tmpPathRoot.getAbsolutePath() + File.separator + path);
        if(!tmpPathRoot.exists()) {
            if(!tmpPathRoot.mkdirs()) {
                throw new PathAEMPelpException("Cannot create temporal folder <" + tmpPathRoot.getPath() + ">");
            }
            _tmpFiles.add(tmpPathRoot);
        }
        
        // Create a temporal subfolder name
        File tmpPath=null;
        do {
            tmpPath=new File(tmpPathRoot.getAbsolutePath() + File.separator + "tmp" + Long.toString(System.nanoTime()));
        } while(tmpPath.exists());
        
        // Create the path
        if(!tmpPath.mkdirs()) {
            throw new PathAEMPelpException("Cannot create temporal folder <" + tmpPath.getPath() + ">");
        }
        
        // Activate auto deletion 
        _tmpFiles.add(tmpPath);
        
        return tmpPath;
    }
    
    /**
     * Perform the building process for codes contained in a String
     * @param code String with the code
     * @return Results from the building process
     * @throws CompilerAEMPelpException If some error problem ocurred during the building process.
     * @throws PathAEMPelpException If working directory is incorrect
     */
    protected BuildResult build(String code) throws PathAEMPelpException,CompilerAEMPelpException {
        
        // Check the code
        if(code==null) {
            throw new CompilerAEMPelpException("String code is null");
        }
        
        // Create a temporal a new folder and a new file
        File tmpFolder=createTemporalFolder("strCodePojects");
        File srcFile=new File(tmpFolder.getAbsolutePath() + File.separator + getDestinationFileName(code));
        Assert.isTrue(tmpFolder.exists(),"Check temporal folder is created.");
        Assert.isTrue(!srcFile.exists(),"Check temporal code file does not exist.");
               
        // Write the code to the file
        PrintWriter srcFileWriter;   
        try {
            srcFileWriter = new PrintWriter(new FileOutputStream(srcFile));
        } catch (FileNotFoundException ex) {
            throw new CompilerAEMPelpException("Cannot create temporal file <" + srcFile.getPath() + ">");
        }
        srcFileWriter.printf(code);
        srcFileWriter.close();
        
        // Create a new code project using files
        CodeProject newProject=new CodeProject(tmpFolder);
        try {
            newProject.addMainFile(srcFile);
        } catch (ExecPelpException ex) {
            throw new CompilerAEMPelpException("Cannot add the temporal file to the temporal CodeProject");
        }
        
        // Build the new project
        BuildResult result=build(newProject);
        
        // Store temporal path to be removed
        _tmpFiles.add(srcFile);      
        
        return result;
    }
    
    /**
     * Remove temporal files and folders
     */
    public void clearData() {
        // Remove files
        clearTempFiles();
        
        // Clear analysis results
        
    }
    
    /**
     * Delete all temporal files and directories created during the program analysis
     */
    protected void clearTempFiles() {
        
        File[] fileList=new File[_tmpFiles.size()];
        _tmpFiles.toArray(fileList);
                
        // Remove main file
        if(_buildingMainFile!=null) {
            if(_buildingMainFile.exists()) {
                // Delete the file or if it is not possble, mark it for delayed deletion
                if(!_buildingMainFile.delete()) {
                    _buildingMainFile.deleteOnExit();
                }
                _buildingMainFile=null;
            }
        }
        
        // Remove files in the list
        for(File f:fileList) {
            if(f.exists() && f.isFile()) {
                // Delete the file or if it is not possble, mark it for delayed deletion
                if(!f.delete()) {
                    f.deleteOnExit();
                }
                // Remove the file from the list
                _tmpFiles.remove(f);
            }
        }
        
        // Sort folders in reverse order, to ensure child folders to be the first to be deleted
        Collections.sort(_tmpFiles,Collections.reverseOrder());
        
        // Create a new list with remaining paths
        fileList=new File[_tmpFiles.size()];
        _tmpFiles.toArray(fileList);
        
        // Remove folders 
        for(File f:fileList) {
            if(!f.delete()) {
                f.deleteOnExit();
            }
        }
    }
    
    public AnalysisResults analyzeProject(CodeProject project,TestData[] tests) throws PathAEMPelpException, CompilerAEMPelpException {
               
        // Call internal building method
        BuildResult buildResult=build(project);
        AnalysisResults result=new AnalysisResults(buildResult);
        
        // Test the code
        if(buildResult.isCorrect() && tests!=null) {
            for(TestData testData:tests) {
                result.addTestResult(test(testData));
            }
        }
        
        return result;
    }
    
    protected File makeRelative(File path,File root) {
        
        if(root==null) {
            return path;
        }
        
        String relativePath=path.getAbsolutePath();
        
        // Check that both routes are compatible
        if(relativePath.indexOf(root.getAbsolutePath())!=0) {
            return null;
        }
        
        // Remove the root path
        relativePath=relativePath.substring(root.getAbsolutePath().length());
        
        // Remove initial slash
        if(relativePath.charAt(0)==File.separatorChar) {
            relativePath=relativePath.substring(1);
        }
        
        return new File(relativePath);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        
        // Remove old temporal information
        clearData();
    }
    
    
}