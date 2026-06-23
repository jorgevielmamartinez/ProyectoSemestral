package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {

    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;

    private ArrayList<Pasaje> pasajes;
    private Pago pago;

    public Venta(String idDoc, TipoDocumento tipo, LocalDate fec, Cliente cli) {
        this.idDocumento = idDoc;
        this.tipo = tipo;
        this.fecha = fec;
        this.cliente = cli;
        this.pasajes = new ArrayList<>();

        cli.addVenta(this);
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

    public int getMonto() {
        int total = 0;

        for (Pasaje p : pasajes) {
            total += p.getViaje().getPrecio();
        }

        return total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void createPasaje(int asiento, Viaje viaje, Pasajero pasajero) {
        Pasaje pasaje = new Pasaje(asiento, viaje, pasajero, this);
        pasajes.add(pasaje);
        viaje.addPasaje(pasaje);
    }

    public Pasaje[] getPasajes() {
        return pasajes.toArray(new Pasaje[0]);
    }

    public int getMontoPagado() {
        if (pago == null) {
            return 0;
        }

        return pago.getMonto();
    }

    public boolean pagaMonto() {
        pago = new PagoEfectivo(getMonto());
        return true;
    }

    public boolean pagaMonto(long nroTarjeta) {
        pago = new PagoTarjeta(getMonto(), nroTarjeta);
        return true;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }

        if (!(otro instanceof Venta venta)) {
            return false;
        }

        return idDocumento.equals(venta.idDocumento)
                && tipo.equals(venta.tipo);
    }

    public String getTipoPago() {
        if (pago == null) {
            return "";
        }

        return pago.getClass().getSimpleName();
    }

    /*

    ===========
    Metodo extras al uml
    ===========

    */

    public void addPasaje(Pasaje pasaje) {
        if (!pasajes.contains(pasaje)) {
            pasajes.add(pasaje);
        }
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }
}