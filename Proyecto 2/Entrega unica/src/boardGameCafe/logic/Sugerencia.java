package boardGameCafe.logic;
import java.io.Serializable;

public class Sugerencia implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean estaAprobado;
    
    // true = comida, false = turno
    private boolean tipoSugerencia; 
    private String sugerenciaID;
    private String diaCambio;
    private Empleado empleado;
    private ProductoMenu productoMenu;

    // --- CONSTRUCTOR ---
    public Sugerencia(String sugerenciaID, boolean estaAprobado, boolean tipoSugerencia, String diaCambio, 
            Empleado empleado, ProductoMenu productoMenu) {
    	this.sugerenciaID = sugerenciaID;
    	this.estaAprobado = estaAprobado;
    	this.empleado = empleado;
    	this.tipoSugerencia = tipoSugerencia;

    	if (tipoSugerencia) {
// es comida
    		if (productoMenu == null) {
    			throw new IllegalArgumentException("Una sugerencia de comida debe tener productoMenu.");	
    		}
    		this.productoMenu = productoMenu;
    		this.diaCambio = null;
    	} else {
// sugerencia de turnos
    		if (diaCambio == null) {
    			throw new IllegalArgumentException("Una sugerencia de turno debe tener diaCambio.");
    		}
    		this.diaCambio = diaCambio;
    		this.productoMenu = null;
    	}
    }

    // --- MÉTODOS GET (Para todos los atributos) ---
    
    
    public boolean isEstaAprobado() {
        return estaAprobado;
    }

    public String getSugerenciaID() {
		return sugerenciaID;
	}

	public boolean isTipoSugerencia() {
        return tipoSugerencia;
    }

    public String getDiaCambio() {
        return diaCambio;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public ProductoMenu getProductoMenu() {
        return productoMenu;
    }

    // --- MÉTODO SET (Solo para estaAprobado) ---

    public void setEstaAprobado(boolean estaAprobado) {
        this.estaAprobado = estaAprobado;
    }
}
