package cz.tomascejka.learn.jpa.jpabstract.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.jpa.jpabstract.domain.BaseDomain;
import cz.tomascejka.learn.jpa.jpabstract.tx.TxOperation;
import cz.tomascejka.learn.jpa.jpabstract.tx.TxManager;

public abstract class BaseDao<E extends BaseDomain> 
{
	private static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);
	private CriteriaBuilder cb;
	private boolean logTx = true;
		
	/**
	 * Insert or Update storage operation
	 * 
	 * @param domain modified/new domain which will be persisted
	 * 
	 * @return primaryKey of modified/new entity
	 */
	public final Long save(final E domain)
	{
		Long id = tm.processOperation(new TxOperation<Long>() 
		{
			public Long execute(EntityManager em, Object... parameters)
					throws Exception 
			{
				if(logTx)
				{
					LOG.info("Persist domain, {}", domain);
				}
				em.persist(domain);
				return domain.getId();
			}
		});
		return id;
	}
	
	/**
	 * Delete operation over storage record
	 * 
	 * @param domain specific entity which will be removed
	 * 
	 * @return true if success
	 */	
	public final boolean remove(final E domain)
	{
		Boolean state = tm.processOperation(new TxOperation<Boolean>() 
		{
			public Boolean execute(EntityManager em, Object... parameters)
					throws Exception 
			{
				if(logTx)
				{
					LOG.info("Remove domain {}", domain);
				}
				em.remove(domain);
				return true;
			}
		});
		return state == null ? false : state.booleanValue();
	}
	
	@SuppressWarnings("unchecked")
	protected final E fetchOne(final CriteriaQuery<E> criteriaQuery)
	{
		E domain = tm.processOperation(new TxOperation<E>() 
		{
			@SuppressWarnings("rawtypes")
			public E execute(EntityManager em, Object... parameters)
					throws Exception 
			{
				try 
				{
					TypedQuery query = em.createQuery(criteriaQuery);
					LOG.info("Single result query={}", tm.getSql(query));
					return (E) query.getSingleResult();
				}
				catch (NoResultException e)
				{
					LOG.warn("There is no entity found");
					return null;
				}
			}
		});
		return domain;
	}
	
	protected final List<E> fetchAll(final CriteriaQuery<E> criteriaQuery)
	{
		List<E> domains = tm.processOperation(new TxOperation<List<E>>() 
		{
			@SuppressWarnings("unchecked")
			public List<E> execute(EntityManager em, Object... parameters)
					throws Exception 
			{
				try 
				{
					Query query = em.createQuery(criteriaQuery);
					return (List<E>) query.getResultList();
				}
				catch (NoResultException e)
				{
					LOG.warn("There is no entity found");
					return null;
				}
			}
		});
		return domains;
	}
	
	protected CriteriaBuilder getQueryBuilder()
	{
		if(cb == null)
		{
			cb = tm.getCriteriaBuilder();
		}
		return cb;
	}
	
	// --- IoC
	private TxManager tm;
	
	public void setTransactionManager(TxManager transactionManager)
	{
		this.tm = transactionManager;
	}
}
