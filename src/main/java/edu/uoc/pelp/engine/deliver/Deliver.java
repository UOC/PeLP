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
package edu.uoc.pelp.engine.deliver;

import edu.uoc.pelp.engine.aem.CodeProject;
import edu.uoc.pelp.engine.deliver.DeliverFile.FileType;
import edu.uoc.pelp.exception.ExecPelpException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * This class represents a Deliver.
 * @author Xavier Baró
 */
public class Deliver implements Comparable {
    /**
     * Deliver unique identifier
     */
    private DeliverID _deliverID=null;
    
    /**
     * Root path where the deliver files are stored
     */
    private File _rootPath=null;
    
    /**
     * List of files included in this deliver
     */
    private ArrayList<DeliverFile> _files=new ArrayList<DeliverFile>();
    
    /**
     * Creation date
     */
    private Date _creationDate=null;
    
    /**
     * Basic constructor
     * @param newID Deliver Identiryer
     * @param rootPath Deliver root path
     */
    public Deliver(DeliverID newID, File rootPath) {
        _deliverID=newID;
        _rootPath=rootPath;
        _creationDate=new Date();
    }
    
    /**
     * Basic copy constructor, which creates a new deliver assigning a new ID
     * @param newID Deliver Identiryer
     * @param deliver Deliver object with data
     */
    public Deliver(DeliverID newID, Deliver deliver) {
        _deliverID=newID;
        _rootPath=deliver._rootPath;
        for(DeliverFile file:deliver._files) {
            _files.add(file.clone());
        }
        _creationDate=deliver._creationDate;
    }
    
    /**
     * Basic constructor
     * @param rootPath Deliver root path
     */
    public Deliver(File rootPath) {
        _rootPath=rootPath;
    }
    
    /**
     * Get the deliver Identifier
     * @return Identifyer object
     */
    public DeliverID getID() {
        return _deliverID;
    }
    
    /**
     * Get the root path for deliver files
     * @return Root path
     */
    public File getRootPath() {
        return _rootPath;
    }
    
    /**
     * Set the root path for deliver files
     * @param rootPath Root path
     */
    public void setRootPath(File rootPath) {
        _rootPath=rootPath;
    }
    
    /**
     * Checks if the deliver is correct
     * @return True if the deliver is correct or False otherwise.
     */
    public boolean correct() {
        if(_rootPath==null) {
            return false;
        }
        for(DeliverFile df:_files) {
            File f=df.getAbsolutePath(_rootPath);
            if(!f.exists() || !f.canRead()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Add a new file to the deliver
     * @param file Path to the file
     * @return True if the file is correctly added or False otherwise.
     */
    public boolean addFile(DeliverFile file) {
        if(!file.getAbsolutePath(_rootPath).exists()) {
            return false;
        }
        _files.add(file.clone());
        return true;
    }
    
    /**
     * Get the list of files for this deliver
     * @return Array of files sorted by path
     */
    public DeliverFile[] getFiles() {
        ArrayList<DeliverFile> files=new ArrayList<DeliverFile>();
        
        // Create the list of files
        for(DeliverFile deliverFile:_files) {
            files.add(deliverFile);
        }
        
        // Sort the list of files
        Collections.sort(files);
        
        // Create the output array
        DeliverFile[] retList=new DeliverFile[files.size()];
        files.toArray(retList);
        
        return retList;
    }
    
    /**
     * Obtain the code project from this deliver
     * @return Code project with the code files of the deliver
     * @throws ExecPelpException If some file cannot be accessed
     */
    public CodeProject getCodeProject() throws ExecPelpException {
        CodeProject project=new CodeProject(_rootPath); 
        
        // Add code files to the project
        for(DeliverFile deliverFile:_files) {
            if(deliverFile.getType().equals(FileType.Code)) {
                if(deliverFile.isMainFile()) {
                    project.addMainFile(deliverFile.getAbsolutePath(_rootPath));
                } else {
                    project.addFile(deliverFile.getAbsolutePath(_rootPath));
                }
            }
        }
        
        return project;
    }
    
    @Override
    public Deliver clone() {
        return new Deliver(_deliverID,this);
    }

    public int compareTo(Object t) {
        if(t==null) {
            return -1;
        }
        return _deliverID.compareTo(((Deliver)t).getID());
    }
}
