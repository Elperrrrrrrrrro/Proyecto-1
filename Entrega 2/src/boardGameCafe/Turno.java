package boardGameCafe;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Turno implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String diaSemana;
	private List<Empleado> empleadosAsignados;
	
	public Turno(String diaSemana) {
		this.diaSemana = diaSemana;
		this.empleadosAsignados = new ArrayList<>();
	}
	
	public List<Empleado> getEmpleadosAsignados() {
		return empleadosAsignados;
	}
	
	public String getDiaSemana() {
		return diaSemana;
	}
	
	public void adicionarEmpleado(Empleado e) {
		empleadosAsignados.add(e);
	}

	public boolean validarMinimoOperativo() {
		if (empleadosAsignados.size() < 3) {
			return false;
		}
		else {
			int meseros = 0;
			int cocineros = 0;
			for (Empleado e : empleadosAsignados) {
				if (e instanceof Mesero) {
					meseros++;
				}
				else if (e instanceof Cocinero) {
					cocineros++;
				}
				if (meseros >= 2 && cocineros >= 1) {
					return true;
				}
			}
			return false;
		}
	}
	

}
