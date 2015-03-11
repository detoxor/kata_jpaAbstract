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
		Root<Employee> c = q.from(Employee.class);
		
		q.select(c);// select klauzule
		q.where(cb.equal(c.get("name"), name));// where e.name=:n
		
		return fetchOne(q);
	}
}
