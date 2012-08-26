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

import java.io.File;

/**
 * This class implements each of the files included in a deliver.
 * @author Xavier Baró
 */
public class DeliverFile implements Comparable{

    /**
     * Enumerator for file's type.
     */
    public enum FileType {Code, Report};
    
    /**
     * Relative path of the file
     */
    private File _file=null;
    
    /**
     * Type of this file
     */
    private FileType _type=null;

    /**
     * Default constructor
     * @param path Path of the file
     * @param type Type of file
     */
    public DeliverFile(File path,FileType type) {
        _file=path;
        _type=type;
    }
    
    @Override
    public DeliverFile clone() {
        DeliverFile file=new DeliverFile(_file,_type);
        return file;
    }
    
    public File getAbsolutePath(File rootPath) {
        if(rootPath==null) {
            return _file.getAbsoluteFile();
        }
        // Check if the current file is in the rootPath
        if(_file.getPath().indexOf(rootPath.getAbsolutePath())>=0) {
            return _file;
        }
        
        // Create the absolute path
        String path=_file.getPath();
        if(path.charAt(0)==File.separatorChar) {
            // Remove initial path separator
            path.substring(1);
        }
        return new File(rootPath.getAbsolutePath() + File.separator + path);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DeliverFile other = (DeliverFile) obj;
        if (this._file != other._file && (this._file == null || !this._file.equals(other._file))) {
            return false;
        }
        if (this._type != other._type) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this._file != null ? this._file.hashCode() : 0);
        hash = 71 * hash + (this._type != null ? this._type.hashCode() : 0);
        return hash;
    }
    
    public int compareTo(Object t) {
        if(t==null) {
            return -1;
        }
        return _file.compareTo(((DeliverFile)t)._file);
    }
}
