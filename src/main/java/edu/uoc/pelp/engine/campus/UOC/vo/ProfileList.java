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
package edu.uoc.pelp.engine.campus.UOC.vo;

/**
 *  Class encoding a list of profiles
 *  @author Xavier Baró
 */
public class ProfileList implements java.io.Serializable {

  private java.util.Collection<Profile> profiles;


  public java.util.Collection<Profile> getProfiles() {
    return this.profiles;
  }
  
  public void setProfiles(java.util.Collection<Profile> profiles) {
    this.profiles = profiles;
  }
}
