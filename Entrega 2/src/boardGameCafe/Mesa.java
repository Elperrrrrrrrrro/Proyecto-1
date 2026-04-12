package boardGameCafe;
import java.util.*;
public class Mesa {
	private int numeroMesa;
	private Cliente clienteActual;
	private ArrayList<ProductoMenu> pedidoActual;
	private ArrayList<Prestamo >prestamoActicos;
	private int cantidadPersonas;
	private boolean hayMenores;
	private ArrayList<String> alergenos;
	
	public Mesa(int numero, int cantidad, boolean menores, ArrayList<String> alergenos, Cliente cliente) {
		this.numeroMesa= numero;
		this.cantidadPersonas = cantidad;
		this.hayMenores = menores; 
		this.alergenos = alergenos;
		this.pedidoActual = new ArrayList<>();
		this.prestamoActicos = new ArrayList<>();
		this.clienteActual = cliente;
		
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
		return prestamoActicos;
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
	

	
	
}
