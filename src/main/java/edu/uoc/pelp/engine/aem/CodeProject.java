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

import edu.uoc.pelp.exception.AuthPelpException;
import edu.uoc.pelp.exception.ExecPelpException;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represent a programming project consisting of several code files.
 * @author Xavier Baró
 */
public class CodeProject {
    /**
     * Enumerator to store the source of the code
     */
    public enum ProjectSource {String,Files};
    
    /**
     * Stores the source of the code
     */
    private ProjectSource _codeSource=null;
    
    /**
     * Stores the source code in projects without files
     */
    private String _strCode=null;
    
    /**
     * Path where files are stored
     */
    private File _path=null;
    
    /**
     * Main file of the project. Relative to the project path.
     */
    private File _mainFile=null;
    
    /**
     * Programming Language
     */
    private String _language=null;
    
    /**
     * All the files of the project, relative to the project path. Main file must be in this list.
     */
    private ArrayList<File> _srcFiles=null;
    
    /**
     * Default constructor for CodeProject from source files
     * @param rootPath Path where files are stored.
     */
    public CodeProject(File rootPath) {
        
        // Set the root path
        _path=rootPath;
        
        // Set the source as code files
        _codeSource=ProjectSource.Files;
        
        // No string code can be provided
        _strCode=null;
    }
    
    /**
     * Default constructor for CodeProject from source files
     * @param languageID Programming language identifier
     * @param code String containing the code
     */
    public CodeProject(String languageID, String code) {
        // String projects do not have root path for files
        _path=null;
        
        // Store the code
        _strCode=code;
        
        // Store the language ID
        _language=languageID;
        
        // Set the source as string
        _codeSource=ProjectSource.String;
    }
    
    /**
     * Get the code source
     * @return Code source value
     */
    public ProjectSource getProjectSourceType() {
        return _codeSource;
    }
    
    /**
     * Change the root path for the given Code Project
     * @param rootPath Root path all files are relative to. Existing files must be in the new path
     * @throws ExecPelpException If some of the existing files are outside the new path
     */
    public final void changeRootPath(File rootPath) throws ExecPelpException{
        if(ProjectSource.Files.equals(_codeSource)) {
            // Create a copy of the arraylist
            File[] srcFiles=new File[_srcFiles.size()];
            _srcFiles.toArray(srcFiles);
            for(File f:srcFiles){
                File newFile=getRelativePath(getAbsolutePath(f,_path),rootPath);
                if(!getAbsolutePath(newFile,rootPath).exists()) {
                    throw new ExecPelpException("File <" + getAbsolutePath(f,_path).getPath() + "> cannot be converted to <" + getAbsolutePath(newFile,rootPath).getPath() + ">");
                }
                // Remove old file and insert new one
                _srcFiles.remove(f);
                _srcFiles.add(f);
            }
            _path=rootPath;
        }
    }
    
