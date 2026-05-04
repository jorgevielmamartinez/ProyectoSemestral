package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private Bus bus;
    private List<Pasaje> pasajes;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, Bus bus) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.bus = bus;
        this.pasajes = new ArrayList<>();
        if (this.bus != null) {
            this.bus.addViaje(this);
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Bus getBus() {
        return bus;
    }

    public String[][] getAsientos() {
        String[][] matriz = new String[bus.getNroAsientos()][2];
        for (int i = 0; i < bus.getNroAsientos(); i++) {
            matriz[i][0] = String.valueOf(i + 1);
            matriz[i][1] = "L";
        }

        for (Pasaje p : pasajes) {
            int numAsiento = p.getAsiento();
            matriz[numAsiento - 1][1] = "O";
        }

        return matriz;
    }

    public void addPasaje(Pasaje pasaje) {
        this.pasajes.add(pasaje);
    }

    public String[][] getListaPasajeros() {
        List<Pasajero> pasajerosUnicos = new ArrayList<>();

        for (Pasaje p : pasajes) {
            if (!pasajerosUnicos.contains(p.getPasajero())) {
                pasajerosUnicos.add(p.getPasajero());
            }
        }

        String[][] lista = new String[pasajerosUnicos.size()][4];
        for (int i = 0; i < pasajerosUnicos.size(); i++) {
            Pasajero p = pasajerosUnicos.get(i);
            lista[i][0] = p.getIdPersona().toString();
            lista[i][1] = p.getNombreCompleto().toString();
            lista[i][2] = p.getNomContacto().toString();
            lista[i][3] = p.getFonoContacto();
        }
        return lista;
    }

    public boolean existeDisponibilidad() {
       if (getNroAsientosDisponibles() > 0) {
           return true;
       }
       return false;
    }

    public int getNroAsientosDisponibles() {
        return bus.getNroAsientos() - pasajes.size();
    }

    public int getNroAsientosOcupados() {
        return pasajes.size();
    }
}