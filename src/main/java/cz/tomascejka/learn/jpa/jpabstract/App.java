package cz.tomascejka.learn.jpa.jpabstract;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.jpa.jpabstract.dao.DepartmentDao;
import cz.tomascejka.learn.jpa.jpabstract.dao.EmployeeDao;
import cz.tomascejka.learn.jpa.jpabstract.domain.Department;
import cz.tomascejka.learn.jpa.jpabstract.domain.Employee;
import cz.tomascejka.learn.jpa.jpabstract.tx.TxManagerImpl;
import cz.tomascejka.learn.jpa.jpabstract.tx.log.EclipseLinkLogSqlStrategy;
import cz.tomascejka.learn.jpa.jpabstract.tx.log.OpenJpaLogSqlStrategy;

public class App {

	private static final String PERSISTENCE_UNIT_NAME = "local-unit";
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) 
	{
		// 1. DIRECT-USING EXAMPLE
		/*if(true)*/if(args != null && args.length > 1) 
		{
			new DirectApproach().process(PERSISTENCE_UNIT_NAME);
		}
		
		// 2. ENTERPRISE EXAMPLE (using DAO pattern)
		useEnterpriseApproach();
		
		System.exit(0);
	}

	private static void useEnterpriseApproach()
	{
		TxManagerImpl tm = new TxManagerImpl(PERSISTENCE_UNIT_NAME);
		tm.setSqlLogStrategy(new OpenJpaLogSqlStrategy());//vendor-specific
		
		DepartmentDao departmentDao = new DepartmentDao();
		EmployeeDao employeeDao = new EmployeeDao();
		departmentDao.setTransactionManager(tm);
		employeeDao.setTransactionManager(tm);
		
		tm.open();
		
		String name = "tomas";
		Department d = new Department("tomas");
		departmentDao.save(d);
		Department d2 = new Department("kaja");
		departmentDao.save(d2);
		
		Employee tomasEmpl = new Employee("Tomas Cejka", d);
		employeeDao.save(tomasEmpl);
		
		Department domain = departmentDao.findByName(name);
		domain.setEmployees(Arrays.asList(employeeDao.findByName("Tomas Cejka")));
		LOG.info("Entity department founded data={}", domain);
		
		LOG.info("Find all existing department entities");
		List<Department> departments = departmentDao.fetchAll();
		for (Department entity : departments) 
		{
			LOG.info("Entity: {}", entity);
		}
		
		tm.close();
	}
	
	

}
