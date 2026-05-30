package boardGameCafe.logic;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class PasteleriaTest {
	@Test
	public void testEsAptoSinAlergias() {
	    // mesa sin alergias
	    Mesa mesa = new Mesa("1", 4, false, new ArrayList<>(), false);
	    // producto con alergias
	    ArrayList<String> alergenosProducto = new ArrayList<>();
	    alergenosProducto.add("gluten");

	    Pasteleria pastel = new Pasteleria(
	            "Torta",
	            "C1",
	            8000,
	            "P1",
	            "Chocolate",
	            alergenosProducto
	    );
	    // no coinciden alergias asi q si se es apto
	    assertTrue(pastel.esApto(mesa));
	}
	@Test
	public void testEsAptoConAlergia() {
	    // mesa con alergia
	    ArrayList<String> alergiasMesa = new ArrayList<>();
	    alergiasMesa.add("gluten");

	    Mesa mesa = new Mesa("1", 4, false, alergiasMesa, false);
	    // producto con misma alergia
	    ArrayList<String> alergenosProducto = new ArrayList<>();
	    alergenosProducto.add("gluten");

	    Pasteleria pastel = new Pasteleria(
	            "Torta",
	            "C1",
	            8000,
	            "P1",
	            "Chocolate",
	            alergenosProducto
	    );
	    // no es apto
	    assertFalse(pastel.esApto(mesa));
	}

}
