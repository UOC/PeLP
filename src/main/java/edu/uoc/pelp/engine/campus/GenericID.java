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
package edu.uoc.pelp.engine.campus;

import biz.source_code.base64Coder.Base64Coder;
import edu.uoc.pelp.exception.PelpException;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements a generic implementation for the IPelpID interface. This class can be 
 * extended in order to create basic identifier classes for each campus entity.
 * @author Xavier Baró
 */
public abstract class GenericID implements IPelpID, Serializable {
              
    /**
     * Method that copy the attribute values from a given object to the current object.
     * @param genericID Object with the values to copy from
     * @throws PelpException The given object cannot be copied to the target object.
     */
    abstract protected void copyData(GenericID genericID) throws PelpException ;
    
    /**
     * Retrieves the content of a serialized object.
     * @param str String representation of the object data
     * @return Identified loaded or null if an error occurr.
     */
    public IPelpID parse( String str ) {
        ObjectInputStream ois = null;
        try {
            byte [] data = Base64Coder.decode( str );
            ois = new ObjectInputStream( 
                          new ByteArrayInputStream(  data ) );
            Object loadedObject = ois.readObject();
            ois.close();
            return (IPelpID)loadedObject;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenericID.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GenericID.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(GenericID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * Create a representation of the object content based on serialization
     * @return String serialization of the object or null if an erro occurr.
     */
    @Override
    public String toString() {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream( baos );
            oos.writeObject( this );
            oos.close();
            return new String( Base64Coder.encode( baos.toByteArray() ) );
        } catch (IOException ex) {
            Logger.getLogger(GenericID.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(GenericID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
