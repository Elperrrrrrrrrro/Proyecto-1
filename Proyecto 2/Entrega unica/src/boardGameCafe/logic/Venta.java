package boardGameCafe.logic;
import java.util.*;

import java.time.*;
import java.io.Serializable;
public class Venta implements Serializable {
	private static final long serialVersionUID = 1L;
	private String idVenta;
	private LocalDateTime fecha;
	//private Cliente cliente;
	private Usuario comprador;
	private ArrayList<ProductoMenu> itemsVendidos ;
	private ArrayList<JuegoMesa> juegosVendidos;
	private double subtotal;
	private static double impuestoComun = 0.08;
	private static double ivaJuegos = 0.19;
	private double propina;
	private double total;
	

    public Venta(String id, LocalDateTime fecha, Usuario comprador) {
        this.idVenta = id;
        this.fecha = fecha; 
        this.comprador = comprador;
        //this.cliente = cliente;
        

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
    
    public double calcularTotal() {

        this.subtotal = 0.0;

        for (ProductoMenu item : itemsVendidos) {
            this.subtotal += item.getPrecioBase();
        }

        double impoconsumo = calcularImpoconsumo();
        double iva = calcularIva();

        this.total = this.subtotal + impoconsumo + iva + this.propina;

        return this.total;
    }


    public void setPropina(double propina) {
        this.propina = propina;
    }
    public void setTotal(double total) {
        this.total = total;
    }

	
    public String getIdVenta() {
		return idVenta;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	//public Cliente getCliente() {
		//return cliente;
	//}
	
	public Usuario getComprador() {
	    return comprador;
	    }
	    

	public ArrayList<ProductoMenu> getItemsVendidos() {
		return itemsVendidos;
	}
    
    public ArrayList<JuegoMesa> getJuegosVendidos() {
		return juegosVendidos;
	}
    
    public double getSubtotal() {
		return subtotal;
	}

    public double getPropina() {
		return propina;
	}
    
    public double getTotal() {
		return total;
	}

    public static double getImpuestoComun() {
		return impuestoComun;
	}

    public static double getIvaJuegos() {
		return ivaJuegos;
	}

    
}
