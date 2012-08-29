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
package edu.uoc.pelp.test.conf;

import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.campus.ISubjectID;
import edu.uoc.pelp.engine.campus.UOC.Semester;
import edu.uoc.pelp.engine.campus.UOC.SubjectID;
import java.io.File;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides an implementation for the IPelpConfiguration interface,
 * storing predefined configuration values for different predefined machines. It uses
 * the machine name to decide the proper configuration values.
 * @author Xavier Baró
 */
public class PCPelpConfiguration implements IPelpConfiguration {
    
    /**
     * Table of temporal paths
     */
    private static HashMap<String,File> _tempPath=new HashMap<String,File>() {
        {
            put("XBARO-W7",new File("D:\\TempPELP\\Temp"));
        }
    };
    
    /**
     * Table of delivery paths
     */
    private static HashMap<String,File> _deliveryPath=new HashMap<String,File>() {
        {
            put("XBARO-W7",new File("D:\\TempPELP\\Delivers"));
        }
    };
    
    /**
     * Table of C Compiler paths
     */
    private static HashMap<String,File> _CCompiler=new HashMap<String,File>() {
        {
            put("XBARO-W7",new File("D:\\MinGW\\bin\\gcc.exe"));
        }
    };
    
    /**
     * Table of active subjects
     */
    private static HashMap<String,ISubjectID[]> _configActiveSubjects=new HashMap<String,ISubjectID[]>() {
        {
            ISubjectID[] ids={new SubjectID("05.554",new Semester("20111")),
                              new SubjectID("05.554",new Semester("20112")),
                              new SubjectID("05.555",new Semester("20112"))};
            put("XBARO-W7",ids);
        }
    };
    
    /**
     * Table of configuration descriptions
     */
    private static HashMap<String,String> _configDesc=new HashMap<String,String>() {
        {
            put("XBARO-W7","Laptop with Windows 7 (64bits)");
        }
    };
    
    public String getEnvironmentID() {
        String machineName=null;
        
        try {
            java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
            machineName=localMachine.getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(PCPelpConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return machineName;
        
    }

    public String getEnvironmentDesc() {
        return _configDesc.get(getEnvironmentID());
    }

    public File getTempPath() {
        return _tempPath.get(getEnvironmentID());
    }

    public File getDeliveryPath() {
        return _deliveryPath.get(getEnvironmentID());
    }

    public File getCompiler(String languageID) {
        File compiler=null;
        
        // Check that a correct language is given
        if(languageID==null) {
            return null;
        }
        
        // Search for compilers
        if(languageID.equalsIgnoreCase("C")) {
            compiler=_CCompiler.get(getEnvironmentID());
        }
        
        return compiler;
    }

    public ISubjectID[] getActiveSubjects() {
        return _configActiveSubjects.get(getEnvironmentID());
    }
}
