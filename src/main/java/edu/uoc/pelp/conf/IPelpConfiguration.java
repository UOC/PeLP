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
package edu.uoc.pelp.conf;

import edu.uoc.pelp.engine.campus.ISubjectID;
import java.io.File;

/**
 * This interface describes the methods to access the platform configuration information
 * @author Xavier Baró
 */
public interface IPelpConfiguration {
    
    /**
     * Get an identifier for the current configuration environtment
     * @return String identifying the current environment
     */
    String getEnvironmentID();
    
    /**
     * Get a description for the current configuration environtment
     * @return String describing the current environment
     */
    String getEnvironmentDesc();
    
    /**
     * Obtain the temporal folder used to store temporal files
     * @return Path to the temporal folder
     */
    File getTempPath();
    
    /**
     * Obtain the folder where deliveries are stored
     * @return Path to the deliveries folder.
     */
    File getDeliveryPath();
        
    /**
     * Obtain the compiler for the given language
     * @param languageID Identifier for the language.
     * @return Path to the C compiler for this environtment.
     */
    File getCompiler(String languageID);
    
    /**
     * Obtain the list of active subjects in the PeLP system
     * @return List of subject identifyers
     */
    ISubjectID[] getActiveSubjects();
}
