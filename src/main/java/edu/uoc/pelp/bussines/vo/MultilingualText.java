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
 * Multilingual description
 * @author Xavier Baró
 */
public class MultilingualText {
    /**
     * Language code {"CAT","ESP","ENG",....}
     */
    private String _language;
    
    /**
     * MultilingualText text
     */
    private String _text;

    /**
     * Get de text
     * @return The text
     */
    public String getText() {
        return _text;
    }

    /**
     * Set de text
     * @param text The text
     */
    public void setText(String text) {
        this._text = text;
    }

    /**
     * Get de description language code for this text
     * @return Language code {"CAT","ESP","ENG",....}
     */
    public String getLanguage() {
        return _language;
    }

    /**
     * Set de description language code
     * @return Language code {"CAT","ESP","ENG",....}
     */
    public void setLanguage(String _language) {
        this._language = _language;
    }
}
