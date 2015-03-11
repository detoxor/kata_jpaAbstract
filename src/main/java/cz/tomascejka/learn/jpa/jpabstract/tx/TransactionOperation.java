package cz.tomascejka.learn.jpa.jpabstract.tx;

import javax.persistence.EntityManager;

/**
 * Wrapper over storage operation
 * 
 * @author tomas.cejka
 *
 * @param <E> data type result type
 */
public interface TransactionOperation<E> 
{
	/**
	 * Perform storage operation via API {@link EntityManager}. Operation can be parametrized.
	 * 
	 * @param em entity manager
	 * @param parameters for operation
	 * @return result of storage operation
	 * 
	 * @throws Exception
	 */
	E execute(EntityManager em, Object... parameters) throws Exception;
}
