package uoc.edu.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import uoc.edu.model.vo.Example;

@ContextConfiguration("/test-services.xml")
public class ExampleServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Resource
	
	
	
	Example example1, example2;

	private Example addExample(String name, String url) {
		Example e = new Example();
		e.setName(name);
		e.setUrl(url);
		
	

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
	}
	
	
	@Test
	public void findAll() {
		
		
	}
	
	@Test
	public void find() {
		
		
	
	}
	
	@Test
	public void delete() {
	
	}	
}
