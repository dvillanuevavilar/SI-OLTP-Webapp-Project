package es.uvigo.webapp;

import static es.uvigo.webapp.util.DesktopEntityManagerManager.getDesktopEntityManager;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import es.uvigo.Employee;
import es.uvigo.TransactionUtils;

public class TestVM {
	private String name = "anonymous";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEmployeesCount() {
		return getDesktopEntityManager().createQuery("SELECT e FROM Employee e", Employee.class).getResultList().size();
	}

	@Command
	@NotifyChange("employeesCount")
	public void submitEmployee() {
		Employee emp = new Employee();
		emp.setName(name);

		TransactionUtils.doTransaction(getDesktopEntityManager(), em -> {
			em.persist(emp);
		});
	}
}
