package es.uvigo.webapp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import es.uvigo.webapp.util.DesktopEntityManagerManager;
import es.uvigo.TransactionUtils;
import es.uvigo.Accidente;
import es.uvigo.Clima;

public class ClimasVM {
	private Clima currentClima = null;
	
	/**
	 * 
	 * @return currentClima
	 */
	public Clima getCurrentClima() {
		return currentClima;
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
	 * Inicializa currentClima con un objeto Clima
	 */
	@Command
	@NotifyChange("currentClima")
	public void newClima() {
		this.currentClima = new Clima();
	}

	/**
	 * Permite editar un clima. Para ello almacena en 
	 * currentClima el clima pasado como parámetro.
	 * @param clima
	 */
	@Command
	@NotifyChange("currentClima")
	public void edit(@BindingParam("c") Clima clima) {
		this.currentClima = clima;
	}

	/**
	 * Elimina el clima de la base de datos 
	 * pasado como parámetro
	 * @param clima
	 */
	@Command
	@NotifyChange("climas")
	public void delete(@BindingParam("c") Clima clima) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			Set<Accidente> accidentesCopy = new HashSet<>(clima.getAccidentes());

			for (Accidente ac : accidentesCopy) {
				ac.setClima(null);
			}

			em.remove(clima);
		});
	}
	
	/**
	 * Permite almacenar o persistir el objeto currentClima en
	 * base de datos. Una vez hecho esto lo establece a null.
	 */
	@Command
	@NotifyChange({"climas", "currentClima"})
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentClima);
		});
		this.currentClima= null;
	}
	
	/**
	 * Cancela la acción estableciendo currentClima a null
	 */
	@Command
	@NotifyChange("currentClima")
	public void cancel() {
		this.currentClima= null;
	}

}
