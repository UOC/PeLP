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
package edu.uoc.pelp.engine.aem.exception;

/**
 * Unsupported language exception class for PeLP. Launched when detected a Project with
 * an unsupported programming language.
 * @author Xavier Baró
 */
public class LanguageAEMPelpException extends AEMPelpException {

    public LanguageAEMPelpException(String msg) {
        super(msg);
    }

    public LanguageAEMPelpException() {
        super("Invalid Programming Language. It is not supported.");
    }  
}
