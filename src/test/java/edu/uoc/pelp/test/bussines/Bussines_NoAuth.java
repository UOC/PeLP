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
package edu.uoc.pelp.test.bussines;

import edu.uoc.pelp.bussines.PelpBussines;
import edu.uoc.pelp.bussines.exception.InvalidCampusConnectionException;
import edu.uoc.pelp.bussines.exception.InvalidConfigurationException;
import edu.uoc.pelp.bussines.exception.InvalidEngineException;
import edu.uoc.pelp.bussines.exception.InvalidSessionFactoryException;
import edu.uoc.pelp.test.conf.PCPelpConfiguration;
import edu.uoc.pelp.test.engine.campus.TestUOC.LocalCampusConnection;
import junit.framework.Assert;

/**
 * Perform tests over the bussines class without authentication.
 * @author Xavier Baró
 */
public class Bussines_NoAuth {    
    
    private PelpBussines _bussines;   
        
    public Bussines_NoAuth() {                
        try {
            // Create the bussines object using local resource
            _bussines=new LocalPelpBussinesImpl("hibernate_test.cfg.xml");
            
            // Assign the campus connection
            _bussines.setCampusConnection(new LocalCampusConnection());
            
            // Assign the test local configuration object
            PCPelpConfiguration conf=new PCPelpConfiguration();
            _bussines.setConfiguration(conf);
           
            // Initialize the engine
            _bussines.initializeEngine();
        } catch (InvalidEngineException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidConfigurationException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidSessionFactoryException ex) {
            Assert.fail(ex.getMessage());
        } catch (InvalidCampusConnectionException ex) {
            Assert.fail(ex.getMessage());
        }
    } 
}
