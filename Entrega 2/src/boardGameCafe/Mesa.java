package boardGameCafe;
import java.util.*;
public class Mesa {
	private int numeroMesa;
	private Cliente clienteActual;
	private ArrayList<ProductoMenu> preidoActual;
	private ArrayList<Prestamo >prestamoActicos;
	private int cantidadPersonas;
	private boolean hayMenores;
	private ArrayList<String> alergenos;
	public int getNumeroMesa() {
		return numeroMesa;
	}
	public Cliente getClienteActual() {
		return clienteActual;
	}
	public ArrayList<ProductoMenu> getPreidoActual() {
		return preidoActual;
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
	
	
}
