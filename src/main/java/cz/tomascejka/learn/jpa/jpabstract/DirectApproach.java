package cz.tomascejka.learn.jpa.jpabstract;

import java.util.List;

import javax.persistence.EntityManager;

import cz.tomascejka.learn.jpa.jpabstract.domain.Department;
import cz.tomascejka.learn.jpa.jpabstract.tx.TxOperation;
import cz.tomascejka.learn.jpa.jpabstract.tx.TxManagerImpl;

public class DirectApproach 
{

	public void process(String entities)
	{
		TxManagerImpl tm = new TxManagerImpl(entities);
      
		tm.open();
		
		/* Insert a record */
		Long id = tm.processOperation(new SaveDepartment(), "java");
		/* Update a record */
		tm.processOperation(new SaveDepartment(), id, "groovy");
		/* Delete a record */
		tm.processOperation(new DeleteDepartment(), id);
		/* Select records */
		tm.processOperation(new FetchAllDepartments());

		tm.close();
	}
	
	/**
	 * insert nebo update Department entity
	 * 
	 * @author tomas.cejka
	 *
	 */
	private static final class SaveDepartment implements
			TxOperation<Long> 
	{
		public Long execute(EntityManager em, Object... parameters) 
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
			return department.getId();
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
		@SuppressWarnings("unchecked")
		public List<Department> execute(EntityManager em, Object... parameters)
				throws Exception 
		{
			return em.createQuery("FROM Department").getResultList();
		}

	}	
}
