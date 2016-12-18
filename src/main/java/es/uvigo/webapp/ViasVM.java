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
	
	/**
	 * 
	 * @return currentVia
	 */
	public Via getCurrentVia() {
		return currentVia;
	}
	
	/**
	 * Devuelve una lista con las vias existentes en base de datos
	 * @return List Via
	 */
	public List<Via> getVias() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT v FROM Via v", Via.class).getResultList();
	}
	
	/**
	 * Inicializa currentVia con un objeto Via
	 */
	@Command
	@NotifyChange("currentVia")
	public void newVia() {
		this.currentVia = new Via();
	}
	
	/**
	 * Permite editar una vía. Para ello almacena en currentVia la 
	 * via pasada como parámetro.
	 * @param Via
	 */
	@Command
	@NotifyChange("currentVia")
	public void edit(@BindingParam("v") Via Via) {
		this.currentVia = Via;
	}
	
	/**
	 * Elimina una vía de la base de datos pasada como parámetro.
	 * Para ello recorre los accidentes asociados a la vía 
	 * eliminado dicha vía de cada accidente.
	 * @param ViaToDelete
	 */
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
	
	/**
	 * Almacena en base de datos el objeto currentVia.
	 * Una vez hecho esto lo establece a null
	 */
	@Command
	@NotifyChange({ "vias", "currentVia" })
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentVia);
		});
		this.currentVia = null;
	}
	
	/**
	 * Cancela la acción estableciendo currenVia a null
	 */
	@Command
	@NotifyChange("currentVia")
	public void cancel() {
		this.currentVia = null;
	}

}
