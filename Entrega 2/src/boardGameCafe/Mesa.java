package boardGameCafe;
import java.util.*;
import java.time.*;
import java.io.Serializable;
public class Mesa implements Serializable{
	private int numeroMesa;
	private Cliente clienteActual;
	private ArrayList<ProductoMenu> pedidoActual;
	private ArrayList<Prestamo>prestamosActivos;
	private int cantidadPersonas;
	private boolean hayMenores;
	private ArrayList<String> alergenos;
	private boolean hayInfantes;
	
	public Mesa(int numero, int cantidad, boolean menores, ArrayList<String> alergenos,boolean hayInfantes) {
		this.numeroMesa= numero;
		this.cantidadPersonas = cantidad;
		this.hayMenores = menores; 
		this.alergenos = alergenos;
		this.pedidoActual = new ArrayList<>();
		this.prestamosActivos = new ArrayList<>();
		this.clienteActual = null;
		this.hayInfantes = hayInfantes;

		
	}
	public int getNumeroMesa() {
		return numeroMesa;
	}
	public Cliente getClienteActual() {
		return clienteActual;
	}
	public ArrayList<ProductoMenu> getPreidoActual() {
		return pedidoActual;
	}
	public ArrayList<Prestamo> getPrestamoActicos() {
		return prestamosActivos;
	}
	public int getCantidadPersonas() {
		return cantidadPersonas;
	}
	public boolean isHayMenores() {
		return hayMenores;
	}
	public ArrayList<String> getAlergenos() {
		return alergenos;
	}
	
	public void agregarAlPedido(ProductoMenu producto ) {
		this.pedidoActual.add(producto);
	}
	public boolean isHayInfantes() {
		return hayInfantes;
	}
	public void setClienteActual(Cliente clienteActual) {
		this.clienteActual = clienteActual;
	}
	
	
	public void liberarMesa() {
		
		this.cantidadPersonas=0;
		this.clienteActual= null;
		this.pedidoActual = new ArrayList<>();
		
		for (Prestamo prestamo: this.prestamosActivos) {
			prestamo.getJuego().setPrestado(false);
			prestamo.setFechaDevolucion(LocalDateTime.now());
		}
		
		this.prestamosActivos = new ArrayList<>();
		this.hayInfantes= false;
		this.hayMenores= false;

	}
	
	public ArrayList<ProductoMenu> getPedidoActual() {
		return pedidoActual;
	}
	public void setNumeroMesa(int numeroMesa) {
		this.numeroMesa = numeroMesa;
	}
	public void setCantidadPersonas(int cantidadPersonas) {
		this.cantidadPersonas = cantidadPersonas;
	}
	public void setHayMenores(boolean hayMenores) {
		this.hayMenores = hayMenores;
	}
	public void setHayInfantes(boolean hayInfantes) {
		this.hayInfantes = hayInfantes;
	}
	public void setAlergenos(ArrayList<String> alergenos) {
		this.alergenos = alergenos;
	}
	
	public void AgregarPrestamo(Prestamo prestamo) {
		this.prestamosActivos.add(prestamo);
	}
	

	
	
}
