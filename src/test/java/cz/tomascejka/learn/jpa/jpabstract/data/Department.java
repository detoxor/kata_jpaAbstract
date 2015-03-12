package cz.tomascejka.learn.jpa.jpabstract.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="DEPARTMENT")
public class Department extends BaseDomain 
{
	@Id
	@GeneratedValue
	@Column(name="ID_DEPARTMENT", nullable=false)
	private Long id;

	@Column(name="NAME", nullable=false)
	private String name;

	@OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST)
	private List<Employee> employees = new ArrayList<Employee>();

	public Department() {
		super();
	}

	public Department(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", employees="
				+ employees + "]";
	}
}
