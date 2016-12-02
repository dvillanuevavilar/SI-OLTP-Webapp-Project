package es.uvigo.webapp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import es.uvigo.Conductor;
import es.uvigo.webapp.util.DesktopEntityManagerManager;
import es.uvigo.TransactionUtils;
import es.uvigo.Vehiculo;

public class ConductoresVM {

	private Conductor currentConductor = null;

	public Conductor getCurrentConductor() {
		return currentConductor;
	}

	public List<Conductor> getConductores() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT c FROM Conductor c", Conductor.class).getResultList();
	}

	@Command
	@NotifyChange("currentConductor")
	public void newConductor() {
		this.currentConductor = new Conductor();
	}

	@Command
	@NotifyChange("conductores")
	public void delete(@BindingParam("e") Conductor conductor) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			if (conductor.getVehiculo() != null) {
				Vehiculo veh = conductor.getVehiculo();
				veh.setConductor(null);
			}
			em.remove(conductor);
		});
	}

	@Command
	@NotifyChange("currentConductor")
	public void edit(@BindingParam("e") Conductor conductor) {
		this.currentConductor = conductor;
	}

	@Command
	@NotifyChange({ "conductores", "currentConductor" })
	public void save() {
		this.setRango_edad();
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentConductor);
		});
		this.currentConductor = null;
	}

	@Command
	@NotifyChange("currentConductor")
	public void cancel() {
		this.currentConductor = null;
	}

	public void setRango_edad() {

		switch ((int) this.getCurrentConductor().getEdad() / 10) {
		case 0:
			this.getCurrentConductor().setRango_edad("0-9");
			break;
		case 1:
			this.getCurrentConductor().setRango_edad("10-19");
			break;
		case 2:
			this.getCurrentConductor().setRango_edad("20-29");
			break;
		case 3:
			this.getCurrentConductor().setRango_edad("30-39");
			break;
		case 4:
			this.getCurrentConductor().setRango_edad("40-49");
			break;
		case 5:
			this.getCurrentConductor().setRango_edad("50-59");
			break;
		case 6:
			this.getCurrentConductor().setRango_edad("60-69");
			break;
		case 7:
			this.getCurrentConductor().setRango_edad("70-79");
			break;
		case 8:
			this.getCurrentConductor().setRango_edad("80-89");
			break;
		case 9:
			this.getCurrentConductor().setRango_edad("90-99");
			break;
		case 10:
			this.getCurrentConductor().setRango_edad("100-109");
			break;
		default:
			this.getCurrentConductor().setRango_edad("+110");

		}
	}
}
