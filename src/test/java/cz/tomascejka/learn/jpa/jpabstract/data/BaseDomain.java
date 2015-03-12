package cz.tomascejka.learn.jpa.jpabstract.data;

import javax.persistence.MappedSuperclass;

/**
 * Abstract domain object
 * 
 * @author tomas.cejka
 *
 */
@MappedSuperclass
public abstract class BaseDomain 
{
//	@Version
//	protected Long version;
//	
//	public Long getVersion()
//	{
//		return version;
//	}
//	
//	public void setVersion(Long version)
//	{
//		this.version = version;
//	}
	
	public abstract Long getId();
	
	public abstract String toString();
}

