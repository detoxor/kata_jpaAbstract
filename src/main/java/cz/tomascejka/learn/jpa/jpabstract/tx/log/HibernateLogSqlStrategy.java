package cz.tomascejka.learn.jpa.jpabstract.tx.log;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import cz.tomascejka.learn.jpa.jpabstract.tx.TxLogSqlStrategy;

/**
 * Logovani SQL dotazu via Hibernate
 * 
 * @author tomas.cejka
 * 
 * @see http://antoniogoncalves.org/2012/05/24/how-to-get-the-jpqlsql-string-from-a-criteriaquery-in-jpa/
 *
 */
public class HibernateLogSqlStrategy implements TxLogSqlStrategy
{

	@Override
	public String getSqlString(TypedQuery<?> query, EntityManager em) 
	{
		return query.unwrap(org.hibernate.Query.class).getQueryString();
	}

}
