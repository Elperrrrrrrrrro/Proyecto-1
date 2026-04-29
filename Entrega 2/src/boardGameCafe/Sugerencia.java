package boardGameCafe;

public class Sugerencia {
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
        this.tipoSugerencia = tipoSugerencia;
        this.diaCambio = diaCambio;
        this.empleado = empleado;
        this.productoMenu = productoMenu;
    }
    // Arreglar constructor para el tipo de sugerenacia TODO

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
