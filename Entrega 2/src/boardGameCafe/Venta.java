package boardGameCafe;
import java.util.*;
import java.time.*;
public class Venta {
	private String idVenta;
	private LocalDateTime fecha;
	private Cliente cliente;
	private ArrayList<ProductoMenu> itemsVendidos ;
	private ArrayList<JuegoMesa> juegosVendidos;
	private double subtotal;
	private static double impuestoComun = 0.08;
	private static double ivaJuegos = 0.19;
	private double propina;
	private double total;
	

    public Venta(String id, LocalDateTime fecha, Cliente cliente) {
        this.idVenta = id;
        this.fecha = fecha; 
        this.cliente = cliente;
        

        this.itemsVendidos = new ArrayList<>();
        this.juegosVendidos = new ArrayList<>();
        
        this.subtotal = 0.0;
        this.propina = 0.0;
        this.total = 0.0;
    }
    
    public void agregarProducto(ProductoMenu producto) {
        this.itemsVendidos.add(producto);
    }

    public void agregarJuegoComprado(JuegoMesa juego) {
        this.juegosVendidos.add(juego);
    }
    
    
    public double calcularImpoconsumo() { 
        double sumaAlimentos = 0.0;
        

        for (ProductoMenu item : itemsVendidos) {

            sumaAlimentos += item.getPrecioBase(); 
        }
        
        return sumaAlimentos * impuestoComun; 
    }
    
    public double calcularIva() {
        double sumaJuegos = 0.0;
        
        for (JuegoMesa juego : juegosVendidos) {

            sumaJuegos += juego.getPrecioVenta(); 
        }
        
        return sumaJuegos * ivaJuegos; // Retorna el 19% de la suma
    }
    
    public double calcularTotal(boolean esEmpleado) {
        this.subtotal = 0.0;
        

        for (ProductoMenu item : itemsVendidos) {
            this.subtotal += item.getPrecioBase();
        }

        

        double valorImpoconsumo = calcularImpoconsumo();
        double valorIva = calcularIva();
        

        this.total = this.subtotal + valorImpoconsumo + valorIva + this.propina;
        

        if (esEmpleado) {
            this.total = this.total * 0.80;
        }
        
        return this.total;
    }


    public void setPropina(double propina) {
        this.propina = propina;
    }
	
	
}
