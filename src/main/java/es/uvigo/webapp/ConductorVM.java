package es.uvigo.webapp;

import static es.uvigo.webapp.util.DesktopEntityManagerManager.getDesktopEntityManager;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.Listen;

import es.uvigo.Conductor;
import es.uvigo.Employee;
import es.uvigo.TransactionUtils;
import es.uvigo.webapp.util.DesktopEntityManagerManager;

public class ConductorVM{
	
	private boolean ebrio = false;
	private int edad = 0;
	private String rango_edad="0-10";
	private String sexo = "unknow";

	
	public boolean getEbrio() {
		return ebrio;
	}
	
	public void setEbrio(boolean ebrio) {
		this.ebrio = ebrio;
	}

	public int getEdad() {
		return edad;
	}
	
	@NotifyChange({"edad", "rango_edad"})
	public void setEdad(int edad) {
		this.edad = edad;
		this.setRango_edad();
	}

	public String getRango_edad() {
		return rango_edad;
	}
	
	public void setRango_edad() {
		
		switch((int)this.edad/10){
			case 0:
				this.rango_edad = "0-9";
				break;
			case 1:
				this.rango_edad = "10-19";
				break;
			case 2:
				this.rango_edad = "20-29";
				break;
			case 3:
				this.rango_edad = "30-39";
				break;
			case 4:
				this.rango_edad = "40-49";
				break;
			case 5:
				this.rango_edad = "50-59";
				break;
			case 6:
				this.rango_edad = "60-69";
				break;
			case 7:
				this.rango_edad = "70-79";
				break;
			case 8:
				this.rango_edad = "80-89";
				break;
			case 9:
				this.rango_edad = "90-99";
				break;
			case 10:
				this.rango_edad = "100-109";
				break;
			default:
				this.rango_edad = "+110";
				
		}
		
		this.rango_edad = rango_edad;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public int getConductorCount(){
		return getDesktopEntityManager().createQuery("SELECT c FROM Conductor c", Conductor.class).getResultList().size();
	}
	

	@Command
	@NotifyChange("conductorCount")
	public void submitConductor() {
		Conductor con= new Conductor();
		con.setEbrio(ebrio);
		con.setEdad(edad);
		con.setRango_edad(rango_edad);
		con.setSexo(sexo);

		TransactionUtils.doTransaction(getDesktopEntityManager(), em -> {
			em.persist(con);
		});
	}
}