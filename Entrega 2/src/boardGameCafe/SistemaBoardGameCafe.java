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
	private Map<String, PrestamoCliente> historialPrestamosClientes;// llave id prestamo
	private Map<String, PrestamoEmpleado> historailPrestamosEmpleados;// llave id prestamo
	private Map<String, Sugerencia> Sugerencias; // llave id sugerencia
	private Map<String, ProductoMenu> menu; // llave nombre del plato
	private Usuario usuarioActual ;
	
	private void verificarSesion() {
		if (this.usuarioActual == null) {
			throw new SecurityException("Acceso denegado: Ningún usuario ha iniciado sesión.");
		}
	}
	
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
	        Sugerencias = new HashMap<>();
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
			
			this.usuarioActual = this.empleados.get(login+password);
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
		verificarSesion();
		mesa.liberarMesa();
		
	}
	
	public void registrarVenta(int idMesa, LocalDateTime fecha, Cliente cliente) {
		verificarSesion();
		String Id = String.valueOf(this.historialVenta.size()+1);
		Venta venta = new Venta( Id,  fecha,  cliente);
		this.historialVenta.put(Id, venta);
		
	}
	
	public boolean SolicitarCambioTurno(String idEmpleado, String dianuevo) {
		verificarSesion();
		String Id = String.valueOf(this.Sugerencias.size()+1);
		try {
			Sugerencia sugerencia = new Sugerencia(Id,false,false,dianuevo,this.empleados.get(idEmpleado),null) ;
			this.Sugerencias.put(Id, sugerencia);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	public boolean aprobarCambioTurno(String idEmpleado, Sugerencia sugerencia) {
		if (!(this.usuarioActual instanceof Administrador)) {
			throw new SecurityException("Acceso denegado: Solo el Administrador puede aprobar cambios de turno.");
		}

		if(sugerencia.isTipoSugerencia()) {
			this.menu.put(sugerencia.getProductoMenu().getNombre(), sugerencia.getProductoMenu());
		} else {
			// Nota: Corregí el error de sintaxis que tenías aquí
			this.turnos.get(sugerencia.getDiaCambio()).adicionarEmpleado(this.empleados.get(idEmpleado)); 
		}
		sugerencia.setEstaAprobado(true);
		return true;
	}
	
	public void agregarJuegoMesa(JuegoMesa juego) {
		if (!(this.usuarioActual instanceof Administrador)) {
			throw new SecurityException("Acceso denegado: Solo el Administrador puede agregar juegos al inventario.");
		}
		String id = juego.getId();
		this.inventario.put(id, juego);
	}
	
	public void cambiarEstadoJuego(String idJuego, String estado) {
		if (!(this.usuarioActual instanceof Administrador)) {
			throw new SecurityException("Acceso denegado: Solo el Administrador puede cambiar el estado de los juegos.");
		}
		this.inventario.get(idJuego).setEstado(estado);
	}
	
	public void registrarCliente(Cliente cliente) {
		verificarSesion();
		this.clientes.put(cliente.getDocumento(), cliente);
	}
	
	public void agregarplatoaMesa(String idMesa, String nombrePlato) {
		verificarSesion();
		this.mesas.get(idMesa).agregarAlPedido(this.menu.get(nombrePlato));
	}
	
}
