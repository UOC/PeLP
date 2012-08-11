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
import edu.uoc.pelp.engine.aem.exception.PathAEMPelpException;
import java.io.File;

/**
 * This interface describes the methods to analyze a Code Project.
 * @author Xavier Baró
 */
public interface ICodeAnalyzer {
    /**
     * Assigns the configuration object, which allows to access to the platform parameters.
     * @param confObject Configuration object.
     */
    void setConfiguration(IPelpConfiguration confObject);
    
    /**
     * Build the project. If a working path is provided, output files are generated in this directory,
     * otherwise, are generated in the default path.
     * @param project Code Project which will be builded
     * @return Returns the result of the project building process.
     * @throws AEMPelpException If working directory is incorrect
     * @throws CompilerAEMPelpException Cannot be reached
     */
    BuildResult build(CodeProject project) throws PathAEMPelpException, CompilerAEMPelpException;
    
    /**
     * Test the last builded project. If a working path is provided, output files are generated in this directory,
     * otherwise, are generated in the default path.
     * @param project Code Project which will be builded
     * @return Returns the result of the project building process.
     */
    TestResult test(ProgramTest test);
    
    /**
     * Assign a working path where output files are generated.
     * @param path Path to the working directory
     */    
    void setWorkingPath(File path);
    
    /**
     * Remove output files. 
     */
    void clearData();
    
    /**
     * Check the files in this project to ensure that all of them are correct in
     * terms of extension, format, ..., taking into account the language supported
     * by this implementation.
     * @return True if the files of this code project are valid or False otherwise.
     */
    boolean isValidProject(CodeProject project);
    
    /** 
     * Get an string representation for the language supported by this implementation
     * @return The string representation.
     */
    String getLanguageID();
}
