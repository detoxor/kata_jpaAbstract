package cz.tomascejka.learn.jpa.jpabstract.tx.log;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import cz.tomascejka.learn.jpa.jpabstract.tx.TxLogSqlStrategy;

public class OpenJpaLogSqlStrategy implements TxLogSqlStrategy {

	@Override
	public String getSqlString(TypedQuery<?> query, EntityManager em) 
	{
		return query.unwrap(org.apache.openjpa.persistence.QueryImpl.class).getQueryString();
	}

}
