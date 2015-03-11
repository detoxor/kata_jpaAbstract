package cz.tomascejka.learn.jpa.jpabstract.tx;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * 
 * @author tomas.cejka
 *
 * @see http://antoniogoncalves.org/2012/05/24/how-to-get-the-jpqlsql-string-from-a-criteriaquery-in-jpa/
 */
public interface TxLogSqlStrategy 
{
	String getSqlString(TypedQuery<?> query, EntityManager em);
}
