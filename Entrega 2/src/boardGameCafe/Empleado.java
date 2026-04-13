package boardGameCafe;

public class Empleado {
    private String nombre;
    private String documentoIdentidad;
    private int edad;
    private String login;
    private String password;
    private List<juegoMesa> juegosConocidos;

    public Empleado(String nombre, String documentoIdentidad, int edad, String login, String password) {
        this.nombre = nombre;
        this.documentoIdentidad = documentoIdentidad;
        this.edad = edad;
        this.login = login;
        this.password = password;
        this.juegosConocidos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public int getEdad() {
        return edad;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
    public void adicionarJuegoConocido(juegoMesa juego) {
        if (!juegosConocidos.contains(juego)) {
            juegosConocidos.add(juego);
        }
    
  public List<juegoMesa> getJuegosConocidos() {
            return juegosConocidos;


}