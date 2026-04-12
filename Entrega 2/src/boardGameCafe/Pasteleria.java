package boardGameCafe;
import java.util.*;
public class Pasteleria extends ProductoMenu{
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