    /**
     * Checks if the project files are correct or not.
     * @return True if all of them exist and are readable or False otherwise.
     */
    public boolean checkProject() {
        // Check that files exist and can be readed
        if(ProjectSource.Files.equals(_codeSource)) {
            for(File f:getAbsoluteFiles()) {
                try {
                    checkFile(f);
                } catch (ExecPelpException ex) {
                    return false;
                }
            }
        } else if(ProjectSource.String.equals(_codeSource)) {
            if(_strCode==null || _strCode.isEmpty() || _language==null) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Copy a file from source location to destination location
     * @param src Source filename
     * @param dst Destionation filename
     * @return True if the file is successfully copied or False otherwise
     * @deprecated As JDK 1.7 introduces Files package, replaced by {@link #Files.copy(Path,Path,CopyOption...)}
     */
    public static boolean copyFile(File src, File dst) {
        // When use JDK 1.7, consider using the following implementation
        // Files.copy(src,dst,REPLACE_EXISTING)
        
        boolean retVal=false;
        InputStream in =null;
        OutputStream out = null;
                
        try {
            // Create IO streams
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            
            retVal=true;
                        
        } catch(FileNotFoundException ex){
            retVal=false;
        } catch(IOException e) {
            retVal=false;
        } finally {
            if(in!=null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(CodeProject.class.getName()).log(Level.SEVERE, null, ex);
                    retVal=false;
                }
            }
            if(out!=null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(CodeProject.class.getName()).log(Level.SEVERE, null, ex);
                    retVal=false;
                }
            }
        }
        
        return retVal;
    }
    
    /**
     * Add a new file to the project. Files are assumed to be relative to rootPath, if not,
     * the rootPath is removed from the given path. If rootPath is null, files must be absolute.
     * @param file File to add. The file must exists and be inside the rootPath.
     * @throws ExecPelpException If the file does not exist or is outside the rootPath
     */
    public void addFile(File file) throws ExecPelpException {
        File newFile=null;
        
        if(ProjectSource.Files.equals(_codeSource)) {
            // Get relative path to the root path
            if(_path!=null) {
                newFile=getRelativePath(file,_path);
            } else {
                newFile=file.getAbsoluteFile();
            }
            
            // Check that new file is in the root path
            if(newFile==null) {
                throw new ExecPelpException("File <" + file.getPath() + "> is outside with the root path of the project <" + _path.getPath() + ">");
            }
            
            // Check the new file (path and existence)
            checkFile(newFile);

            // Create the arraylist
            if(_srcFiles==null) {
                _srcFiles=new ArrayList<File>();
            }

            // Check that the file is not in the list
            boolean found=false;
            for(File f:_srcFiles) {
                if(f.equals(newFile)) {
                    found=true;
                    break;
                }
            }

            // If the file is not in the list, add it
            if(!found) {
                _srcFiles.add(newFile);
            }
        }
    }
    
    /**
     * Add a new main file to the project. File is assumed to be relative to rootPath, if not,
     * the rootPath is removed from its path. If it does not exist the list of files, it is also 
     * added to the list of files.
     * @param newFile File to add. The file must exists and be inside the rootPath.
     * @throws ExecPelpException If the file does not exist or is outside the rootPath
     */
    public void addMainFile(File newFile) throws ExecPelpException{
        
        if(ProjectSource.Files.equals(_codeSource)) {
            // Check the new file (path and existence)
            checkFile(newFile);

            // Get relative path to the root path
            if(_path!=null) {
                newFile=getRelativePath(newFile,null);
            }

            // Store as main file
            _mainFile=newFile;

            // Add to the list of files
            addFile(newFile);
        }
    }
    
    /**
     * Move the project files to the given path, and use new location.
     * @param dstPath Final path where files are moved.
     * @return New project with moved files
     * @throws AuthPelpException Not enought rights to perform file operation
     */
    public CodeProject moveFiles(File dstPath) throws AuthPelpException, ExecPelpException {
        CodeProject newProject=clone();
                
        if(ProjectSource.Files.equals(_codeSource)) {
        
            // Check that files can be readed
            for(File f:getAbsoluteFiles()) {
                if(!f.canRead()) {
                    throw new AuthPelpException("Cannot read file " + f.getAbsolutePath());
                }
            }

            // Check permisions for output directory
            if(!dstPath.exists()) {
                // Create the output directory
                if(!dstPath.mkdirs()) {
                    throw new AuthPelpException("Cannot create output directory: " + dstPath.getAbsolutePath());
                }
            } else if(!dstPath.isDirectory()){
                throw new ExecPelpException("Cannot move to a file. Output path must be a directory.");
            }

            // Move all files to the destination directory        
            for(File f:getRelativeFiles()) {
                File src=null;
                File dst=null;
                try {
                    src=new File(_path.getCanonicalPath()+File.separator+f.getPath());
                    dst=new File(dstPath.getCanonicalPath()+File.separator+f.getPath());
                    if(!src.renameTo(dst)) {
                        throw new ExecPelpException("Cannot move file \"" + src.getAbsolutePath() + "\" to \"" + dst.getAbsolutePath() + "\"");
                    }
                } catch (IOException ex) {
                    throw new ExecPelpException("Cannot move file \"" + src.getAbsolutePath() + "\" to \"" + dst.getAbsolutePath() + "\"");
                }
            }   

            // Once moved, change the root path
            newProject._path=dstPath;
        }
        
        return newProject;
    }     
    
    /**
     * Copy the project files to the given path.
     * @param dstPath Final path where files are copyed.
     * @return New project with moved files
     * @throws AuthPelpException Not enought rights to perform file operation
     */
    public CodeProject copyFiles(File dstPath) throws AuthPelpException, ExecPelpException {
        CodeProject newProject=clone();
        
        if(ProjectSource.Files.equals(_codeSource)) {
            // Check that files can be readed
            for(File f:getAbsoluteFiles()) {
                if(!f.canRead()) {
                    throw new AuthPelpException("Cannot read file " + f.getAbsolutePath());
                }
            }

            // Check permisions for output directory
            if(!dstPath.exists()) {
                // Create the output directory
                if(!dstPath.mkdirs()) {
                    throw new AuthPelpException("Cannot create output directory: " + dstPath.getAbsolutePath());
                }
            } else if(!dstPath.isDirectory()){
                throw new ExecPelpException("Cannot move to a file. Output path must be a directory.");
            }

            // Copy all files to the destination directory        
            for(File f:getRelativeFiles()) {
                File src=null;
                File dst=null;
                try {
                    src=new File(_path.getCanonicalPath()+File.separator+f.getPath());
                    dst=new File(dstPath.getCanonicalPath()+File.separator+f.getPath());
                    if(!copyFile(src,dst)) {
                        throw new ExecPelpException("Cannot copy file \"" + src.getAbsolutePath() + "\" to \"" + dst.getAbsolutePath() + "\"");
                    }
                } catch (IOException ex) {
                    throw new ExecPelpException("Cannot copy file \"" + src.getAbsolutePath() + "\" to \"" + dst.getAbsolutePath() + "\"");
                }
            }
            
            // Once copyed, change the root path
            newProject._path=dstPath;
        }
        
        return newProject;
    }     
    
    /**
     * Gets the list of files in the current project.
     * @return List with the relative path of each file in the project
     */
    public File[] getRelativeFiles() {
        File[] retArray=null;
        if(_srcFiles!=null) {
            retArray=new File[_srcFiles.size()];
            _srcFiles.toArray(retArray);
            for(int i=0;i<retArray.length;i++) {
                retArray[i]=getRelativePath(retArray[i],null);
            }            
        } else {
            retArray=new File[0];
        }
                
        return retArray;
    }
    
    /**
     * Gets the list of files in the current project.
     * @return List with the absolute path of each file in the project
     */
    public File[] getAbsoluteFiles() {                
        File[] retArray=getRelativeFiles();
        for(int i=0;i<retArray.length;i++) {
            retArray[i]=getAbsolutePath(retArray[i],null);
        }
        return retArray;
    }
    
    /**
     * Returns the absolut path of a given relative/absolute path from a certain root path.
     * @param file Relative or Absolute file.
     * @param rootPath Root path. If null, default root path is used.
     * @return Absolute file object.
     */
    public File getAbsolutePath(File file,File rootPath) {
        File absFile=null;
        
        // If no rootPath is provided, use default
        if(rootPath==null) {
            rootPath=_path;
        }
        
        // In case that project path is also null, consider the root as root path
        if(rootPath==null && _path==null) {
            return file.getAbsoluteFile();
        }
        
        // Be sure that this file is in relative form
        File relFile=getRelativePath(file,null);
        
        // Add the root path to the file
        if(relFile!=null) {
            absFile=new File(rootPath.getAbsolutePath()+File.separator+relFile.getPath());
        } else {
            String strPath=file.getPath();
            if(strPath.length()>0 && strPath.charAt(0)==File.separatorChar) {
                strPath.substring(1);
            }
            absFile=new File(rootPath.getAbsolutePath()+File.separator+strPath);
        }
        
        return absFile;
    }

    /**
     * Returns the relative path of a given file to the given root path. 
     * If root path is null, project root path is used, and in case of beeing null, 
     * paths are considered absolute
     * @param file Relative or Absolute file.
     * @param rootPath Path that will be removed
     * @return Relative file object or null if it is outside the used path
     */
    public File getRelativePath(File file,File rootPath) {
        File relFile=null;
        
        // Get the default path
        if(rootPath==null) {
            rootPath=_path;
        }
        
        // If no root path, file is considered absolute.
        if(rootPath==null) {
            return file.getAbsoluteFile();
        }
        
        try {
            // Get canonical paths
            String absFilePath=file.getCanonicalPath();
            
            // If no project path is given, parent folder is considered as root path
            String absRootPath=rootPath.getCanonicalPath();
                   
            // Find root path in file path
            if(absFilePath.contains(absRootPath)) {
                String strPath=absFilePath.substring(absRootPath.length());
                if(strPath.charAt(0)==File.separatorChar) {
                    strPath=strPath.substring(1);
                }
                if(strPath.length()>0) {
                    relFile=new File(strPath);
                }
            } else {
                // Chech if it is outside the path or if it already is a relative path
                if(file.equals(file.getAbsoluteFile())) {
                    // The given path is an absolute path, outside the given path. Therefore, return null.
                    return null;
                } else {
                    // The given file is a relative path. Add the working path
                    String strPath=file.getPath();
                    if(strPath.length()>0 && strPath.charAt(0)==File.separatorChar) {
                        strPath.substring(1);
                    }
                    relFile=new File(strPath);                
                }
            }
        } catch (IOException ex) {
            return null;
        }
        
        return relFile;
    }
    
    /**
     * Gets the main file
     * @return Main file
     */
    public File getMainFile() {
        return _mainFile;
    }

    /**
     * Ckech that a given file exist, is readable, and is inside the root path
     * @param newFile File to be checked
     * @throws ExecPelpException If the file does not exist or is out of the root path.
     */
    private void checkFile(File file) throws ExecPelpException {
        File relFile=getRelativePath(file,null);
        
        // Check that given file is in the root path.
        if(relFile==null) {
            throw new ExecPelpException("File is not in the root path.");
        }
        
        // Check that given file exists
        if(!getAbsolutePath(relFile,null).exists()) {
            throw new ExecPelpException("File not found: \"" + getAbsolutePath(relFile,null).getAbsolutePath() + "\"");
        }
        
        // Check that given file is readable
        if(!getAbsolutePath(relFile,null).canRead()) {
            throw new ExecPelpException("Cannot reade file: \"" + getAbsolutePath(relFile,null).getAbsolutePath() + "\"");
        }
    }
    
    /**
     * Assign the language of the Code Project
     * @param language String representing the language
     */
    public void setLanguage(String language) {
        _language=language.toUpperCase().trim();
    }
    
    /**
     * Get the language assigned to this Code Project, or null if it is unknown.
     * @return The language ID for this project or null if it is not assigned
     */
    public String getLanguage() {
        return _language;        
    }

    /**
     * Gets the root path for this project
     * @return Root path of the project
     */
    public File getRootPath() {
        return _path;
    }
    
    /**
     * Get the code of the project. If the project is not a String source code, it will be null.
     * @return String with the project code
     */
    public String getProjectCode() {
        return _strCode;
    }
    
    /**
     * Find the maximum shared path between all files in the project
     * @return Estimated root path or null if files do not share a common root.
     */
    public File getEstimatedRootPath() {
        File rootPath=null;
        String estimatedPath=null;
        
        for(File f:_srcFiles){
            // Get the new abs path
            String filePath=f.getParentFile().getAbsolutePath();
            
            // In the first iteration, store the whole path
            if(estimatedPath==null) {
                estimatedPath=filePath;
            }
            
            // If paths are diferent, combine both
            if(!estimatedPath.equals(filePath)) {
                int length=Math.min(estimatedPath.length(),filePath.length());
                int sharedLen=0;
                for(int i=0;i<length;i++) {
                    if(estimatedPath.charAt(i)==filePath.charAt(i)) {
                        sharedLen++;
                    } else {
                        break;
                    }
                }
                // Update the shared path
                if(sharedLen>0) {
                    estimatedPath=estimatedPath.substring(sharedLen);
                } else {
                    estimatedPath=null;
                    break;
                }
            }
        }
        
        // Create the path from remaining string
        if(estimatedPath!=null) {
            rootPath=new File(estimatedPath);
        }
        
        return rootPath;
    }
    
    @Override
    public CodeProject clone() {
        CodeProject newObj=null;
        
        switch(_codeSource) {
            case String:
                newObj=new CodeProject(_language,_strCode);
                break;
            case Files:
                newObj=new CodeProject(_path);
                newObj._language=_language;
                newObj._mainFile=_mainFile;
                newObj._srcFiles=new ArrayList<File>();
                for(File f:_srcFiles) {
                    newObj._srcFiles.add(f);
                }
                break;
        }
        
        return newObj;
    }
}
