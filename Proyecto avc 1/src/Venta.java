import java.time.LocalDate;
import java.util.ArrayList;
//clase hecha por Jorge Vielma
public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;

    private ArrayList<Pasaje> pasajes;

    public Venta(String id, TipoDocumento tipo, LocalDate fecha, Cliente cliente) {
        this.idDocumento = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.cliente = cliente;
        this.pasajes = new ArrayList<Pasaje>();

        cliente.addVenta(this);
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void createPasaje(int asiento, Viaje viaje, Pasajero pasajero) {

        Pasaje pasaje = new Pasaje(asiento, viaje, pasajero, this);

        this.pasajes.add(pasaje);
    }

    public Pasaje[] getPasajes() {
        Pasaje[] arregloPasajes = new Pasaje[pasajes.size()];

        for (int i = 0; i < pasajes.size(); i++) {
            arregloPasajes[i] = pasajes.get(i);
        }
        return arregloPasajes;
    }

    public int getMonto() {
        int total = 0;

        for (Pasaje pasaje : pasajes) {
            total = total + pasaje.getViaje().getPrecio();
        }
        return total;
    }
}
