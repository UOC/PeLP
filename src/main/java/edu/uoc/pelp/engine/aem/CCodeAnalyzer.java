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

import edu.uoc.pelp.engine.aem.exception.CompilerAEMPelpException;
import edu.uoc.pelp.engine.aem.exception.PathAEMPelpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class implements the Code Analyzer for the C programming language.
 * @author Xavier Baró
 */
public class CCodeAnalyzer extends BasicCodeAnalyzer {

    public BuildResult build(CodeProject project) throws PathAEMPelpException, CompilerAEMPelpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getLanguageID() {
        return "C";
    }

    @Override
    protected String[] getAllowedExtensions() {
        String[] exts={".c",".h"};
        return exts;
    }

    @Override
    protected boolean isMainFile(File file) {
        //TODO: Remove comments before search for main function and use more sofisticated patterns
        boolean isMain=false;
        Scanner scanner=null;
        try {
            scanner = new Scanner(new FileInputStream(file), "UTF-8");
            while (scanner.hasNextLine() && !isMain){
                if(scanner.nextLine().trim().indexOf("voidmain(")>=0 ||
                   scanner.nextLine().trim().indexOf("intmain(")>=0) {
                    isMain=true;
                }
            }
        } catch (FileNotFoundException ex) {
            isMain=false;
        } finally{
            if(scanner!=null) {
                scanner.close();
            }
        }
        
        return isMain;
    }
}
