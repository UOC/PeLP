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
package edu.uoc.pelp.services;

import edu.uoc.pelp.bussines.UOC.UOCPelpBussines;
import edu.uoc.pelp.bussines.UOC.UOCPelpBussinesImpl;
import edu.uoc.pelp.bussines.exception.InvalidEngineException;
import edu.uoc.pelp.conf.IPelpConfiguration;
import edu.uoc.pelp.engine.campus.ICampusConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import uoc.edu.pelp.model.vo.ActivityData;
import uoc.edu.pelp.model.vo.DeliverData;
import uoc.edu.pelp.model.vo.DeliverReport;

/** 
 * Services implementation. It expects that sessionFactory, campusConnection and configuration parameters are created with spring
 * @author Xavier Baró
 */
public class PelpServiceImpl implements PelpService {

    /**
     * Session factory
     */
    protected SessionFactory sessionFactory;
    
    /**
     * Campus connection object
     */
    protected ICampusConnection campusConnection;
    
    /**
     * Configuration manager
     */
    protected IPelpConfiguration configuration;
    
    /**
     * Bussines access object
     */
    protected UOCPelpBussines _bussines;
    
    /**
     * Default constructor. If all objects are assigned by spring, the bussines object is created
     */
    public PelpServiceImpl() {
        if(sessionFactory!=null && campusConnection!=null && configuration!=null) {
            try {
                _bussines=new UOCPelpBussinesImpl(campusConnection,sessionFactory,configuration);
            } catch (InvalidEngineException ex) {
                Logger.getLogger(PelpServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public DeliverReport setDeliver(DeliverData objDeliver,
                    ActivityData objActivity) {
        
        return null;
    }

    @Override
    public List<DeliverReport> getDeliverInfo(String campusSession,
                    Boolean incBinari, ActivityData objActivityData) {
        return null;
    }
	
    @Override
    public List<DeliverReport> getDeliverInfoById(String campusSession,
                    Boolean incBinari, int deliverId) {
        return null;
    }	
	
    @Override
    public ActivityData[] getActivityInfo(ActivityData objActivityData,String campusSession){
        return null;
    }
	
    @Override
    public ActivityData[] getActivityInfoById(int activityId,String campusSession){
        return null;    
    }
	
    @Override
    public ActivityData setActivityInfo(ActivityData objActivityData,String campusSession){
            return null;
    }

    public UOCPelpBussines getBussines() {
        return _bussines;
    }

    public void setBussines(UOCPelpBussines bussines) {
        this._bussines = bussines;
    }

    public ICampusConnection getCampusConnection() {
        return campusConnection;
    }

    public void setCampusConnection(ICampusConnection campusConnection) {
        this.campusConnection = campusConnection;
    }

    public IPelpConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(IPelpConfiguration configuration) {
        this.configuration = configuration;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
 