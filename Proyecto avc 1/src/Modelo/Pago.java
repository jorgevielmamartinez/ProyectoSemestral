package Modelo;

public abstract class Pago {
    private int monto;

    public Pago(int monto) {
        this.monto = monto;
    }

    public Pago() {
    }

    public int getMonto() {
        return monto;
    }
}
