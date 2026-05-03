package boardGameCafe.logic;
import java.io.Serializable;
public class JuegoMesa implements Serializable{
  
	private String id;
	private String nombre;
	private int anoPublicacion;
	private String empresa;
	private int minJugadores;
	private int maxJugadores;
	private String categoria;
	private int vecesPrestado;
	private boolean prestado;
	private boolean SoloAdultos;
	private String estado;
	private boolean dificil;
	private double precioVenta;
	
	
	public JuegoMesa (String id, String nombre, int anoPublicacion, String empresa, int minJugadores, int maxJugadores, String categoria, boolean SoloAdultos, String estado, boolean dificil, double precioVenta) {
		this.id = id;
		this.nombre = nombre;
		this.anoPublicacion = anoPublicacion;
		this.empresa = empresa;
		this.minJugadores = minJugadores;
		this.maxJugadores = maxJugadores;
		this.categoria = categoria;
		this.SoloAdultos = SoloAdultos;
		this.dificil = dificil;
		this.estado = estado;
		this.precioVenta = precioVenta;
		this.vecesPrestado = 0;
		this.prestado = false;	
	}
	
	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}


	public int getAnoPublicacion() {
		return anoPublicacion;
	}


	public String getEmpresa() {
		return empresa;
	}


	public int getMinJugadores() {
		return minJugadores;
	}


	public int getMaxJugadores() {
		return maxJugadores;
	}


	public String getCategoria() {
		return categoria;
	}


	public int getVecesPrestado() {
		return vecesPrestado;
	}


	public boolean isPrestado() {
		return prestado;
	}


	public boolean isSoloAdultos() {
		return SoloAdultos;
	}


	public String getEstado() {
		return estado;
	}


	public boolean isDificil() {
		return dificil;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public void setAnoPublicacion(int anoPublicacion) {
		this.anoPublicacion = anoPublicacion;
	}


	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}


	public void setMinJugadores(int minJugadores) {
		this.minJugadores = minJugadores;
	}


	public void setMaxJugadores(int maxJugadores) {
		this.maxJugadores = maxJugadores;
	}


	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}


	public void sumarVecesPrestado() {
		this.vecesPrestado++;
	}


	public void setPrestado(boolean prestado) {
		this.prestado = prestado;
	}


	public void setSoloAdultos(boolean soloAdultos) {
		SoloAdultos = soloAdultos;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public void setDificil(boolean dificil) {
		this.dificil = dificil;
	}
	
	public double getPrecioVenta() {
		return precioVenta;
	}
	
	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}
	
	
}
