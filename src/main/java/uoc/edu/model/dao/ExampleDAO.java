/**
 * 
 */
package uoc.edu.model.dao;

import java.util.List;

import uoc.edu.model.vo.Example;

/**
 * @author xaracil
 */
public interface ExampleDAO {
	public List<Example> findAll();
	public Example find(Integer id);
	public void save(Example service);
	public void delete(Integer id);
}
