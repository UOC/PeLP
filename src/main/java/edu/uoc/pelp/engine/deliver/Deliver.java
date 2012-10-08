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
import edu.uoc.pelp.engine.campus.IClassroomID;
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
     * Main classroom of the student for this deliver
     */
    private IClassroomID _userMainClassroom=null;
    
    /** 
     * Laboratory classroom of the student for this deliver
     */
    private IClassroomID _userLabClassroom=null;
    
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
        _userLabClassroom=deliver._userLabClassroom;
        _userMainClassroom=deliver._userMainClassroom;
    }
    
    /**
     * Basic copy constructor
     * @param deliver Deliver object with data
     */
    public Deliver(Deliver deliver) {
        _deliverID=new DeliverID(deliver._deliverID);
        _rootPath=deliver._rootPath;
        for(DeliverFile file:deliver._files) {
            _files.add(file.clone());
        }
        _creationDate=deliver._creationDate;
        _userLabClassroom=deliver._userLabClassroom;
        _userMainClassroom=deliver._userMainClassroom;
    }
    
    /**
     * Basic constructor
     * @param rootPath Deliver root path
     */
    public Deliver(File rootPath) {
        _creationDate=new Date();
        _rootPath=rootPath;
    }
    
    /**
     * Set the deliver Identifier
     * @param deliverID Identifyer object
     */
    public void setID(DeliverID deliverID) {
        _deliverID=deliverID;
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
     * Get the creation date of the deliver
     * @return Creation date
     */
    public Date getCreationDate() {
        return _creationDate;
    }

    /**
     * Set the creation date of the deliver
     * @param creationDate Creation date
     */
    public void setCreationDate(Date creationDate) {
        this._creationDate = creationDate;
    }

    /**
     * Get the laboratory classroom assigned to this deliver
     * @return Classroom identifier for the laboratory
     */
    public IClassroomID getUserLabClassroom() {
        return _userLabClassroom;
    }

    /**
     * Set the laboratory classroom assigned to this deliver
     * @param userLabClassroom Classroom identifier for the laboratory
     */
    public void setUserLabClassroom(IClassroomID userLabClassroom) {
        this._userLabClassroom = userLabClassroom;
    }

    /**
     * Get the main classroom assigned to this deliver
     * @return Classroom identifier for the main classroom
     */
    public IClassroomID getUserMainClassroom() {
        return _userMainClassroom;
    }

    /**
     * Set the main classroom assigned to this deliver
     * @param userMainClassroom Classroom identifier for the main classroom
     */
    public void setUserMainClassroom(IClassroomID userMainClassroom) {
        this._userMainClassroom = userMainClassroom;
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
    
    /**
     * Move the files of the deliver to a new path. If old path 
     * @param dstPath Path to store the files. It cannot exist previously.
     * @return True if all files have been correctly moved to the new path of False otherwise
     */
    public boolean moveFiles(File dstPath) {
        // Check that destination folder does not exist
        if(dstPath.getAbsoluteFile().exists()) {
            return false;
        }
        
        // Create the output path
        if(!dstPath.getAbsoluteFile().mkdirs()) {
            return false;
        }
        
        // Move the files    
        for(DeliverFile f:_files) {
            if(!f.getAbsolutePath(_rootPath).renameTo(f.getAbsolutePath(dstPath))) {
                return false;
            }
        }
        
        // Remove old path
        _rootPath.getAbsoluteFile().delete();
        
        // Change the root path to the new folder
        _rootPath=dstPath;
        
        return true;
    }
    
    @Override
    public Deliver clone() {
        return new Deliver(_deliverID,this);
    }

    @Override
    public int compareTo(Object t) {
        if(t==null) {
            return -1;
        }
        return _deliverID.compareTo(((Deliver)t).getID());
    }

    /**
     * Add the main classroom to the deliver
     * @param classroom Identifier of the classroom
     */
    public void addMainClassroom(IClassroomID classroom) {
        _userMainClassroom=classroom;
    }
    
    /**
     * Add the laboratory classroom to the deliver
     * @param classroom Identifier of the classroom
     */
    public void addLabClassroom(IClassroomID classroom) {
        _userLabClassroom=classroom;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Deliver other = (Deliver) obj;
        if (this._deliverID != other._deliverID && (this._deliverID == null || !this._deliverID.equals(other._deliverID))) {
            return false;
        }
        if (this._rootPath != other._rootPath && (this._rootPath == null || !this._rootPath.equals(other._rootPath))) {
            return false;
        }
        if (this._files != other._files && (this._files == null || !this._files.equals(other._files))) {
            // Before to reject, try sorting file lists
            if(this._files!=null && other._files!=null && this._files.size()==other._files.size()) {
                Collections.sort(this._files);
                Collections.sort(other._files);
            
                return this._files.equals(other._files);
            }
            return false;
        }
        if (this._userMainClassroom != other._userMainClassroom && (this._userMainClassroom == null || !this._userMainClassroom.equals(other._userMainClassroom))) {
            return false;
        }
        if (this._userLabClassroom != other._userLabClassroom && (this._userLabClassroom == null || !this._userLabClassroom.equals(other._userLabClassroom))) {
            return false;
        }
        if (this._creationDate != other._creationDate && (this._creationDate == null || !this._creationDate.equals(other._creationDate))) {
            if(this._creationDate!=null && other._creationDate!=null) {
                long diff=Math.abs(this._creationDate.getTime()-other._creationDate.getTime());
                if(diff>=1000) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this._deliverID != null ? this._deliverID.hashCode() : 0);
        hash = 59 * hash + (this._rootPath != null ? this._rootPath.hashCode() : 0);
        hash = 59 * hash + (this._files != null ? this._files.hashCode() : 0);
        hash = 59 * hash + (this._creationDate != null ? this._creationDate.hashCode() : 0);
        hash = 59 * hash + (this._userMainClassroom != null ? this._userMainClassroom.hashCode() : 0);
        hash = 59 * hash + (this._userLabClassroom != null ? this._userLabClassroom.hashCode() : 0);
        return hash;
    }
}
