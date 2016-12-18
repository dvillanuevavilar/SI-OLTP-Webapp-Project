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
	
	/**
	 * 
	 * @return currentDamnificado
	 */
	public Damnificado getCurrentDamnificado() {
		return currentDamnificado;
	}
	
	/**
	 * Devuelve una lista con los damnificados existentes en base de datos
	 * @return List Damnificado
	 */
	public List<Damnificado> getDamnificados() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT d FROM Damnificado d", Damnificado.class).getResultList();
	}
	
	/**
	 * Inicializa currentDamnificado con un objeto Damnificado
	 */
	@Command
	@NotifyChange("currentDamnificado")
	public void newDamnificado() {
		this.currentDamnificado = new Damnificado();
	}
	
	/**
	 * Elimina el damnificado de la base de datos pasado como parámetro.
	 * Para ello se recorre todos los accidentes asociados de ese damnificado
	 * eliminando dicho damnificado por cada accidente
	 * @param damnificado
	 */
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
	
	/**
	 * Permite editar un damnificado. Para ello almacena en currentDamnificado 
	 * el damnificado pasado como parámetro.
	 * @param damnificado
	 */
	@Command
	@NotifyChange("currentDamnificado")
	public void edit(@BindingParam("d") Damnificado damnificado) {
		this.currentDamnificado = damnificado;
	}
	
	/**
	 * Permite almacenar o persistir el objeto currentDamnificado
	 * en base de datos. Una vez hecho esto lo establece a null.
	 */
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
	
	/**
	 * Cancela la acción estableciendo currentDamnificado a null
	 */
	@Command
	@NotifyChange("currentDamnificado")
	public void cancel() {
		this.currentDamnificado = null;
	}
	
	/**
	 * Establece un rango de edad en función de su edad.
	 */
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
