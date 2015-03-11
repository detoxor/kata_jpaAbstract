package cz.tomascejka.learn.jpa.jpabstract.tx;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create instance of JPA  {@link EntityManager} and offer api to call
 * safety (transaction-based) storage operation.
 *  
 * @author tomas.cejka
 *
 */
public class TxManagerImpl implements TxManager
{
	private static final Logger LOG = LoggerFactory.getLogger(TxManagerImpl.class);
	private EntityManager em;
	private String entities;
	
	public TxManagerImpl(String entities) 
	{
		this.entities = entities;
	}
	
	@Override
	public void open()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(entities);
		em = emf.createEntityManager();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T processOperation(TransactionOperation operation, Object... parameters)
	{
		LOG.info("Processing transactionally operation: {}", operation.getClass().getSimpleName());
		EntityTransaction tx = null;
		try
		{
			tx = em.getTransaction();
			tx.begin();
			
			T retval = (T) operation.execute(em, parameters);
			
			tx.commit();
			LOG.info("Function's result commited into database - God be praised");
			return retval;
		} 
		catch (Exception e) 
		{
			LOG.error("Problem during processing transactionally operation", e);
			if (tx != null)
			{
				tx.rollback();
				LOG.error("Rollback of transaction has been successfull finished");
			}
		}
		return null;
	}

	@Override
	public void close()
	{
		LOG.info("Closing EntityManager.");
		em.close();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() 
	{
		return em.getCriteriaBuilder();
	}

	public void flush() {
		em.flush();
	}
}
