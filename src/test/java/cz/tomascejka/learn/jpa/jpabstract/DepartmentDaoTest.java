package cz.tomascejka.learn.jpa.jpabstract;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.tomascejka.learn.jpa.jpabstract.data.Department;
import cz.tomascejka.learn.jpa.jpabstract.data.DepartmentDao;
import cz.tomascejka.learn.jpa.jpabstract.data.EmployeeDao;
import cz.tomascejka.learn.jpa.jpabstract.tx.TxManagerImpl;
import cz.tomascejka.learn.jpa.jpabstract.tx.log.EclipseLinkLogSqlStrategy;

public class DepartmentDaoTest 
{
	private static final String PERSISTENCE_UNIT_NAME = "local-unit";
	private TxManagerImpl tm;
	private DepartmentDao testedObject;
	@SuppressWarnings("unused")
	private EmployeeDao employeeDao;
	
	@Before
	public void setUp()
	{
		tm = new TxManagerImpl(PERSISTENCE_UNIT_NAME);
		tm.setSqlLogStrategy(new EclipseLinkLogSqlStrategy());//vendor-specific
		
		testedObject = new DepartmentDao();
		testedObject.setTransactionManager(tm);
		
		tm.open();
	}
	
	@Test
	public void testInsert()
	{
		/* Insert a record */
		String name = "krabathor";
		Department actual = testedObject.save(new Department(name));
		
		Assert.assertFalse("New entities cannot have empty primarKey", actual.getId() == null);
		Assert.assertEquals(name, actual.getName());
	}
	
	@Test
	public void testUpdate()
	{
		// priprava entity pro test
		Department item = testedObject.save(new Department("dibraborothar"));
		
		/* TEST Update a record */
		String name = "kaja";
		item.setName(name);
		Department actual = testedObject.save(item);
		
		Assert.assertNotNull(actual);
		Assert.assertEquals(name, actual.getName());
	}
	
	@Test
	public void testFetchAll()
	{
		/* Insert a record */
		testedObject.save(new Department("java"));
		testedObject.save(new Department("groovy"));
		
		/* TEST Select records */
		List<Department> items = testedObject.fetchAll();
		Assert.assertNotNull("Returned collection cannot be NULL", items);
		Assert.assertFalse("Collection cannot be EMPTY", items.isEmpty());
		Assert.assertEquals(2, items.size());
	}
	
	@Test
	public void testDelete()
	{
		// priprava entity pro test
		String name = "bosakrythos";
	    testedObject.save(new Department(name));
		List<Department> items = testedObject.fetchAll();
		int before = items.size();
		
		/* TEST Delete a record */
		testedObject.remove(testedObject.findByName(name));
		
		List<Department> itemsAfter = testedObject.fetchAll();
		int after = itemsAfter.size();
		
		Assert.assertNotEquals("Counts must be different", before, after);
		Assert.assertTrue("Before must be greater than after", before > after);
		Assert.assertEquals("No entity can be found", 0, after);
	}
	
	@Test
	public void testRelation()
	{
//		// priprava entity pro test
//		String name = "Dykroprohnutanus";
//	    testedObject.save(new Department(name));
//		employeeDao = new EmployeeDao();
//		employeeDao.setTransactionManager(tm);
//	    
//		String ename = "Kryhoplhocathor";
//		Employee tomasEmpl = new Employee(ename, testedObject.findByName(name));
//		employeeDao.save(tomasEmpl);
//		
//		Department domain = testedObject.findByName(name);
//		domain.setEmployees(Arrays.asList(employeeDao.findByName(ename)));		
	}
	
	@After
	public void tearDown()
	{
		testedObject.removeAll(Department.class);
		tm.close();
	}
}
