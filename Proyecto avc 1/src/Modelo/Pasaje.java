package Modelo;

public class Pasaje {
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
        return "Pasaje{" +
                "numero=" + numero +
                ", asiento=" + asiento +
                ", pasajero=" + pasajero +
                ", viaje=" + viaje +
                '}';
    }


}