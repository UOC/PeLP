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
package edu.uoc.pelp.engine.activity;

import java.util.Date;
import java.util.HashMap;

/**
 * This class implements an activity
 * @author Xavier Baró
 */
public class Activity implements Comparable<Activity>{
    /**
     * Activity unique identifier
     */
    private ActivityID _activityID=null;
    
    /**
     * Activity description in multiple languages.
     */
    HashMap<String,String> _description=new HashMap<String,String>();
    
    /**
     * Starting date for this activity
     */
    private Date _start=null;
    
    /**
     * Ending date for this activity
     */
    private Date _end=null;
    
    /**
     * Indicates the language for this activity (null for any)
     */
    private String _language=null;
    
    /**
     * Indicates the number of delivers that students can perform to this activity (null means infinite)
     */
    private Integer _maxDelivers=null;
    
    /**
     * Constructor for Activity class
     */
    public Activity() {
        
    }
    
    /**
     * Constructor for Activity class
     * @param activityID Identifier for the activity
     * @param start Starting date. If null, is considered started from always.
     * @param end Ending date. If null, will remain opened forever.
     */
    public Activity(ActivityID activityID, Date start, Date end) {
        _activityID=activityID;
        _start=start;
        _end=end;
        
        // Check the parameters
        if(_activityID==null) {
            throw new IllegalArgumentException("Activity ID cannot be null.");
        }
        if(_start!=null && _end!=null) {
            if(_start.after(end)) {
                throw new IllegalArgumentException("Ending date must be posterior to the starting date.");
            }
        }
    }
    
    /**
     * Constructor for Activity class
     * @param activity Object to be copied
     */
    public Activity(ActivityID id, Activity object) {
        _activityID=id;
        _start=object._start;
        _end=object._end;
        _language=object._language;
        _maxDelivers=object._maxDelivers;
        for(String lang:object.getLanguageCodes()) {
            setDescription(lang, object.getDescription(lang));
        }
    }
    
    /**
     * Copy constructor for Activity class
     * @param activity Object to be copied
     */
    public Activity(Activity object) {
        _activityID=new ActivityID(object._activityID.subjectID,object._activityID.index);
        _start=object._start;
        _end=object._end;
        _language=object._language;
        _maxDelivers=object._maxDelivers;
        for(String lang:object.getLanguageCodes()) {
            setDescription(lang, object.getDescription(lang));
        }
    }
    
    /**
     * Ckecks if the activity is active at this moment
     * @return True if the current date is compatible with openning and clossing dates
     */
    public boolean isActive() {
        Date now=new Date();
        
        // Check initial restriction
        if(_start!=null) {
            if(_start.after(now)) {
                return false;
            }
        }
        
        // Check final restriction
        if(_end!=null) {
            if(_end.before(now)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public Activity clone() {
        return new Activity(this);
    }
    
    /**
     * Assigns the activity Identifier. Only valid if the current identifier is null.
     * @par id Activity ID 
     */
    public void setActivityID(ActivityID id) {
        if(_activityID==null) {
            _activityID=id;
        }
    }
    
    /**
     * Gets the activity ID
     * @return Activity ID 
     */
    public ActivityID getActivity() {
        return _activityID;
    }
    
    /**
     * Gets the starting time
     * @return Date object with starting time.
     */
    public Date getStart() {
        return _start;
    }
    
    /**
     * Gets the ending time
     * @return Date object with ending time.
     */
    public Date getEnd() {
        return _end;
    }
    
    /**
     * Set the initial date.
     * @param start Initial date or null to make an always opened activity
     */
    public void setStart(Date start) {
        _start=start;
    }
    
    /**
     * Set the ending date.
     * @param end Ending date or null to make anever closed activity
     */
    public void setEnd(Date end) {
        _end=end;
    }
    
    /**
     * Get the description of this activity for a certain language
     * @param languageCode Code for desired language (CAT for Catalan, ES for Spanish, ENG for English, ...)
     * @return Description of the activity
     */
    public String getDescription(String languageCode) {
        return _description.get(languageCode);
    }
    
    /**
     * Adds a description for the activity in a certain language.
     * @param languageCode Code for desired language (CAT for Catalan, ES for Spanish, ENG for English, ...)
     * @param description Description of the activity
     */
    final public void setDescription(String languageCode,String description) {
        // Remove old descriptions
        if(_description.containsKey(languageCode)) {
            _description.remove(languageCode);
        }
        // Add new description
        if(description!=null) {
            _description.put(languageCode, description);
        }
    }
    
    /**
     * Obtain the list of available language codes
     * @return Array with the codes of the languages for which a description is provided
     */
    public String[] getLanguageCodes() {
        String[] retList=new String[_description.keySet().size()];
        _description.keySet().toArray(retList);
        return retList;
    }
    
    /**
     * Set the maximum number of delivers for this activity
     * @param numDelivers Number of delivers or null for infinite
     */
    public void setMaxDelivers(Integer numDelivers) {
        _maxDelivers=numDelivers;
    }

    /**
     * Get the maximum number of delivers for this activity.
     * @return Number of deliversof or null for infinite
     */
    public Integer getMaxDelivers() {
        return _maxDelivers;
    }
    
    /**
     * Set the programming language for this activity.
     * @param language Language code or null for undefined language
     */
    public void setLanguage(String language) {
        _language=language;
    }
    
    /**
     * Get the programming language for this activity
     * @return Language code or null if it is not defined.
     */
    public String getLanguage() {
        return _language;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Activity other = (Activity) obj;
        if (this._activityID != other._activityID && (this._activityID == null || !this._activityID.equals(other._activityID))) {
            return false;
        }
        if (this._description != other._description && (this._description == null || !this._description.equals(other._description))) {
            return false;
        }
        if (this._start != other._start && (this._start == null || !this._start.equals(other._start))) {
            if(this._start!=null && other._start!=null) {
                long diff=Math.abs(this._start.getTime()-other._start.getTime());
                if(diff>=1000) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (this._end != other._end && (this._end == null || !this._end.equals(other._end))) {
            if(this._end!=null && other._end!=null) {
                long diff=Math.abs(this._end.getTime()-other._end.getTime());
                if(diff>=1000) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (this._language != other._language && (this._language == null || !this._language.equals(other._language))) {
            return false;
        }
        if (this._maxDelivers != other._maxDelivers && (this._maxDelivers == null || !this._maxDelivers.equals(other._maxDelivers))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this._activityID != null ? this._activityID.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Activity t) {
        if(t==null) {
            return -1;
        }
        ActivityID id=t.getActivity();
        if(_activityID==null) {
            if(id==null) {
                return 0;
            } else {
                return 1;
            }
        }
        return _activityID.compareTo(id);
    }            
}
