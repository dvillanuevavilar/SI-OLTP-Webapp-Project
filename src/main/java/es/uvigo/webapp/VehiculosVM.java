package es.uvigo.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import es.uvigo.Conductor;
import es.uvigo.TransactionUtils;
import es.uvigo.Vehiculo;
import es.uvigo.webapp.util.DesktopEntityManagerManager;

public class VehiculosVM {
	
	private Vehiculo currentVehiculo = null;
	
	public Vehiculo getCurrentVehiculo() {
		return currentVehiculo;
	}
	
	public List<Conductor> getConductores(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT c FROM Conductor c WHERE c.id NOT IN (SELECT conductor FROM Vehiculo)", Conductor.class).getResultList();
	}
	
	public List<Vehiculo> getVehiculos(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT v FROM Vehiculo v", Vehiculo.class).getResultList();
	}
	
	@Command
	@NotifyChange("currentVehiculo")
	public void newVehiculo() {
		this.currentVehiculo = new Vehiculo();
	}
	
	@Command
	@NotifyChange("vehiculos")
	public void delete(@BindingParam("v") Vehiculo vehiculo) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(vehiculo);
		});
	}
	
	@Command
	@NotifyChange("currentVehiculo")
	public void edit(@BindingParam("v") Vehiculo vehiculo) {
		this.currentVehiculo= vehiculo;
	}
	
	@Command
	@NotifyChange({"vehiculos", "currentVehiculo","conductores"})
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentVehiculo);
		});
		this.currentVehiculo= null;
	}
	
	@Command
	@NotifyChange("currentVehiculo")
	public void cancel() {
		this.currentVehiculo= null;
	}
}
