package es.uvigo.webapp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import es.uvigo.Accidente;
import es.uvigo.TransactionUtils;
import es.uvigo.Via;
import es.uvigo.webapp.util.DesktopEntityManagerManager;

public class ViasVM {
	private Via currentVia = null;

	public Via getCurrentVia() {
		return currentVia;
	}

	public List<Via> getVias() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT v FROM Via v", Via.class).getResultList();
	}

	@Command
	@NotifyChange("currentVia")
	public void newVia() {
		this.currentVia = new Via();
	}

	@Command
	@NotifyChange("currentVia")
	public void edit(@BindingParam("v") Via Via) {
		this.currentVia = Via;
	}

	@Command
	@NotifyChange("vias")
	public void delete(@BindingParam("v") Via ViaToDelete) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			Set<Accidente> accidentesCopy = new HashSet<>(ViaToDelete.getAccidentes());

			for (Accidente a : accidentesCopy) {
				a.setVia(null);
			}

			em.remove(ViaToDelete);
		});

	}

	@Command
	@NotifyChange({ "vias", "currentVia" })
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentVia);
		});
		this.currentVia = null;
	}

	@Command
	@NotifyChange("currentVia")
	public void cancel() {
		this.currentVia = null;
	}

}
