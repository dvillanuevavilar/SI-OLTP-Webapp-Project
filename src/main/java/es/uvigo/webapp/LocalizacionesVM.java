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

	public Localizacion getCurrentLocalizacion() {
		return currentLocalizacion;
	}

	public List<Localizacion> getLocalizaciones() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT l FROM Localizacion l", Localizacion.class).getResultList();
	}

	@Command
	@NotifyChange("currentLocalizacion")
	public void newLocalizacion() {
		this.currentLocalizacion = new Localizacion();
	}

	@Command
	@NotifyChange("currentLocalizacion")
	public void edit(@BindingParam("l") Localizacion localizacion) {
		this.currentLocalizacion = localizacion;
	}

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

	@Command
	@NotifyChange({ "localizaciones", "currentLocalizacion" })
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentLocalizacion);
		});
		this.currentLocalizacion = null;
	}

	@Command
	@NotifyChange("currentLocalizacion")
	public void cancel() {
		this.currentLocalizacion = null;
	}

}
