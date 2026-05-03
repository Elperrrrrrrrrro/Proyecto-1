package boardGameCafe;
import java.io.Serializable;
public abstract class ProductoMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private double precioBase;
	private String codigoItem ;
	private String descripcion;
	
public ProductoMenu(String nombre, double precioBase,String codigoItem,String descripcion) {
	this.nombre = nombre;
	this.precioBase=precioBase;
	this.codigoItem=codigoItem;
	this.descripcion=descripcion;
}

public String getNombre() {
	return nombre;
}

public double getPrecioBase() {
	return precioBase;
}

public String getCodigoItem() {
	return codigoItem;
}

public String getDescripcion() {
	return descripcion;
}

public abstract boolean esApto(Mesa mesa);

}
