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
        if (bus == null) return new String[0][0];

        int totalAsientos = bus.getNroAsientos();
        String[][] matriz = new String[totalAsientos][2];

        for (int i = 0; i < totalAsientos; i++) {
            matriz[i][0] = String.valueOf(i + 1);
            matriz[i][1] = "Libre";
        }

        for (pasajes)

        return matriz;
    }

    public void addPasaje(Pasaje pasaje) {
        if (pasaje != null && !this.pasajes.contains(pasaje)) {
            this.pasajes.add(pasaje);
        }
    }

    public String[][] getListaPasajeros() {
        String[][] lista = new String[pasajes.size()][4];
        for (int i = 0; i < pasajes.size(); i++) {
            Pasajero p = pasajes.get(i).getPasajero();
            if (p != null) {
                lista[i][0] = p.getIdPersona() != null ? p.getIdPersona().toString() : "";
                lista[i][1] = p.getNombrePersona() != null ? p.getnNombrePersona().toString() : "";
                lista[i][2] = p.getNomPersona() != null ? p.getNomPersona().toString() : "";
                lista[i][3] = p.getFonoPersona() != null ? p.getFonoPersona().toString() : "";
            }
        }
        return lista;
    }

    public boolean existeDisponibilidad() {
        return getNroAsientosDisponibles() > 0;
    }

    public int getNroAsientosDisponibles() {
        if (bus == null) return 0;
        return bus.getNroAsientos() - pasajes.size();
    }
}