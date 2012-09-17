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
package edu.uoc.pelp.test.model.dao;

import edu.uoc.pelp.engine.activity.ActivityTestResult;
import edu.uoc.pelp.engine.deliver.DeliverID;
import edu.uoc.pelp.model.dao.IDeliverTestResultDAO;
import edu.uoc.pelp.model.vo.DeliverPK;
import edu.uoc.pelp.model.vo.DeliverTestResult;
import edu.uoc.pelp.model.vo.DeliverTestResultPK;
import edu.uoc.pelp.model.vo.ObjectFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class implements a DAO object for table deliverTestResults
 * @author Xavier Baró
 */
public class DeliverTestResultDAO implements IDeliverTestResultDAO {
    
    /**
     * Table simulating the database table
     */
    private HashMap<DeliverTestResultPK,DeliverTestResult> _deliverTestResults=new HashMap<DeliverTestResultPK,DeliverTestResult>();


    @Override
    public boolean add(DeliverID deliverID, ActivityTestResult testResult) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(DeliverID deliverID, ActivityTestResult testResult) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean update(DeliverID deliverID, ActivityTestResult testResult) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DeliverTestResult> find(DeliverID deliverID) {
        // Obtain the deliver primary key
        DeliverPK deliverPK=ObjectFactory.getDeliverPK(deliverID);
        if(deliverPK==null) {
            return null;
        }
        
        // Search in the database
        ArrayList<DeliverTestResult> returnList=new ArrayList<DeliverTestResult>();
        for(DeliverTestResult test:_deliverTestResults.values()) {
            if(test.getDeliverTestResultPK().getDeliverPK().equals(deliverPK)) {
                returnList.add(test);
            }
        }
        
        return returnList;
        
    }

}
