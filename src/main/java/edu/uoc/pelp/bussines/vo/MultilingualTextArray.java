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

/**
 * Array of Multilingual descriptions. Used to give a set of descriptions for a single object
 * @author Xavier Baró
 */
public class MultilingualTextArray{
    /**
     * Multilingual text Array
     */
    private MultilingualText[] _array;  
    
    public MultilingualTextArray(int size) {
        _array=new MultilingualText[size];
    }

    /**
     * Get de array of descriptions
     * @return The multilingual text array
     */
    public MultilingualText[] getArray() {
        return _array;
    }

    /**
     * Set de array of descriptions
     * @param array The multilingual text array
     */
    public void setArray(MultilingualText[] array) {
        this._array = array;
    }
    
    /** 
     * Set the multilingual text in given position
     * @param index Position of the array
     * @param text Multilingual text object
     * @return True if the addition is correct of False otherwise.
     */
    public boolean setText(int index, MultilingualText text) {
        if(index<0 && index>=_array.length) {
            return false;
        }
        _array[index]=text;
        return true;
    }
    
    /** 
     * Get the multilingual text at given position
     * @param index Position of the array
     * @return The description object or null if is not valid.
     */
    public MultilingualText getText(int index) {
        if(index<0 && index>=_array.length) {
            return null;
        }
        return _array[index];
    }
    
    /**
     * Get the size of the array
     * @return Size of the array
     */
    public int size() {
        if(_array==null) {
            return 0;
        }
        return _array.length;
    }
}
