package cz.tomascejka.learn.jpa.jpabstract.data;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class DepartmentDao extends BaseDao<Department> 
{
	/*
	 * SELECT d FROM Department d WHERE d.name = :p
	 */
	public Department findByName(String name)
	{
		CriteriaBuilder cb = getQueryBuilder();
		CriteriaQuery<Department> q = cb .createQuery(Department.class);

		// from Deparment
		Root<Department> c = q.from(Department.class);
		//  select klauzule
		q.select(c);
		// where e.name=:p
		q.where(cb.equal(c.get("name"), name));
		
		return fetchOne(q);
	}
	
	public List<Department> fetchAll()
	{
		CriteriaBuilder cb = getQueryBuilder();
		CriteriaQuery<Department> q = cb .createQuery(Department.class);
		Root<Department> c = q.from(Department.class);
		q.select(c);
		return fetchAll(q);
	}
}
