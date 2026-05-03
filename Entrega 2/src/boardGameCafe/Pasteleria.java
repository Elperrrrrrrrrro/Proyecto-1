package boardGameCafe;
import java.io.Serializable;
import java.util.*;
public class Pasteleria extends ProductoMenu implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> alergenos;
	public Pasteleria(String nombre, String codigo  , 
			 double precioBase,String codigoItem,String descripcion, ArrayList<String> alergenos) {
		super( nombre,  precioBase, codigoItem, descripcion);
		this.alergenos = alergenos;
	}
	
	public boolean esApto(Mesa mesa) {
		for(String i: alergenos) {
			if(mesa.getAlergenos().contains(i)) {
				return false;
			}
		}
		
		
		return true;
	}
}
