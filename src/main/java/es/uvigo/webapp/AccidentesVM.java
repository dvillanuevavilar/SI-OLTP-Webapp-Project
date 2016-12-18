package es.uvigo.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import es.uvigo.webapp.util.DesktopEntityManagerManager;
import es.uvigo.TransactionUtils;
import es.uvigo.Vehiculo;
import es.uvigo.Via;
import es.uvigo.Accidente;
import es.uvigo.Clima;
import es.uvigo.Damnificado;
import es.uvigo.Localizacion;

public class AccidentesVM {

	private Accidente currentAccidente = null;
	
	/**
	 * @return currentAccidente
	 */
	public Accidente getCurrentAccidente() {
		return currentAccidente;
	}
	
	/**
	 * Devuelve una lista con los accidentes existentes en base de datos
	 * @return List Accidente
	 */
	public List<Accidente> getAccidentes() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT a FROM Accidente a", Accidente.class).getResultList();
	}
	
	/**
	 * Devuelve una lista con los climas existentes en base de datos
	 * @return List Clima
	 */
	public List<Clima> getClimas() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT c FROM Clima c", Clima.class).getResultList();
	}
	
	/**
	 * Devuelve una lista con las localizaciones existentes en base de datos
	 * @return List Localizacion
	 */
	public List<Localizacion> getLocalizaciones(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT l FROM Localizacion l", Localizacion.class).getResultList();
	}
	
	/**
	 * Devuelve una lista con las vias existentes en base de datos
	 * @return List Via
	 */
	public List<Via> getVias(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT v FROM Via v", Via.class).getResultList();
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
	 * Devuelve una lista con los damnificados existentes en base de datos
	 * @return List Damnificado
	 */
	public List<Damnificado> getDamnificados(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT d FROM Damnificado d", Damnificado.class).getResultList();
	}
	
	/**
	 * Inicializa currentAccidente con un objeto Accidente
	 */
	@Command
	@NotifyChange("currentAccidente")
	public void newAccidente() {
		this.currentAccidente = new Accidente();
	}
	
	/**
	 * Elimina el accidente de la base de datos 
	 * pasado como parámetro
	 * @param accidente
	 */
	@Command
	@NotifyChange("accidentes")
	public void delete(@BindingParam("a") Accidente accidente) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(accidente);
		});
	}
	
	/**
	 * Permite editar un accidente. Para ello almacena en 
	 * currentAccidente el accidente pasado como parámetro.
	 * @param accidente
	 */
	@Command
	@NotifyChange("currentAccidente")
	public void edit(@BindingParam("a") Accidente accidente) {
		this.currentAccidente = accidente;
	}
	
	/**
	 * Permite almacenar o persistir el objeto currentAccidente en
	 * base de datos. Una vez hecho esto lo establece a null.
	 */
	@Command
	@NotifyChange({ "accidentes", "currentAccidente" })
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentAccidente);
		});
		this.currentAccidente = null;
	}
	
	/**
	 * Cancela la acción estableciendo currentAccidente a null
	 */
	@Command
	@NotifyChange("currentAccidente")
	public void cancel() {
		this.currentAccidente = null;
	}
}
