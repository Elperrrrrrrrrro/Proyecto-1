package boardGameCafe;

import java.util.ArrayList;
import java.util.Map;
import java.util.*;

import java.time.*;

public class SistemaBoardGameCafe {
	private Map<String, JuegoMesa> inventario; // llave id del juego
	private Map<String, JuegoMesa> inventarioVender; // llave id del juego
	private Map<String, Empleado> empleados; // llave es el login+password de los empleados
	private Map<String, Cliente> clientes; // llave documento clientes
	private Map<String, Mesa> mesas; //llave numero mesa 
	private Map<String, Turno> turnos; // llave dia de la semana 
	private Map<String, Venta>  historialVenta ; // la llave es el ID venta
	private Map<String, PrestamoCliente> historialPrestamosClientes;
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
			, Empleado empleado , JuegoMesa juego,  LocalDateTime inicio) {
		if (esCliente) {
			String Id = String.valueOf(this.historialPrestamosClientes.size()+1);
			PrestamoCliente prestamo = new PrestamoCliente(Id,this.inventario.get(idJuego),inicio, null,cliente ); // aca el null esta por que no tiene fecha de entrega aun
			try {
				
				if(prestamo.sonAptos(this.mesas.get(idMesa)) &&  (!this.inventario.get(idJuego).isPrestado())) {
					this.mesas.get(idMesa).AgregarPrestamo(prestamo);
					this.inventario.get(idJuego).setPrestado(true);
					this.historialPrestamosClientes.put(idJuego, prestamo);
					this.inventario.get(idJuego).sumarVecesPrestado();
					
					return true;
				}else {
					return false;
				}
				
			} catch(Exception e) {
				return false;
			}
			
		}else {
			String Id = String.valueOf(this.historailPrestamosEmpleados.size()+1);
			PrestamoEmpleado prestano = new PrestamoEmpleado(Id,this.inventario.get(idJuego),inicio,null,empleado, this.turnos);// aca el null esta por que no tiene fecha de entrega aun
			try {
				if(prestano.sonAptos(null) &&  !this.inventario.get(idJuego).isPrestado() ){
					this.historailPrestamosEmpleados.put(idJuego, prestano);
					this.inventario.get(idJuego).setPrestado(true);
					this.inventario.get(idJuego).sumarVecesPrestado();
					return true;
				} else {
					return false;
				}
			}catch(Exception e) {
				return false;
			}
		}

		
	
	}
	
	public void limpiarMesa(LocalDateTime fin, Mesa mesa) {
		mesa.liberarMesa();
		
	}
	
	public void registrarVenta(int idMesa, LocalDateTime fecha, Cliente cliente) {
		String Id = String.valueOf(this.historialVenta.size()+1);
		Venta venta = new Venta( Id,  fecha,  cliente);
	}
	
}
