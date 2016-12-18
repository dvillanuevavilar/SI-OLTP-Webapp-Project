package es.uvigo.webapp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import es.uvigo.Accidente;
import es.uvigo.Localizacion;
import es.uvigo.TransactionUtils;
import es.uvigo.webapp.util.DesktopEntityManagerManager;

public class LocalizacionesVM {
	private Localizacion currentLocalizacion = null;
	
	/**
	 * 
	 * @return currentLocalizacion
	 */
	public Localizacion getCurrentLocalizacion() {
		return currentLocalizacion;
	}
	
	/**
	 * Devuelve una lista con las localizaciones existentes en base de datos
	 * @return List Localizacion
	 */
	public List<Localizacion> getLocalizaciones() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT l FROM Localizacion l", Localizacion.class).getResultList();
	}
	
	/**
	 * Inicializa currentLocalizacion con un objeto Localizacion
	 */
	@Command
	@NotifyChange("currentLocalizacion")
	public void newLocalizacion() {
		this.currentLocalizacion = new Localizacion();
	}
	
	/**
	 * Permite editar una localización. Para ello almacena en 
	 * currentLocalizacion la localización pasada como parámetro.
	 * @param localizacion
	 */
	@Command
	@NotifyChange("currentLocalizacion")
	public void edit(@BindingParam("l") Localizacion localizacion) {
		this.currentLocalizacion = localizacion;
	}
	
	/**
	 * Elimina la localización pasada como parámetro de la base de datos.
	 * Para ello recorre los accidentes asociados a la localización 
	 * eliminando dicha localización en cada accidente.
	 * @param localizacionToDelete
	 */
	@Command
	@NotifyChange("localizaciones")
	public void delete(@BindingParam("l") Localizacion localizacionToDelete) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			Set<Accidente> accidentesCopy = new HashSet<>(localizacionToDelete.getAccidentes());

			for (Accidente a : accidentesCopy) {
				a.setLocalizacion(null);
			}

			em.remove(localizacionToDelete);
		});

	}
	
	/**
	 * Almacena el objeto currentLocalizacion en base de datos.
	 * Una vez hecho esto lo establece a null.
	 */
	@Command
	@NotifyChange({ "localizaciones", "currentLocalizacion" })
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentLocalizacion);
		});
		this.currentLocalizacion = null;
	}
	
	/**
	 * Cancela la acción estableciendo currentLocalizacion a null
	 */
	@Command
	@NotifyChange("currentLocalizacion")
	public void cancel() {
		this.currentLocalizacion = null;
	}

}
