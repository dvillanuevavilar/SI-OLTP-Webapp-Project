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

	public Accidente getCurrentAccidente() {
		return currentAccidente;
	}

	public List<Accidente> getAccidentes() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT a FROM Accidente a", Accidente.class).getResultList();
	}

	public List<Clima> getClimas() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT c FROM Clima c", Clima.class).getResultList();
	}
	
	public List<Localizacion> getLocalizaciones(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT l FROM Localizacion l", Localizacion.class).getResultList();
	}
	
	public List<Via> getVias(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT v FROM Via v", Via.class).getResultList();
	}
	
	public List<Vehiculo> getVehiculos(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT v FROM Vehiculo v", Vehiculo.class).getResultList();
	}
	
	public List<Damnificado> getDamnificados(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT d FROM Damnificado d", Damnificado.class).getResultList();
	}

	@Command
	@NotifyChange("currentAccidente")
	public void newAccidente() {
		this.currentAccidente = new Accidente();
	}

	@Command
	@NotifyChange("accidentes")
	public void delete(@BindingParam("a") Accidente accidente) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(accidente);
		});
	}

	@Command
	@NotifyChange("currentAccidente")
	public void edit(@BindingParam("a") Accidente accidente) {
		this.currentAccidente = accidente;
	}

	@Command
	@NotifyChange({ "accidentes", "currentAccidente" })
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentAccidente);
		});
		this.currentAccidente = null;
	}

	@Command
	@NotifyChange("currentAccidente")
	public void cancel() {
		this.currentAccidente = null;
	}
}
