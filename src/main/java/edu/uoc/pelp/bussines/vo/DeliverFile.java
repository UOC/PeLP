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
package edu.uoc.pelp.bussines.vo;

import java.io.File;

/**
 * Information for a delivered file
 * @author Xavier Baró
 */
public class DeliverFile {
    /**
     * Deliver file index
     */
    private int _index;  
    
    /**
     * Indicates if it is a code file
     */
    private boolean _isCode;  
    
    /**
     * Indicates if it is a report file
     */
    private boolean _isReport;  
    
    /**
     * Indicates if it is a main code file
     */
    private boolean _isMain;  
    
    /**
     * Relative path of this file respect to the rootPath
     */
    private String _relativePath;
    
    /**
     * Root path for this file. It is used to allow multiple directories of code files (i.e. Java Packages)
     */
    private String _rootPath;
    
    /**
     * File content for files to be uploaded to the server
     */
    private Byte[] _fileContent;

    public Byte[] getFileContent() {
        return _fileContent;
    }

    public void setFileContent(Byte[] _fileContent) {
        this._fileContent = _fileContent;
    }

    public int getIndex() {
        return _index;
    }

    public void setIndex(int _index) {
        this._index = _index;
    }

    public boolean isIsCode() {
        return _isCode;
    }

    public void setIsCode(boolean _isCode) {
        this._isCode = _isCode;
    }

    public boolean isIsMain() {
        return _isMain;
    }

    public void setIsMain(boolean _isMain) {
        this._isMain = _isMain;
    }

    public boolean isIsReport() {
        return _isReport;
    }

    public void setIsReport(boolean _isReport) {
        this._isReport = _isReport;
    }

    public String getRelativePath() {
        return _relativePath;
    }

    public void setRelativePath(String _path) {
        this._relativePath = _path;
    }
    
    public String getRootPath() {
        return _rootPath;
    }

    public void setRootPath(String _rootPath) {
        this._relativePath = _rootPath;
    }
    
    public String getAbsolutePath() {
        if(_relativePath==null) {
            return null;
        }
        if(_rootPath==null) {
            return _relativePath;
        }
        
        if(_rootPath.endsWith("/") || _rootPath.endsWith("\\")) {
            _rootPath=_rootPath.substring(0, _rootPath.length()-1);
        }
        return _rootPath + File.separator + _relativePath;
    }
}
