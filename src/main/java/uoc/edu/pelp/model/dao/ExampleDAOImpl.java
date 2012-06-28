/**
 * 
 */
package uoc.edu.pelp.model.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import uoc.edu.pelp.model.vo.Example;

/**
 * @author xaracil
 *
 */
public class ExampleDAOImpl extends HibernateDaoSupport implements ExampleDAO {

	/* (non-Javadoc)
	 * @see uoc.edu.model.dao.ExampleDAO#findAll()
	 */
	@SuppressWarnings("unchecked")
	public List<Example> findAll() {	
        Query query = getSession().createQuery("from Example e order by e.name asc");
        return query.list();
	}

	/* (non-Javadoc)
	 * @see uoc.edu.model.dao.ExampleDAO#find(java.lang.Integer)
	 */
	public Example find(Integer id) {
		return (Example) getSession().get(Example.class, id);
	}

	/* (non-Javadoc)
	 * @see uoc.edu.model.dao.ExampleDAO#save(uoc.edu.oauth.model.vo.OAuthService)
	 */
	public void save(Example service) {
		getSession().saveOrUpdate(service);
	}

	/* (non-Javadoc)
	 * @see uoc.edu.model.dao.ExampleDAO#delete(uoc.edu.oauth.model.vo.OAuthService)
	 */
	public void delete(Integer id) {
		Example service = find(id);
		if (service != null) {
			getSession().delete(service);
		}
	}

}
