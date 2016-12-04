package es.uvigo.webapp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import es.uvigo.Accidente;
import es.uvigo.Damnificado;
import es.uvigo.TransactionUtils;
import es.uvigo.webapp.util.DesktopEntityManagerManager;

public class DamnificadosVM {

	private Damnificado currentDamnificado = null;

	public Damnificado getCurrentDamnificado() {
		return currentDamnificado;
	}

	public List<Damnificado> getDamnificados() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT d FROM Damnificado d", Damnificado.class).getResultList();
	}

	@Command
	@NotifyChange("currentDamnificado")
	public void newDamnificado() {
		this.currentDamnificado = new Damnificado();
	}

	@Command
	@NotifyChange("damnificados")
	public void delete(@BindingParam("d") Damnificado damnificado) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			Set<Accidente> accidentesCopy = new HashSet<>(damnificado.getAccidentes());
			for (Accidente ac : accidentesCopy){
				ac.removeDamnificado(damnificado);
			}
			em.remove(damnificado);
		});
	}

	@Command
	@NotifyChange("currentDamnificado")
	public void edit(@BindingParam("d") Damnificado damnificado) {
		this.currentDamnificado = damnificado;
	}

	@Command
	@NotifyChange({ "damnificados", "currentDamnificado" })
	public void save() {
		this.setRango_edad();
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentDamnificado);
		});
		this.currentDamnificado = null;
	}

	@Command
	@NotifyChange("currentDamnificado")
	public void cancel() {
		this.currentDamnificado = null;
	}

	public void setRango_edad() {

		switch ((int) this.getCurrentDamnificado().getEdad() / 10) {
		case 0:
			this.getCurrentDamnificado().setRango_edad("0-9");
			break;
		case 1:
			this.getCurrentDamnificado().setRango_edad("10-19");
			break;
		case 2:
			this.getCurrentDamnificado().setRango_edad("20-29");
			break;
		case 3:
			this.getCurrentDamnificado().setRango_edad("30-39");
			break;
		case 4:
			this.getCurrentDamnificado().setRango_edad("40-49");
			break;
		case 5:
			this.getCurrentDamnificado().setRango_edad("50-59");
			break;
		case 6:
			this.getCurrentDamnificado().setRango_edad("60-69");
			break;
		case 7:
			this.getCurrentDamnificado().setRango_edad("70-79");
			break;
		case 8:
			this.getCurrentDamnificado().setRango_edad("80-89");
			break;
		case 9:
			this.getCurrentDamnificado().setRango_edad("90-99");
			break;
		case 10:
			this.getCurrentDamnificado().setRango_edad("100-109");
			break;
		default:
			this.getCurrentDamnificado().setRango_edad("+110");

		}
	}

}
