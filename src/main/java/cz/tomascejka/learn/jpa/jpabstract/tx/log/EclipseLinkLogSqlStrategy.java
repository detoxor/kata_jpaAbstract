package cz.tomascejka.learn.jpa.jpabstract.tx.log;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.internal.jpa.EJBQueryImpl;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.eclipse.persistence.sessions.Session;

import cz.tomascejka.learn.jpa.jpabstract.tx.TxLogSqlStrategy;

public class EclipseLinkLogSqlStrategy implements TxLogSqlStrategy {

	@SuppressWarnings("rawtypes")
	@Override
	public String getSqlString(TypedQuery<?> query, EntityManager em) 
	{
		Session session = em.unwrap(JpaEntityManager.class).getActiveSession();
		DatabaseQuery databaseQuery = ((EJBQueryImpl)query).getDatabaseQuery();
		databaseQuery.prepareCall(session, new DatabaseRecord());
		String sqlString = databaseQuery.getSQLString();
		return sqlString;
	}

}
