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

	public Clima getCurrentClima() {
		return currentClima;
	}

	public List<Clima> getClimas() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT c FROM Clima c", Clima.class).getResultList();
	}

	@Command
	@NotifyChange("currentClima")
	public void newClima() {
		this.currentClima = new Clima();
	}

	@Command
	@NotifyChange("currentClima")
	public void edit(@BindingParam("c") Clima clima) {
		this.currentClima = clima;
	}

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
	
	@Command
	@NotifyChange({"climas", "currentClima"})
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentClima);
		});
		this.currentClima= null;
	}
	
	@Command
	@NotifyChange("currentClima")
	public void cancel() {
		this.currentClima= null;
	}

}
