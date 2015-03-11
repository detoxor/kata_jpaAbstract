package cz.tomascejka.learn.jpa.jpabstract.tx;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public interface TxManager 
{
	/**
	 * Create/open dialog between data storage and client's entity manager
	 */
	void open();
	
	/**
	 * <p>Create transaction-safety environment in order to be called given operation (can be
	 * modified by given parameters) within storage/database transaction.</p>
	 * 
	 * <p>If anything fails there is operation cancelled and there is processed rollback.</p>
	 * 
	 * @param operation client request to call query
	 * @param parameters for query
	 * 
	 * @return operation result
	 */
	@SuppressWarnings("rawtypes")
	public <T> T processOperation(TransactionOperation operation, Object... parameters);
	
	/**
	 * Close dialog between data storage and client's entity manager
	 */
	void close();

	/** 
	 * @return builder for specific (non-trivial) queries via {@link CriteriaQuery} interface.
	 */
	CriteriaBuilder getCriteriaBuilder();
}
