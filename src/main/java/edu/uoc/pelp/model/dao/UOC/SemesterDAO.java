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
package edu.uoc.pelp.model.dao.UOC;

import edu.uoc.pelp.engine.campus.ITimePeriod;
import edu.uoc.pelp.model.dao.ITimePeriodDAO;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Implements the DAO object for the SemesterDO class
 * @author Xavier Baró
 */
public class SemesterDAO extends HibernateDaoSupport implements ITimePeriodDAO {

    public boolean save(ITimePeriod object) {
        getSession().saveOrUpdate(object);
        return true;
    }

    public boolean delete(ITimePeriod object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean update(ITimePeriod object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ITimePeriod> findAll() {
        org.hibernate.Query query = getSession().createQuery("from semester s order by s.id asc");
        return query.list();
    }

    public List<ITimePeriod> findActive() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ITimePeriod find(ITimePeriod object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
