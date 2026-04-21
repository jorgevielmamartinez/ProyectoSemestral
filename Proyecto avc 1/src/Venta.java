import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;
    private ArrayList<Pasaje> pasajes;
        this.idDocumento = id;
        this.tipo = tipo;
        this.cliente = cliente;
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
    }
    public Pasaje[] getPasajes() {
        }
    public int getMonto() {
        int total = 0;
        }
        return total;
    }
}
