package boardGameCafe;

import java.util.*;
import java.io.Serializable;
import java.time.*;


public class SistemaBoardGameCafe implements Serializable {
	private static final long serialVersionUID = 1L;
	
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
	private Queue<Sugerencia> sugerenciasPendientes;
	private Map<String, ProductoMenu> menu; // llave nombre del plato
	private Queue<Mesa> mesasDesocupadas;
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
	        sugerenciasPendientes = new LinkedList<>();
	        mesasDesocupadas = new LinkedList<>();
	        menu = new HashMap<>();
	    }

	    public void guardar() {
	        Persistencia.guardarSistema(this);
	    }
	
	public void cargarDatos() {  
		SistemaBoardGameCafe sistema = Persistencia.cargarSistema();

	    this.inventario = sistema.inventario;
	    this.inventarioVender = sistema.inventarioVender;
	    this.empleados = sistema.empleados;
	    this.clientes = sistema.clientes;
	    this.mesas = sistema.mesas;
	    this.turnos = sistema.turnos;
	    this.historialVenta = sistema.historialVenta;
	    this.historialPrestamosClientes = sistema.historialPrestamosClientes;
	    this.historailPrestamosEmpleados = sistema.historailPrestamosEmpleados;
	    this.Sugerencias = sistema.Sugerencias;
	    this.sugerenciasPendientes = sistema.sugerenciasPendientes;
	    this.menu = sistema.menu;
	    this.mesasDesocupadas = sistema.mesasDesocupadas;
		
	}
	
	public void guardarDatos() { 
		verificarSesion();  
		Persistencia.guardarSistema(this); 	
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
		this.mesasDesocupadas.add(mesa);
		
	}
	
	public void registrarVenta(Integer idMesa, LocalDateTime fecha, Usuario comprador, double propina,
	        ArrayList<ProductoMenu> productosExtras,
	        ArrayList<JuegoMesa> juegosComprados,
	        boolean descuentoCompartido) {

	    verificarSesion();

	    String id = String.valueOf(this.historialVenta.size() + 1);
	    Venta venta = new Venta(id, fecha, comprador);
	    Mesa mesa = null;
//cliente
	    if (comprador instanceof Cliente) {
	        if (idMesa == null) {
	            throw new IllegalArgumentException("El cliente necesita una mesa.");
	        }
	        mesa = this.mesas.get(String.valueOf(idMesa));
	        if (mesa == null) {
	            throw new IllegalArgumentException("La mesa no existe.");
	        }
	        for (ProductoMenu producto : mesa.getPedidoActual()) {
	            venta.agregarProducto(producto);
	        }
	    }
	    // empleado
	    if (comprador instanceof Empleado) {
	    	 Empleado empleado = (Empleado) comprador;
	    	    String idEmpleado = empleado.getLogin() + empleado.getPassword();

	    	    boolean enTurno = empleadoEnTurno(idEmpleado);

	        if (!empleado.puedeComprar(enTurno, !mesasDesocupadas.isEmpty())) {
	            throw new IllegalStateException("Empleado no puede comprar ahora.");
	        }
	        if (productosExtras != null) {
	            for (ProductoMenu producto : productosExtras) {
	                venta.agregarProducto(producto);
	            }
	        }
	    }

	    // ambos pueden comprar juegos
	    if (juegosComprados != null) {
	        for (JuegoMesa juego : juegosComprados) {

	            if (!juego.isPrestado()) {
	                venta.agregarJuegoComprado(juego);
	                juego.setPrestado(true);
	                this.inventarioVender.remove(juego.getId());
	            }
	        }
	    }

	    //propinas
	    venta.setPropina(propina);
	    //total
	    double total = venta.calcularTotal();
	    // descuentos
	    if (comprador instanceof Empleado) {
	        total *= 0.80;
	    } else if (descuentoCompartido) {
	        total *= 0.90;
	    }
	    //puntos de fidelifad
	    if (comprador instanceof Cliente) {
	        Cliente c = (Cliente) comprador;
	        double descuento = c.usarPuntosFidelidad(total);
	        total -= descuento;
	        
	        c.agregarPuntosFidelidad(total * 0.01);
	    }
	    venta.setTotal(total);
	    this.historialVenta.put(id, venta);
	}
		
			
	public boolean SolicitarCambioTurno(String idEmpleado, String dianuevo) {
		verificarSesion();
		String Id = String.valueOf(this.Sugerencias.size()+1);
		try {
			Sugerencia sugerencia = new Sugerencia(Id,false,false,dianuevo,this.empleados.get(idEmpleado),null) ;
			this.sugerenciasPendientes.offer(sugerencia);
			return true;
		}catch(Exception e) {
			return false;
		}	
	}
	
	public boolean aprobarCambioTurno(String idEmpleado, Sugerencia sugerencia) { //Tambien se usa para aprobar sugerencias de comida TODO
		    if (!(this.usuarioActual instanceof Administrador)) {
		        throw new SecurityException("Acceso denegado: Solo el Administrador puede aprobar cambios de turno.");
		    }
		    //si es para sugerencia de comida
		    if (sugerencia.isTipoSugerencia()) {
		        this.menu.put(
		            sugerencia.getProductoMenu().getNombre(),
		            sugerencia.getProductoMenu()
		        );  
		    } 
 // si es para cambio de turno
		    else {
		        Empleado empleadoSolicitante = sugerencia.getEmpleado();
		        String diaDestino = sugerencia.getDiaCambio();

		        Turno turnoDestino = this.turnos.get(diaDestino);
		        Turno turnoOrigen = null;

		        // 1. encontrar el turno actual del empleado
		        for (Turno t : this.turnos.values()) {
		            if (t.getEmpleadosAsignados().contains(empleadoSolicitante)) {
		                turnoOrigen = t;
		                break;
		            }
		        }
		        if (turnoDestino == null || turnoOrigen == null) {
		            throw new IllegalStateException("Turnos inválidos para intercambio.");
		        }
		        //ver si se cumplen condiciones para el cambio
		        if (!turnoOrigen.validarMinimoOperativo()) {
		            throw new IllegalStateException(
		                "No se puede realizar el cambio: el turno origen quedaría inválido."
		            );
		        }
		        // quitar del turno atual
		        turnoOrigen.getEmpleadosAsignados().remove(empleadoSolicitante);
		        // intercambio con otro si se puede
		        Empleado reemplazo = null;

		        if (!turnoDestino.getEmpleadosAsignados().isEmpty()) {
		            reemplazo = turnoDestino.getEmpleadosAsignados().get(0);
		            turnoDestino.getEmpleadosAsignados().remove(reemplazo);
		        }
		        // mover empleado que pide el cambio
		        turnoDestino.adicionarEmpleado(empleadoSolicitante);
		        // poner al remplazo 
		        if (reemplazo != null) {
		            turnoOrigen.adicionarEmpleado(reemplazo);
		        }
		    }
		    //cambiar el estado de la sug, se agrega a sugerencias y se elimina de las pendientes
		    sugerencia.setEstaAprobado(true);
		    this.Sugerencias.put(sugerencia.getSugerenciaID(), sugerencia);
		    this.sugerenciasPendientes.remove(sugerencia);

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
		this.clientes.put(cliente.getDocumentoIdentidad(), cliente);
	}
	
	public void agregarplatoaMesa(String idMesa, String nombrePlato) {
		verificarSesion();
		this.mesas.get(idMesa).agregarAlPedido(this.menu.get(nombrePlato));
	}
	public Map<String, Cliente> getClientes() {
	    return clientes;
	}

	public Map<String, JuegoMesa> getInventario() {
	    return inventario;
	}

	public Map<String, Mesa> getMesas() {
	    return mesas;
	}

	public void asignarMesa(Cliente cliente) {
		verificarSesion();
		Mesa mesa = this.mesasDesocupadas.poll();
		if (mesa == null) {
	        throw new IllegalStateException("No hay mesas disponibles.");
	    }
		mesa.setClienteActual(cliente);


	}
	public void agregarEmpleado(Empleado empleado) {
		verificarSesion();
		this.empleados.put(empleado.getLogin()+empleado.getPassword(), empleado);
	}
	
	public void agregarTurno(Turno turno) {
		verificarSesion();
		this.turnos.put(turno.getDiaSemana(), turno);
	}
	
	public void agregarMesa(Mesa mesa) {
		verificarSesion();
		this.mesas.put(String.valueOf(this.mesas.size()+1), mesa);
		this.mesasDesocupadas.offer(mesa);
	}

	public void agregarProductoMenu(ProductoMenu producto) {
		verificarSesion();
		this.menu.put(producto.getNombre(), producto);
		
	}
	
	public void agregarPrestamoCliente(PrestamoCliente prestamo) {
		verificarSesion();
		this.historialPrestamosClientes.put(prestamo.getIdPrestamo(), prestamo);
	}

	public void agregarPrestamoEmpleado(PrestamoEmpleado prestamo) {
		verificarSesion();
		this.historailPrestamosEmpleados.put(prestamo.getIdPrestamo(), prestamo);
	}
	public void agregarSugerencia(Sugerencia sugerencia) {
		verificarSesion();
		this.Sugerencias.put(sugerencia.getSugerenciaID(), sugerencia);
	}
	public void agregarVenta(Venta venta) {
		verificarSesion();
		this.historialVenta.put(venta.getIdVenta(), venta);
	}

	public void agregarJuegoVender(JuegoMesa juego) {
		verificarSesion();
		this.inventarioVender.put(juego.getId(), juego);
	}

	public List<Turno> verTurnosEmpleado(String idEmpleado) {
	    verificarSesion();
	    Empleado empleado = this.empleados.get(idEmpleado);
	    if (empleado == null) {
	        throw new IllegalArgumentException("Empleado no existe.");
	    }
	    List<Turno> turnosEmpleado = new ArrayList<>();
	    for (Turno t : this.turnos.values()) {
	        if (t.getEmpleadosAsignados().contains(empleado)) {
	            turnosEmpleado.add(t);
	        }
	    }
	    return turnosEmpleado;
	}

	public boolean sugerirPlatillo(String idEmpleado, String nombreProducto) {
	    verificarSesion();
	    Empleado empleado = this.empleados.get(idEmpleado);
	    if (empleado == null) {
	        throw new IllegalArgumentException("Empleado no existe.");
	    }
	    ProductoMenu producto = this.menu.get(nombreProducto);
	    if (producto == null) {
	        throw new IllegalArgumentException("El producto no existe en el sistema.");
	    } 
	    String id = String.valueOf(this.Sugerencias.size() + 1);
	    Sugerencia sugerencia = new Sugerencia( id,false,true,null, empleado, producto    		
	    		);
	    this.sugerenciasPendientes.offer(sugerencia);
	    return true;
	}
	
	
	public void moverJuegoAInventarioVenta(String idJuego) {
	    verificarSesion();
	    JuegoMesa juego = this.inventario.get(idJuego);	   
	    if (juego == null) {
	        throw new IllegalArgumentException("El juego no existe en inventario de préstamo.");
	    }
	    if (juego.isPrestado()) {
	        throw new IllegalStateException("El juego está prestado y no puede venderse aún.");
	    }
	    // mover a inventario de venta    
	    this.inventarioVender.put(idJuego, juego);
	    // quitar del inventario de préstamo
	    this.inventario.remove(idJuego);
	}
	
	public boolean empleadoEnTurno(String idEmpleado) {
	    return !verTurnosEmpleado(idEmpleado).isEmpty();
	}
	
	public void cambiarTurnoDirecto(String idEmpleado, String diaOrigen, String diaDestino) {
	    verificarSesion();
	    if (!(this.usuarioActual instanceof Administrador)) {
	        throw new SecurityException("Solo el administrador puede modificar turnos directamente.");
	    }
	    Empleado empleado = this.empleados.get(idEmpleado);
	    if (empleado == null) {
	        throw new IllegalArgumentException("Empleado no existe.");
	    }
	    Turno turnoOrigen = this.turnos.get(diaOrigen);
	    Turno turnoDestino = this.turnos.get(diaDestino);
	    if (turnoOrigen == null || turnoDestino == null) {
	        throw new IllegalArgumentException("Turnos inválidos.");
	    }
	    if (!turnoOrigen.getEmpleadosAsignados().contains(empleado)) {
	        throw new IllegalStateException("El empleado no está en el turno origen.");
	    }
	    // regla del minimo o
	    if (!turnoOrigen.validarMinimoOperativo()) {
	        throw new IllegalStateException("No se puede mover el empleado: el turno quedaría inválido.");
	    }
	    // mover empleado
	    turnoOrigen.getEmpleadosAsignados().remove(empleado);
	    turnoDestino.adicionarEmpleado(empleado);
	}
	
	public void generarInformeVentasDetallado(LocalDate inicio, LocalDate fin) {
	    verificarSesion();
	    if (!(this.usuarioActual instanceof Administrador)) {
	        throw new SecurityException("Solo el administrador puede ver informes.");
	    }
	    for (Venta venta : historialVenta.values()) {

	        LocalDate fechaVenta = venta.getFecha().toLocalDate();
	        if (fechaVenta.isBefore(inicio) || fechaVenta.isAfter(fin)) {
	            continue;
	        }
	        String idComprador = venta.getComprador().getDocumentoIdentidad();
	        double comida = 0;
	        double juegos = 0;
	        for (ProductoMenu p : venta.getItemsVendidos()) {
	            comida += p.getPrecioBase();
	        }

	        for (JuegoMesa j : venta.getJuegosVendidos()) {
	            juegos += j.getPrecioVenta();
	        }

	        double impuestos = venta.calcularImpoconsumo() + venta.calcularIva();

	        //esto en consola pero pa tener la idea
	        System.out.println("Informe de venta");
	        System.out.println("ID Venta: " + venta.getIdVenta());
	        System.out.println("Fecha: " + venta.getFecha());
	        System.out.println("ID Comprador: " + idComprador);
	        System.out.println("Comida: " + comida);
	        System.out.println("Juegos: " + juegos);
	        System.out.println("Impuestos: " + impuestos);
	        System.out.println("Propina: " + venta.getPropina());
	        System.out.println("TOTAL: " + venta.getTotal());
	    }
	}
	
}
