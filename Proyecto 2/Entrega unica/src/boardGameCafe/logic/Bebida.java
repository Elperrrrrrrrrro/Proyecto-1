package boardGameCafe.logic;
import java.io.Serializable;
public class Bebida extends ProductoMenu implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean esAlcoholica ;
	private boolean temperatura; 
	
	public Bebida(String nombre, String codigo  , boolean alcoholica, boolean temperatura, double precioBase,String codigoItem,String descripcion) {
		super( nombre,  precioBase, codigoItem, descripcion);
		this.esAlcoholica=alcoholica;
		this.temperatura=temperatura;
		
	}	
	
	
	
	public boolean esApto(Mesa mesa) {
			if (mesa.isHayMenores() && this.esAlcoholica) {
				return false;
			}

			if (this.temperatura) {
				for (Prestamo i : mesa.getPrestamoActicos()) {
					if ( "Accion".equals(i.getJuego().getCategoria())){
						return false;
					}
					
				}
			}
			
			return true;
	}
}
