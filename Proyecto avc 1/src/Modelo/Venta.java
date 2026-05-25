package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

//clase hecha por Jorge Vielma
public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;
    private Pago pago;
    private ArrayList<Pasaje> pasajes;

    public Venta(String id, TipoDocumento tipo, LocalDate fecha, Cliente cliente) {
        this.idDocumento = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.cliente = cliente;
        this.pasajes = new ArrayList<Pasaje>();
        this.pago=null;
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


    public int getMontoPagado(){
        if (pago != null) {
            return pago.getMonto();
        }
        return 0;
    }
    public boolean pagaMonto(){
        if (pago == null) {
            int montoTotal = getMonto();
            pago = new PagoEfectivo(montoTotal);
            return true;
        }
        return false;
    }
    public boolean pagaMonto(long nroTarjeta){
        if (pago == null) {
            int montoTotal = getMonto();
            pago = new PagoTarjeta((long) montoTotal, (int) nroTarjeta);
            return true;
        }
        return false;
    }
    public String getTipoPago(){
        if (pago instanceof PagoEfectivo){
            return "Efectivo";

        } else if (pago instanceof PagoTarjeta) {
            return "Tarjeta";
        }

        return null;
    }

    public int getMonto(){
        int monto = 0;
        for (Pasaje pasaje : pasajes) {
            monto += pasaje.getViaje().getPrecio();
        }
        return monto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venta venta = (Venta) o;
        return Objects.equals(idDocumento, venta.idDocumento) && tipo == venta.tipo && Objects.equals(fecha, venta.fecha) && Objects.equals(cliente, venta.cliente) && Objects.equals(pago, venta.pago) && Objects.equals(pasajes, venta.pasajes);
    }
}
