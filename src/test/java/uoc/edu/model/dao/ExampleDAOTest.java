package uoc.edu.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import uoc.edu.pelp.model.dao.ExampleDAO;
import uoc.edu.pelp.model.vo.Example;

@ContextConfiguration("/test-daos.xml")
public class ExampleDAOTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Resource
	ExampleDAO exampleDAO;
	
	Example example1, example2, example3;
	
	private Example addExample(String name, String url) {
		Example e = new Example();
		e.setName(name);
		e.setUrl(url);
		
		exampleDAO.save(e);

		return e;
	}

	private void assertExample(Example a, Example b) {
		assertExample(a, b.getId(), b.getName(), b.getUrl());
	}
	
	private void assertExample(Example a, Integer id, String name, String url) {
		assertNotNull(a);
		assertEquals("Id of example MUST be \"" + id+ "\" but is \"" + a.getId() + "\"", a.getId(), id);		
		assertEquals("Name of example MUST be \"" + name + "\" but is \"" + a.getName() + "\"", a.getName(), name);
		assertEquals("Url of example MUST be \"" + url + "\" but is \"" + a.getUrl() + "\"", a.getUrl(), url);
	}

	@Before
	public void setUpInsideTransaction() {
		this.example1 = addExample("example1", "url1");
		this.example2 = addExample("example2", "url2");
		this.example3 = addExample("example3", "url3");
	}
	
	@Test
	public void findAll() {
		List<Example> examples = exampleDAO.findAll();
		assertNotNull(examples);
		assertTrue("There MUST be 3 examples but there are " + examples.size() , examples.size() == 3);
	}
		
	@Test
	public void find() {
		Example found = exampleDAO.find(example1.getId());
		assertNotNull(found);
		assertExample(found, example1);
		
		found = exampleDAO.find(123);
		assertNull(found);
	}
	
	@Test
	public void save() {
		List<Example> examples = exampleDAO.findAll();
		assertNotNull(examples);
		assertTrue("There MUST be 3 examples but there are " + examples.size() , examples.size() == 3);
		int size = examples.size();
		
		Example added = addExample("example4", "url4");
		assertExample(added, added.getId(), "example4", "url4");
		assertTrue("Example id MUST be valid", added.getId().intValue() > 0);
		examples = exampleDAO.findAll();
		assertTrue("Actual count of examples (" + examples.size() + ") MUST be greater than previous one (" + size + ") by one element", examples.size() == size + 1);
		size = examples.size();
		
		// update account
		Integer id = added.getId();
		added.setUrl("new url");
		exampleDAO.save(added);
		assertExample(added, id, "example4", "new url");
		assertEquals("Example id MUST be the same as before saving", added.getId(), id);
		assertTrue("Example id MUST be valid", added.getId().intValue() > 0);
		examples = exampleDAO.findAll();
		assertTrue("Actual count of examples (" + examples.size() + ") MUST be equals than previous one (" + size + ")", examples.size() == size);

		// nothing to update
		exampleDAO.save(added);
		assertExample(added, id, "example4", "new url");
		assertEquals("Example id MUST be the same as before saving", added.getId(), id);
		assertTrue("Example id MUST be valid", added.getId().intValue() > 0);
		examples = exampleDAO.findAll();
		assertTrue("Actual count of examples (" + examples.size() + ") MUST be equals than previous one (" + size + ")", examples.size() == size);
	}
	
	@Test
	public void delete() {
		List<Example> examples = exampleDAO.findAll();
		assertNotNull(examples);
		assertTrue("There MUST be 3 examples but there are " + examples.size() , examples.size() == 3);
		int size = examples.size();

		Example firstExample = examples.get(0);
		Integer id = firstExample.getId();
		exampleDAO.delete(id);
		firstExample = exampleDAO.find(id);
		assertNull(firstExample);
		
		examples = exampleDAO.findAll();
		assertNotNull(examples);
		assertTrue("Actual count of examples (" + examples.size() + ") MUST be less than previous one (" + size + ") by one element", size == examples.size() + 1);
		
		exampleDAO.delete(new Integer(123));
		examples = exampleDAO.findAll();
		assertNotNull(examples);
		assertTrue("Actual count of examples (" + examples.size() + ") MUST be less than previous one (" + size + ") by one element", size == examples.size() + 1);
	}	
}
