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
import edu.uoc.pelp.engine.aem.exception.LanguageAEMPelpException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

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
     * Table of classes implementing specific analyzers. In the future, store this table outside.
     */
    private static HashMap<String,Class> _specificImplementations=new HashMap<String,Class>() {
        {
            put("C",CCodeAnalyzer.class);
            put("JAVA",JavaCodeAnalyzer.class);
        }
    };
    
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
    
    public void setConfiguration(IPelpConfiguration confObject) {
        _confObject=confObject;
    }
    
    public TestResult test(ProgramTest test) {
        // Call an exec method
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setWorkingPath(File path) {
        _workingPath=path;
    }

    public void clearData() {
        // Remove all generated files in the working path
        throw new UnsupportedOperationException("Not supported yet.");
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
}

class StrOutputStream extends OutputStream
{
    private StringBuffer textBuffer = new StringBuffer();
    
    /**
     * Default constructor
     */
    public StrOutputStream()
    {
        super();
    }

    public void write(int b) throws IOException
    {
        char a = (char)b;
        textBuffer.append(a);
    }

    @Override
    public String toString()
    {
        return textBuffer.toString();
    }
  
    public void clear()
    {
        textBuffer.delete(0, textBuffer.length());
    }
}