package boardGameCafe;

import java.time.LocalDateTime;

public abstract class Prestamo {

    protected String idPrestamo;
    protected JuegoMesa juego;
    protected LocalDateTime fechaPrestamo;
    protected LocalDateTime fechaDevolucion;

    public Prestamo(String idPrestamo, JuegoMesa juego,
                    LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucion) {
        this.idPrestamo = idPrestamo;
        this.juego = juego;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    public abstract boolean sonAptos(Mesa mesa);

    public String getIdPrestamo() {
        return idPrestamo;
    }

    public JuegoMesa getJuego() {
        return juego;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }
    
    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}
}