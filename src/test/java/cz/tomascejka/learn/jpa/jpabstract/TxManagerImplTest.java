package cz.tomascejka.learn.jpa.jpabstract;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.tomascejka.learn.jpa.jpabstract.data.Department;
import cz.tomascejka.learn.jpa.jpabstract.tx.TxOperation;
import cz.tomascejka.learn.jpa.jpabstract.tx.TxManagerImpl;

public class TxManagerImplTest 
{
	private static final String PERSISTENCE_UNIT_NAME = "local-unit";
	private TxManagerImpl testedObject;
	
	// database operations
	private static final TxOperation<Department> INSERT_OR_UPDATE = new SaveDepartment();
	private static final TxOperation<Boolean> DELETE = new DeleteDepartment();
	private static final TxOperation<List<Department>> FETCH_ALL = new FetchAllDepartments();
	
	@Before
	public void setUp()
	{
		testedObject = new TxManagerImpl(PERSISTENCE_UNIT_NAME);
		testedObject.open();
	}
	
	@Test
	public void testInsert()
	{
		/* Insert a record */
		Department domain = testedObject.processOperation(INSERT_OR_UPDATE, "java");
		Assert.assertFalse("New entity cannot have empty primarKey", domain.getId() == null);
	}
	
	@Test
	public void testUpdate()
	{
		// priprava entity pro test
		Department insertDomain = testedObject.processOperation(INSERT_OR_UPDATE, "java");
		
		/* TEST Update a record */
		String newName = "groovy";
		Department updatedDomain = testedObject.processOperation(INSERT_OR_UPDATE, insertDomain.getId(), newName);
		Assert.assertEquals(newName, updatedDomain.getName());
	}
	
	@Test
	public void testFetchAll()
	{
		/* Insert a record */
		testedObject.processOperation(INSERT_OR_UPDATE, "java");
		testedObject.processOperation(INSERT_OR_UPDATE, "groovy");
		
		/* TEST Select records */
		List<Department> items = testedObject.processOperation(FETCH_ALL);
		Assert.assertNotNull("Returned collection cannot be NULL", items);
		Assert.assertFalse("Collection cannot be EMPTY", items.isEmpty());
		Assert.assertEquals(2, items.size());
	}
	
	@Test
	public void testDelete()
	{
		// priprava entity pro test
		Department insertDomain = testedObject.processOperation(INSERT_OR_UPDATE, "java");
		List<Department> items = testedObject.processOperation(FETCH_ALL);
		int before = items.size();
		
		/* TEST Delete a record */
		testedObject.processOperation(DELETE, insertDomain.getId());
		
		List<Department> itemsAfter = testedObject.processOperation(FETCH_ALL);
		int after = itemsAfter.size();
		
		Assert.assertNotEquals("Counts must be different", before, after);
		Assert.assertTrue("Before must be greater than after", before > after);
		Assert.assertEquals("No entity can be found", 0, after);
	}	
	
	@After
	public void tearDown()
	{
		testedObject.processOperation(new TxOperation<Integer>() 
		{
			@Override
			public Integer execute(EntityManager em, Object... parameters)
					throws Exception 
			{
				return em.createQuery("DELETE FROM "+Department.class.getSimpleName()).executeUpdate();
			}
		});
		testedObject.close();
	}
	
	/**
	 * insert nebo update Department entity
	 * 
	 * @author tomas.cejka
	 *
	 */
	private static final class SaveDepartment implements
			TxOperation<Department> 
	{
		public Department execute(EntityManager em, Object... parameters) 
		{
			Department department = null;
			if (parameters.length == 1)// insert
			{
				department = new Department((String) parameters[0]);
			} 
			else // update
			{
				department = em.find(Department.class, parameters[0]);
				department.setName((String) parameters[1]);
			}
			em.persist(department);
			return department;
		}
	}

	/**
	 * delete department entity
	 * 
	 * @author tomas.cejka
	 *
	 */
	private static final class DeleteDepartment implements
			TxOperation<Boolean> 
	{
		public Boolean execute(EntityManager em, Object... parameters) 
		{
			Department d = em.find(Department.class, parameters[0]);
			em.remove(d);
			return true;
		}
	}

	/**
	 * select * from department
	 * 
	 * @author tomas.cejka
	 *
	 */
	private static final class FetchAllDepartments implements
			TxOperation<List<Department>> 
	{
		public List<Department> execute(EntityManager em, Object... parameters)
				throws Exception 
		{
			CriteriaQuery<Department> q = em.getCriteriaBuilder().createQuery(Department.class);
			
			// from
			Root<Department> from = q.from(Department.class);
			// select
			q.select(from);

			return em.createQuery(q).getResultList();
		}

	}	
}
