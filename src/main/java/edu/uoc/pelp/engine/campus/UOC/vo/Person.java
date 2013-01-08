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

import java.util.Date;

/**
 *  UOC Campus person information
 *  @author Xavier Baró
 */
public class Person {

  private java.lang.String id;
  private java.lang.String username;
  private java.lang.String name;
  private java.lang.String surname1;
  private java.lang.String surname2;
  private java.lang.String email;
  private Profile[] profiles;
  private java.lang.String userNumber;
  private java.lang.String hobbies;
  private java.lang.String skills;
  private java.lang.String about;
  private java.lang.String NGOes;
  private java.lang.String languages;
  private java.lang.String secondaryEmail;
  private java.lang.String blog;
  private java.lang.String personalSite;
  private java.util.Date lastUpdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname1() {
        return surname1;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile[] getProfiles() {
        return profiles;
    }

    public void setProfiles(Profile[] profiles) {
        this.profiles = profiles;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getNGOes() {
        return NGOes;
    }

    public void setNGOes(String NGOes) {
        this.NGOes = NGOes;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getPersonalSite() {
        return personalSite;
    }

    public void setPersonalSite(String personalSite) {
        this.personalSite = personalSite;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
