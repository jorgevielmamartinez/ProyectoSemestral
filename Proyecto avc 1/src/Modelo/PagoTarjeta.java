package Modelo;

import java.io.Serializable;

public class PagoTarjeta extends Pago implements Serializable {
    private long nroTarjeta;
    public PagoTarjeta(long nroTarjeta,int monto) {
        super(monto);
        this.nroTarjeta = nroTarjeta;
    }

    public PagoTarjeta(int monto, long nroTarjeta) {
        super();
    }

    public long getNroTarjeta() {
        return nroTarjeta;
    }
}
