package cz.tomascejka.learn.jpa.jpabstract.tx;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public interface TxLogSqlStrategy {

	String getSqlString(TypedQuery<?> query, EntityManager em);
	
}
