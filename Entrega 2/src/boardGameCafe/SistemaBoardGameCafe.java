package boardGameCafe;

import java.util.ArrayList;
import java.util.Map;
import java.util.*;
import java.io.Serializable;
import java.time.*;

public class SistemaBoardGameCafe implements Serializable {
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
		    inventario = new HashMap<>();
	        inventarioVender = new HashMap<>();
	        empleados = new HashMap<>();
	        clientes = new HashMap<>();
	        mesas = new HashMap<>();
	        turnos = new HashMap<>();
	        historialVenta = new HashMap<>();
	        historialPrestamosClientes = new HashMap<>();
	        historailPrestamosEmpleados = new HashMap<>();
	        Sugerencias = new ArrayList<>();
	    }

	    public void guardar() {
	        Persistencia.guardarSistema(this);
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

			PrestamoCliente prestamo = new PrestamoCliente(this.inventario.get(idJuego),inicio, null,cliente  ); // aca el null esta por que no tiene fecha de entrega aun
			try {
				if(PrestamoCliente.sonAptos(this.mesas.get(idMesa)) &&  (!this.inventario.get(idJuego).isPrestado())) {
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
			PrestamoEmpleado prestano = new PrestamoEmpleado(this.inventario.get(idJuego),inicio,null,empleado);// aca el null esta por que no tiene fecha de entrega aun
			try {
				if(PrestamoEmpleado.sonAptos(null) &&  !this.inventario.get(idJuego).isPrestado() ){
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
		return true;
		
	
	}
	
	public void limpiarMesa(LocalDateTime fin, Mesa mesa) {
		mesa.liberarMesa();
		
	}
	
	public void registrarVenta(int idMesa) {
		Venta venta = new Venta();
	}
	
}
