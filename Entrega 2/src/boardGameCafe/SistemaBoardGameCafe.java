package boardGameCafe;

import java.util.ArrayList;
import java.util.Map;
import java.util.*;

import java.time.*;

public class SistemaBoardGameCafe {
	private Map<String, juegoMesa> inventario; // llave id del juego
	private Map<String, Empleado> empleados; // llave es el login+password de los empleados
	private Map<String, Cliente> clientes; // llave documento clientes
	private Map<String, Mesa> mesas; //llave numero mesa 
	private Map<String, Turno> turnos; // llave dia de la semana 
	private Map<String, Venta>  historialVenta ; // la llave es el ID venta
	private Map<String, PrestamoCliente> historailPrestamosClientes;
	private Map<String, PrestamoEmpleado> historailPrestamosEmpleados;
	private ArrayList<String> Sugerencias;
	private Empleado usuarioActual ;
	
	public SistemaBoardGameCafe() {
		
	}
	
	public void cargarDatos() {  // carga de datos falta percistencia
		
	}
	
	public void guardarDatos() {  // guardar datos falta percistencia 
		
	}
	
	public boolean inciarSesion(String login, String password) {
		try {
			this.empleados.get(login+password);
			return true;
		}
		catch(Exception e) {
			return false;
		}
		

	}
	
	public boolean cerrarSesion() {
		this.usuarioActual = null;
		return true;
	}
	
	public boolean procesarPrestamo(String idJuego, int idMesa, boolean esCliente, Cliente cliente 
			, Empleado empleado , juegoMesa juego,  LocalDateTime inicio) {
		if (esCliente) {

			PrestamoCliente prestamo = new PrestamoCliente(this.inventario.get(idJuego),inicio, null,cliente  ); // aca el null esta por que no tiene fecha de entrega aun
			try {
				if(PrestamoCliente.sonAptos(this.mesas.get(idMesa)) &&  !this.inventario.get(idJuego).getEstado()) {
					this.mesas.get(idMesa).AgregarPrestamo(prestamo);
					this.inventario.get(idJuego).cambiarEstado(true);
					this.historailPrestamosClientes.put(idJuego, prestamo);
					
					return true;
				}else {
					return false;
				}
				
			} catch(Exception e) {
				return false;
			}
			
		}else {
			PrestamoEmpleado prestano = new PrestamoEmpleado(this.inventario.get(idJuego),inicio,null,empleado);// aca el null esta por que no tiene fecha de entrega aun
			try {
				if(PrestamoEmpleado.sonAptos(null) &&  !this.inventario.get(idJuego).getEstado() ){
					this.historailPrestamosEmpleados.put(idJuego, prestano);
					this.inventario.get(idJuego).cambiarEstado(true);
					return true;
				} else {
					return false;
				}
			}catch(Exception e) {
				return false;
			}
		}
		return true;
		
	
	}
	
	public void limpiarMesa(LocalDateTime fin, Mesa mesa) {
		ArrayList<String> codigos = mesa.liberarMesa();
		for(String i: codigos) {
			this.historailPrestamosClientes.get(i).cambiarEstado(false);
		}
		
	}
	
	public Venta registrarVenta(int idMesa) {
		Venta venta = new Venta()
	}
	
}
