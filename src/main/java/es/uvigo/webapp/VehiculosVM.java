package es.uvigo.webapp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import es.uvigo.Accidente;
import es.uvigo.Conductor;
import es.uvigo.TransactionUtils;
import es.uvigo.Vehiculo;
import es.uvigo.webapp.util.DesktopEntityManagerManager;

public class VehiculosVM {
	
	private Vehiculo currentVehiculo = null;
	
	/**
	 * 
	 * @return currentVehiculo
	 */
	public Vehiculo getCurrentVehiculo() {
		return currentVehiculo;
	}
	
	/**
	 * Devuelve una lista con los conductores existentes en base de datos 
	 * que no disponen de un vehículo asociado
	 * @return List Conductor 
	 */
	public List<Conductor> getConductores(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT c FROM Conductor c WHERE c.id NOT IN (SELECT conductor FROM Vehiculo)", Conductor.class).getResultList();
	}
	
	/**
	 * Devuelve una lista con los vehiculos existentes en base de datos
	 * @return List Vehiculo
	 */
	public List<Vehiculo> getVehiculos(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT v FROM Vehiculo v", Vehiculo.class).getResultList();
	}
	
	/**
	 * Inicializa currentVehiculo con un objeto Vehiculo
	 */
	@Command
	@NotifyChange("currentVehiculo")
	public void newVehiculo() {
		this.currentVehiculo = new Vehiculo();
	}
	
	/**
	 * Elimina el vehículo de la base de datos pasado como parámetro.
	 * Para ello recorre los accidentes asociados al vehículo eliminando
	 * dicho vehículo en cada accidente.
	 * @param vehiculo
	 */
	@Command
	@NotifyChange("vehiculos")
	public void delete(@BindingParam("v") Vehiculo vehiculo) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			Set<Accidente> accidentesCopy = new HashSet<>(vehiculo.getAccidentes());
			for (Accidente ac : accidentesCopy){
				ac.removeVehiculo(vehiculo);
			}
			em.remove(vehiculo);
		});
	}
	
	/**
	 * Edita un vehículo. Para ello almacena en currentVehiculo el vehículo pasado
	 * como parámetro.
	 * @param vehiculo
	 */
	@Command
	@NotifyChange("currentVehiculo")
	public void edit(@BindingParam("v") Vehiculo vehiculo) {
		this.currentVehiculo= vehiculo;
	}
	
	/**
	 * Persiste el objeto currentVehiculo en base de datos.
	 * Un vez hecho esto lo establece a null.
	 */
	@Command
	@NotifyChange({"vehiculos", "currentVehiculo","conductores"})
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentVehiculo);
		});
		this.currentVehiculo= null;
	}
	
	/**
	 * Cancela la acción estableciendo currentVehiculo a null
	 */
	@Command
	@NotifyChange("currentVehiculo")
	public void cancel() {
		this.currentVehiculo= null;
	}
}
