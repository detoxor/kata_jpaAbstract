package cz.tomascejka.learn.jpa.jpabstract.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.tomascejka.learn.jpa.jpabstract.domain.Employee;

public class EmployeeDao extends BaseDao<Employee> 
{
	/*
	 * SELECT e FROM Employee e WHERE e.name = :p
	 */
	public Employee findByName(String name)
	{
		CriteriaBuilder cb = getQueryBuilder();
		CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
		
		// From
		Root<Employee> c = q.from(Employee.class);
		// Select 
		q.select(c);
		// where e.name=:n
		q.where(cb.equal(c.get("name"), name));
		
		return fetchOne(q);
	}
}
