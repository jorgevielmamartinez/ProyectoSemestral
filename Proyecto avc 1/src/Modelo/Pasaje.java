package Modelo;

import java.io.Serializable;

public class Pasaje implements Serializable {
    private static int contadorNumero = 1;

    private int numero;
    private int asiento;
    private Viaje viaje;
    private Pasajero pasajero;
    private Venta venta;

    public Pasaje(int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {
        this.numero = contadorNumero++;
        this.asiento = asiento;
        this.viaje = viaje;
        this.pasajero = pasajero;
        this.venta = venta;

        if (viaje != null) {
            viaje.addPasaje(this);
        }
    }

    public int getAsiento() {
        return asiento;
    }

    public int getNumero() {
        return numero;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Venta getVenta() {
        return venta;
    }

    public Viaje getViaje() {
        return viaje;
    }

    @Override
    public String toString() {
        return String.format(
                "--------------------- PASAJE ELECTRÓNICO ---------------------\n" +
                        "%-25s %s\n" +
                        "%-25s %s\n" +
                        "%-40s %s\n" +
                        "%-40s %s\n" +
                        "%-15s %-15s %s\n" +
                        "%-15s %-15s %s\n" +
                        "%-20s %-20s %-15s %s\n" +
                        "%-20s %-20s %-15s %s\n" +
                        "--------------------------------------------------------------\n",
                "Nombre Empresa", "Número de pasaje",
                viaje.getBus().getEmpresa().getNombre(), numero,
                "Nombre Pasajero", "RUT/Pasaporte",
                pasajero.getNombreCompleto(), pasajero.getIdPersona(),
                "Patente bus", "Asiento", "Valor Pagado",
                viaje.getBus().getPatente(), asiento, venta.getMontoPagado(),
                "Terminal origen", "Terminal destino", "Fecha", "Hora",
                viaje.getTerminalSalida().getNombre(),
                viaje.getTerminalLlegada().getNombre(),
                viaje.getFecha(),
                viaje.getHora()
        );
    }


}