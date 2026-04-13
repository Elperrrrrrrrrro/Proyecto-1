package boardGameCafe;

public static void main(String[] args) {

	// 1. Cargar sistema (si existe archivo lo trae, si no crea uno nuevo)
	SistemaBoardGameCafe sistema = Persistencia.cargarSistema();
	
	// 2. Crear datos de prueba
	Cliente c1 = new Cliente("Juan", "123", "juan", "1234");
	sistema.getClientes().put("123", c1);
	
	JuegoMesa j1 = new JuegoMesa("Catan", 2010, "Devir", 2, 4,
	        "Estrategia", false, "nuevo", false, 50000);
	sistema.getInventario().put("J1", j1);
	
	// 3. Guardar sistema
	Persistencia.guardarSistema(sistema);
	
	System.out.println(" Datos guardados correctamente");
}