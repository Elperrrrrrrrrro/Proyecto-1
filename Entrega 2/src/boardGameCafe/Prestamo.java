package boardGameCafe;

import java.time.LocalDateTime;

public abstract class Prestamo {

    protected String idPrestamo;
    protected juegoMesa juego;
    protected LocalDateTime fechaPrestamo;
    protected LocalDateTime fechaDevolucion;

    public Prestamo(String idPrestamo, juegoMesa juego,
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

    public juegoMesa getJuego() {
        return juego;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }
}